/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.database;

import com.vas.portal.utils.object.Teams;
import com.vas.portal.utils.common.Common;
import com.vas.portal.utils.object.AccountInfo;
import com.vas.portal.utils.object.CdrRechargeObj;
import com.vas.portal.utils.object.ChargeLog;
import com.vas.portal.utils.object.CurrentQuestion;
import com.vas.portal.utils.object.DailyCode;
import com.vas.portal.utils.object.DonatingObj;
import com.vas.portal.utils.object.ExecutePrizeObj;
import com.vas.portal.utils.object.LuckyCode;
import com.vas.portal.utils.object.MpsConfigObj;
import com.vas.portal.utils.object.MtExtend;
import com.vas.portal.utils.object.MtRecord;
import com.vas.portal.utils.object.PrizeObj;
import com.vas.portal.utils.object.PrizeWinner;
import com.vas.portal.utils.object.ProductInfo;
import com.vas.portal.utils.object.Questions;
import com.vas.portal.utils.object.RegisterInfo;
import com.vas.portal.utils.object.SpinTotalObj;
import com.vas.portal.utils.object.SvAdv;
import com.vas.portal.utils.object.SvAdvScheduler;
import com.vas.portal.utils.object.TopPlayer;
import com.vas.portal.utils.object.TransactionLog;
import com.vas.portal.utils.object.VotingCodeObj;
import com.vas.portal.utils.object.VotingObj;
import com.viettel.cluster.agent.integration.Record;
import com.viettel.smsfw.database.DbProcessorFW;
import com.viettel.smsfw.manager.AppManager;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sungroup
 */
public class DbProcessor extends DbProcessorFW {
//    private Logger logger;

    private String loggerLabel = DbProcessor.class.getSimpleName() + ": ";
//    private StringBuilder br = new StringBuilder();
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private int breakQuery = 120;
    private int dbQueryTimeout = 60;
    private int TIME_ERRORS = 3;
//    private String schema = "";
    private static final String SQL_GET_CONFIG = "SELECT PARAM_NAME, PARAM_VALUE, DEFAULT_VALUE FROM CONFIG WHERE UPPER(MODULE)=?";
    private static final String SQL_TRANS_LOG = "INSERT INTO TRANSACTION_LOG(TRANSACTION_ID, TYPE, MSISDN, REQUEST, RESPONSE,"
            + "ERROR_CODE, COMMAND, REQUEST_TIME, RESPONSE_TIME, CHANNEL, RESULT_VALUE) "
            + "VALUES (TRANSACTION_LOG_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String SQL_INSERT_CHARGE_LOG = "INSERT INTO charge_log (ID,MSISDN,FEE,CHARGE_TIME,INSERT_TIME,DESCRIPTION,PRODUCT_NAME) "
            + "VALUES(charge_log_seq.nextval, ?, ?, ?, sysdate, ?, ?)";
    private static final String SQL_INSERT_MT = "insert into mt(mt_id , mo_his_id , msisdn , message , retry_num , channel , receive_time) "
            + "values (mt_seq.nextval , 0  , ?  , ? , 0 , ? , sysdate) ";
    private static final String SQL_INSERT_MT_EXTEND = "insert into mt_extend(id , msisdn , message , retry_num , channel , receive_time, send_time, message_type, broadcast_id) "
            + "values (mt_extend_seq.nextval , ?  , ? , 0 , ? , sysdate, ?,  ?, ?) ";
    private String SQL_GET_REGISTER_WINNER = "SELECT * FROM register_history WHERE product_id = ? AND day_predict = ? AND trim(result_predict) = ?";
    private String SQL_GET_REGISTER_LOSER = "SELECT * FROM register_history WHERE product_id = ? AND day_predict = ? AND trim(result_predict) <> ?";

    private final String SQL_LOAD_PRODUCT = "SELECT * FROM PRODUCT WHERE status = 1";
    private final String SQL_LOAD_LOTTERY_PACKAGE = "SELECT * FROM LOTTERY_PACKAGE WHERE status = 1";
    private static final String SQL_CHECK_EXIST_RESULT = " Select rs.*, p.product_name from \n"
            + "    (SELECT a.id, a.product_id, a.result_day, a.result_value, a.import_time,\n"
            + "                  a.result_type, 'Processing' status\n"
            + "             FROM lottery_result a WHERE result_type = 1\n"
            + "             UNION ALL\n"
            + "              SELECT b.id, b.product_id, b.result_day, b.result_value, b.import_time,\n"
            + "                  b.result_type, 'Finished' status\n"
            + "             FROM lottery_result_his b WHERE result_type = 1 \n"
            + "     ) rs\n"
            + "    left join product p on p.id= rs.product_id \n"
            + "WHERE rs.product_id = ? AND result_type = 1 and result_day = trunc(?)";
    private static final String SQL_UPDATE_LAST_EXTEND
            = "Update account_user set last_extend = sysdate, last_update = sysdate WHERE account_id = ? And status = 1";
    private static final String SQL_CHECK_USED_MSISDN
            = "select * from reg_info WHERE msisdn = ?";
    private static final String SQL_CHECK_USED_ACCOUNT
            = "select * from account_user WHERE msisdn = ?";
    private static final String SQL_CHECK_USING_MSISDN
            = "select * from reg_info WHERE msisdn = ? and status = 1";
    private static final String SQL_ALREADY_GIFTED_MSISDN
            = "select * from gift_times WHERE msisdn = ?  ";
    private static final String SQL_CHECK_CURRENT_QUESTION
            = "select * from current_question WHERE msisdn = ?  ";
    private static final String SQL_SELECT_QUESTION_LIST
            = "SELECT * FROM (SELECT * FROM  QUESTIONS ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1 and question_level = ?";

    private static final String SQL_LOAD_TEAMS
            = "SELECT * FROM teams WHERE status = 1";
    private static final String SQL_LOAD_PRIZE_LIST
            = "select * from list_prize p \n"
            + "left join group_prize g on p.sub_group_prize = g.sub_group_id\n"
            + "where p.status = 1 ";
    private static final String SQL_INSERT_EXECUTE_PRIZE
            = "INSERT INTO execute_prize (ID,PRIZE_ID,MSISDN,INSERT_TIME) \n"
            + "VALUES(execute_prize_seq.nextval, ?, ?, sysdate)";
    private static final String SQL_INSERT_PRIZE_WINNER
            = "INSERT INTO prize_winner (ID,MSISDN,PRIZE_ID,CODE,CODE_TIME,PROCESS_TIME) \n"
            + "VALUES(prize_winner_seq.nextval, ?, ?, ?, ?, sysdate)";

