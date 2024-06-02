/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author sungroup
 */
public class SpinTotalObj implements Record {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String CREATED_TIME = "CREATED_TIME";
    public static final String SPIN_NUM = "SPIN_NUM";
    public static final String SPIN_GIFT = "SPIN_GIFT";
    public static final String LAST_ACTION = "LAST_ACTION";
    public static final String LAST_UPDATE = "LAST_UPDATE";
    public static final String LAST_RENEW = "LAST_RENEW";
    private long id;
    private String msisdn;
    private Timestamp createdTime;
    private int spinNum;
    private int spinGift;
    private String lastAction;
    private Timestamp lastUpdate;
    private Timestamp lastRenew;

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Timestamp getLastRenew() {
        return lastRenew;
    }

    public void setLastRenew(Timestamp lastRenew) {
        this.lastRenew = lastRenew;
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

    public int getSpinNum() {
        return spinNum;
    }

    public void setSpinNum(int spinNum) {
        this.spinNum = spinNum;
    }

    public int getSpinGift() {
        return spinGift;
    }

    public void setSpinGift(int spinGift) {
        this.spinGift = spinGift;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setLastAction(String lastAction) {
        this.lastAction = lastAction;
    }

    @Override
    public long getID() {
        return id;
    }

    @Override
    public String toString() {
        return msisdn + "-" + spinNum + "|" + spinGift;
    }

}
