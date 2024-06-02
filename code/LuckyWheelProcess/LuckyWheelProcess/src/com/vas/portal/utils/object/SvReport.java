/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

/**
 *
 * @author Sungroup
 */
public class SvReport {

    public static final String SV_CODE = "SV_CODE";
    public static final String DB_NAME = "DB_NAME";
    public static final String CONNECTION_STRING = "CONNECTION_STRING";
    public static final String DB_USERNAME = "DB_USERNAME";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    public static final String SQL_REPORT_DAILY = "SQL_REPORT_DAILY";
    public static final String SQL_REPORT_HOURLY = "SQL_REPORT_HOURLY";
    public static final String STATUS = "STATUS";
    private String svCode;
    private String dbName;
    private String connectionString;
    private String dbUsername;
    private String dbPassword;
    private String sqlReportDaily;
    private String sqlReportHourly;
    private int status;

    public String getSvCode() {
        return svCode;
    }

    public void setSvCode(String svCode) {
        this.svCode = svCode;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getSqlReportDaily() {
        return sqlReportDaily;
    }

    public void setSqlReportDaily(String sqlReportDaily) {
        this.sqlReportDaily = sqlReportDaily;
    }

    public String getSqlReportHourly() {
        return sqlReportHourly;
    }

    public void setSqlReportHourly(String sqlReportHourly) {
        this.sqlReportHourly = sqlReportHourly;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
