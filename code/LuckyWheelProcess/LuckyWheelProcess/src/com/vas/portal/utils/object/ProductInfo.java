/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.util.List;

/**
 *
 * @author tungnv6
 * @version x.x
 * @since April, 2015
 */
public class ProductInfo implements Record {

    public static final String ID = "ID";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    public static final String PRODUCT_TYPE = "PRODUCT_TYPE";
    public static final String FEE = "FEE";
    public static final String PRIZE = "PRIZE";
    public static final String STATUS = "STATUS";
    public static final String BALANCE_ID = "BALANCE_ID";
    public static final String FRAME_TIME = "FRAME_TIME";
    public static final String NUM_REG_IN_DAY = "NUM_REG_IN_DAY";
    public static final String SUB_SELECTION = "SUB_SELECTION";
    public static final String VALUE_FORMAT = "VALUE_FORMAT";
    public static final String GROUP_NAME = "GROUP_NAME";
    public static final String PATTERN_RESULT = "PATTERN_RESULT";
    public static final String MULTIPLE_LAPOULA = "MULTIPLE_LAPOULA";
    public static final String MULTIPLE_BALANCE = "MULTIPLE_BALANCE";
    private long id;
    private String productName;
    private int productType;
    private double fee;
    private String prize;
    private int status;
    private String balanceId;
    private List<String[]> frameTime;
    private int numRegInDay;
    private String subSelection;
    private String valueFormat;
    private String groupName;
    private String patternResult;
    private long multipleLapoula;
    private long multipleBalance;

    public long getMultipleBalance() {
        return multipleBalance;
    }

    public void setMultipleBalance(long multipleBalance) {
        this.multipleBalance = multipleBalance;
    }

    public long getMultipleLapoula() {
        return multipleLapoula;
    }

    public void setMultipleLapoula(long multipleLapoula) {
        this.multipleLapoula = multipleLapoula;
    }

    public String getPatternResult() {
        return patternResult;
    }

    public void setPatternResult(String patternResult) {
        this.patternResult = patternResult;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBalanceId() {
        return balanceId;
    }

    public void setBalanceId(String balanceId) {
        this.balanceId = balanceId;
    }

    public List<String[]> getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(List<String[]> frameTime) {
        this.frameTime = frameTime;
    }

    public int getNumRegInDay() {
        return numRegInDay;
    }

    public void setNumRegInDay(int numRegInDay) {
        this.numRegInDay = numRegInDay;
    }

    public String getSubSelection() {
        return subSelection;
    }

    public void setSubSelection(String subSelection) {
        this.subSelection = subSelection;
    }

    public String getValueFormat() {
        return valueFormat;
    }

    public void setValueFormat(String valueFormat) {
        this.valueFormat = valueFormat;
    }

    @Override
    public String toString() {
        return id + "|" + productName;
    }

    @Override
    public long getID() {
        return id;
    }
}
