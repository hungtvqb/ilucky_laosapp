/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.process;

import com.viettel.cluster.agent.integration.Record;
import com.vas.portal.utils.common.Common;
import com.vas.portal.utils.common.MessageResponse;
import com.vas.portal.utils.database.DbProcessor;
import com.vas.portal.utils.object.ExecutePrizeObj;
import com.vas.portal.utils.object.MtExtend;
import com.vas.portal.utils.object.PrizeObj;
import com.vas.portal.utils.service.WSProcessor;
import com.viettel.smsfw.process.ProcessRecordAbstract;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class ExecutePrize extends ProcessRecordAbstract {

    private WSProcessor ws;
    private DbProcessor db;
    private StringBuilder br = new StringBuilder();

    public ExecutePrize() {
        super();
        logger = Logger.getLogger(ExecutePrize.class);
    }

    @Override
    public void initBeforeStart() throws Exception {
        ws = new WSProcessor(logger, "../etc/webservice.cfg");
        db = new DbProcessor(dbName, logger);
        if (!MessageResponse.init) {
            HashMap listMsg = db.getListConfig("PROCESS");
            logger.info("====> MESSAGE CONFIG:\n" + listMsg);
            MessageResponse.setMessage(listMsg);
        }

        if (Common.iLoadConfig) {
            Common.mapPrize = db.loadPrize(1);
            logger.info("List prize: " + Common.mapPrize);
            Common.loadConfig(); 
//            Common.mapMpsConfig = db.loadMpsConfig();
        }
    }

    @Override
    public List<Record> validateContraint(List<Record> listRecord) throws Exception {
        return listRecord;
    }

    @Override
    public List<Record> processListRecord(List<Record> listMo) throws Exception {

        List<MtExtend> listMtExtend = new ArrayList();
        for (Record record : listMo) {
            ExecutePrizeObj executeObj = (ExecutePrizeObj) record;
            PrizeObj prizeObj = Common.mapPrize.get(executeObj.getPrizeId());
            // command code
            String cmdCode = prizeObj.getActionCode().split("_")[0];
            String amount = prizeObj.getActionCode().split("_")[1];
            // run command
            String rsWs = ws.executeCmd(executeObj.getMsisdn(), amount, cmdCode);

            if ("0".equals(rsWs)) {
                logger.info("Execute success: " + executeObj.getMsisdn() + ", " + prizeObj.getActionCode());
            } else {
                logger.error("Execute failed: " + executeObj.getMsisdn() + ", " + prizeObj.getActionCode());
            }
        }

        if (listMtExtend.size() > 0) {
            db.insertMtExtend(listMtExtend);
            listMtExtend.clear();
        }
        return listMo;
    }

    @Override
    public void printListRecord(List<Record> listRecord) throws Exception {
        br.setLength(0);
        br.append("\r\n").
                append("|\tID\t\t").
                append("|\tMSISDN\t\t").
                append("|\tPRIZE ID\r\n");
        for (Record record : listRecord) {
            ExecutePrizeObj resultObj = (ExecutePrizeObj) record;
            br.append("|\t").
                    append(resultObj.getId()).
                    append("\t|\t").
                    append(resultObj.getMsisdn()).
                    append("\t|\t").
                    append(resultObj.getPrizeId()).
                    append("\t|\r\n");
        }
        logger.info(br);
    }

    @Override
    public List<Record> processException(List<Record> listRecord) {
        logger.error("Exception when processing....");
        return listRecord;
    }

    @Override
    public boolean startProcessRecord() {
        return true;
    }

    private String validateMsisdn(String input) {
        if (input != null) {
            if (input.startsWith(Common.COUNTRY_CODE)) {
                return input;
            } else {
                return Common.COUNTRY_CODE + input;
            }
        }
        return Common.COUNTRY_CODE;
    }
}
