/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

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
    private long id;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
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
