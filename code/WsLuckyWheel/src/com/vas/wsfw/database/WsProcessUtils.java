/*
 * @DbProcess.java	version 1.0	27/02/2010
 *
 * Copyright 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.database;

import com.vas.wsfw.common.WebserviceManager;
import static com.vas.wsfw.database.DbProcessorAbstract.TIME_BREAK;
import com.vas.wsfw.obj.AccountInfo;
import com.vas.wsfw.obj.AdsObj;
import com.vas.wsfw.obj.ChargeLog;
import com.vas.wsfw.obj.ConfirmOtpObj;
import com.vas.wsfw.obj.HistoryPlay;
import com.vas.wsfw.obj.HistoryPrize;
import com.vas.wsfw.obj.MissionObj;
import com.vas.wsfw.obj.MpsConfigObj;
import com.vas.wsfw.obj.PointTotalObj;
import com.vas.wsfw.obj.PrizeObj;
import com.vas.wsfw.obj.PrizeQuantity;
import com.vas.wsfw.obj.ProductInfo;
import com.vas.wsfw.obj.Questions;
import com.vas.wsfw.obj.RankingObj;
import com.vas.wsfw.obj.RegisterInfo;
import com.vas.wsfw.obj.SmsMtObj;
import com.vas.wsfw.obj.SpinGift;
import com.vas.wsfw.obj.SpinRecharge;
import com.vas.wsfw.obj.SpinTotalObj;
import com.vas.wsfw.obj.TopPlayer;
import com.vas.wsfw.obj.TransactionLog;
import com.vas.wsfw.obj.UserInfo;
import com.vas.wsfw.obj.UserPlayObj;
import com.vas.wsfw.obj.UserReport;
import com.vas.wsfw.obj.WebServiceLog;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;

/**
 * Cac truy van de CSDL he thong
 *
 * @author NhungTTT3
 * @since 27-02-2010
 */
public class WsProcessUtils extends DbProcessorAbstract {

//    private long TIME_LAST_QUERY;
    private Logger logger;
    private String loggerLabel = "WsProcessUtils: ";
    private int ERRORTIME = 3;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
//    private String schema;
    // Them phan check user
    private static final String sqlGetUserReport
            = "select * from user_report where username = ? and status = 1";
    private final String SQL_GET_USER_BY_MSISDN
            = "select * from account_user WHERE msisdn = ? and status <> 0";
    private final String SQL_CHECK_USED_MSISDN = "select * from REG_INFO WHERE msisdn = ? and product_name = ?";
    private final String sqlGetUser = "select * from ws_user where username = ?";
    private final String sqlCheckUser = "select * from ws_user where username = ? and password=?";
    private final String sqlCheckCheckPass = "SELECT * FROM blacklist_Password WHERE password = ?";
    private static final String SQL_SELECT_QUESTION_LIST
            = "SELECT * FROM (SELECT * FROM  QUESTIONS ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= ?";
//    private final String SQL_GET_REGISTER_INFO
//            = "SELECT * FROM REG_INFO WHERE MSISDN = ? and product_name = ? and status = 1";
//    private final String SQL_GET_REGISTER_INFO_ALL
//            = "SELECT * FROM REG_INFO WHERE MSISDN = ? and status = 1";
//    private final String SQL_GET_REGISTER_IN_DAY
//            = "SELECT * FROM REG_INFO WHERE MSISDN = ? and product_name = ? and expire_time > sysdate order by register_time desc";
    private final String SQL_GET_TOTAL_MONEY_PAID
            = "select nvl(sum(money),0) money from history_play where money is not null and play_day > trunc(sysdate)";
//    private final String SQL_LOAD_PRIZE
//            = "SELECT * FROM LIST_PRIZE WHERE status = 1 ";
    private final String SQL_SELECT_PRIZE
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_PRIZE_NOTX2
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where type <> 2 or type_add <> 2 ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_REGISTER
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where type <> 4 or number_add = 0 ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_REGISTER_NOTX2
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where (type <> 4 or number_add = 0) and (type <> 2 or type_add <> 2) ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_MONEY
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where type <> 2 or (type = 2 and type_add = 1 and number_add < 2) ORDER BY DBMS_RANDOM.RANDOM) "
            + "WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_MONEY_NOTX2
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where (type <> 2 or (type = 2 and type_add = 1 and number_add < 2)) and (type <> 2 or type_add <> 2) ORDER BY DBMS_RANDOM.RANDOM) "
            + "WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_REGISTER_NO_MONEY
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where (number_add <= 0) or (type = 1) or (type = 2 and type_add = 1 and number_add < 2) ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_SELECT_WINNER_NO_REGISTER_NO_MONEY_NOTX2
            = "SELECT * FROM (SELECT * FROM  LIST_PRIZE where (number_add <= 0 or (type = 1) or (type = 2 and type_add = 1 and number_add < 2)) and (type <> 2 or type_add <> 2) ORDER BY DBMS_RANDOM.RANDOM) WHERE status = 1 and rownum <= 1";
    private final String SQL_GET_CURRENT_RANK
            = "select no, spin_count from (select msisdn, spin_count, rownum no from "
            + "(select * from ranking where sub_type = ? and rank_type = ? and period = ? and end_time > sysdate order by spin_count desc, last_update)) "
            + "where msisdn = ?";
