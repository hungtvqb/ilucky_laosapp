/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author
 */
public class AccountInfo implements Record {

    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    public static final String MSISDN = "MSISDN";
    public static final String PASSWORD = "PASSWORD";
    public static final String USER_NAME = "USER_NAME";
    public static final String GENDER = "GENDER";
    public static final String BIRTHDAY = "BIRTHDAY";
    public static final String EMAIL = "EMAIL";
    public static final String REGISTER_TIME = "REGISTER_TIME";
    public static final String EXPIRE_TIME = "EXPIRE_TIME";
    public static final String WRONG_TIMES = "WRONG_TIMES";
    public static final String LOCKED = "LOCKED";
    public static final String LOCK_UNTIL = "LOCK_UNTIL";
    public static final String STATUS = "STATUS";
    public static final String EXTEND_STATUS = "EXTEND_STATUS";
    public static final String LAST_EXTEND = "LAST_EXTEND";
    public static final String LAST_LOGIN = "LAST_LOGIN";
    public static final String AUTO_EXTEND = "AUTO_EXTEND";
    private long id;
    private String accountId;
    private String msisdn;
    private String password;
    private String userName;
    private String gender;
    private Timestamp birthDay;
    private String email;
    private Timestamp registerTime;
    private Timestamp expireTime;
    private int wrongTime;
    private Timestamp lockUntil;
    private int status;
    private int extendStatus;
    private Timestamp lastExtend;
    private Timestamp lastLogin;
    private String errorCode;
    private int autoExtend;

    public int getAutoExtend() {
        return autoExtend;
    }

    public void setAutoExtend(int autoExtend) {
        this.autoExtend = autoExtend;
    }

    public AccountInfo() {
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Timestamp getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Timestamp birthDay) {
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public int getWrongTime() {
        return wrongTime;
    }

    public void setWrongTime(int wrongTime) {
        this.wrongTime = wrongTime;
    }

    public Timestamp getLockUntil() {
        return lockUntil;
    }

    public void setLockUntil(Timestamp lockUntil) {
        this.lockUntil = lockUntil;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getExtendStatus() {
        return extendStatus;
    }

    public void setExtendStatus(int extendStatus) {
        this.extendStatus = extendStatus;
    }

    public Timestamp getLastExtend() {
        return lastExtend;
    }

    public void setLastExtend(Timestamp lastExtend) {
        this.lastExtend = lastExtend;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return accountId;
    }

    @Override
    public long getID() {
        return id;
    }
}
