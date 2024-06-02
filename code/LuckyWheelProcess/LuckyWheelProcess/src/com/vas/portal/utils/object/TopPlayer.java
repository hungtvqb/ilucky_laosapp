/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

/**
 *
 * @author sungroup
 */
public class TopPlayer {

    private int score;
//    private long accountId;
    private String msisdn;
    private String code;
    private String prizeDescription;
//    private int rank;
//    private int money;

//    public int getMoney() {
//        return money;
//    }
//
//    public void setMoney(int money) {
//        this.money = money;
//    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

//    public int getRank() {
//        return rank;
//    }
//
//    public void setRank(int rank) {
//        this.rank = rank;
//    }
//    public long getAccountId() {
//        return accountId;
//    }
//
//    public void setAccountId(long accountId) {
//        this.accountId = accountId;
//    }
    @Override
    public String toString() {
        return msisdn;
    }
}
