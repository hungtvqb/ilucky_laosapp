/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 * Thong tin ban ghi MO
 *
 * @author TungTT
 * @version 1.0
 * @since 01-03-2011
 */
public class MoRecord {

    public static final String MO_ID = "MO_ID";
    public static final String MSISDN = "MSISDN";
    public static final String COMMAND = "COMMAND";
    public static final String PARAM = "PARAM";
    public static final String RECEIVE_TIME = "RECEIVE_TIME";
    public static final String STATUS = "STATUS";
    public static final String CHANNEL = "CHANNEL";
    public static final String ACTION_TYPE = "ACTION_TYPE";
    // tungnv 05102013
    public static final String MO_HIS_ID = "MO_HIS_ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String RETRY_TURN = "RETRY_TURN";
    public static final String INSERT_TIME = "INSERT_TIME";
    public static final String SUB_ID = "SUB_ID";
    public static final String PRODUCT_CODE = "PRODUCT_CODE";
    public static final String SUB_TYPE = "SUB_TYPE";
    //tungnv 09012014
    public static final String REG_DATA = "REG_DATA";
    //
    public static final int PREPAID = 1;
    public static final int POSTPAID = 0;
    //
    private Long id;
    private String msisdn;
    private Long subId;
    private String productCode;
    private Integer subType;
    private String command;
    private String param;
    private Timestamp receiveTime;
    private String channel;
    private Integer actionType;
    private String errCode;
    private String errOcs;
    private long fee;
    private String nodeName;
    private String clusterName;
    //
    private String message;
    private Object object;
    //VT Free
    private String productName;
    private long validateCode;
    private Object obj;
    private long feeAction;
    private long promotionValue;
    //update 30112011
    private long timeStart;
    private Object registeredMsisdn;
    private Timestamp lastRegister;
    private String threadName;
    private int status;
    private int retryTurn;
    private Timestamp insertTime;
    private long moHisId;
    //tungnv 09012014
    private boolean regData;
    //tungnv 07042014
    private int passPromotion;
    //tungnv 24072014
    private int channelType;
    private String startTime;
    private String expireTime;

    public int getChannelType() {
        return channelType;
    }

    public void setChannelType(int channelType) {
        this.channelType = channelType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRetryTurn() {
        return retryTurn;
    }

    public void setRetryTurn(int retryTurn) {
        this.retryTurn = retryTurn;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public long getMoHisId() {
        return moHisId;
    }

    public void setMoHisId(long moHisId) {
        this.moHisId = moHisId;
    }

    public int getPassPromotion() {
        return passPromotion;
    }

    public void setPassPromotion(int passPromotion) {
        this.passPromotion = passPromotion;
    }

    public boolean isRegData() {
        return regData;
    }

    public void setRegData(boolean regData) {
        this.regData = regData;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public Timestamp getLastRegister() {
        return lastRegister;
    }

    public void setLastRegister(Timestamp lastRegister) {
        this.lastRegister = lastRegister;
    }

    public long getPromotionValue() {
        return promotionValue;
    }

//    public Timestamp getExpireTime() {
//        return expireTime;
//    }
//
//    public void setExpireTime(Timestamp expireTime) {
//        this.expireTime = expireTime;
//    }
    public void setPromotionValue(long promotionValue) {
        this.promotionValue = promotionValue;
    }

    public long getFeeAction() {
        return feeAction;
    }

    public void setFeeAction(long feeAction) {
        this.feeAction = feeAction;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public long getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(long validateCode) {
        this.validateCode = validateCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return id + "|" + msisdn;
    }

    public MoRecord() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getSubType() {
        return subType;
    }

    public void setSubType(Integer subType) {
        this.subType = subType;
    }

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public Integer getActionType() {
        return actionType;
    }

    public void setActionType(Integer actionType) {
        this.actionType = actionType;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrOcs() {
        return errOcs;
    }

    public void setErrOcs(String errOcs) {
        this.errOcs = errOcs;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getID() {
        return this.id;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(long timeStart) {
        this.timeStart = timeStart;
    }

    public Object getRegisteredMsisdn() {
        return registeredMsisdn;
    }

    public void setRegisteredMsisdn(Object registeredMsisdn) {
        this.registeredMsisdn = registeredMsisdn;
    }
}
