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
public class SvAdv {

    public static final String SV_CODE = "SV_CODE";
    public static final String MSG_ADV = "MSG_ADV";
    public static final String MSG_ADV_PUSH = "MSG_ADV_PUSH";
    public static final String CHANNEL_ADV = "CHANNEL_ADV";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String FROM_MONEY = "FROM_MONEY";
    public static final String STATUS = "STATUS";
    private String svCode;
    private String msgAdv;
    private String msgAdvPush;
    private String channelAdv;
    private int messageType;
    private long fromMoney;
    private int status;
    private int scheduleDetailId;

    public int getScheduleDetailId() {
        return scheduleDetailId;
    }

    public void setScheduleDetailId(int scheduleDetailId) {
        this.scheduleDetailId = scheduleDetailId;
    }

    public String getSvCode() {
        return svCode;
    }

    public void setSvCode(String svCode) {
        this.svCode = svCode;
    }

    public String getMsgAdv() {
        return msgAdv;
    }

    public void setMsgAdv(String msgAdv) {
        this.msgAdv = msgAdv;
    }

    public String getMsgAdvPush() {
        return msgAdvPush;
    }

    public void setMsgAdvPush(String msgAdvPush) {
        this.msgAdvPush = msgAdvPush;
    }

    public String getChannelAdv() {
        return channelAdv;
    }

    public void setChannelAdv(String channelAdv) {
        this.channelAdv = channelAdv;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public long getFromMoney() {
        return fromMoney;
    }

    public void setFromMoney(long fromMoney) {
        this.fromMoney = fromMoney;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
