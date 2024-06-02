/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author sungroup
 */
public class MtExtend implements Record {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String MESSAGE = "MESSAGE";
    public static final String RETRY_NUM = "RETRY_NUM";
    public static final String CHANNEL = "CHANNEL";
    public static final String MESSAGE_TYPE = "MESSAGE_TYPE";
    public static final String RECEIVE_TIME = "RECEIVE_TIME";
    public static final String SEND_TIME = "SEND_TIME";
    public static final String NODE_NAME = "NODE_NAME";
    public static final String CLUSTER_NAME = "CLUSTER_NAME";
    public static final String BROADCAST_ID = "BROADCAST_ID";
    private long id;
    private String msisdn;
    private String message;
    private int retryNum;
    private String channel;
    private int messageType;
    private Timestamp receiveTime;
    private Timestamp sendTime;
    private String nodeName;
    private String clusterName;
    private long broadcastId;

    public long getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(long broadcastId) {
        this.broadcastId = broadcastId;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public Timestamp getSendTime() {
        return sendTime;
    }

    public void setSendTime(Timestamp sendTime) {
        this.sendTime = sendTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(int retryNum) {
        this.retryNum = retryNum;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Timestamp getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Timestamp receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public long getID() {
        return id;
    }
}
