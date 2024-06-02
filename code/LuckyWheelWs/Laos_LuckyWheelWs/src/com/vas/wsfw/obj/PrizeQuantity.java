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
public class PrizeQuantity {

    // addition
    private int id;
    private int prizeId;
    private String account;
    private String prizeDate;
    private int quantity;
    private int prized;
    private int totalId;
    private int totalQuantity;
    private int totalPrized;

    public int getTotalId() {
        return totalId;
    }

    public void setTotalId(int totalId) {
        this.totalId = totalId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPrizeDate() {
        return prizeDate;
    }

    public void setPrizeDate(String prizeDate) {
        this.prizeDate = prizeDate;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrized() {
        return prized;
    }

    public void setPrized(int prized) {
        this.prized = prized;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getTotalPrized() {
        return totalPrized;
    }

    public void setTotalPrized(int totalPrized) {
        this.totalPrized = totalPrized;
    }

    @Override
    public String toString() {
        return this.prizeId + "-" + this.account + "-" + this.quantity;
    }

}