//    private final String SQL_GET_CURRENT_RANK_UNSUB
//            = "select no, spin_count from (select msisdn, spin_count, rownum no from "
//            + "(select * from ranking where sub_type = 0 and end_time > sysdate order by spin_count desc)) "
//            + "where msisdn = ?";
    private static final String SQL_CHECK_TIMES_PLAY
            = "SELECT * FROM REG_INFO WHERE MSISDN = ? and status = 1";
    private static final String SQL_INSERT_HISTORY_PLAY_WINNER
            = "insert into history_play(id,list_id, register_id,MSISDN,product_name, play_day,spin_times,played_times,type,type_add,number_add, money, balance_id,note) "
            + "values(history_play_seq.NEXTVAL, ?,?,?,?, sysdate, ?, ?, ?,?,?,?,?,?)";
    private static final String SQL_TRANS_LOG
            = "INSERT INTO TRANSACTION_LOG(TRANSACTION_ID, TYPE, MSISDN, REQUEST, RESPONSE,"
            + "ERROR_CODE, COMMAND, REQUEST_TIME, RESPONSE_TIME, CHANNEL) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public WsProcessUtils(String dbName, Logger logger) {
        this.logger = logger;
        this.dbName = dbName;
        init(dbName, logger);
    }

    public WsProcessUtils() {
        super();
    }

    /**
     * Close Statement
     *
     * @param stmt
     * @param smName
     */
    private void closeStatement(PreparedStatement stmt, String smName) {
        if (stmt != null) {
            try {
                br.setLength(0);
                br.append(loggerLabel).
                        append("Close Statement: ").
                        append(smName);
                logger.debug(br);
                stmt.close();
                stmt = null;
            } catch (SQLException ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR close Statement ").
                        append(smName).
                        append(": ").
                        append(ex.getMessage());
                logger.warn(br);
                stmt = null;
            }
        }
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
//        schema = ConnectionPoolManager.getConnectionMap().get(dbName).getUser() + ".";
    }

    public String getDbName() {
        return this.dbName;
    }

    public HashMap getConfig(String module) throws Exception {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        HashMap listMessage = new HashMap();
        PreparedStatement getConfigSt = null;
        String sql = "select PARAM_NAME, PARAM_VALUE, PARAM_VALUE_FR from CONFIG where upper(module)=?";

        br.setLength(0);
        br.append(loggerLabel).
                append(" GET CONFIG: ").
                append(sql);
        logger.info(br);
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            getConfigSt = connection.prepareStatement(sql);
            getConfigSt.setString(1, module.toUpperCase());
            rs = getConfigSt.executeQuery();
            while (rs.next()) {
                listMessage.put(rs.getString("PARAM_NAME"), rs.getString("PARAM_VALUE"));
                listMessage.put(rs.getString("PARAM_NAME") + "_FR", rs.getString("PARAM_VALUE_FR"));
            }
        } catch (Exception ex) {
            logger.error(loggerLabel + "ERROR getListMessage", ex);

            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(getConfigSt, sql);
            closeConnection(connection);
            logTime("Time to getConfig", timeStart);
        }
        return listMessage;
    }

    protected void closeResource(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (SQLException e) {
            this.logger.warn(e);
            rs = null;
        }
    }

    protected void closeResource(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        } catch (SQLException e) {
            this.logger.warn(e);
            stmt = null;
        }
    }

    protected void closeResource(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException e) {
            this.logger.warn(e);
            conn = null;
        }
    }

    /**
     * Kiem tra thue bao dang su dung dich vu hay khong
     *
     * @param msisdn
     * @param productName
     * @return
     */
    /**
     * Ghi log webservice
     *
     * @param ws
     * @return
     */
    public boolean iInsertWsLog(WebServiceLog ws) {
        long timeSt = System.currentTimeMillis();
        boolean result = false;

        while (true) {
            result = insertWsLog(ws);
            if (result != true) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK iInsertWsLog ==> ERROR DATABASE\n").
                            append(ws.toString());
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

    private boolean insertWsLog(WebServiceLog ws) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sqlInsertWSLog = "insert into ws_log (ID,REQUEST,RESPONSE,REQUESTTIME,RESPONSETIME,RESPONSECODE,USERID,CHANNEL,IPCLIENT,MSISDN,FEE,WS_NAME)"
                + "values ( WS_LOG_SEQ.NEXTVAL,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )";

        br.setLength(0);
        br.append(loggerLabel).
                append("Insert WEBSERVICE_LOG:\n").
                append(ws.toString());
        logger.debug(br);

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlInsertWSLog);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setString(1, ws.getRequest().toString());
            ps.setString(2, ws.getResponse().toString());
            ps.setTimestamp(3, ws.getRequestTime());
            ps.setTimestamp(4, ws.getResponseTime());
            ps.setString(5, ws.getResponseCode());
            ps.setLong(6, ws.getUserId());
            ps.setString(7, ws.getChannel());
            ps.setString(8, ws.getIpClient());
            ps.setString(9, ws.getMsisdn());
            ps.setDouble(10, ws.getFee());
            ps.setString(11, ws.getWsName());
            return ps.execute();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR Insert to WEBSERVICE_LOG: ").
                    append(ex.toString()).
                    append("\n").
                    append(ws.toString());
            logger.error(br, ex);
        } finally {
            closeStatement(ps, sqlInsertWSLog);
            closeConnection(connection);
            logTime("Time to iInsertWsLog", timeStart);
        }
        return false;
    }

    public Boolean updateAddMoreTimes(long registerId, int times) {

        try {
            return iUpdateAddMoreTimes(registerId, times);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateAddMoreTimes");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean iUpdateAddMoreTimes(long registerId, int times) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "UPDATE reg_info SET number_spin = number_spin + ? WHERE register_id = ? AND status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setInt(1, times);
            ps.setLong(2, registerId);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iUpdateAddMoreTimes");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iUpdateAddMoreTimes", timeStart);
        }
    }

    public Boolean updateRenewReg(RegisterInfo regInfo) {

        try {
            return iUpdateRenewReg(regInfo);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateRegisterInfo");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean iUpdateRenewReg(RegisterInfo regInfo) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "update reg_info set expire_time=?, number_spin=? , extend_status=?, status = ?,   played_times = ?, last_extend = sysdate "
                + "  where register_id=?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setTimestamp(1, regInfo.getExpireTime());
            ps.setInt(2, regInfo.getNumberSpin());
            ps.setInt(3, regInfo.getExtendStatus());
            ps.setInt(4, regInfo.getStatus());
            ps.setInt(5, regInfo.getPlayedTimes());
            ps.setLong(6, regInfo.getRegisterId());

            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iUpdateRenewReg");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iUpdateRenewReg", timeStart);
        }
    }

    public boolean iInsertChargeLog(ChargeLog ws) {
        long timeSt = System.currentTimeMillis();
        boolean result = false;

        while (true) {
            result = insertChargeLog(ws);
            if (result != true) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK iInsertChargeLog ==> ERROR DATABASE\n").
                            append(ws.toString());
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

    private boolean insertChargeLog(ChargeLog ws) {

        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        String sql = "INSERT INTO charge_log (ID,MSISDN,FEE,CHARGE_TIME,INSERT_TIME,DESCRIPTION,PACKG_NAME) \n"
                + "VALUES(charge_log_seq.nextval, ?, ?, ?, sysdate, ?, ?)";

        br.setLength(0);
        br.append(loggerLabel).
                append("Insert WEBSERVICE_LOG:\n").
                append(ws.toString());
        logger.debug(br);

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setString(1, ws.getMsisdn());
            ps.setDouble(2, ws.getFee());
            ps.setTimestamp(3, ws.getChargeTime());
            ps.setString(4, ws.getDescription());
            ps.setString(5, ws.getPackgName());
            return ps.execute();

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR insertChargeLog: ").
                    append(ex.toString()).
                    append("\n").
                    append(ws.toString());
            logger.error(br, ex);
        } finally {
            closeStatement(ps, sql);
            closeConnection(connection);
            logTime("Time to insertChargeLog", timeStart);
        }
        return false;
    }

    public UserInfo iGetUser(String userName) {
        UserInfo user = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (user == null) {
            try {
                user = getUserInfo(userName);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).append("\n").append(sqlGetUser);
                logger.error(br, ex);
                count++;
            }
            if (user == null && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).append("==>BREAK query select VTFREE_USER\n");
                logger.error(br);
                break;
            }
        }
        return user;
    }

    /**
     * Lay thong tin nguoi dung
     *
     * @param userName
     * @return
     * @throws SQLException
     */
    private UserInfo getUserInfo(String userName) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        UserInfo user = null;
        long timeStart = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetUser);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, userName);
            rs = ps.executeQuery();
            user = new UserInfo();
            if (rs.next()) {
                user.setWsUserId(rs.getLong(UserInfo.WS_USER_ID));
                user.setUser(userName);
                user.setPass(rs.getString(UserInfo.PASS));
                user.setIp(rs.getString(UserInfo.IP));
            }
            return user;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps, sqlGetUser);
            closeConnection(connection);
            logTime("Time to getUserInfo", timeStart);
        }
    }

    /**
     * Lay mo_seq de insert db
     *
     * @return
     */
    public Long iGetMoSequence() {
        long timeSt = System.currentTimeMillis();
        Long result = null;

        while (true) {
            result = getMoSequence();
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK getMoSequence ==> ERROR DATABASE\n");
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

    private Long getMoSequence() {
        long timeStart = System.currentTimeMillis();
        String sqlGetMoSeq = "select mo_seq.nextval SEQ from dual";

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetMoSeq);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            //check MSISDN
            while (rs.next()) {
                return rs.getLong("SEQ");
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getMoSequence").
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeStatement(ps, sqlGetMoSeq);
            closeResultSet(rs);
            closeConnection(connection);
            logTime("Time to getMoSequence", timeStart);
        }
        return null;
    }

    public int[] insertMt(SmsMtObj mt) {
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        int[] result = null;
        while (result == null) {
            try {
                count++;
                result = iInsertMt(mt);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert insertMt");
                logger.error(br, ex);
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) || (count > 3))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insertMt\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertMt(SmsMtObj mt) throws Exception {
        PreparedStatement ps = null;
        Connection connection = null;
        String sqlInsertMt = "insert into MT(MT_ID,MO_HIS_ID,MSISDN,MESSAGE,RECEIVE_TIME,RETRY_NUM,CHANNEL) "
                + "values(MT_SEQ.NEXTVAL, 0, ?, ?, sysdate, 0, ?)";
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlInsertMt);
            ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            ps.clearParameters();
            ps.setString(1, mt.getMsisdn());
            ps.setString(2, mt.getMessage());
            ps.setString(3, mt.getChannel());
            ps.addBatch();
            return ps.executeBatch();

        } catch (Exception ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertMt", startTime);
        }
        return null;
    }

    public int[] insertMt(String msisdn, String message, String channel) {
        long timeBegin = System.currentTimeMillis();
        int count = 0;
        int[] result = null;
        while (result == null) {
            try {
                count++;
                result = iInsertMt(msisdn, message, channel);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).
                        append(new Date()).
                        append("\nERROR insert insertMt");
                logger.error(br, ex);
            }
            if (result == null && ((System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) || (count > 3))) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK query insertMt\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private int[] iInsertMt(String msisdn, String message, String channel) {
        PreparedStatement ps = null;
        Connection connection = null;
        String sqlInsertMt = "insert into MT(MT_ID,MO_HIS_ID,MSISDN,MESSAGE,RECEIVE_TIME,RETRY_NUM,CHANNEL) "
                + "values(MT_SEQ.NEXTVAL, 0, ?, ?, sysdate, 0, ?)";
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlInsertMt);
            ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            ps.clearParameters();
            ps.setString(1, msisdn);
            ps.setString(2, message);
            ps.setString(3, channel);
            ps.addBatch();
            return ps.executeBatch();

        } catch (Exception ex) {
            logger.error("Exception : ", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertMt", startTime);
        }
        return null;
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
            if ((result == false && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery)
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
        String sql = "insert into reg_info (register_id, msisdn, product_name, register_time, number_spin, played_times, extend_status, status, expire_time) "
                + "values (reg_info_seq.nextval, ?, ?, sysdate, ?, ?, 0, 1, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setString(1, regInfo.getMsisdn());
            ps.setString(2, regInfo.getProductName());
            ps.setInt(3, regInfo.getNumberSpin());
            ps.setInt(4, regInfo.getPlayedTimes());
            ps.setTimestamp(5, regInfo.getExpireTime());
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

    public Boolean checkRegistered(String msisdn, String packName) {
        Boolean registered = null;
        try {
            registered = iCheckRegistered(msisdn, packName);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkRegistered");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckRegistered(String msisdn, String packName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_CHECK_USED_MSISDN);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, packName);
            rs = ps.executeQuery();
            if (rs.next()) {
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

    public int getTotalMoneyPaid() {
        try {
            return iGetTotalMoneyPaid();
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getTotalMoneyPaid");
            logger.error(br, ex);
        }
        return 3000;
    }

    private int iGetTotalMoneyPaid() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_GET_TOTAL_MONEY_PAID);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            //ps.setInt(1, questionLevel);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("money");
            }
            return 0;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetTotalMoneyPaid");
            logger.error(br, ex);
            return 3000;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetTotalMoneyPaid", startTime);
        }
    }

    public LinkedHashMap loadPrize(int isAuto, int isPad) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        LinkedHashMap result = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "select * from list_prize p \n"
                + "left join group_prize g on p.sub_group_prize = g.sub_group_id\n"
                + "where p.status = 1 ";
        try {
            connection = getConnection(dbName);
            if (isAuto >= 0) {
                sql += " and is_auto = ?";
            }
            if (isPad >= 0) {
                sql += " and is_pad = ?";
            }
            sql += " order by sub_group_prize";
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            if (isAuto >= 0) {
                ps.setInt(i++, isAuto);
            }
            if (isPad >= 0) {
                ps.setInt(i++, isPad);
            }
            rs = ps.executeQuery();
            result = new LinkedHashMap<>();
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
                prize.setIsPad(rs.getInt(PrizeObj.IS_PAD));
                prize.setPrizeName(rs.getString(PrizeObj.PRIZE_NAME));
                prize.setImage(rs.getString(PrizeObj.IMAGE));
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
            logTime("Time to loadPrize", startTime);
        }
    }

    public List<PrizeQuantity> getRandomPrize(String showroom, int programId) {
        List<PrizeQuantity> listPrize = new ArrayList();
        try {
            listPrize = iGetRandomPrize(showroom, programId);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRandomPrize");
            logger.error(br, ex);
        }
        return listPrize;
    }

    private List<PrizeQuantity> iGetRandomPrize(String showroom, int programId) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<PrizeQuantity> prizeList = new ArrayList();
        long startTime = System.currentTimeMillis();
