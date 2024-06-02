/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author Sungroup
 */
public class CdrRechargeObj {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String RECHARGE_TIME = "RECHARGE_TIME";
    public static final String INSERT_TIME = "INSERT_TIME";
    public static final String MONEY = "MONEY";
    public static final String CHANNEL = "CHANNEL";
    private long id;
    private String msisdn;
    private Timestamp rechargeTime;
    private Timestamp insertTime;
    private long money;
    private String channel;

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

    public Timestamp getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Timestamp rechargeTime) {
        this.rechargeTime = rechargeTime;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

}
