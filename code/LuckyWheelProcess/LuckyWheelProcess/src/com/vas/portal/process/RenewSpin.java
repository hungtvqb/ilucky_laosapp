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
import com.vas.portal.utils.object.SpinTotalObj;
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
public class RenewSpin extends ProcessRecordAbstract {

    private WSProcessor ws;
    private DbProcessor db;
    private StringBuilder br = new StringBuilder();

    public RenewSpin() {
        super();
        logger = Logger.getLogger(RenewSpin.class);
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
    public List<Record> processListRecord(List<Record> listRecord) throws Exception {

        List<MtExtend> listMtExtend = new ArrayList();
        for (Record record : listRecord) {
            SpinTotalObj spinTotal = (SpinTotalObj) record;
            spinTotal.setSpinNum(Common.FREE_SPIN);
        }

        db.updateRenewSpin(listRecord);

        if (listMtExtend.size() > 0) {
            db.insertMtExtend(listMtExtend);
            listMtExtend.clear();
        }
        return listRecord;
    }

    @Override
    public void printListRecord(List<Record> listRecord) throws Exception {
        br.setLength(0);
        br.append("\r\n").
                append("|\tID\t\t").
                append("|\tMSISDN\t\t").
                append("|\tSPIN\r\n");
        for (Record record : listRecord) {
            SpinTotalObj resultObj = (SpinTotalObj) record;
            br.append("|\t").
                    append(resultObj.getId()).
                    append("\t|\t").
                    append(resultObj.getMsisdn()).
                    append("\t|\t").
                    append(resultObj.getSpinNum()).
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
