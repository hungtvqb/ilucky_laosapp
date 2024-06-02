package com.vas.wsfw.obj;

/**
 *
 * @author sungroup
 */
public class ProductInfo {

    private int productId;
    private String productName;
    private String smsSyntax;
    private double fee;
    private int numberSpin;
    private int bizId;
    private int expireDays;

    public int getExpireDays() {
        return expireDays;
    }

    public void setExpireDays(int expireDays) {
        this.expireDays = expireDays;
    }

    public int getBizId() {
        return bizId;
    }

    public void setBizId(int bizId) {
        this.bizId = bizId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSmsSyntax() {
        return smsSyntax;
    }

    public void setSmsSyntax(String smsSyntax) {
        this.smsSyntax = smsSyntax;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getNumberSpin() {
        return numberSpin;
    }

    public void setNumberSpin(int numberSpin) {
        this.numberSpin = numberSpin;
    }
}
