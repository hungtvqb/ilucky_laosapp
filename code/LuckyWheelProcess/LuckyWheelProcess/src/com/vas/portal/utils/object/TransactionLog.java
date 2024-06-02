/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author kdvt_minhnh@viettel.com.vn
 * @since May 16, 2012
 * @version 1.0
 */
public class TransactionLog {

    private int type;
    private String msisdn;
    private String request;
    private String response;
    private String errorCode;
    private String command;
    private Timestamp requestTime;
    private Timestamp responseTime;
    private String channel;
    private String errOcs; // Su dung trong code SMS
    private String resultValue;
    private String serial;
    private double money;
    private String balanceId;
    private String balanceExpire;

    public String getErrOcs() {
        return errOcs;
    }

    public void setErrOcs(String errOcs) {
        this.errOcs = errOcs;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Timestamp getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Timestamp requestTime) {
        this.requestTime = requestTime;
    }

    public Timestamp getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Timestamp responseTime) {
        this.responseTime = responseTime;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getBalanceExpire() {
        return balanceExpire;
    }

    public void setBalanceExpire(String balanceExpire) {
        this.balanceExpire = balanceExpire;
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    @Override
    public String toString() {
        StringBuilder info = new StringBuilder();
        info.append("\n<TransLog>");
        info.append("\n<type>").append(type).append("</type>");
        info.append("\n<msisdn>").append(msisdn==null ? "":msisdn).append("</msisdn>");
        info.append("\n<request>").append(request==null ? "":request).append("</request>");
        info.append("\n<response>").append(response==null ? "" : response).append("</response>");
        info.append("\n<errorCode>").append(errorCode==null ? "" : errorCode).append("</errorCode>");
        info.append("\n<command>").append(command==null ? "" : command).append("</command>");
        info.append("\n<requestTime>").append(requestTime==null ? "" :requestTime).append("</requestTime>");
        info.append("\n<responseTime>").append(responseTime==null ? "" : responseTime).append("</responseTime>");
        info.append("\n<channel>").append(channel==null ? "" : channel).append("</channel>");
        info.append("\n<errOcs>").append(errOcs==null ? "" : errOcs).append("</errOcs>");
        info.append("\n<resultValue>").append(responseTime==null ? "" : responseTime).append("</resultValue>");
        info.append("\n</TransLog>\n");
        return info.toString();

    }
}