//        String sql = "select * from \n"
//                + "(select d.*, t.id total_id, \n"
//                + "  nvl(t.quantity,0) total_quantity, nvl(t.prized,0) total_prized\n"
//                + " from daily_prize_config d  \n"
//                + "left join total_prize_config t on d.prize_id = t.prize_id and t.account = d.account and t.program_id= d.program_id \n"
//                + "where  d.account = ? and d.program_id  = ? and d.prize_date = trunc(sysdate) )\n"
//                + "where   quantity > prized order by quantity";
        String sql = "select * from "
                + "   total_prize_config d "
                + "where  d.account = ? and d.program_id  = ?  "
                + "and   quantity > prized order by DBMS_RANDOM.RANDOM ";
        try {
            connection = getConnection(dbName);

            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, showroom);
            ps.setInt(2, programId);
            rs = ps.executeQuery();
            while (rs.next()) {
                PrizeQuantity prize = new PrizeQuantity();
                // addition
                prize.setId(rs.getInt("ID"));
                prize.setTotalId(rs.getInt("ID"));
                prize.setAccount(rs.getString("ACCOUNT"));
                prize.setPrizeId(rs.getInt("PRIZE_ID"));
//                prize.setPrizeDate(rs.getString("PRIZE_DATE"));
                prize.setQuantity(rs.getInt("QUANTITY"));
                prize.setPrized(rs.getInt("PRIZED"));
//                prize.setTotalQuantity(rs.getInt("TOTAL_QUANTITY"));
//                prize.setTotalPrized(rs.getInt("TOTAL_PRIZED"));
                prizeList.add(prize);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetRandomPrize");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetRandomPrize", startTime);
        }
        return prizeList;

    }

    public Boolean addPrizeQuantity(long id, long totalId) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps1 = null;
        PreparedStatement ps2 = null;
        Connection connection = null;
        String sql1 = "UPDATE daily_prize_config SET prized=prized+1 ,last_update = sysdate WHERE id = ?";
        String sql2 = "UPDATE total_prize_config SET prized=prized+1 ,last_update = sysdate WHERE id = ?";
        try {
            connection = getConnection(dbName);
            connection.setAutoCommit(false);
            ps1 = connection.prepareStatement(sql1);
            ps2 = connection.prepareStatement(sql2);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps1.setQueryTimeout(WebserviceManager.queryDbTimeout);
                ps2.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps1.setLong(1, id);
            ps1.executeUpdate();
            ps2.setLong(1, totalId);
            ps2.executeUpdate();

            connection.commit();
            connection.setAutoCommit(true);

            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updatePointTotal");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps1);
            closeResource(ps2);
            closeResource(connection);
            logTime("Time to addPrizeQuantity", timeStart);
        }
    }

    public List<HistoryPrize> getMyPrizes(String msisdn) {
        try {
            return iGetMyPrizes(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getMyPrizes");
            logger.error(br, ex);
        }

        return null;
    }

    private List<HistoryPrize> iGetMyPrizes(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        List<HistoryPrize> result = null;
        String sql = "select * from (Select msisdn, to_char(play_day,'dd/MM/yyyy HH24:mi:ss') play_day, play_day play_time, note from history_play "
                + "where msisdn = ? order by play_time desc) where rownum <= 10";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                HistoryPrize obj = new HistoryPrize();
                obj.setMsisdn(rs.getString("MSISDN"));
                obj.setPlayDay(rs.getString("play_day"));
                obj.setNote(rs.getString("note"));
                result.add(obj);
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetMyPrizes");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetMyPrizes", startTime);
        }
    }

    public List<TopPlayer> getCurrentTopPlayer(int subType, int rankType, int period) {
        try {
            return iGetCurrentTopPLayer(subType, rankType, period);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getCurrentTopPlayer");
            logger.error(br, ex);
        }

        return null;
    }

    private List<TopPlayer> iGetCurrentTopPLayer(int subType, int rankType, int period) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        List<TopPlayer> result = null;
        String sql = "Select a.* , rownum from (select * from ranking where sub_type = ? and rank_type = ? and period = ? "
                + "and end_time > sysdate order by spin_count desc,last_update) a "
                + "where end_time > sysdate and rownum <= 10";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, subType);
            ps.setInt(2, rankType);
            ps.setInt(3, period);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                TopPlayer obj = new TopPlayer();
                obj.setMsisdn(rs.getString("MSISDN"));
                obj.setSpinCount(rs.getInt("SPIN_COUNT"));
                obj.setRank(rs.getInt("ROWNUM"));
                result.add(obj);
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetCurrentTopPLayer");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetCurrentTopPLayer", startTime);
        }
    }

    /**
     *
     * @param msisdn
     * @return Integer[order, spin_count]
     */
    public Integer[] iGetCurrentRank(String msisdn, int subType, int rankType, int period) {
        try {
            return getCurrentRank(msisdn, subType, rankType, period);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR iGetCurrentRank");
            logger.error(br, ex);
        }

        return null;
    }

    private Integer[] getCurrentRank(String msisdn, int subType, int rankType, int period) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();

        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_GET_CURRENT_RANK);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, subType);
            ps.setInt(2, rankType);
            ps.setInt(3, period);
            ps.setString(4, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                int no = rs.getInt("NO");
                int spin = rs.getInt("spin_count");
                return new Integer[]{no, spin};
            }
            return new Integer[]{0, 0};
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getCurrentRank");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to get current rank", startTime);
        }
    }

