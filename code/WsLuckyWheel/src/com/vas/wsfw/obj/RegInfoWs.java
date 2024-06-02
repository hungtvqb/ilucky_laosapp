/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author Sungroup
 */
public class RegInfoWs {

    private String productName;
    private String registerTime;
    private String expireTime;
    private String extendStatus;
    private String lastExtend;
    private int remainQuestion;
    private int freeQuestion;
    private int playedTimes;

    public int getFreeQuestion() {
        return freeQuestion;
    }

    public void setFreeQuestion(int freeQuestion) {
        this.freeQuestion = freeQuestion;
    }

    public int getRemainQuestion() {
        return remainQuestion;
    }

    public void setRemainQuestion(int remainQuestion) {
        this.remainQuestion = remainQuestion;
    }

    public int getPlayedTimes() {
        return playedTimes;
    }

    public void setPlayedTimes(int playedTimes) {
        this.playedTimes = playedTimes;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getExtendStatus() {
        return extendStatus;
    }

    public void setExtendStatus(String extendStatus) {
        this.extendStatus = extendStatus;
    }

    public String getLastExtend() {
        return lastExtend;
    }

    public void setLastExtend(String lastExtend) {
        this.lastExtend = lastExtend;
    }
}
