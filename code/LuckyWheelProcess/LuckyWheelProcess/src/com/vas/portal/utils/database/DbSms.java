/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.MtRecord;
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
public class DbSms extends DbProcessorAbstract {
    
    private String loggerLabel = DbSms.class.getSimpleName() + ": ";
    private final String SQL_DELETE_MT = "delete from mt where mt_id = ?";
    private final String SQL_INSERT_MT_HIS =
            "INSERT INTO mt_his "
            + "(MT_HIS_ID,MO_HIS_ID,MSISDN,RECEIVE_TIME,SENT_TIME,"
            + "MESSAGE,STATUS,CHANNEL,NODE_NAME,CLUSTER_NAME ) "
            + "VALUES(?,0,?,?,sysdate,?,?,?,?,?  )";
    private final int TIMES_ERROR = 3;
    
    public DbSms() {
    }
    
    public DbSms(String dbName, Logger logger) {
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
                result = iDeleteMt(listRecords);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_DELETE_MT);
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
    
    private int[] iDeleteMt(List<Record> listMt) throws Exception {
        
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_DELETE_MT);
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
            logTime("Time to iDeleteMt ", startTime);
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
                result = iInsertQueueHis(list);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert insertQueueHis");
                logger.error(br, ex);
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > AppManager.breakQuery) || (count > 3))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insertQueueHis\n");
                logger.error(br);
                break;
            }
            count++;
        }
        return result;
    }
    
    private int[] iInsertQueueHis(List<Record> listMo) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_MT_HIS);
            ps.setQueryTimeout(AppManager.queryDbTimeout);
            for (Record record : listMo) {
                MtRecord mtRecord = (MtRecord) record;
                ps.clearParameters();
                ps.setLong(1, mtRecord.getID());
                ps.setString(2, mtRecord.getMsisdn());
                ps.setTimestamp(3, mtRecord.getReceiveTime());
                ps.setString(4, mtRecord.getMessage());
                ps.setInt(5, 1);
                ps.setString(6, mtRecord.getChannel());
                ps.setString(7, mtRecord.getNodeName());
                ps.setString(8, mtRecord.getClusterName());
                
                ps.addBatch();
            }
            return ps.executeBatch();
            
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iInsertMtHis", startTime);
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
        MtRecord obj = new MtRecord();
        try {
            try {
                obj.setMtId(Long.parseLong(rs.getString(MtRecord.MT_ID)));
            } catch (Exception ex2) {
            }
            obj.setMsisdn(rs.getString(MtRecord.MSISDN));
            obj.setChannel(rs.getString(MtRecord.CHANNEL));
            obj.setMessage(rs.getString(MtRecord.MESSAGE));
            obj.setReceiveTime(rs.getTimestamp(MtRecord.RECEIVE_TIME));
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR parse MtRecord");
            logger.error(br, ex);
        }
        return obj;
    }
}
