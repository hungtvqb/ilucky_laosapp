/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.database;

import com.vas.wsfw.common.WebserviceManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author MinhNH
 */
public class DbProcessorAbstract {

    public String dbName;
    //
    private Logger logger;
    private String loggerLabel = "DbProcessorAbstract: ";
    public static int SUCCESS = 0;
    public static int FAIL = -1;
    //
    public StringBuffer br = new StringBuffer();
    //
    public static int QUERY_TIMEOUT = 300; // Time out query
    public static int TIME_BREAK = 0;
    public static int TIME_BREAK_DELETE_RECORD_TIMEOUT = 0;
    //

    public DbProcessorAbstract() {
    }

    public void init(String dbName, Logger logger) {
        try {
            this.logger = logger;
            this.dbName = dbName;
        } catch (Exception ex) {
            logger.error(loggerLabel + new Date() + "\nLoi khoi tao DbProcessor() Framework", ex);
        }
    }

    public void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
                logger.warn(loggerLabel + "Close Statement: " + ex);
                conn = null;
            }
        }
    }

    public void closeStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
                stmt = null;
            } catch (SQLException ex) {
                logger.warn(loggerLabel + "ERROR close Statement: " + ex.getMessage());
                stmt = null;
            }
        }
    }

    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
                logger.warn(loggerLabel + "ERROR close ResultSet: " + ex.getMessage());
                rs = null;
            }
        }
    }

    public Connection getConnection(String dbName) {
        return ConnectionPoolManager.getConnection(dbName);
    }

    public void logTime(String strLog, long timeSt) {
        long timeEx = System.currentTimeMillis() - timeSt;
        if (timeEx >= WebserviceManager.minTimeDb && WebserviceManager.loggerDbMap != null) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(WebserviceManager.getTimeLevelDb(timeEx)).append(": ").
                    append(strLog).
                    append(": ").
                    append(timeEx).
                    append(" ms");

            logger.warn(br);
        } else {
            br.setLength(0);
            br.append(loggerLabel).
                    append(strLog).
                    append(": ").
                    append(timeEx).
                    append(" ms");

            logger.info(br);
        }
    }
}
