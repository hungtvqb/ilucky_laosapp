/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 *
 * @author Sungroup
 */
public class ConfirmOtpObj {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String REQUEST_ID = "REQUEST_ID";
    public static final String INSERT_TIME = "INSERT_TIME";
    public static final String EXPIRE_TIME = "EXPIRE_TIME";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String ACTION = "ACTION";
    public static final String OTP = "OTP";
    public static final String SV_ID = "SV_ID";
    public static final String PASSWORD = "PASSWORD";
    private long id;
    private String msisdn;
    private String requestId;
    private Timestamp insertTime;
    private Timestamp expireTime;
    private String otp;
    private int action;
    private String productName;
    private long svId;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getSvId() {
        return svId;
    }

    public void setSvId(long svId) {
        this.svId = svId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

}
