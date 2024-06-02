/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.common;


import com.sun.net.httpserver.HttpExchange;
import com.vas.wsfw.obj.UserInfo;
import java.lang.reflect.Method;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.apache.log4j.Logger;

/**
 *
 * @author minhnh@viettel.com.vn
 * @since Jun 4, 2013
 * @version 1.0
 */
@WebService
public abstract class WebserviceAbstract {

    public Logger logger;
    //
    public static String SUCCESS = "0";
    public static String SYNTAX_ERROR = "1";
    public static String WRONG_PASSWORD = "2";
    public static String NOT_ALLOW = "3";
    public static String MSISDN_NOT_VALID = "4";
    public static String PARAM_NOT_ENOUGH = "-1";
    public static String EXCEPTION = "6";
    public static String PARAM_NOT_VALID = "7";
    public static String NOT_CONFIG = "-1";
    public static String NOT_SUPPORT = "-2";
    public static String DUPLICATE_MESSAGE = "8";
    public static String ACCOUNT_EXIST = "9";
    public static String ACCOUNT_NOT_EXIST = "10";
    public static String ACCOUNT_WAS_BLOCK = "11";
    public static String ACCOUNT_WAS_UNBLOCK = "12";
    public static String CHECK_ACC_BALANCE_FAIL = "13";
    public static String NOT_ENOUGH_BALANCE = "14";
    public static String CHARGE_BALANCE_FAIL = "15";
    public static String EWALLET_ACCOUNT_NOT_EXISTED = "16";
    public static String PIN_NOT_VALID = "17";
    public static String NOT_CHANGE_PIN = "18";
    public static String WRONG_PIN = "19";
    public static String PAY_FAIL = "20";
    public static String LIST_ID_MONEY="1|44|79|78|81|88|20|51|72|84|86|19|21|22|25|73|85|50|80|23|41|77";

   
    //
    @Resource
    public WebServiceContext wsContext;

    public WebserviceAbstract(String logName) {
        logger = Logger.getLogger(logName);
    }

    /**
     * *******************************************************
     */
    abstract public UserInfo authenticate(String userName, String password, String ipAddress);

    /**
     * ***********************************************************
     */
    public String getIpClient() {
        MessageContext msgCtxt = wsContext.getMessageContext();
        try {
            HttpExchange httpEx = (HttpExchange) msgCtxt.get("com.sun.xml.ws.http.exchange");
            return httpEx.getRemoteAddress().getAddress().getHostAddress();
        } catch (Exception ex) {
            HttpExchange httpEx = (HttpExchange) msgCtxt.get("com.sun.xml.internal.ws.http.exchange");
            return httpEx.getRemoteAddress().getAddress().getHostAddress();
        }
    }

    public boolean pair(String ipClient, String ipConfig) {
        if (ipClient == null || ipClient.equals("") || ipConfig == null || ipConfig.equals("")) {
            return false;
        }
        ipConfig = ipConfig.replaceAll("x", "\\\\d+");
        return ipClient.matches(ipConfig);
    }
    
}
