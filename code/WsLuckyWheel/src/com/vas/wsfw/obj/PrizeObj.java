/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.util.List;

/**
 *
 * @author Sungroup
 */
public class PrizeObj {

    public static final String ID = "ID";
    public static final String ACTION_CODE = "ACTION_CODE";
    public static final String NUMBER_PRIZE = "NUMBER_PRIZE";
    public static final String PERIOD = "PERIOD";
    public static final String CHANNEL = "CHANNEL";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String TIMES_PRIZE = "TIMES_PRIZE";
    public static final String STATUS = "STATUS";
    public static final String IS_TOP = "IS_TOP";
    public static final String PRIZE_NAME = "PRIZE_NAME";
    public static final String IS_AUTO = "IS_AUTO";
    public static final String NUMBER_PRIZE_TYPE = "NUMBER_PRIZE_TYPE";
    public static final String GROUP_ID = "GROUP_ID";
    public static final String SUB_GROUP_PRIZE = "SUB_GROUP_PRIZE";
    public static final String PREFIX_MSISDN = "PREFIX_MSISDN";
    public static final String IS_PAD = "IS_PAD";
    public static final String IMAGE = "IMAGE";
    private int id;
    private String actionCode;
    private int numberPrize;
    private int period;
    private int channel;
    private String description;
    private int timesPrize;
    private int status;
    private int isTop;
    private String prizeName;
    private int isAuto;
    private int numberPrizeType;
    private int groupPrize;
    private int subGroupPrize;
    private List<String> prefixMsisdn;
    private int isPad;
    private String image;
    // addition
//    private String account;
//    private String prizeDate;
//    private int quantity;
//    private int prized;
//    private int totalQuantity;
//    private int totalPrized;

//    public String getAccount() {
//        return account;
//    }
//
//    public void setAccount(String account) {
//        this.account = account;
//    }
//
//    public String getPrizeDate() {
//        return prizeDate;
//    }
//
//    public void setPrizeDate(String prizeDate) {
//        this.prizeDate = prizeDate;
//    }
//
//    public int getQuantity() {
//        return quantity;
//    }
//
//    public void setQuantity(int quantity) {
//        this.quantity = quantity;
//    }
//
//    public int getPrized() {
//        return prized;
//    }
//
//    public void setPrized(int prized) {
//        this.prized = prized;
//    }
//
//    public int getTotalQuantity() {
//        return totalQuantity;
//    }
//
//    public void setTotalQuantity(int totalQuantity) {
//        this.totalQuantity = totalQuantity;
//    }
//
//    public int getTotalPrized() {
//        return totalPrized;
//    }
//
//    public void setTotalPrized(int totalPrized) {
//        this.totalPrized = totalPrized;
//    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIsPad() {
        return isPad;
    }

    public void setIsPad(int isPad) {
        this.isPad = isPad;
    }

    public List<String> getPrefixMsisdn() {
        return prefixMsisdn;
    }

    public void setPrefixMsisdn(List<String> prefixMsisdn) {
        this.prefixMsisdn = prefixMsisdn;
    }

    public int getSubGroupPrize() {
        return subGroupPrize;
    }

    public void setSubGroupPrize(int subGroupPrize) {
        this.subGroupPrize = subGroupPrize;
    }

    public int getGroupPrize() {
        return groupPrize;
    }

    public void setGroupPrize(int groupPrize) {
        this.groupPrize = groupPrize;
    }

    public int getNumberPrizeType() {
        return numberPrizeType;
    }

    public void setNumberPrizeType(int numberPrizeType) {
        this.numberPrizeType = numberPrizeType;
    }

    public int getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(int isAuto) {
        this.isAuto = isAuto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public int getNumberPrize() {
        return numberPrize;
    }

    public void setNumberPrize(int numberPrize) {
        this.numberPrize = numberPrize;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimesPrize() {
        return timesPrize;
    }

    public void setTimesPrize(int timesPrize) {
        this.timesPrize = timesPrize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsTop() {
        return isTop;
    }

    public void setIsTop(int isTop) {
        this.isTop = isTop;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    @Override
    public String toString() {
        return this.prizeName;
    }

}
