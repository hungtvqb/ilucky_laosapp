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
public class SpinGift {

    private long id;
    private String msisdn;
    private String giftMsisdn;
    private Timestamp giftTime;
    private int numberSpin;
    private Timestamp lastUpdate;
    private int subType;

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
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

    public String getGiftMsisdn() {
        return giftMsisdn;
    }

    public void setGiftMsisdn(String giftMsisdn) {
        this.giftMsisdn = giftMsisdn;
    }

    public Timestamp getGiftTime() {
        return giftTime;
    }

    public void setGiftTime(Timestamp giftTime) {
        this.giftTime = giftTime;
    }

    public int getNumberSpin() {
        return numberSpin;
    }

    public void setNumberSpin(int numberSpin) {
        this.numberSpin = numberSpin;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