    public DbProcessor() {
        super();
    }

    public DbProcessor(String dbName, Logger logger) {
        this.dbName = dbName;
        this.logger = logger;
        init(dbName, logger);
    }

    public HashMap getListConfig(String module) {
        HashMap mConfig = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (mConfig == null) {
            try {
                mConfig = iGetListConfig(module);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_GET_CONFIG);
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR select CONFIG");
                logger.error(br, ex);
                break;
            }
            if (mConfig == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query select CONFIG\n");
                logger.error(br);
                break;
            }
        }
        return mConfig;
    }

    private HashMap iGetListConfig(String moduleName) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        HashMap listConfig = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_GET_CONFIG);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, moduleName);

            rs = ps.executeQuery();
            String paramValue = "";
            listConfig = new HashMap();
            while (rs.next()) {
                paramValue = rs.getString("PARAM_VALUE");
                if (paramValue == null || paramValue.trim().length() == 0) {
                    paramValue = rs.getString("DEFAULT_VALUE");
                }
                if (paramValue == null) {
                    paramValue = "";
                }
                listConfig.put(rs.getString("PARAM_NAME").trim(), paramValue);
            }

        } catch (SQLException ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to select CONFIG", startTime);
        }
        return listConfig;
    }

    public HashMap<Long, ProductInfo> loadProduct() {
        HashMap result = null;
        int count = 1;
        while (result == null) {
            try {
                result = iLoadProduct();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_LOAD_PRODUCT);
                logger.error(br, ex);
                count++;
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query loadProduct \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private HashMap iLoadProduct() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        HashMap result = null;
        try {
            connection = getConnection(dbName);
            String sqlCheck = SQL_LOAD_PRODUCT;
            ps = connection.prepareStatement(sqlCheck);
            rs = ps.executeQuery();
            result = new HashMap();
            while (rs.next()) {
                ProductInfo product = new ProductInfo();
                product.setId(rs.getLong(ProductInfo.ID));
                product.setProductName(rs.getString(ProductInfo.PRODUCT_NAME));
                product.setProductType(rs.getInt(ProductInfo.PRODUCT_TYPE));
                product.setFee(rs.getDouble(ProductInfo.FEE));
                product.setPrize(rs.getString(ProductInfo.PRIZE));
                product.setBalanceId(rs.getString(ProductInfo.BALANCE_ID));
                String frameTime = rs.getString(ProductInfo.FRAME_TIME);
                if (frameTime != null && frameTime.trim().length() > 0) {
                    List frmList = new ArrayList();
                    String[] times = frameTime.split(",");
                    for (String string : times) {
                        String[] timeOcs = string.split("-");
                        frmList.add(timeOcs);
                    }
                    product.setFrameTime(frmList);
                }
                product.setNumRegInDay(rs.getInt(ProductInfo.NUM_REG_IN_DAY));
                product.setSubSelection(rs.getString(ProductInfo.SUB_SELECTION));
                product.setValueFormat(rs.getString(ProductInfo.VALUE_FORMAT));
                product.setGroupName(rs.getString(ProductInfo.GROUP_NAME));
                product.setPatternResult(rs.getString(ProductInfo.PATTERN_RESULT));
                product.setMultipleLapoula(rs.getLong(ProductInfo.MULTIPLE_LAPOULA));
                product.setMultipleBalance(rs.getLong(ProductInfo.MULTIPLE_BALANCE));
                //
                result.put(product.getId(), product);
            }

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iLoadProduct: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iLoadProduct", timeStart);
        }
        return result;
    }

    public int[] insertTransLog(List<TransactionLog> listTransLog) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertTransLog(listTransLog);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_TRANS_LOG);
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert TRANSACTION_LOG");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insert TRANSACTION_LOG\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertTransLog(List<TransactionLog> listTransLog) throws SQLException {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_TRANS_LOG);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (TransactionLog log : listTransLog) {
                ps.setInt(1, log.getType());
                ps.setString(2, log.getMsisdn());
                ps.setString(3, log.getRequest());
                ps.setString(4, log.getResponse());
                ps.setString(5, log.getErrorCode());
                ps.setString(6, log.getCommand());
                ps.setTimestamp(7, log.getRequestTime());
                ps.setTimestamp(8, log.getResponseTime());
                ps.setString(9, log.getChannel());
                ps.setString(10, log.getResultValue());

                ps.addBatch();
            }

            return ps.executeBatch();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insert TRANSACTION_LOG", startTime);
        }
    }

    public int[] insertChargeLog(List<ChargeLog> listTransLog) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertChargeLog(listTransLog);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_INSERT_CHARGE_LOG);
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert CHARGE_LOG");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insert CHARGE_LOG\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertChargeLog(List<ChargeLog> listLog) throws Exception {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_CHARGE_LOG);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (ChargeLog log : listLog) {
                ps.setString(1, log.getMsisdn());
                ps.setDouble(2, log.getFee());
                ps.setTimestamp(3, log.getChargeTime());
                ps.setString(4, log.getDescription());
                ps.setString(5, log.getProductName());

                ps.addBatch();
            }

            return ps.executeBatch();
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iInsertChargeLog", startTime);
        }
    }

    public int[] insertMt(List<MtRecord> listMt) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertMt(listMt);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_INSERT_MT);
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert MT");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insert MT \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertMt(List<MtRecord> listMt) throws Exception {
//        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_MT);
            ps.setQueryTimeout(AppManager.queryDbTimeout);

            for (MtRecord mt : listMt) {
                ps.clearParameters();
                ps.setString(1, mt.getMsisdn());
                ps.setString(2, mt.getMessage());
                ps.setString(3, mt.getChannel());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insert MT ", startTime);
        }
    }

    public int[] insertDailyCdrCount(int countCdr) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertDailyCdrCount(countCdr);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("insertDailyCdrCount");
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insertDailyCdrCount");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertDailyCdrCount \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertDailyCdrCount(int countCdr) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        String sql = "INSERT INTO daily_cdr_count (counter,day) \n"
                + "VALUES( ? , trunc(sysdate) )";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            ps.setQueryTimeout(AppManager.queryDbTimeout);

            ps.setInt(1, countCdr);
            ps.addBatch();
            result = ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertDailyCdrCount ", startTime);
        }
        return result;
    }

    public int[] insertMtExtend(List<MtExtend> listMt) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertMtExtend(listMt);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_INSERT_MT_EXTEND);
                logger.error(br, ex);
                count++;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insert SQL_INSERT_MT_EXTEND \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertMtExtend(List<MtExtend> listMt) throws Exception {

        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_MT_EXTEND);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            for (MtExtend mt : listMt) {
                ps.clearParameters();
                ps.setString(1, mt.getMsisdn());
                ps.setString(2, mt.getMessage());
                ps.setString(3, mt.getChannel());
                ps.setTimestamp(4, mt.getSendTime());
                ps.setInt(5, mt.getMessageType());
                ps.setLong(6, mt.getBroadcastId());
                ps.addBatch();
            }
            result = ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insert SQL_INSERT_MT_EXTEND ", startTime);
        }
        return result;
    }

    public Boolean checkExistResult(long productId, Timestamp resultDay) {
        Boolean result = null;
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = iCheckExistResult(productId, resultDay);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_CHECK_EXIST_RESULT);
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query checkExistResult \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private Boolean iCheckExistResult(long productId, Timestamp resultDay) throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            String sqlCheck = SQL_CHECK_EXIST_RESULT;
            ps = connection.prepareStatement(sqlCheck);
            ps.setLong(1, productId);
            ps.setTimestamp(2, resultDay);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iCheckExistResult: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iCheckExistResult", timeStart);
        }
    }

    public Boolean iCheckRegisterResult(String msisdn) {
        long timeBegin = System.currentTimeMillis();
        Boolean result = null;
        while (result == null) {
            try {
                result = checkRegisterResult(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR iCheckRegisterResult");
                logger.error(br, ex);
            }
            if (result == null && (System.currentTimeMillis() - timeBegin) / 1000 > AppManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query iCheckRegisterResult\n").
                        append((System.currentTimeMillis() - timeBegin) / 1000);
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean checkRegisterResult(String msisdn) throws SQLException {

        PreparedStatement ps = null;
        Connection connection = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM register_result WHERE msisdn = ? AND status = 1";
        try {
            connection = getConnection(dbName);
            setTimeSt(System.currentTimeMillis());
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.clearParameters();
            ps.setString(1, msisdn);

            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to checkRegisterResult");
        }
    }

    public String querySqlReport() {
        String result = null;
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = iQuerySqlReport();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(Common.SQL_REPORT);
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query querySqlReport \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private String iQuerySqlReport() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(Common.SQL_REPORT);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getString(1);
            }
            return "";
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iQuerySqlReport: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iQuerySqlReport", timeStart);
        }
    }

    public List<TopPlayer> getWeeklyTopPlayer() {
        List<TopPlayer> result = null;
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = iGetWeeklyTopPlayer();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR getWeeklyTopPlayer");
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK getWeeklyTopPlayer \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private List<TopPlayer> iGetWeeklyTopPlayer() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<TopPlayer> result = null;
        String sql = "select count(*) scores, msisdn from daily_code where insert_time <= trunc(sysdate) + " + Common.EXPIRE_HOUR + "/24 "
                + "group by msisdn "
                + "order by count(*) desc";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                TopPlayer topPlayer = new TopPlayer();
                topPlayer.setMsisdn(rs.getString("msisdn"));
                topPlayer.setScore(rs.getInt("scores"));
                result.add(topPlayer);
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR getWeeklyTopPlayer: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to getWeeklyTopPlayer", timeStart);
        }
    }

    public int[] deleteLuckyCode() {
        int[] result = null;
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = iDeleteLuckyCode();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR deleteLuckyCode");
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK deleteLuckyCode \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iDeleteLuckyCode() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "delete from lucky_code where expire_time  <  sysdate";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            ps.addBatch();
            return ps.executeBatch();
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iDeleteLuckyCode: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iDeleteLuckyCode", timeStart);
        }
    }

    public int insertLuckyCodeHis() {
        int result = 0;
        int count = 0;
        while (true) {
            try {
                count++;
                result = iInsertLuckyCodeHis();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertLuckyCodeHis");
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK insertLuckyCodeHis \n");
                logger.error(br);
                break;
            }
            break;
        }
        return result;
    }

    private int iInsertLuckyCodeHis() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "insert into lucky_code_his "
                + "SELECT a.id, a.msisdn, a.code, a.insert_time, a.expire_time, a.period,\n"
                + "       a.status, a.confirm_time, a.confirm_channel, a.fee,  sysdate from lucky_code a where expire_time < sysdate and status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            return ps.executeUpdate();
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR insertLuckyCodeHis: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertLuckyCodeHis", timeStart);
        }
    }

    public int[] insertWeeklyPrize(TopPlayer topPlayer) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertWeeklyPrize(topPlayer);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertWeeklyPrize ");
                logger.error(br, ex);
                count++;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertWeeklyPrize \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertWeeklyPrize(TopPlayer topPlayer) throws Exception {

        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        int[] result = null;
        String sql = "INSERT INTO weekly_prize (ID,MSISDN,SCORE, PROCESS_TIME,MONEY) "
                + "VALUES(weekly_prize_seq.nextval, ? , ?,  sysdate, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.clearParameters();
            ps.setString(1, topPlayer.getMsisdn());
            ps.setInt(2, topPlayer.getScore());
            ps.setInt(3, Common.PRIZE_WEEKLY);
            ps.addBatch();
            result = ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Exception : ", ex);
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iInsertWeeklyPrize ", startTime);
        }
        return result;
    }

    public Boolean disableAutoExtend(AccountInfo accountInfo) {
        long timeSt = System.currentTimeMillis();
        Boolean result = false;

        while (true) {
            result = iDisableAutoExtend(accountInfo);
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK disableAutoExtend ==> ERROR DATABASE\n").
                            append(accountInfo);
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private Boolean iDisableAutoExtend(AccountInfo accountInfo) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql1 = "update account_user set status = 0, last_update = sysdate  where account_id=? and status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql1);

            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, accountInfo.getAccountId());
            ps.execute();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).append(new Date()).
                    append("\nERROR iDisableAutoExtend: ").
                    append(sql1).append("\n").
                    append(accountInfo);
            logger.error(br, ex);
            return null;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iDisableAutoExtend", timeStart);
        }
    }

    public int[] updateLastExtend(List<AccountInfo> listAccount) {
        long timeSt = System.currentTimeMillis();
        int[] result = null;

        while (true) {
            result = iUpdateLastExtend(listAccount);
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK iUpdateLastExtend ");
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private int[] iUpdateLastExtend(List<AccountInfo> listAccount) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_UPDATE_LAST_EXTEND);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (AccountInfo accountInfo : listAccount) {
                ps.clearParameters();
                ps.setString(1, accountInfo.getAccountId());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).append(new Date()).
                    append("\nERROR iUpdateLastExtend: ");
            logger.error(br, ex);
            return null;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iUpdateLastExtend", timeStart);
        }
    }

    public List<RegisterInfo> getListRegToSend() {
        List<RegisterInfo> result = null;
        int count = 1;
        while (result == null) {
            try {
                result = iGetListRegToSend();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("Get list register");
                logger.error(br, ex);
                count++;
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query getListReg \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private List iGetListRegToSend() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<RegisterInfo> result = null;
        try {
            connection = getConnection(dbName);
            String sqlCheck = "SELECT * FROM REG_INFO WHERE STATUS = 1 AND register_time < trunc(sysdate)";
            ps = connection.prepareStatement(sqlCheck);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                RegisterInfo regInfo = new RegisterInfo();
                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
                regInfo.setMsisdn(rs.getString("MSISDN"));
                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
                regInfo.setEndTime(rs.getTimestamp("END_TIME"));
                regInfo.setExpireTime(rs.getTimestamp("EXPIRE_TIME"));
                regInfo.setAlreadyCharged(rs.getDouble("ALREADY_CHARGED"));
                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
                regInfo.setLastExtend(rs.getTimestamp("LAST_EXTEND"));
                regInfo.setStatus(rs.getInt("STATUS"));
                //
                result.add(regInfo);
            }

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iGetListReg: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iGetListReg", timeStart);
        }
        return result;
    }

    public List<RegisterInfo> getRegisterInfo(String msisdn) {
        List<RegisterInfo> result = null;
        int count = 1;
        while (result == null) {
            try {
                result = iGetRegisterInfo(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("Get getRegisterInfo");
                logger.error(br, ex);
                count++;
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query getRegisterInfo \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private List iGetRegisterInfo(String msisdn) throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<RegisterInfo> result = null;
        try {
            connection = getConnection(dbName);
            String sqlCheck = "SELECT * FROM REG_INFO WHERE msisdn = ? AND STATUS = 1";
            ps = connection.prepareStatement(sqlCheck);
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                RegisterInfo regInfo = new RegisterInfo();
                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
                regInfo.setMsisdn(rs.getString("MSISDN"));
                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
                regInfo.setEndTime(rs.getTimestamp("END_TIME"));
                regInfo.setExpireTime(rs.getTimestamp("EXPIRE_TIME"));
                regInfo.setAlreadyCharged(rs.getDouble("ALREADY_CHARGED"));
                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
                regInfo.setLastExtend(rs.getTimestamp("LAST_EXTEND"));
                regInfo.setStatus(rs.getInt("STATUS"));
                regInfo.setPlayedTimes(rs.getInt("PLAYED_TIMES"));
                regInfo.setNumQuestions(rs.getInt("NUM_QUESTIONS"));
                //
                result.add(regInfo);
            }

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iGetRegisterInfo: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iGetRegisterInfo", timeStart);
        }
        return result;
    }

    public List<DailyCode> getDailyCode() {
        List<DailyCode> result = null;
        int count = 1;
        while (result == null) {
            try {
                result = iGetDailyCode();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("GetdailyCode");
                logger.error(br, ex);
                count++;
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query getDailyCode \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private List iGetDailyCode() throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<DailyCode> result = null;
        try {
            connection = getConnection(dbName);
            String sql = "SELECT * FROM DAILY_CODE WHERE insert_time >= trunc(sysdate)";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                DailyCode dailyCode = new DailyCode();
                dailyCode.setId(rs.getLong("ID"));
                dailyCode.setMsisdn(rs.getString("MSISDN"));
                dailyCode.setCode(rs.getString("CODE"));
                dailyCode.setInsertTime(rs.getTimestamp("INSERT_TIME"));
                //
                result.add(dailyCode);
            }

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR iGetDailyCode: ").append(ex.toString());
            logger.error(br, ex);
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iGetDailyCode", timeStart);
        }
        return result;
    }

    public Boolean checkRegistered(String msisdn, String dbName) {
        Boolean registered = null;
        try {
            registered = iCheckRegistered(msisdn, dbName);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkRegistered");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckRegistered(String msisdn, String dbName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_CHECK_USED_MSISDN);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iCheckRegistered");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check registered", startTime);
        }
    }

    public Boolean checkRegisteredAccount(String msisdn, String dbName) {
        Boolean registered = null;
        try {
            registered = iCheckRegisteredAccount(msisdn, dbName);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR iCheckRegisteredAccount");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckRegisteredAccount(String msisdn, String dbName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_CHECK_USED_ACCOUNT);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iCheckRegisteredAccount");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check iCheckRegisteredAccount", startTime);
        }
    }

    public Boolean checkUsing(String msisdn) {
        Boolean registered = null;
        try {
            registered = iCheckUsing(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkUsing");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckUsing(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_CHECK_USING_MSISDN);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR checkUsing");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to checkUsing", startTime);
        }
    }

    public Boolean checkAlreadyGifted(String msisdn) {
        Boolean result = null;
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = iCheckAlreadyGifted(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_ALREADY_GIFTED_MSISDN);
                logger.error(br, ex);
            }
            if (count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("==> BREAK query checkAlreadyGifted \n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private Boolean iCheckAlreadyGifted(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_ALREADY_GIFTED_MSISDN);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR checkAlreadyGifted");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to checkAlreadyGifted", startTime);
        }
    }

    public boolean iInsertRegisterInfo(RegisterInfo regInfo) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertRegisterInfo(regInfo);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR iInsertRegisterInfo ");
                logger.error(br, ex);
                count++;
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK iInsertRegisterInfo\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertRegisterInfo(RegisterInfo regInfo) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "INSERT INTO reg_info (REGISTER_ID,MSISDN,REGISTER_TIME,EXTEND_STATUS,STATUS,EXPIRE_TIME,LAST_EXTEND,END_TIME,ALREADY_CHARGED,PLAYED_TIMES,NUM_QUESTIONS) \n"
                + "VALUES(reg_info_seq.nextval, ? , sysdate , 0, 1, ?, null, null, 0, 0 , ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, regInfo.getMsisdn());
            ps.setTimestamp(2, regInfo.getExpireTime());
            ps.setInt(3, Common.NUM_QUESTIONS);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertRegisterInfo", startTime);
        }
        return rs;
    }

    public boolean iInsertGiftTimes(String msisdn) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        while (result == false) {
            try {
                count++;
                result = insertGiftTimes(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertGiftTime ");
                logger.error(br, ex);
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertGiftTime\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertGiftTimes(String msisdn) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "INSERT INTO gift_times (ID,MSISDN,INSERT_TIME,STATUS,LAST_UPDATE) "
                + "VALUES(gift_times_seq.nextval, ?, sysdate, 0, sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, msisdn);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertGiftTime", startTime);
        }
        return rs;
    }

    public boolean iUpdateGifted(String msisdn) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        while (result == false) {
            try {
                count++;
                result = updateGifted(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR updateGifted ");
                logger.error(br, ex);
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK updateGifted\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean updateGifted(String msisdn) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "UDPATE gift_times set status = 1, last_update = sysdate where msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, msisdn);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateGifted", startTime);
        }
        return rs;
    }

    public Boolean deleteCurrentQuestion(String msisdn) {
        Boolean registered = null;
        try {
            registered = iDeleteCurrentQuestion(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR deleteCurrentQuestion");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iDeleteCurrentQuestion(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String sql = "DELETE from current_question where msisdn = ?";
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR deleteCurrentQuestion");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to deleteCurrentQuestion", startTime);
        }
    }

    public Boolean checkCurrentQuestion(String msisdn) {
        Boolean registered = null;
        try {
            registered = iCheckCurrentQuestion(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkCurrentQuestion");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckCurrentQuestion(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_CHECK_CURRENT_QUESTION);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iCheckCurrentQuestion");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iCheckCurrentQuestion", startTime);
        }
    }

    public CurrentQuestion getCurrentQuestion(String msisdn) {
        CurrentQuestion registered = null;
        try {
            registered = iGetCurrentQuestion(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getCurrentQuestion");
            logger.error(br, ex);
        }

        return registered;
    }

    private CurrentQuestion iGetCurrentQuestion(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String sql = "select * from current_question where msisdn = ?  ";
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                CurrentQuestion curQuest = new CurrentQuestion();
                curQuest.setId(rs.getLong(CurrentQuestion.ID));
                curQuest.setMsisdn(rs.getString(CurrentQuestion.MSISDN));
                curQuest.setQuestionId(rs.getLong(CurrentQuestion.QUESTION_ID));
                curQuest.setAnswer(rs.getString(CurrentQuestion.ANSWER));
                curQuest.setStartTime(rs.getTimestamp(CurrentQuestion.START_TIME));
                curQuest.setExpireTime(rs.getTimestamp(CurrentQuestion.EXPIRE_TIME));
                return curQuest;
            }
            return null;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getCurrentQuestion");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getCurrentQuestion", startTime);
        }
    }

    public boolean iInsertCurrentQuestion(String msisdn, Questions question) {
        long timeSt = System.currentTimeMillis();
        boolean result = false;

        while (true) {
            result = insertCurrentQuestion(msisdn, question);
            if (result != true) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK insertCurrentQustion ==> ERROR DATABASE");
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private boolean insertCurrentQuestion(String msisdn, Questions question) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sql = "INSERT INTO current_question (ID,MSISDN,QUESTION_ID,START_TIME,EXPIRE_TIME, ANSWER) \n"
                + "VALUES(current_question_seq.nextval, ?, ?, sysdate, sysdate + ?/86400, ?)";

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, msisdn);
            ps.setLong(2, question.getQuestionId());
            ps.setInt(3, question.getTimeout());
            ps.setString(4, question.getAnswer().trim());
            return ps.execute();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR insertCurrentQuestion: ");
            logger.error(br, ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertCurrentQuestion", timeStart);
        }
        return false;
    }

    public boolean iUpdateCurrentQuestion(String msisdn, Questions question) {
        long timeSt = System.currentTimeMillis();
        boolean result = false;

        while (true) {
            result = updateCurrentQuestion(msisdn, question);
            if (result != true) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK updateCurrentQuestion ==> ERROR DATABASE");
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private boolean updateCurrentQuestion(String msisdn, Questions question) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sql = "UPDATE current_question set question_id = ?, start_time = sysdate, expire_time = sysdate + ?/86400, answer = ? "
                + " where msisdn = ?";

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setLong(1, question.getQuestionId());
            ps.setInt(2, question.getTimeout());
            ps.setString(3, question.getAnswer().trim());
            ps.setString(4, msisdn);
            return ps.execute();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updateCurrentQuestion: ");
            logger.error(br, ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to updateCurrentQuestion", timeStart);
        }
        return false;
    }

    public boolean iInsertDailyCode(String msisdn, String code) {
        long timeSt = System.currentTimeMillis();
        boolean result = false;

        while (true) {
            result = insertDailyCode(msisdn, code);
            if (result != true) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK insertDailyCode ==> ERROR DATABASE\n").
                            append(msisdn);
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private boolean insertDailyCode(String msisdn, String code) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sql = "INSERT INTO daily_code (ID,MSISDN,CODE,INSERT_TIME) \n"
                + "VALUES(daily_code_seq.nextval, ?, ?, sysdate)";

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setString(1, msisdn);
            ps.setString(2, code);
            return ps.execute();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR insertDailyCode: ").
                    append(ex.toString()).
                    append("\n").
                    append(msisdn);
            logger.error(br, ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertDailyCode", timeStart);
        }
        return false;
    }

    public boolean iBuyQuestion(long registerId) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = buyQuestion(registerId);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR buyQuestion ");
                logger.error(br, ex);
                count++;
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK buyQuestion\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean buyQuestion(long registerId) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "UPDATE reg_info set num_questions = num_questions + 1, played_times = played_times + 1  WHERE register_id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setLong(1, registerId);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to buyQuestion", startTime);
        }
        return rs;
    }

    public boolean iStartQuestion(long registerId) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = startQuestion(registerId);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR startQuestion ");
                logger.error(br, ex);
                count++;
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK startQuestion\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean startQuestion(long registerId) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "UPDATE reg_info set played_times = played_times + 1  WHERE register_id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setLong(1, registerId);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to startQuestion", startTime);
        }
        return rs;
    }

    public boolean iCancelService(long registerId) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = cancelService(registerId);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR cancelService ");
                logger.error(br, ex);
                count++;
            }
            if ((result == false && System.currentTimeMillis() - timeBegin > AppManager.breakQuery)
                    || count >= 3) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK cancelService\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean cancelService(long registerId) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "UPDATE reg_info set status = 0, end_time = sysdate WHERE register_id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }

            ps.setLong(1, registerId);
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to cancelService", startTime);
        }
        return rs;
    }

    public List<Questions> getRandomQuestion(int questionLevel) {
        List<Questions> questionList = new ArrayList<Questions>();
        try {
            questionList = iGetRandomQuestion(questionLevel);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRandomQuestion");
            logger.error(br, ex);
        }

        return questionList;
    }

    private List<Questions> iGetRandomQuestion(int questionLevel) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Questions> questionList = new ArrayList<Questions>();
        try {
            connection = getConnection(dbName);
            setTimeSt(System.currentTimeMillis());
            ps = connection.prepareStatement(SQL_SELECT_QUESTION_LIST);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setInt(1, questionLevel);
            rs = ps.executeQuery();
            while (rs.next()) {
                Questions question = new Questions();
                question.setQuestionId(rs.getLong("QUESTION_ID"));
                question.setQuestionContent(rs.getString("question_content"));
                question.setAnswer(rs.getString("answer"));
                question.setQuestionLevel(rs.getInt("question_level"));
                question.setTimeout(rs.getInt("timeout"));
                questionList.add(question);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetRandomQuestion");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to get random question");
        }
        return questionList;
    }

    public HashMap loadTeam() throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        HashMap result = null;
        setTimeSt(System.currentTimeMillis());
        Connection connection = null;
        String sql = SQL_LOAD_TEAMS;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            result = new HashMap();
            while (rs.next()) {
                Teams team = new Teams();
                team.setId(rs.getInt("ID"));
                team.setTeamCode(rs.getString("TEAM_CODE").toUpperCase());
                team.setTeamName(rs.getString("TEAM_NAME"));
                team.setStatus(rs.getInt("STATUS"));
                result.put(team.getVotedCode(), team);
            }
            return result;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to loadTeam");
        }
    }

    public int[] insertVoting(List<VotingObj> listVote) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO VOTING (ID,MSISDN,VOTED_ID,VOTED_CODE,VOTE_VALUE,VOTE_TIME,PRO_CODE)"
                + " VALUES(VOTING_seq.nextval,?, ?, ?, ?, sysdate, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (VotingObj voting : listVote) {
                ps.clearParameters();
                ps.setString(1, voting.getMsisdn());
                ps.setLong(2, voting.getVotedId());
                ps.setString(3, voting.getVotedCode());
                ps.setLong(4, voting.getVoteValue());
                ps.setString(5, voting.getProCode());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Error insertVoting", ex);
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertVoting", timeStart);
        }
        return null;
    }

    public int[] insertDonating(List<DonatingObj> listDonate) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO DONATING (ID,MSISDN,TEAM_ID,TEAM_CODE,DONATE_VALUE,DONATE_TIME)"
                + " VALUES(DONATING_seq.nextval, ?, ?, ?, ?, sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (DonatingObj voting : listDonate) {
                ps.clearParameters();
                ps.setString(1, voting.getMsisdn());
                ps.setLong(2, voting.getTeamId());
                ps.setString(3, voting.getTeamCode());
                ps.setLong(4, voting.getDonateValue());
                ps.addBatch();
            }
            return ps.executeBatch();
        } catch (Exception ex) {
            logger.error("Error insertDonating", ex);
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertDonating", timeStart);
        }
        return null;
    }

    public boolean insertCdrRecharge(List<CdrRechargeObj> listCdr) {
        boolean rs = false;
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO CDR_RECHARGE (ID,MSISDN,RECHARGE_TIME,INSERT_TIME,MONEY,CHANNEL)"
                + " VALUES(CDR_RECHARGE_seq.nextval,?, ?, sysdate, ?, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (CdrRechargeObj cdrRecharge : listCdr) {
                ps.clearParameters();
                ps.setString(1, cdrRecharge.getMsisdn());
                ps.setTimestamp(2, cdrRecharge.getRechargeTime());
                ps.setLong(3, cdrRecharge.getMoney());
                ps.setString(4, cdrRecharge.getChannel());
                ps.addBatch();
            }
            ps.executeBatch();
            rs = true;
        } catch (Exception ex) {
            logger.error("Error insertCdrRecharge", ex);
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertCdrRecharge", timeStart);
        }
        return rs;
    }

    public List<CdrRechargeObj> getListCdr(long lastValue) {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<CdrRechargeObj> result = null;
        String sql = "select * from cdr_recharge where id > ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setLong(1, lastValue);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                CdrRechargeObj cdr = new CdrRechargeObj();
                cdr.setId(rs.getLong(CdrRechargeObj.ID));
                cdr.setMsisdn(rs.getString(CdrRechargeObj.MSISDN));
                cdr.setMoney(rs.getLong(CdrRechargeObj.MONEY));
                cdr.setRechargeTime(rs.getTimestamp(CdrRechargeObj.RECHARGE_TIME));
                cdr.setInsertTime(rs.getTimestamp(CdrRechargeObj.INSERT_TIME));
                cdr.setChannel(rs.getString(CdrRechargeObj.CHANNEL));
                result.add(cdr);
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR getListCdr: ").append(ex.toString());
            logger.error(br, ex);
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to getListCdr", timeStart);
        }
    }

    public int[] insertLuckyCode(List<LuckyCode> listLc) {
        long timeSt = System.currentTimeMillis();
        int[] result = null;

        while (true) {
            result = iInsertLuckyCode(listLc);
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK insertLuckyCode ==> ERROR DATABASE\n").
                            append(listLc.toString());
                    logger.error(br);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    private int[] iInsertLuckyCode(List<LuckyCode> listLc) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sql = "INSERT INTO lucky_code (ID,MSISDN,CODE,INSERT_TIME,EXPIRE_TIME,PERIOD,STATUS,CONFIRM_TIME,CONFIRM_CHANNEL) \n"
                + "VALUES(lucky_code_seq.nextval, ?, ?, sysdate, ?, ?, ?, NULL, NULL)";

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (LuckyCode code : listLc) {
                ps.clearParameters();
                ps.setString(1, code.getMsisdn());
                ps.setString(2, code.getCode());
                ps.setTimestamp(3, code.getExpireTime());
                ps.setInt(4, code.getPeriod());
                ps.setInt(5, code.getStatus());
                ps.addBatch();
            }
            return ps.executeBatch();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR insertLuckyCode: ").
                    append(ex.toString()).
                    append("\n").
                    append(listLc.toString());
            logger.error(br, ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertLuckyCode", timeStart);
        }
        return null;
    }

    public HashMap loadPrize(int isAuto) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        HashMap result = null;
        setTimeSt(System.currentTimeMillis());
        Connection connection = null;
        String sql = SQL_LOAD_PRIZE_LIST;
        try {
            connection = getConnection(dbName);
            if (isAuto >= 0) {
                sql += " and is_auto = ?";
            }
            sql += " order by sub_group_prize";
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            if (isAuto >= 0) {
                ps.setInt(1, isAuto);
            }
            rs = ps.executeQuery();
            result = new HashMap<>();
            while (rs.next()) {
                PrizeObj prize = new PrizeObj();
                prize.setId(rs.getInt(PrizeObj.ID));
                prize.setNumberPrize(rs.getInt(PrizeObj.NUMBER_PRIZE));
                prize.setActionCode(rs.getString(PrizeObj.ACTION_CODE));
                prize.setPeriod(rs.getInt(PrizeObj.PERIOD));
                prize.setChannel(rs.getInt(PrizeObj.CHANNEL));
                prize.setDescription(rs.getString(PrizeObj.DESCRIPTION));
                prize.setTimesPrize(rs.getInt(PrizeObj.TIMES_PRIZE));
                prize.setStatus(rs.getInt(PrizeObj.STATUS));
                prize.setIsTop(rs.getInt(PrizeObj.IS_TOP));
                prize.setPrizeName(rs.getString(PrizeObj.PRIZE_NAME));
                prize.setIsAuto(rs.getInt(PrizeObj.IS_AUTO));
                prize.setNumberPrizeType(rs.getInt(PrizeObj.NUMBER_PRIZE_TYPE));
                prize.setGroupPrize(rs.getInt(PrizeObj.GROUP_ID));
                prize.setSubGroupPrize(rs.getInt(PrizeObj.SUB_GROUP_PRIZE));
                String prefix = rs.getString(PrizeObj.PREFIX_MSISDN);
                if (prefix != null && !prefix.trim().isEmpty()) {
                    prize.setPrefixMsisdn(Arrays.asList(prefix.split(",")));
                }
                result.put(prize.getId(), prize);
            }
            return result;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to loadPrize");
        }
    }

    public int getCountLuckyCode(int channel, int period, int times) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        int result = 0;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "select count(*) count_times from lucky_code lc \n"
                + "where expire_time < sysdate and status = 1"
                + " and period = ? and decode(count_times, 0, 0, 1, 1, 2) = ?  ";
        if (channel >= 0) {
            sql = sql + " and decode(nvl(confirm_channel,-1), 1, 1, -1, -1, 0) = ? ";
        }

        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setInt(1, period);
            ps.setInt(2, times);
            if (channel >= 0) {
                ps.setInt(3, channel);
            }
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("count_times");
            }
            return result;
        } catch (SQLException ex) {
            logger.error("Error get getCountLuckyCode", ex);
            return 0;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getCountLuckyCode", startTime);
        }
    }

    public List<LuckyCode> getRandomWinnerLimited(int channel, int period, int times, int countWinner) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<LuckyCode> result = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "select * from (select * from lucky_code lc \n"
                + "where expire_time < sysdate and status = 1 "
                + "and period = ? and decode(count_times, 0, 0, 1, 1, 2) = ? ";
        if (channel >= 0) {
            sql = sql + "and decode(nvl(confirm_channel,-1), 1, 1, -1, -1, 0) = ? ";
        }
        sql = sql + "order by dbms_random.value) where rownum <= ? ";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setInt(1, period);
            ps.setInt(2, times);
            if (channel >= 0) {
                ps.setInt(3, channel);
                ps.setInt(4, countWinner);
            } else {
                ps.setInt(3, countWinner);
            }
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                LuckyCode lc = new LuckyCode();
                lc.setMsisdn(rs.getString("MSISDN"));
                lc.setCode(rs.getString("CODE"));
                lc.setConfirmTime(rs.getTimestamp("CONFIRM_TIME"));
                result.add(lc);
            }
            return result;
        } catch (SQLException ex) {
            logger.error("Error get getRandomWinner", ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getRandomWinner", startTime);
        }
    }

    public List<LuckyCode> getRandomWinner(int channel, int period, int times) {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<LuckyCode> result = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "select * from (select * from lucky_code lc \n"
                + "where expire_time < sysdate and status = 1 "
                + "and period = ? and decode(count_times, 0, 0, 1, 1, 2) = ? ";
        if (channel >= 0) {
            sql = sql + "and decode(nvl(confirm_channel,-1), 1, 1, -1, -1, 0) = ? ";
        }
        sql = sql + "order by dbms_random.value) ";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setInt(1, period);
            ps.setInt(2, times);
            if (channel >= 0) {
                ps.setInt(3, channel);
            }
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                LuckyCode lc = new LuckyCode();
                lc.setMsisdn(rs.getString("MSISDN"));
                lc.setCode(rs.getString("CODE"));
                lc.setConfirmTime(rs.getTimestamp("CONFIRM_TIME"));
                result.add(lc);
            }
            return result;
        } catch (SQLException ex) {
            logger.error("Error get getRandomWinner", ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getRandomWinner", startTime);
        }
    }

    public int[] insertExecutePrize(List<ExecutePrizeObj> listCmd) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertExecutePrize(listCmd);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("insertExecutePrize");
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insertExecutePrize");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertExecutePrize\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertExecutePrize(List<ExecutePrizeObj> listCmd) throws Exception {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_EXECUTE_PRIZE);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (ExecutePrizeObj log : listCmd) {
                ps.setLong(1, log.getPrizeId());
                ps.setString(2, log.getMsisdn());
                ps.addBatch();
            }

            return ps.executeBatch();
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to iInsertExecutePrize", startTime);
        }
    }

    public HashMap loadMpsConfig() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        HashMap<String, MpsConfigObj> mMps = new HashMap();
        long startTime = System.currentTimeMillis();
        String sql = "SELECT * FROM MPS_CONFIG";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                MpsConfigObj mpsConfig = new MpsConfigObj();
                mpsConfig.setActionCode(rs.getString(MpsConfigObj.ACTION_CODE));
                mpsConfig.setCategory(rs.getString(MpsConfigObj.CATEGORY));
                mpsConfig.setCommand(rs.getString(MpsConfigObj.COMMAND));
                mpsConfig.setCpName(rs.getString(MpsConfigObj.CP_NAME));
                mpsConfig.setPrice(rs.getString(MpsConfigObj.PRICE));
                mpsConfig.setService(rs.getString(MpsConfigObj.SERVICE));
                mpsConfig.setSubService(rs.getString(MpsConfigObj.SUB_SERVICE));
                mpsConfig.setKeyPath(rs.getString(MpsConfigObj.KEY_PATH));
                mMps.put(mpsConfig.getActionCode(), mpsConfig);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR loadMpsConfig");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to load mps config", startTime);
        }
        return mMps;
    }

    public int[] insertPrizeWinner(List<PrizeWinner> listCmd) {
        int[] result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == null) {
            try {
                result = iInsertPrizeWinner(listCmd);
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append("insertPrizeWinner");
                logger.error(br, ex);
                count++;
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insertPrizeWinner");
                logger.error(br, ex);
                break;
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > breakQuery) || (count > TIME_ERRORS))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertPrizeWinner\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertPrizeWinner(List<PrizeWinner> listCmd) throws Exception {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_INSERT_PRIZE_WINNER);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
//            ID,MSISDN,PRIZE_ID,CODE,CODE_TIME,PROCESS_TIME
            for (PrizeWinner log : listCmd) {
                ps.setString(1, log.getMsisdn());
                ps.setLong(2, log.getPrizeId());
                ps.setString(3, log.getCode());
                ps.setTimestamp(4, log.getCodeTime());
                ps.addBatch();
            }

            return ps.executeBatch();
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertPrizeWinner", startTime);
        }
    }

    public VotingCodeObj iGetVotedByCode(String code) {
        VotingCodeObj result = null;
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        while (result == null) {
            try {
                count++;
                result = getVotedByCode(code);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count);
                logger.error(br, ex);
            }
            if (result == null && System.currentTimeMillis() - timeBegin > AppManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).append("==>BREAK query select getVotedByCode\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private VotingCodeObj getVotedByCode(String code) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        VotingCodeObj result = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "select * from voting_code WHERE code = ?  and status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setString(1, code);
            rs = ps.executeQuery();
            result = new VotingCodeObj();
            if (rs.next()) {
                result.setId(rs.getInt("ID"));
                result.setCode(rs.getString("CODE"));
                result.setName(rs.getString("NAME"));
                result.setProCode(rs.getString("PRO_CODE"));
            }
            return result;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getLuckyCode", startTime);
        }
    }

    public List<String> loadBlackList() {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List<String> result = null;
        String sql = "select msisdn from blacklist ";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                result.add(rs.getString("msisdn"));
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("ERROR loadBlackList: ").append(ex.toString());
            logger.error(br, ex);
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to loadBlackList", timeStart);
        }
    }

    public List<SvAdvScheduler> loadListSvAdvSchedule() {
        ResultSet rs = null;
        PreparedStatement ps = null;
        List<SvAdvScheduler> result = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = " select d.* from sv_adv_schedule s \n"
                + "join sv_adv_schedule_detail d on s.id = d.schedule_id and d.status = 1 \n"
                + "where s.from_date <= sysdate and s.to_date >= sysdate and s.status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            ps.setQueryTimeout(dbQueryTimeout);

            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                SvAdvScheduler sv = new SvAdvScheduler();
                sv.setId(rs.getInt(SvAdvScheduler.ID));
                sv.setScheduleId(rs.getInt(SvAdvScheduler.SCHEDULE_ID));
                sv.setSvAdvId(rs.getInt(SvAdvScheduler.SV_ADV_ID));
                sv.setPercent(rs.getInt(SvAdvScheduler.PERCENT));
//                sv.setSvAdv(getServiceAdv(sv.getSvAdvId()));
                SvAdv svAdv = new SvAdv();
                svAdv.setSvCode(rs.getString(SvAdv.SV_CODE));
                svAdv.setMsgAdv(rs.getString(SvAdv.MSG_ADV));
                svAdv.setChannelAdv(rs.getString(SvAdv.CHANNEL_ADV));
                svAdv.setMessageType(rs.getInt(SvAdv.MESSAGE_TYPE));
                svAdv.setFromMoney(rs.getLong(SvAdv.FROM_MONEY));
                svAdv.setScheduleDetailId(sv.getId());
                sv.setSvAdv(svAdv);
                result.add(sv);
            }

        } catch (SQLException ex) {
            logger.error("Exception : ", ex);
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to loadListSvAdvSchedule", startTime);
        }
        return result;
    }

    public Boolean updateCdrCounter(int scheduleDetailId, int increament) {
        CallableStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall("{call count_cdr(? , ?)}");
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            ps.setInt(1, scheduleDetailId);
            ps.setInt(2, increament);
            return ps.execute();
        } catch (Exception ex) {
            logger.error("Error update cdr counter", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to updateCdrCounter", startTime);
        }
        return false;
    }

    public Boolean updateRenewSpin(List<Record> listRecord) {
        boolean rs = false;
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "Update spin_total set last_renew = sysdate, spin_num = ? WHERE id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (AppManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(AppManager.queryDbTimeout);
            }
            for (Record record : listRecord) {
                SpinTotalObj spinObj = (SpinTotalObj) record;
                ps.setInt(1, spinObj.getSpinNum());
                ps.setLong(2, spinObj.getId());
                ps.addBatch();
            }
            ps.executeBatch();
            rs = true;
        } catch (Exception ex) {
            logger.error("Error updateRenewSpin", ex);
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateRenewSpin", timeStart);
        }
        return rs;
    }

}