//    public Integer[] iGetCurrentRankUnsub(String msisdn) {
//        try {
//            return getCurrentRankUnsub(msisdn);
//        } catch (Exception ex) {
//            br.append(loggerLabel).append(new Date()).append("\nERROR iGetCurrentRankUnsub");
//            logger.error(br, ex);
//        }
//
//        return null;
//    }
//
//    private Integer[] getCurrentRankUnsub(String msisdn) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection connection = null;
//        long startTime = System.currentTimeMillis();
//
//        try {
//            connection = getConnection(dbName);
//            ps = connection.prepareStatement(SQL_GET_CURRENT_RANK_UNSUB);
//            if (WebserviceManager.queryDbTimeout > 0) {
//                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
//            }
//            ps.setString(1, msisdn);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                int no = rs.getInt("NO");
//                int spin = rs.getInt("spin_count");
//                return new Integer[]{no, spin};
//            }
//            return new Integer[]{0, 0};
//        } catch (Exception ex) {
//            br.setLength(0);
//            br.append(loggerLabel).
//                    append(new Date()).
//                    append("\nERROR getCurrentRankUnsub");
//            logger.error(br, ex);
//            return null;
//        } finally {
//            closeResource(rs);
//            closeResource(ps);
//            closeResource(connection);
//            logTime("Time to get current rank unsub", startTime);
//        }
//    }
//    public List<RegisterInfo> getTimesPlay(String msisdn) {
//        List<RegisterInfo> questionNumberTimesPlay = new ArrayList<RegisterInfo>();
//        try {
//            questionNumberTimesPlay = iGetTimesPlay(msisdn);
//        } catch (Exception ex) {
//            br.append(loggerLabel).append(new Date()).append("\nERROR getTimesPlay");
//            logger.error(br, ex);
//        }
//
//        return questionNumberTimesPlay;
//    }
//
//    private List<RegisterInfo> iGetTimesPlay(String msisdn) {
//        PreparedStatement ps = null;
//        ResultSet rs = null;
//        Connection connection = null;
//        List<RegisterInfo> questionNumberTimesPlayList = new ArrayList<RegisterInfo>();
//
//        long startTime = System.currentTimeMillis();
//        try {
//            connection = getConnection(dbName);
//            ps = connection.prepareStatement(SQL_CHECK_TIMES_PLAY);
//            if (WebserviceManager.queryDbTimeout > 0) {
//                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
//            }
//            ps.setString(1, msisdn);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                RegisterInfo regInfo = new RegisterInfo();
//                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
//                regInfo.setMsisdn(rs.getString("MSISDN"));
//                regInfo.setProductName(rs.getString("PRODUCT_NAME"));
//                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
//                regInfo.setNumberSpin(rs.getInt("NUMBER_SPIN"));
////                regInfo.setPlayedTimes(rs.getInt("PLAYED_TIMES"));
//                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
//                regInfo.setStatus(rs.getInt("STATUS"));
//                questionNumberTimesPlayList.add(regInfo);
//            }
//        } catch (Exception ex) {
//            br.setLength(0);
//            br.append(loggerLabel).
//                    append(new Date()).
//                    append("\nERROR iGetTimesPlay");
//            logger.error(br, ex);
//            return null;
//        } finally {
//            closeResource(rs);
//            closeResource(ps);
//            closeResource(connection);
//            logTime("Time to iGetTimesPlay", startTime);
//        }
//        return questionNumberTimesPlayList;
//    }
    public Boolean updatePlayedTimes(int newSpinTimes, long registerId) {

        try {
            return iUpdatePlayedTimes(newSpinTimes, registerId);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updatePlayedTimes");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean iUpdatePlayedTimes(int newSpinTimes, long registerId) {
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "UPDATE reg_info SET played_times = played_times + 1, number_spin = ? WHERE register_id = ? AND status = 1";

        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, newSpinTimes);
            ps.setLong(2, registerId);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iUpdatePlayedTimes");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iUpdatePlayedTimes " + registerId, startTime);
        }
    }

    public boolean iInsertHistoryPlayWinner(HistoryPlay historyWinner) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertHistoryPlayWinner(historyWinner);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).
                        append("\n").append(SQL_INSERT_HISTORY_PLAY_WINNER);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK iInsertHistoryPlayWinner\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertHistoryPlayWinner(HistoryPlay historyWinner) throws Exception {
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        try {

            connection = getConnection(dbName);

            ps = connection.prepareStatement(SQL_INSERT_HISTORY_PLAY_WINNER);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setLong(1, historyWinner.getListId());
            ps.setLong(2, historyWinner.getRegisterId());
            ps.setString(3, historyWinner.getMsisdn());
            ps.setString(4, historyWinner.getProductName());
            ps.setInt(5, historyWinner.getSpinedTime());
            ps.setInt(6, historyWinner.getPlayedTimes());
            ps.setInt(7, historyWinner.getType());
            ps.setInt(8, historyWinner.getTypeAdd());
            ps.setInt(9, historyWinner.getNumberAdd());
            ps.setDouble(10, historyWinner.getMoney());
            ps.setString(11, historyWinner.getBalanceId());
            ps.setString(12, historyWinner.getNote());
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertHistoryPlayWinner", startTime);
        }
        return rs;
    }

    public AccountInfo iGetAccountByMsisdn(String msisdn) {
        AccountInfo user = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (user == null) {
            try {
                user = getAccountByMsisdn(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).append("\n").append(sqlGetUser);
                logger.error(br, ex);
                count++;
            }
            if (user == null && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).append("==>BREAK query iGetAccountByMsisdn\n");
                logger.error(br);
                break;
            }
        }
        return user;
    }

    private AccountInfo getAccountByMsisdn(String msisdn) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        AccountInfo user = null;
        long timeStart = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_GET_USER_BY_MSISDN);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            user = new AccountInfo();
            if (rs.next()) {
                user.setAccountId(rs.getInt(AccountInfo.ACCOUNT_ID));
                user.setMsisdn(rs.getString(AccountInfo.MSISDN));
                user.setPassword(rs.getString(AccountInfo.PASSWORD));
                user.setCreateDate(rs.getTimestamp(AccountInfo.CREATE_DATE));
                user.setStatus(rs.getInt(AccountInfo.STATUS));
                user.setPoint(rs.getInt(AccountInfo.POINT));
                user.setLastUpdate(rs.getTimestamp(AccountInfo.LAST_UPDATE));
                user.setLastLogin(rs.getTimestamp(AccountInfo.LAST_LOGIN));
            }
            return user;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps, SQL_GET_USER_BY_MSISDN);
            closeConnection(connection);
            logTime("Time to getAccountByMsisdn", timeStart);
        }
    }

    public Boolean insertAccountUser(String password, String msisdn) {
        long timeSt = System.currentTimeMillis();
        Boolean result = false;

        while (true) {
            result = iInsertAccountUser(password, msisdn);
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK insertAccountUser ==> ERROR DATABASE\n").
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

    private Boolean iInsertAccountUser(String password, String msisdn) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql1 = "insert into account_user (account_id, msisdn, password, status, create_date, last_update, point) \n"
                + "values(account_user_seq.nextval, ?, ?, 1, sysdate, sysdate, 0)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql1);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, password);
            ps.execute();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).append(new Date()).
                    append("\nERROR iInsertAccountUser: ").
                    append(sql1).append("\n").
                    append(msisdn);
            logger.error(br, ex);
            return null;
        } finally {
            closeStatement(ps, sql1);
            closeConnection(connection);
            logTime("Time to iInsertAccountUser", timeStart);
        }
    }

    public List<SpinRecharge> checkValidSpinRecharge(String msisdn) {
        List<SpinRecharge> result = new ArrayList<SpinRecharge>();
        try {
            result = iCheckValidSpinRecharge(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkValidSpinRecharge");
            logger.error(br, ex);
        }

        return result;
    }

    private List<SpinRecharge> iCheckValidSpinRecharge(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<SpinRecharge> listReg = new ArrayList<SpinRecharge>();
        String sql = "select * from spin_recharge where msisdn = ? and charge_time >= trunc(sysdate) and status = 0";
        long timeStart = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                SpinRecharge spinRecharge = new SpinRecharge();
                spinRecharge.setId(rs.getLong("ID"));
                spinRecharge.setMsisdn(rs.getString("MSISDN"));
                spinRecharge.setChargeTime(rs.getTimestamp("CHARGE_TIME"));
                spinRecharge.setMoney(rs.getDouble("MONEY"));
                spinRecharge.setSpinAdded(rs.getInt("SPIN_ADDED"));
                spinRecharge.setStatus(rs.getInt("STATUS"));
                spinRecharge.setLastUpdate(rs.getTimestamp("LAST_UPDATE"));
                listReg.add(spinRecharge);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR checkValidSpinRecharge");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check valid spin recharge " + msisdn, timeStart);
        }
        return listReg;
    }

    public Boolean iUpdateClaimSpinRecharge(long id) {

        try {
            return updateClaimSpinRecharge(id);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateClaimSpinRecharge");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updateClaimSpinRecharge(long id) {
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "UPDATE spin_recharge SET status = 1, last_update = sysdate WHERE id = ?";
        long timeStart = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setLong(1, id);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updateClaimSpinRecharge");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateClaimSpinRecharge", timeStart);
        }
    }

    public List<SpinGift> getSpinGift(String msisdn) {
        List<SpinGift> questionNumberTimesPlay = new ArrayList<SpinGift>();
        try {
            questionNumberTimesPlay = iGetSpinGift(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getSpinGift");
            logger.error(br, ex);
        }

        return questionNumberTimesPlay;
    }

    private List<SpinGift> iGetSpinGift(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long timeStart = System.currentTimeMillis();
        List<SpinGift> listReg = new ArrayList<SpinGift>();
        String sql = "select * from spin_gift where msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                SpinGift spinGift = new SpinGift();
                spinGift.setId(rs.getLong("ID"));
                spinGift.setMsisdn(rs.getString("MSISDN"));
                spinGift.setGiftMsisdn(rs.getString("GIFT_MSISDN"));
                spinGift.setGiftTime(rs.getTimestamp("GIFT_TIME"));
                spinGift.setNumberSpin(rs.getInt("NUMBER_SPIN"));
                spinGift.setLastUpdate(rs.getTimestamp("LAST_UPDATE"));
                listReg.add(spinGift);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetSpinGift");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetSpinGift", timeStart);
        }
        return listReg;
    }

    public Boolean iUpdateSpinGift(SpinGift sg) {

        try {
            return updateSpinGift(sg);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateSpinGift");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updateSpinGift(SpinGift sg) {
        PreparedStatement ps = null;
        Connection connection = null;
        long timeStart = System.currentTimeMillis();
        String sql = "UPDATE spin_gift SET gift_msisdn=?, number_spin = ?, last_update = sysdate WHERE msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, sg.getGiftMsisdn());
            ps.setInt(2, sg.getNumberSpin());
            ps.setString(3, sg.getMsisdn());
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updateSpinGift");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateSpinGift", timeStart);
        }
    }

    public boolean iInsertSpinGift(SpinGift sg) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertSpinGift(sg);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertSpinGift - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertSpinGift\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertSpinGift(SpinGift sg) throws Exception {
        boolean rs = false;
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO spin_gift (ID,MSISDN,GIFT_MSISDN,GIFT_TIME,NUMBER_SPIN,LAST_UPDATE) \n"
                + "VALUES(spin_gift_seq.nextval, ?, ?, sysdate, ?, sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setString(1, sg.getMsisdn());
            ps.setString(2, sg.getGiftMsisdn());
            ps.setInt(3, sg.getNumberSpin());

            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertSpinGift", timeStart);
        }
        return rs;
    }

    public Boolean iUpdatePassword(String msisdn, String newPassword) {
        try {
            return updatePassword(msisdn, newPassword);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR iUpdatePassword");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updatePassword(String msisdn, String newPassword) {
        PreparedStatement ps = null;
        Connection connection = null;
        long timeStart = System.currentTimeMillis();
        String sql = "UPDATE account_user SET password=?,  last_update = sysdate WHERE msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, newPassword);
            ps.setString(2, msisdn);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updatePassword");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updatePassword", timeStart);
        }
    }

    public Boolean iUpdateLastLogin(String msisdn) {
        try {
            return updateLastLogin(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateLastLogin");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updateLastLogin(String msisdn) {
        PreparedStatement ps = null;
        Connection connection = null;
        long timeStart = System.currentTimeMillis();
        String sql = "UPDATE account_user SET  last_update = sysdate, last_login = sysdate WHERE msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updateLastLogin");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateLastLogin", timeStart);
        }
    }

    public Boolean disableAutoExtend(long registerId) {
        long timeSt = System.currentTimeMillis();
        Boolean result = false;

        while (true) {
            result = iDisableAutoExtend(registerId);
            if (result != null) {
                break;
            } else {
                if (TIME_BREAK <= 0 || (System.currentTimeMillis() - timeSt) > TIME_BREAK) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("BREAK disableAutoExtend ==> ERROR DATABASE\n").
                            append(registerId);
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

    private Boolean iDisableAutoExtend(long registerId) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql1 = "update reg_info set status = 0, end_time = sysdate where register_id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql1);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setLong(1, registerId);
            ps.execute();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).append(new Date()).
                    append("\nERROR iDisableAutoExtend: ").
                    append(sql1).append("\n").
                    append(registerId);
            logger.error(br, ex);
            return null;
        } finally {
            closeStatement(ps, sql1);
            closeConnection(connection);
            logTime("Time to iDisableAutoExtend", timeStart);
        }
    }

    public List<RankingObj> getRankObj(String msisdn, int rankType, int period) {
        List<RankingObj> rankList = new ArrayList<RankingObj>();
        try {
            rankList = iGetRankObj(msisdn, rankType, period);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRankObj");
            logger.error(br, ex);
        }

        return rankList;
    }

    private List<RankingObj> iGetRankObj(String msisdn, int rankType, int period) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<RankingObj> listRanking = new ArrayList<RankingObj>();
        String sql = "select * from ranking where msisdn = ? and rank_type = ? and period = ? and end_time > sysdate";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setInt(2, rankType);
            ps.setInt(3, period);
            rs = ps.executeQuery();
            while (rs.next()) {
                RankingObj rankObj = new RankingObj();
                rankObj.setId(rs.getLong("ID"));
                rankObj.setMsisdn(rs.getString("MSISDN"));
                rankObj.setStartTime(rs.getTimestamp("START_TIME"));
                rankObj.setEndTime(rs.getTimestamp("END_TIME"));
                rankObj.setSpintCount(rs.getInt("SPIN_COUNT"));
                rankObj.setRankType(rs.getInt("RANK_TYPE"));
                listRanking.add(rankObj);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetRankObj");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetRankObj", timeStart);
        }
        return listRanking;
    }

    public Boolean iUpdateRanking(long id, int subType, int count) {

        try {
            return updateRanking(id, subType, count);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updateRanking");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updateRanking(long id, int subType, int count) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "UPDATE ranking  SET spin_count=spin_count+ ? ,last_update = sysdate, sub_type = ? WHERE id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, count);
            ps.setInt(2, subType);
            ps.setLong(3, id);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updateRanking");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updateRanking", timeStart);
        }
    }

    public boolean iInsertRanking(RankingObj ranking) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertRanking(ranking);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR InsertRanking - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK InsertRanking\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertRanking(RankingObj ranking) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps = null;
