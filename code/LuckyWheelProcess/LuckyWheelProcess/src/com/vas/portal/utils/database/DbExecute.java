/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.ExecutePrizeObj;
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
public class DbExecute extends DbProcessorAbstract {

    private String loggerLabel = DbExecute.class.getSimpleName() + ": ";
    private final String SQL_DELETE_EXECUTE_PRIZE = "delete from execute_prize where id = ?";
    private final String SQL_INSERT_EXECUTE_PRIZE_HIS
            = "INSERT INTO execute_prize_his (ID,PRIZE_ID,MSISDN,INSERT_TIME,PROCESS_TIME) "
            + "VALUES(?, ?, ?, ?, sysdate)";
    private final int TIMES_ERROR = 3;

    public DbExecute() {
    }

    public DbExecute(String dbName, Logger logger) {
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
                result = iDeleteQueue(listRecords);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_DELETE_EXECUTE_PRIZE);
                logger.error(br, ex);
                count++;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > AppManager.breakQuery) || (count > TIMES_ERROR))) {
                br.setLength(0);
                br.append(loggerLabel).append("==> BREAK query update deleteQueue \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iDeleteQueue(List<Record> listMt) throws Exception {

        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_DELETE_EXECUTE_PRIZE);
            ps.setQueryTimeout(AppManager.queryDbTimeout);
            for (Record record : listMt) {
                ps.setLong(1, record.getID());
                ps.addBatch();
            }
            result = ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iDeleteQueue execute_prize ", startTime);
        }
        return result;
    }

    @Override
    public int[] insertQueueHis(List<Record> list) {
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        int[] result = null;
        while (result == null) {
            try {
                result = iInsertExecutePrizeHis(list);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert execute_prize_his");
                logger.error(br, ex);
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > AppManager.breakQuery) || (count > 3))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insert execute_prize_his\n");
                logger.error(br);
                break;
            }
            count++;
        }
        return result;
    }

    private int[] iInsertExecutePrizeHis(List<Record> listMo) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_EXECUTE_PRIZE_HIS);
            ps.setQueryTimeout(AppManager.queryDbTimeout);
            for (Record record : listMo) {
                ExecutePrizeObj executeObj = (ExecutePrizeObj) record;
                ps.clearParameters();
                ps.setLong(1, executeObj.getID());
                ps.setLong(2, executeObj.getPrizeId());
                ps.setString(3, executeObj.getMsisdn());
                ps.setTimestamp(4, executeObj.getInsertTime());

                ps.addBatch();
            }
            return ps.executeBatch();

        } catch (Exception ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iInsertExecutePrizeHis", startTime);
        }
        return null;
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
        ExecutePrizeObj obj = new ExecutePrizeObj();
        try {
            obj.setId(rs.getLong(ExecutePrizeObj.ID));
            obj.setMsisdn(rs.getString(ExecutePrizeObj.MSISDN));
            obj.setPrizeId(rs.getLong(ExecutePrizeObj.PRIZE_ID));
            obj.setInsertTime(rs.getTimestamp(ExecutePrizeObj.INSERT_TIME));
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR parse ExecutePrizeObj");
            logger.error(br, ex);
        }
        return obj;
    }
}
