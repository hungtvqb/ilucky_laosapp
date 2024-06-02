/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.service;

import com.vas.portal.utils.common.Common;
import com.viettel.ussdfw.object.WebserviceMsg;
import com.viettel.ussdfw.object.WebserviceObject;
import com.viettel.ussdfw.webservice.WebserviceFW;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author TuanNS3_S
 */
public class WSProcessor extends WebserviceFW {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static String propertyTag = "original";
    public static String errorTag = "return";
    public static List<String> NOT_ENOUGH_MONEY_RES;
    public static List<String> ACCOUNT_BLOCKED_RES;
    public static List<String> LIST_SUCCESS_RES;

    public WSProcessor(Logger logger, String pathWSConfig) throws Exception {
        super(logger, pathWSConfig);
    }

    public WSProcessor(Logger logger, String pathWSConfig, String pathDatabaseConfig) throws Exception {
        super(logger, pathWSConfig, pathDatabaseConfig);
    }

    public String sendSms(String msisdn, String content, String channel) {
        WebserviceObject obj = this.getWebservice("sendmt");
        WebserviceMsg request = new WebserviceMsg();
        request.setMsisdn(msisdn);
        String msgTemplate = obj.getMsgTemplate().replace("#MSISDN#", msisdn);
        msgTemplate = msgTemplate.replace("#ISDN#", msisdn.substring(Common.COUNTRY_CODE.length()));
        msgTemplate = msgTemplate.replace("#CONTENT#", content);
        msgTemplate = msgTemplate.replace("#CHANNEL#", channel);
        request.setRequest(msgTemplate);

        WebserviceMsg response = this.send(request, obj);
        try {
            String error = StringUtils.substringBetween(response.getResponse(), "<" + errorTag + ">", "</" + errorTag + ">");
            return error.split("\\|")[0];
        } catch (Exception ex) {
            logger.error("Error get response", ex);
            return null;
        }
    }

    public String executeCmd(String msisdn, String amount, String cmdCode) {
        WebserviceObject obj = this.getWebservice(cmdCode);
        WebserviceMsg request = new WebserviceMsg();
        request.setMsisdn(msisdn);
        String msgTemplate = obj.getMsgTemplate().replace("#MSISDN#", msisdn);
        msgTemplate = msgTemplate.replace("#ISDN#", msisdn.substring(Common.COUNTRY_CODE.length()));
        msgTemplate = msgTemplate.replace("#AMOUNT#", amount + "");
        msgTemplate = msgTemplate.replace("#REQ_TIME#", sdf.format(new Date()));
        msgTemplate = msgTemplate.replace("#SEQ#", sdf.format(new Date()) + Common.counterTransId.getAndIncrement());
        request.setRequest(msgTemplate);

        WebserviceMsg response = this.send(request, obj);
        try {
            String error = StringUtils.substringBetween(response.getResponse(), "<" + errorTag + ">", "</" + errorTag + ">");
            return error.split("\\|")[0];
        } catch (Exception ex) {
            logger.error("Error get response", ex);
            return null;
        }
    }

}
