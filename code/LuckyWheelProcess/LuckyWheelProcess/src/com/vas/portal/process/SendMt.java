/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.process;

import com.viettel.cluster.agent.integration.Record;
import com.vas.portal.utils.common.Common;
import com.vas.portal.utils.common.MessageResponse;
import com.vas.portal.utils.database.DbProcessor;
import com.vas.portal.utils.object.MtRecord;
import com.vas.portal.utils.service.WSProcessor;
import com.viettel.smsfw.process.ProcessRecordAbstract;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author
 */
public class SendMt extends ProcessRecordAbstract {

    private WSProcessor ws;
    private DbProcessor db;
    private StringBuilder br = new StringBuilder();

    public SendMt() {
        super();
        logger = Logger.getLogger(SendMt.class);
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
            Common.mapMpsConfig = db.loadMpsConfig();
        }
    }

    @Override
    public List<Record> validateContraint(List<Record> listRecord) throws Exception {

        return listRecord;
    }

    @Override
    public List<Record> processListRecord(List<Record> listMo) throws Exception {
        for (Record record : listMo) {
            MtRecord mt = (MtRecord) record;
            if (mt.getMessage() != null && mt.getMessage().trim().length() > 0) {
                for (String msg : mt.getMessage().split("\\^")) {
                    if (msg != null && msg.trim().length() > 0) {
                        ws.sendSms(mt.getMsisdn(), msg, mt.getChannel());
                    }
                }
            }
        }

        return listMo;
    }

    @Override
    public void printListRecord(List<Record> listRecord) throws Exception {
        br.setLength(0);
        br.append("\r\n").
                append("|\tMSISDN\t\t").
                append("|CHANNEL\t").
                append("|\tCONTENT\t\t\r\n");
        for (Record record : listRecord) {
            MtRecord mtRecord = (MtRecord) record;
            br.append("|\t").
                    append(mtRecord.getMsisdn()).
                    append("\t|").
                    append(mtRecord.getChannel()).
                    append("\t|\t").
                    append(mtRecord.getMessage()).
                    append("\t|\r\n");
            mtRecord.setNodeName(holder.getNodeName());
            mtRecord.setClusterName(holder.getClusterName());
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
}
