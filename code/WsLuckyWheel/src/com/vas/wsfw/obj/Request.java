/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author kdvt_tungtt8
 * @version x.x
 * @since May 27, 2011
 */
public class Request {

    private String requestId;
    private String userId;//msisdn
    private String receiverId;
    private String serviceId;
    private String service;
    private String info;
    private String receiveTime;
    private String user;
    private String pass;
    private String remainSecond;
    private String msisdn;
    private String isSms;
    private String param;
    private String isdn;
    private String amount;
    private String benIsdn;
    private String message;
    private String msgType;
    private String channel;
    private String froDate;
    private String toDate;
    private String fee;
    private String day;
    private String Issentsms;
    private String oldPass;
    private String newPass;
    private String parentIsdn;
    private String childIsdn;

    public String getParentIsdn() {
        return parentIsdn;
    }

    public void setParentIsdn(String parentIsdn) {
        this.parentIsdn = parentIsdn;
    }

    public String getChildIsdn() {
        return childIsdn;
    }

    public void setChildIsdn(String childIsdn) {
        this.childIsdn = childIsdn;
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getIssentsms() {
        return Issentsms;
    }

    public void setIssentsms(String Issentsms) {
        this.Issentsms = Issentsms;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getFroDate() {
        return froDate;
    }

    public void setFroDate(String froDate) {
        this.froDate = froDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBenIsdn() {
        return benIsdn;
    }

    public void setBenIsdn(String benIsdn) {
        this.benIsdn = benIsdn;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getIsSms() {
        return isSms;
    }

    public void setIsSms(String isSms) {
        this.isSms = isSms;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getRemainSecond() {
        return remainSecond;
    }

    public void setRemainSecond(String remainSecond) {
        this.remainSecond = remainSecond;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    @Override
    public String toString() {
        StringBuilder br = new StringBuilder();
        if (user != null) {
            br.append("<User>").append(user).append("</User>").append("\n");
        }
        if (user != null) {
            br.append("<Msisdn>").append(msisdn).append("</Msisdn>").append("\n");
        }
        if (service != null) {
            br.append("<Service>").append(service).append("</Service>").append("\n");
        }
        if (param != null) {
            br.append("<Param>").append(param).append("</Param>").append("\n");
        }
        if (message != null) {
            br.append("<Message>").append(message).append("</Message>").append("\n");
        }
        if (msgType != null) {
            br.append("<MsgType>").append(msgType).append("</MsgTypec>").append("\n");
        }
        if (channel != null) {
            br.append("<Channel>").append(channel).append("</Channel>").append("\n");
        }
        return br.toString();
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
