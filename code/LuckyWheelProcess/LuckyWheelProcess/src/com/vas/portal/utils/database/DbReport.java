/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.ProcessedDate;
import com.viettel.cluster.agent.integration.Record;
import com.viettel.smsfw.database.DbProcessorAbstract;
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
public class DbReport extends DbProcessorAbstract {
//    private Logger logger;

    private String loggerLabel = DbReport.class.getSimpleName() + ": ";
    private int breakQuery = 120;
    private int dbQueryTimeout = 60;
    private int TIME_ERRORS = 3;
    private static final String SQL_UPDATE_LAST_PROCESS
            = "update processed_date set last_process = sysdate, last_value = ? where type_process = ?";

    public DbReport() {
    }

    public DbReport(String dbName, Logger logger) {
        this.dbName = dbName;
        this.logger = logger;
        init(dbName, logger);
    }

    @Override
    public int[] deleteQueue(List<Record> listRecords) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iUpdateProcessedDate(listRecords);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_UPDATE_LAST_PROCESS);
                logger.error(br, ex);
                count++;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query update ProcessedDate \n");
                logger.error(br);
                break;
            }
        }
        return result;
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
        ProcessedDate obj = new ProcessedDate();
        try {
            obj.setLastProcess(rs.getTimestamp("LAST_PROCESS"));
            obj.setTypeProcess(rs.getInt("TYPE_PROCESS"));
            obj.setLastValue(rs.getLong("LAST_VALUE"));
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR parse ProcessedDate");
            logger.error(br, ex);
        }
        return obj;
    }

    private int[] iUpdateProcessedDate(List<Record> listReg) throws Exception {

        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_UPDATE_LAST_PROCESS);
            ps.setQueryTimeout(dbQueryTimeout);

            ProcessedDate pDate = (ProcessedDate) listReg.get(0);
            ps.setLong(1, pDate.getLastValue());
            ps.setInt(2, pDate.getTypeProcess());
            ps.addBatch();
            result = ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to update ProcessedDate ", startTime);
        }
        return result;
    }

}
