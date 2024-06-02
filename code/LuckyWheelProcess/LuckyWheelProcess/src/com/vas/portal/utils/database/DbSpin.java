/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.ExecutePrizeObj;
import com.vas.portal.utils.object.SpinTotalObj;
import com.viettel.cluster.agent.integration.Record;
import com.viettel.smsfw.database.DbProcessorAbstract;
import com.viettel.smsfw.manager.AppManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sungroup
 */
public class DbSpin extends DbProcessorAbstract {

    private String loggerLabel = DbSpin.class.getSimpleName() + ": ";
    private final int TIMES_ERROR = 3;

    public DbSpin() {
    }

    public DbSpin(String dbName, Logger logger) {
        this.dbName = dbName;
        this.logger = logger;
        init(dbName, logger);
    }

    @Override
    public int[] deleteQueue(List<Record> listRecords) {

        return new int[listRecords.size()];
    }

    @Override
    public int[] insertQueueHis(List<Record> list) {
        return new int[list.size()];
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
        SpinTotalObj obj = new SpinTotalObj();
        try {
            obj.setId(rs.getLong(SpinTotalObj.ID));
            obj.setMsisdn(rs.getString(SpinTotalObj.MSISDN));
            obj.setCreatedTime(rs.getTimestamp(SpinTotalObj.CREATED_TIME));
            obj.setLastRenew(rs.getTimestamp(SpinTotalObj.LAST_RENEW));
            obj.setSpinNum(rs.getInt(SpinTotalObj.SPIN_NUM));
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR parse SpinTotalObj");
            logger.error(br, ex);
        }
        return obj;
    }
}