//        PreparedStatement ps2 = null;
        Connection connection = null;
        String sql = "INSERT INTO ranking (ID,MSISDN,START_TIME,END_TIME,SPIN_COUNT,INSERT_TIME,LAST_UPDATE,SUB_TYPE,RANK_TYPE,PERIOD) \n"
                + "VALUES(ranking_seq.nextval, ?, ?, ?, ?, sysdate, sysdate,?, ?, 0)";
//        String sqlWeek = "INSERT INTO ranking (ID,MSISDN,START_TIME,END_TIME,SPIN_COUNT,INSERT_TIME,LAST_UPDATE,SUB_TYPE,RANK_TYPE,PERIOD) \n"
//                + "VALUES(ranking_seq.nextval, ?, ?, trunc(sysdate,'iw') + 7 - 1/86400, ?, sysdate, sysdate,?, ?, 1)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, ranking.getMsisdn());
            ps.setTimestamp(2, ranking.getStartTime());
            ps.setTimestamp(3, ranking.getEndTime());
            ps.setInt(4, ranking.getSpintCount());
            ps.setInt(5, ranking.getSubType());
            ps.setInt(6, ranking.getRankType());
            ps.executeUpdate();

//            ps2 = connection.prepareStatement(sqlWeek);
//            if (WebserviceManager.enableQueryDbTimeout) {
//                ps2.setQueryTimeout(WebserviceManager.queryDbTimeout);
//            }
//            ps2.setString(1, ranking.getMsisdn());
//            ps2.setTimestamp(2, ranking.getStartTime());
//            ps2.setInt(3, ranking.getSpintCount());
//            ps2.setInt(4, ranking.getSubType());
//            ps2.setInt(5, ranking.getRankType());
//            ps2.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
//            closeResource(ps2);
            closeResource(connection);
            logTime("Time to insertRanking", timeStart);
        }
        return rs;
    }

    public boolean iInsertRankingWeekly(RankingObj ranking) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertRankingWeekly(ranking);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertRankingWeekly - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertRankingWeekly\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertRankingWeekly(RankingObj ranking) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps2 = null;
        Connection connection = null;
        String sqlWeek = "INSERT INTO ranking (ID,MSISDN,START_TIME,END_TIME,SPIN_COUNT,INSERT_TIME,LAST_UPDATE,SUB_TYPE,RANK_TYPE,PERIOD) \n"
                + "VALUES(ranking_seq.nextval, ?, ?, trunc(sysdate,'iw') + 7 - 1/86400, ?, sysdate, sysdate,?, ?, 1)";
        try {
            connection = getConnection(dbName);

            ps2 = connection.prepareStatement(sqlWeek);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps2.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps2.setString(1, ranking.getMsisdn());
            ps2.setTimestamp(2, ranking.getStartTime());
            ps2.setInt(3, ranking.getSpintCount());
            ps2.setInt(4, ranking.getSubType());
            ps2.setInt(5, ranking.getRankType());
            ps2.executeUpdate();

            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps2);
            closeResource(connection);
            logTime("Time to insertRankingWeekly", timeStart);
        }
        return rs;
    }

    public boolean iInsertRankingMonth(RankingObj ranking) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertRankingMonth(ranking);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR iInsertRankingMonth - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK iInsertRankingMonth\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertRankingMonth(RankingObj ranking) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps2 = null;
        Connection connection = null;
        String sqlMonth = "INSERT INTO ranking (ID,MSISDN,START_TIME,END_TIME,SPIN_COUNT,INSERT_TIME,LAST_UPDATE,SUB_TYPE,RANK_TYPE,PERIOD) \n"
                + "VALUES(ranking_seq.nextval, ?, ?, add_months(trunc(sysdate,'MM'),1) - 1/86400, ?, sysdate, sysdate,?, ?, 2)";
        try {
            connection = getConnection(dbName);
            ps2 = connection.prepareStatement(sqlMonth);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps2.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps2.setString(1, ranking.getMsisdn());
            ps2.setTimestamp(2, ranking.getStartTime());
//            ps2.setTimestamp(3, ranking.getEndTime());
            ps2.setInt(3, ranking.getSpintCount());
            ps2.setInt(4, ranking.getSubType());
            ps2.setInt(5, ranking.getRankType());
            ps2.executeUpdate();

            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps2);
            closeResource(connection);
            logTime("Time to insertRankingMonth", timeStart);
        }
        return rs;
    }

    public boolean iInsertInviteTemp(String msisdn) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertInviteTemp(msisdn);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertInviteTemp - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertInviteTemp\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertInviteTemp(String msisdn) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO invite_temp (MSISDN) \n"
                + "VALUES(?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);

            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertInviteTemp", timeStart);
        }
        return rs;
    }

    public boolean iInsertInviteHistory(String msisdn, String friend) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertInviteHistory(msisdn, friend);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR insertInviteHistory - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK insertInviteHistory\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertInviteHistory(String msisdn, String friend) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO invite_history (ID,MSISDN,INVITED_MSISDN,INVITE_DATE) \n"
                + "VALUES(invite_history_seq.nextval, ?, ?, sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, friend);

            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertInviteHistory", timeStart);
        }
        return rs;
    }

    public Boolean checkInviteHistory(String msisdn, String friendMsisdn) {
        Boolean registered = null;
        try {
            registered = iCheckInviteHistory(msisdn, friendMsisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkInviteHistory");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckInviteHistory(String msisdn, String friendMsisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "Select * from invite_history where msisdn = ? and invited_msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, friendMsisdn);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR checkInviteHistory");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check checkInviteHistory", startTime);
        }
    }

    public String checkPresentInvite(String friendMsisdn) {
        String registered = null;
        try {
            registered = iCheckPresentInvite(friendMsisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkPresentInvite");
            logger.error(br, ex);
        }

        return registered;
    }

    private String iCheckPresentInvite(String friendMsisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "Select * from invite_history where invited_msisdn = ? and get_present = 0";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, friendMsisdn);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("msisdn");
            }
            return "";
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iCheckPresentInvite");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check iCheckPresentInvite", startTime);
        }
    }

    public Boolean updatePresentInvite(String friendMsisdn) {
        Boolean registered = null;
        try {
            registered = iUpdatePresentInvite(friendMsisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updatePresentInvite");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iUpdatePresentInvite(String friendMsisdn) {
        PreparedStatement ps = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "update invite_history set get_present = 1 where invited_msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, friendMsisdn);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updatePresentInvite");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check updatePresentInvite", startTime);
        }
    }

    public Boolean deleteInviteTemp(String msisdn) {

        try {
            return iDeleteInviteTemp(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR deleteInviteTemp");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean iDeleteInviteTemp(String msisdn) {
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "DELETE from invite_temp where msisdn = ?";

        long startTime = System.currentTimeMillis();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR deleteInviteTemp");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to deleteInviteTemp", startTime);
        }
    }

    public Boolean checkInvited(String msisdn) {
        Boolean registered = null;
        try {
            registered = iCheckInvited(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR checkInvited");
            logger.error(br, ex);
        }

        return registered;
    }

    private Boolean iCheckInvited(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        long startTime = System.currentTimeMillis();
        String sql = "Select * from invite_temp where msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iCheckInvited");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to check iCheckInvited", startTime);
        }
    }

    public List<PointTotalObj> getPointTotal(String msisdn) {
        List<PointTotalObj> rankList = new ArrayList<PointTotalObj>();
        try {
            rankList = iGetPointTotal(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getPointTotal");
            logger.error(br, ex);
        }

        return rankList;
    }

    private List<PointTotalObj> iGetPointTotal(String msisdn) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<PointTotalObj> listPoint = new ArrayList<PointTotalObj>();
        String sql = "select * from point_total where msisdn = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                PointTotalObj pointTotalObj = new PointTotalObj();
                pointTotalObj.setId(rs.getLong("ID"));
                pointTotalObj.setMsisdn(rs.getString("MSISDN"));
                pointTotalObj.setLastUpdate(rs.getTimestamp("LAST_UPDATE"));
                pointTotalObj.setPoint(rs.getInt("POINT"));
                listPoint.add(pointTotalObj);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetRankObj");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to iGetPointTotal", timeStart);
        }
        return listPoint;
    }

    public Integer getChangedPoint(String msisdn) {
        Integer result = null;
        try {
            result = iGetChangedPoint(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getChangedPoint");
            logger.error(br, ex);
        }

        return result;
    }

    private Integer iGetChangedPoint(String msisdn) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        Integer result = null;
        String sql = "select nvl(-sum(fee),0) * 10 point from charge_log where msisdn = ? and charge_time > trunc(sysdate) and description = 'Change point'";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            if (rs.next()) {
                result = rs.getInt("point");
            }
            return result;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getChangedPoint");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to getChangedPoint", timeStart);
        }
    }

    public Boolean iUpdatePointTotal(long id, int pointAdded) {

        try {
            return updatePointTotal(id, pointAdded);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR updatePointTotal");
            logger.error(br, ex);
        }

        return null;
    }

    private Boolean updatePointTotal(long id, int pointAdded) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "UPDATE point_total SET point=point+? ,last_update = sysdate WHERE id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, pointAdded);
            ps.setLong(2, id);
            ps.executeUpdate();
            return true;
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR updatePointTotal");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to updatePointTotal", timeStart);
        }
    }

    public boolean iInsertPointTotal(PointTotalObj pointTotal) {
        boolean result = false;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (result == false) {
            try {
                result = insertPointTotal(pointTotal);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR iInsertPointTotal - ").append(count);
                logger.error(br, ex);
                count++;
            }
            if (result == false && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK iInsertPointTotal\n");
                logger.error(br);
                break;
            }
        }
        return result;
    }

    private boolean insertPointTotal(PointTotalObj pointObj) throws Exception {
        long timeStart = System.currentTimeMillis();
        boolean rs = false;
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO point_total (ID,MSISDN,POINT,LAST_UPDATE,SUB_TYPE) \n"
                + "VALUES(point_total_seq.nextval,?,?,sysdate,?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, pointObj.getMsisdn());
            ps.setInt(2, pointObj.getPoint());
            ps.setInt(3, pointObj.getSubType());

            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            throw ex;
        } finally {
            closeResource(ps);
            closeResource(connection);
            logTime("Time to insertPointTotal", timeStart);
        }
        return rs;
    }

    public List iLoadPackage() {
        List mConfig = null;
        long timeBegin = System.currentTimeMillis();
        while (mConfig == null) {
            try {
                mConfig = loadPackage();
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - iLoadPackage");
                logger.error(br, ex);
            }
            if (mConfig == null && System.currentTimeMillis() - timeBegin > TIME_BREAK) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).
                        append("==> BREAK iLoadPackage");
                logger.error(br);
                break;
            }
        }
        return mConfig;
    }

    private List loadPackage() throws SQLException {
        long timeStart = System.currentTimeMillis();
        ResultSet rs = null;
        PreparedStatement ps = null;
        Connection connection = null;
        List listConfig = null;
        String sql = "SELECT * FROM PACKG";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            listConfig = new ArrayList();
            while (rs.next()) {
                ProductInfo prize = new ProductInfo();
                prize.setProductId(rs.getInt("PRODUCT_ID"));
                prize.setFee(rs.getDouble("FEE"));
                prize.setNumberSpin(rs.getInt("NUMBER_SPIN"));
                prize.setProductName(rs.getString("PRODUCT_NAME"));
                prize.setSmsSyntax(rs.getString("SMS_SYNTAX"));
                prize.setBizId(rs.getInt("BIZ_ID"));
                prize.setExpireDays(rs.getInt("EXPIRE_DAYS"));
                listConfig.add(prize);
            }

        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to loadPackage", timeStart);
        }
        return listConfig;
    }

    public List<RegisterInfo> getRegisterInfo(String msisdn, String packName) {
        List<RegisterInfo> questionNumberTimesPlay = new ArrayList<RegisterInfo>();
        try {
            questionNumberTimesPlay = iGetRegisterInfo(msisdn, packName);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRegisterInfo");
            logger.error(br, ex);
        }

        return questionNumberTimesPlay;
    }

    private List<RegisterInfo> iGetRegisterInfo(String msisdn, String packName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<RegisterInfo> listReg = new ArrayList<RegisterInfo>();
        long startTime = System.currentTimeMillis();
        String sql = "SELECT * FROM REG_INFO WHERE MSISDN = ? and product_name = ? and status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, packName);
            rs = ps.executeQuery();
            while (rs.next()) {
                RegisterInfo regInfo = new RegisterInfo();
                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
                regInfo.setMsisdn(rs.getString("MSISDN"));
                regInfo.setProductName(rs.getString("PRODUCT_NAME"));
                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
                regInfo.setNumberSpin(rs.getInt("NUMBER_SPIN"));
                regInfo.setPlayedTimes(rs.getInt("PLAYED_TIMES"));
                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
                regInfo.setStatus(rs.getInt("STATUS"));
                listReg.add(regInfo);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR iGetRegisterInfo");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to get reg info", startTime);
        }
        return listReg;
    }

    public List<RegisterInfo> getRegisterInfoAll(String msisdn) {
        List<RegisterInfo> questionNumberTimesPlay = new ArrayList<RegisterInfo>();
        try {
            questionNumberTimesPlay = iGetRegisterInfoAll(msisdn);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRegisterInfoAll");
            logger.error(br, ex);
        }

        return questionNumberTimesPlay;
    }

    private List<RegisterInfo> iGetRegisterInfoAll(String msisdn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<RegisterInfo> listReg = new ArrayList<RegisterInfo>();
        long startTime = System.currentTimeMillis();
        String sql = "SELECT * FROM REG_INFO WHERE MSISDN = ? and status = 1";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                RegisterInfo regInfo = new RegisterInfo();
                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
                regInfo.setMsisdn(rs.getString("MSISDN"));
                regInfo.setProductName(rs.getString("PRODUCT_NAME"));
                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
                regInfo.setExpireTime(rs.getTimestamp("EXPIRE_TIME"));
                regInfo.setNumberSpin(rs.getInt("NUMBER_SPIN"));
                regInfo.setPlayedTimes(rs.getInt("PLAYED_TIMES"));
                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
                regInfo.setStatus(rs.getInt("STATUS"));
                listReg.add(regInfo);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getRegisterInfoAll");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to get reg info all", startTime);
        }
        return listReg;
    }

    public List<RegisterInfo> getRegisterInday(String msisdn, String productName) {
        List<RegisterInfo> listReg = new ArrayList<RegisterInfo>();
        try {
            listReg = iGetRegisterInday(msisdn, productName);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRegisterInDay");
            logger.error(br, ex);
        }

        return listReg;
    }

    private List<RegisterInfo> iGetRegisterInday(String msisdn, String productName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<RegisterInfo> listReg = new ArrayList<RegisterInfo>();
        long startTime = System.currentTimeMillis();
        String sql = "SELECT * FROM REG_INFO WHERE MSISDN = ? and product_name = ? and expire_time > sysdate order by register_time desc";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setString(2, productName);
            rs = ps.executeQuery();
            while (rs.next()) {
                RegisterInfo regInfo = new RegisterInfo();
                regInfo.setRegisterId(rs.getLong("REGISTER_ID"));
                regInfo.setMsisdn(rs.getString("MSISDN"));
                regInfo.setProductName(rs.getString("PRODUCT_NAME"));
                regInfo.setRegisterTime(rs.getTimestamp("REGISTER_TIME"));
                regInfo.setNumberSpin(rs.getInt("NUMBER_SPIN"));
                regInfo.setPlayedTimes(rs.getInt("PLAYED_TIMES"));
                regInfo.setExtendStatus(rs.getInt("EXTEND_STATUS"));
                regInfo.setStatus(rs.getInt("STATUS"));
                listReg.add(regInfo);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getRegisterInDay");
            logger.error(br, ex);
            return null;
        } finally {
            closeResource(rs);
            closeResource(ps);
            closeResource(connection);
            logTime("Time to get reg info in day", startTime);
        }
        return listReg;
    }

    public int insertTransLog1(TransactionLog log) throws SQLException {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_TRANS_LOG);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setString(i++, log.getTransactionId());
            ps.setInt(i++, log.getType());
            ps.setString(i++, log.getMsisdn());
            ps.setString(i++, log.getRequest());
            ps.setString(i++, log.getResponse());
            ps.setString(i++, log.getErrorCode());
            ps.setString(i++, log.getCommand());
            ps.setTimestamp(i++, log.getRequestTime());
            ps.setTimestamp(i++, log.getResponseTime());
            ps.setString(i++, log.getChannel());

            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insert TRANSACTION_LOG", startTime);
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
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
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

    public List<Questions> getRandomQuestion(int numQuestion) {
        List<Questions> questionList = new ArrayList<Questions>();
        try {
            questionList = iGetRandomQuestion(numQuestion);
        } catch (Exception ex) {
            br.append(loggerLabel).append(new Date()).append("\nERROR getRandomQuestion");
            logger.error(br, ex);
        }

        return questionList;
    }

    private List<Questions> iGetRandomQuestion(int numQuestion) {
        long startTime = System.currentTimeMillis();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        List<Questions> questionList = new ArrayList<Questions>();
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(SQL_SELECT_QUESTION_LIST);
            if (WebserviceManager.queryDbTimeout > 0) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setInt(1, numQuestion);
            rs = ps.executeQuery();
            while (rs.next()) {
                Questions question = new Questions();
                question.setQuestionId(rs.getLong("QUESTION_ID"));
                question.setQuestionContent(rs.getString("question_content"));
                question.setAnswer(rs.getString("answer"));
                question.setAnswer1(rs.getString("answer1"));
                question.setAnswer2(rs.getString("answer2"));
                question.setAnswer3(rs.getString("answer3"));
                question.setAnswer4(rs.getString("answer4"));
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
            logTime("Time to get random question", startTime);
        }
        return questionList;
    }

    public UserReport iGetUserReport(String username) {
        UserReport user = null;
        long timeBegin = System.currentTimeMillis();
        int count = 1;
        while (user == null) {
            try {
                user = getUserReport(username);
            } catch (Exception ex) {
                br.setLength(0);
                br.append(loggerLabel).append("ERROR - ").append(count).append("\n").append(sqlGetUserReport);
                logger.error(br, ex);
                count++;
            }
            if (user == null && System.currentTimeMillis() - timeBegin > WebserviceManager.breakQuery) {
                br.setLength(0);
                br.append(loggerLabel).append(new Date()).append("==>BREAK query iGetUserReport\n");
                logger.error(br);
                break;
            }
        }
        return user;
    }

    private UserReport getUserReport(String username) throws SQLException {
        ResultSet rs = null;
        PreparedStatement ps = null;
        UserReport user = null;
        long timeStart = System.currentTimeMillis();
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetUserReport);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, username);
            rs = ps.executeQuery();
            user = new UserReport();
            if (rs.next()) {
                user.setUsername(rs.getString(UserReport.USERNAME));
                user.setPassword(rs.getString(UserReport.PASSWORD));
                user.setStatus(rs.getInt(UserReport.STATUS));
                user.setRole(rs.getInt(UserReport.ROLE));
            }
            return user;
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeResultSet(rs);
            closeStatement(ps, sqlGetUserReport);
            closeConnection(connection);
            logTime("Time to getUserReport", timeStart);
        }
    }

    public Long getSequence(String table) {
        long timeStart = System.currentTimeMillis();
        String sqlGetMoSeq = "select " + table + "_seq.nextval SEQ from dual";

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetMoSeq);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            //check MSISDN
            while (rs.next()) {
                return rs.getLong("SEQ");
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("\nERROR").append(sqlGetMoSeq).
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeResultSet(rs);
            closeStatement(ps, sqlGetMoSeq);
            closeConnection(connection);
            logTime("Time to getSequence " + table, timeStart);
        }
        return null;
    }

    public boolean insertUserPlay(long id, String username, String msisdn, String age, String showroom, int programId) {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "INSERT INTO user_play (ID,USERNAME,MSISDN,AGE,INSERT_TIME,ACCOUNT,PROGRAM_ID) \n"
                + "VALUES(?, ?, ?, ?, sysdate, ?, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setLong(i++, id);
            ps.setString(i++, username);
            ps.setString(i++, msisdn);
            ps.setString(i++, age);
            ps.setString(i++, showroom);
            ps.setInt(i++, programId);

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Error insert user play", ex);
            return false;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertUserPlay", startTime);
        }
    }

    public boolean insertPrizeWinner(String msisdn, int prizeId, int playId) {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "INSERT INTO prize_winner (ID,MSISDN,PRIZE_ID,PLAY_ID,INSERT_TIME) \n"
                + "VALUES(prize_winner_seq.nextval, ?, ?, ?, sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setString(i++, msisdn);
            ps.setInt(i++, prizeId);
            ps.setInt(i++, playId);

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Error insert prize winner", ex);
            return false;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertUserPlay", startTime);
        }
    }

    public List<UserPlayObj> getUserPrize(String msisdn, boolean confirmed, int page, int pageSize) {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        List<UserPlayObj> result = null;
        ResultSet rs = null;
        String sql;
        if (confirmed) {
            sql = "select * from (\n"
                    + "select rownum no, s.* from (\n"
                    + "select u.*, p.prize_name, p.image from prize_winner  u\n"
                    + "left join list_prize p on u.prize_id = p.id\n"
                    + "where u.msisdn = ? \n"
                    + "and (action_code like 'ADD%' or action_code =  'NOHU')\n"
                    + "order by created_time desc) s )\n"
                    + "where no > ? and no <= ?";
        } else {
            sql = "select * from (\n"
                    + "select rownum no, s.* from (\n"
                    + "select u.*, p.prize_name, p.image from user_play  u\n"
                    + "left join list_prize p on u.prize_id = p.id\n"
                    + "where u.msisdn = ? \n"
                    + "and (action_code like 'ADD%' or action_code =  'NOHU')\n"
                    + "order by created_time desc) s )\n"
                    + "where no > ? and no <= ?";
        }
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setString(i++, msisdn);
            ps.setInt(i++, (page - 1) * pageSize);
            ps.setInt(i++, page * pageSize);
            rs = ps.executeQuery();
            result = new ArrayList();
            while (rs.next()) {
                UserPlayObj obj = new UserPlayObj();
                obj.setId(rs.getLong("ID"));
                obj.setMsisdn(msisdn);
                obj.setPrizeId(rs.getInt("PRIZE_ID"));
                obj.setMoney(rs.getInt("MONEY"));
                obj.setCreatedDate(sdf.format(rs.getTimestamp("created_time")));
                obj.setPrizeName(rs.getString("Prize_name"));
                obj.setImage(rs.getString("IMAGE"));
                try {
                    obj.setConfirmDate(sdf.format(rs.getTimestamp("insert_time")));
                } catch (Exception exx) {
                }
                obj.setConfirmed(confirmed);
                result.add(obj);
            }
            return result;
        } catch (SQLException ex) {
            logger.error("Error getUserPrize", ex);
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to getUserPrize", startTime);
        }
    }

    public int countUserPrize(String msisdn, boolean confirmed) {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        ResultSet rs = null;
        String sql;
        if (confirmed) {
            sql = "select count(*) counter from (\n"
                    + "select u.*, p.prize_name, p.image from prize_winner  u\n"
                    + "left join list_prize p on u.prize_id = p.id\n"
                    + "where u.msisdn = ? \n"
                    + "and (action_code like 'ADD%' or action_code =  'NOHU')\n"
                    + "order by created_time desc)";
        } else {
            sql = "select count(*) counter from (\n"
                    + "select u.*, p.prize_name, p.image from user_play  u\n"
                    + "left join list_prize p on u.prize_id = p.id\n"
                    + "where u.msisdn = ? \n"
                    + "and (action_code like 'ADD%' or action_code =  'NOHU')\n"
                    + "order by created_time desc)  ";
        }
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setString(i++, msisdn);
            rs = ps.executeQuery();
            while (rs.next()) {
                return rs.getInt("counter");
            }
            return 0;
        } catch (SQLException ex) {
            logger.error("Error getSpin", ex);
            return 0;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to countUserPrize", startTime);
        }
    }

    public int insertQuestionPlay(long playId, long questionId, int correct) throws SQLException {
        PreparedStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "INSERT INTO question_play (ID,PLAY_ID,QUESTION_ID,CORRECT,INSERT_TIME) \n"
                + "VALUES(question_play_seq.nextval, ?, ?, ? , sysdate)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setLong(i++, playId);
            ps.setLong(i++, questionId);
            ps.setInt(i++, correct);

            return ps.executeUpdate();
        } catch (SQLException ex) {
            throw ex;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertQuestionPlay", startTime);
        }
    }

    public boolean completeMission(String msisdn) {
        CallableStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "{ call clear_mission (?)  }";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setString(i++, msisdn);

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Error completeMission", ex);
            return false;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to completeMission", startTime);
        }
    }

    public long actionSpin(String msisdn, String lastAction, int isSpinGift, int prizeId, int money) {
        CallableStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "{ call do_spin (?, ?, ?, ?, ?, ?) }";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.registerOutParameter(6, OracleTypes.NUMBER);
            int i = 1;
            ps.setString(i++, msisdn);
            ps.setString(i++, lastAction);
            ps.setInt(i++, isSpinGift);
            ps.setInt(i++, prizeId);
            ps.setInt(i++, money);
            ps.executeUpdate();
            // get playid
            return ps.getLong(6);
        } catch (SQLException ex) {
            logger.error("Error actionSpin", ex);
            return 0;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to actionSpin", startTime);
        }
    }

    public SpinTotalObj getSpin(String msisdn) {
        CallableStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        SpinTotalObj result = null;
        String sql = "{ call get_spin (? , ?, ? , ?) }";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.registerOutParameter(2, OracleTypes.NUMBER);
            ps.registerOutParameter(3, OracleTypes.NUMBER);
            ps.registerOutParameter(4, OracleTypes.VARCHAR);
            ps.executeUpdate();
            //
            result = new SpinTotalObj();
            result.setMsisdn(msisdn);
            result.setSpinGift(ps.getInt(2));
            result.setSpinNum(ps.getInt(3));
            result.setLastAction(ps.getString(4));
            return result;
        } catch (SQLException ex) {
            logger.error("Error getSpin", ex);
            return null;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to getSpin", startTime);
        }
    }

    public List loadMission() {
        long timeStart = System.currentTimeMillis();
        String sqlGetMoSeq = "select * from mission where status = 1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<MissionObj> listMission = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetMoSeq);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            listMission = new ArrayList();
            //check MSISDN
            while (rs.next()) {
                MissionObj mission = new MissionObj();
                mission.setId(rs.getLong(MissionObj.ID));
                mission.setMissionName(rs.getString(MissionObj.MISSION_NAME));
                mission.setImage(rs.getString(MissionObj.IMAGE));
                mission.setUrl(rs.getString(MissionObj.URL));
                mission.setDescription(rs.getString(MissionObj.DESCRIPTION));
                listMission.add(mission);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("\nERROR").append(sqlGetMoSeq).
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeStatement(ps, sqlGetMoSeq);
            closeResultSet(rs);
            closeConnection(connection);
            logTime("Time to loadMission ", timeStart);
        }
        return listMission;
    }

    public List loadAds() {
        long timeStart = System.currentTimeMillis();
        String sql = "select * from ads where status = 1";

        PreparedStatement ps = null;
        ResultSet rs = null;
        List<AdsObj> listAds = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            listAds = new ArrayList();
            while (rs.next()) {
                AdsObj ads = new AdsObj();
                ads.setId(rs.getLong(AdsObj.ID));
                ads.setAdsName(rs.getString(AdsObj.ADS_NAME));
                ads.setVideoUrl(rs.getString(AdsObj.VIDEO_URL));
                ads.setImageUrl(rs.getString(AdsObj.IMAGE_URL));
                ads.setTimeout(rs.getInt(AdsObj.TIMEOUT));
                listAds.add(ads);
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("\nERROR").append(sql).
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeStatement(ps, sql);
            closeResultSet(rs);
            closeConnection(connection);
            logTime("Time to loadAds ", timeStart);
        }
        return listAds;
    }

    public ConfirmOtpObj getConfirmOtp(String msisdn, long svId, int action) {
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Connection connection = null;
        String sql = "select * from confirm_otp where msisdn = ? and sv_id = ? and action = ?  order by expire_time desc";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setString(1, msisdn);
            ps.setLong(2, svId);
            ps.setInt(3, action);
            rs = ps.executeQuery();
            while (rs.next()) {
                ConfirmOtpObj confirmOtp = new ConfirmOtpObj();
                confirmOtp.setMsisdn(rs.getString(ConfirmOtpObj.MSISDN));
                confirmOtp.setId(rs.getLong(ConfirmOtpObj.ID));
                confirmOtp.setInsertTime(rs.getTimestamp(ConfirmOtpObj.INSERT_TIME));
                confirmOtp.setExpireTime(rs.getTimestamp(ConfirmOtpObj.EXPIRE_TIME));
                confirmOtp.setRequestId(rs.getString(ConfirmOtpObj.REQUEST_ID));
                confirmOtp.setProductName(rs.getString(ConfirmOtpObj.PRODUCT_NAME));
                confirmOtp.setOtp(rs.getString(ConfirmOtpObj.OTP));
                confirmOtp.setSvId(rs.getLong(ConfirmOtpObj.SV_ID));
                confirmOtp.setAction(rs.getInt(ConfirmOtpObj.ACTION));
                confirmOtp.setPassword(rs.getString(ConfirmOtpObj.PASSWORD));
                return confirmOtp;
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(new Date()).
                    append("\nERROR getConfirmOtp");
            logger.error(br, ex);
            return null;
        } finally {
            closeResultSet(rs);
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to getConfirmOtp", timeStart);
        }
        return null;
    }

    public boolean deleteConfirmOtp(ConfirmOtpObj confirmOtp) {
        boolean rs = false;
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "DELETE FROM CONFIRM_OTP WHERE id = ?";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setLong(1, confirmOtp.getId());
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            logger.error("Error deleteConfirmOtp", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to deleteConfirmOtp", timeStart);
        }
        return rs;
    }

    public boolean insertConfirmOtp(ConfirmOtpObj confirmOtp) {
        boolean rs = false;
        long timeStart = System.currentTimeMillis();
        PreparedStatement ps = null;
        Connection connection = null;
        String sql = "INSERT INTO CONFIRM_OTP (ID,MSISDN,REQUEST_ID,INSERT_TIME,EXPIRE_TIME,OTP,SV_ID,PRODUCT_NAME,ACTION,PASSWORD)"
                + " VALUES(CONFIRM_OTP_seq.nextval,?, ?, sysdate, ?, ?, ?, ?, ?, ?)";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }

            ps.setString(1, confirmOtp.getMsisdn());
            ps.setString(2, confirmOtp.getRequestId());
            ps.setTimestamp(3, confirmOtp.getExpireTime());
            ps.setString(4, confirmOtp.getOtp());
            ps.setLong(5, confirmOtp.getSvId());
            ps.setString(6, confirmOtp.getProductName());
            ps.setInt(7, confirmOtp.getAction());
            ps.setString(8, confirmOtp.getPassword());
            ps.executeUpdate();
            rs = true;
        } catch (Exception ex) {
            logger.error("Error insertConfirmOtp", ex);
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to insertConfirmOtp", timeStart);
        }
        return rs;
    }

    public boolean confirmPrize(long playId) {
        CallableStatement ps = null;
        long startTime = System.currentTimeMillis();
        Connection connection = null;
        String sql = "{ call confirm_prize (? ) }";
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sql);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            int i = 1;
            ps.setLong(i++, playId);

            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.error("Error confirmPrize", ex);
            return false;
        } finally {
            closeStatement(ps);
            closeConnection(connection);
            logTime("Time to confirmPrize", startTime);
        }
    }

    public Long getHuValue() {
        long timeStart = System.currentTimeMillis();
        String sqlGetMoSeq = "select hu_value from hu_value";

        PreparedStatement ps = null;
        ResultSet rs = null;

        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareStatement(sqlGetMoSeq);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            rs = ps.executeQuery();
            //check MSISDN
            while (rs.next()) {
                return rs.getLong("hu_value");
            }
        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("\nERROR").append(sqlGetMoSeq).
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeResultSet(rs);
            closeStatement(ps, sqlGetMoSeq);
            closeConnection(connection);
            logTime("Time to getHuValue ", timeStart);
        }
        return null;
    }

    public boolean updateHuValue(long val) {
        long timeStart = System.currentTimeMillis();
        String sqlGetMoSeq = "{ call update_hu (? ) }";
        //

        CallableStatement ps = null;
        Connection connection = null;
        try {
            connection = getConnection(dbName);
            ps = connection.prepareCall(sqlGetMoSeq);
            if (WebserviceManager.enableQueryDbTimeout) {
                ps.setQueryTimeout(WebserviceManager.queryDbTimeout);
            }
            ps.setLong(1, val);
            ps.executeUpdate();
            return true;

        } catch (Exception ex) {
            br.setLength(0);
            br.append(loggerLabel).
                    append("\nERROR").append(sqlGetMoSeq).
                    append(ex.toString());

            logger.error(br, ex);
        } finally {
            closeStatement(ps, sqlGetMoSeq);
            closeConnection(connection);
            logTime("Time to updateHuValue ", timeStart);
        }
        return false;
    }
}
