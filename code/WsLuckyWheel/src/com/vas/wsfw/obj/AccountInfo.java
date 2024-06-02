/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 *
 * @author
 */
public class AccountInfo {

    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    public static final String MSISDN = "MSISDN";
    public static final String PASSWORD = "PASSWORD";
    public static final String CREATE_DATE = "CREATE_DATE";
    public static final String STATUS = "STATUS";
    public static final String LAST_UPDATE = "LAST_UPDATE";
    public static final String LAST_LOGIN = "LAST_LOGIN";
    public static final String POINT = "POINT";
    private int accountId;
    private String msisdn;
    private String password;
    private int status;
    private Timestamp createDate;
    private Timestamp lastUpdate;
    private Timestamp lastLogin;
    private int point;

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return msisdn;
    }
}
