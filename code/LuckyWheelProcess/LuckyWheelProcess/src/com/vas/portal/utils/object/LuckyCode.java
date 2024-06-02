/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author
 */
public class LuckyCode {

    private int id;
    private String msisdn;
    private String code;
    private Timestamp insertTime;
    private Timestamp expireTime;
    private int period;
    private int status;
    private long remainTime;
    private Timestamp confirmTime;
    private int confirmChannel;
    private int fee;
    private long countTimes;

    public long getCountTimes() {
        return countTimes;
    }

    public void setCountTimes(long countTimes) {
        this.countTimes = countTimes;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public Timestamp getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Timestamp confirmTime) {
        this.confirmTime = confirmTime;
    }

    public int getConfirmChannel() {
        return confirmChannel;
    }

    public void setConfirmChannel(int confirmChannel) {
        this.confirmChannel = confirmChannel;
    }

    @Override
    public String toString() {
        return msisdn + "|" + code;
    }

}
