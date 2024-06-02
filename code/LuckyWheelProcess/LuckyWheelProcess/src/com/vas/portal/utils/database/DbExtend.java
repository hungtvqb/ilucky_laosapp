/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.AccountInfo;
import com.viettel.cluster.agent.integration.Record;
import com.viettel.smsfw.database.DbProcessorAbstract;
import java.sql.ResultSet;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sungroup
 */
public class DbExtend extends DbProcessorAbstract {
//    private Logger logger;

    private String loggerLabel = DbExtend.class.getSimpleName() + ": ";

    public DbExtend() {
    }

    public DbExtend(String dbName, Logger logger) {
        this.dbName = dbName;
        this.logger = logger;
        init(dbName, logger);
    }

    @Override
    public int[] deleteQueue(List<Record> listRecords) {
        return new int[listRecords.size()];
    }

    @Override
    public int[] insertQueueHis(List<Record> listRecords) {
        return new int[listRecords.size()];
    }

    @Override
    public int[] insertQueueOutput(List<Record> list) {
        return new int[list.size()];
    }

    @Override
    public void processTimeoutRecord(List<Long> list) {
    }

    @Override
    public Record parse(ResultSet rs) {
        AccountInfo obj = new AccountInfo();
        try {
            try {
                obj.setId(Long.parseLong(rs.getString(AccountInfo.ACCOUNT_ID)));
            } catch (Exception ex2) {
            }
            obj.setAccountId(rs.getString(AccountInfo.ACCOUNT_ID));
            obj.setMsisdn(rs.getString(AccountInfo.MSISDN));
            obj.setRegisterTime(rs.getTimestamp(AccountInfo.REGISTER_TIME));
            obj.setExpireTime(rs.getTimestamp(AccountInfo.EXPIRE_TIME));
            obj.setExtendStatus(rs.getInt(AccountInfo.EXTEND_STATUS));
            obj.setStatus(rs.getInt(AccountInfo.STATUS));
            obj.setAutoExtend(rs.getInt(AccountInfo.AUTO_EXTEND));
            obj.setLastExtend(rs.getTimestamp(AccountInfo.LAST_EXTEND));
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR parse RegisterObj");
            logger.error(br, ex);
        }
        return obj;
    }
}
