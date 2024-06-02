/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author sungroup
 */
public class RegisterInfo {

    private long registerId;
    private String msisdn;
    private Timestamp registerTime;
    private Timestamp endTime;
    private Timestamp expireTime;
    private double alreadyCharged;
    private int extendStatus;
    private Timestamp lastExtend;
    private int playedTimes;
    private int numQuestions;
    private int status;

    public int getPlayedTimes() {
        return playedTimes;
    }

    public void setPlayedTimes(int playedTimes) {
        this.playedTimes = playedTimes;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public void setNumQuestions(int numQuestions) {
        this.numQuestions = numQuestions;
    }

    public Timestamp getLastExtend() {
        return lastExtend;
    }

    public void setLastExtend(Timestamp lastExtend) {
        this.lastExtend = lastExtend;
    }

    public double getAlreadyCharged() {
        return alreadyCharged;
    }

    public void setAlreadyCharged(double alreadyCharged) {
        this.alreadyCharged = alreadyCharged;
    }

    public int getExtendStatus() {
        return extendStatus;
    }

    public void setExtendStatus(int extendStatus) {
        this.extendStatus = extendStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(long id) {
        this.registerId = id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

//    public int getPlayedTimes() {
//        return playedTimes;
//    }
//
//    public void setPlayedTimes(int playedTimes) {
//        this.playedTimes = playedTimes;
//    }
}
