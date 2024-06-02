/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 *
 * @author nhungttt3
 */
public class HistoryPlay {

    private long Id;
    private long listId;
    private long registerId;
    private String msisdn;
    private String productName;
    private Timestamp playDay;
    private int type;
    private int typeAdd;
    private int numberAdd;
    private int SpinedTime;
    private int playedTimes;
    private int promotionId;
    private double money;
    private String balanceId;
    private String note;

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(long registerId) {
        this.registerId = registerId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Timestamp getPlayDay() {
        return playDay;
    }

    public void setPlayDay(Timestamp playDay) {
        this.playDay = playDay;
    }

    public int getSpinedTime() {
        return SpinedTime;
    }

    public void setSpinedTime(int SpinedTime) {
        this.SpinedTime = SpinedTime;
    }

    public int getPlayedTimes() {
        return playedTimes;
    }

    public void setPlayedTimes(int playedTimes) {
        this.playedTimes = playedTimes;
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTypeAdd() {
        return typeAdd;
    }

    public void setTypeAdd(int typeAdd) {
        this.typeAdd = typeAdd;
    }

    public int getNumberAdd() {
        return numberAdd;
    }

    public void setNumberAdd(int numberAdd) {
        this.numberAdd = numberAdd;
    }
}
