/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 *
 * @author sungroup
 */
public class SpinRecharge {

    private long id;
    private String msisdn;
    private Timestamp chargeTime;
    private double money;
    private int spinAdded;
    private int status;
    private Timestamp lastUpdate;

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

    public Timestamp getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Timestamp chargeTime) {
        this.chargeTime = chargeTime;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getSpinAdded() {
        return spinAdded;
    }

    public void setSpinAdded(int spinAdded) {
        this.spinAdded = spinAdded;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
