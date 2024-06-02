/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author loilv3
 */
public class MtRecord implements Record {

    public static final String MT_ID = "MT_ID";
    public static final String MO_HIS_ID = "MO_HIS_ID";
    public static final String MSISDN = "MSISDN";
    public static final String MESSAGE = "MESSAGE";
    public static final String RETRY_NUM = "RETRY_NUM";
    public static final String CHANNEL = "CHANNEL";
    public static final String RECEIVE_TIME = "RECEIVE_TIME";
    private long mtId;
    private long moHisId;
    private String msisdn;
    private String message;
    private String retryNum;
    private String channel;
    private Timestamp receiveTime;
    private String nodeName;
    private String clusterName;

    public long getMtId() {
        return mtId;
    }

    public void setMtId(long mtId) {
        this.mtId = mtId;
    }

    public long getMoHisId() {
        return moHisId;
    }

    public void setMoHisId(long moHisId) {
        this.moHisId = moHisId;
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

    public String getRetryNum() {
        return retryNum;
    }

    public void setRetryNum(String retryNum) {
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

    @Override
    public long getID() {
        return mtId;
    }
}
