/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author Sungroup
 */
public class ChargeLog {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String FEE = "FEE";
    public static final String CHARGE_TIME = "CHARGE_TIME";
    public static final String INSERT_TIME = "INSERT_TIME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String PRODUCT_NAME = "PRODUCT_NAME";
    private long id;
    private String msisdn;
    private double fee;
    private Timestamp chargeTime;
    private Timestamp insertTime;
    private String description;
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Timestamp getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(Timestamp chargeTime) {
        this.chargeTime = chargeTime;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
