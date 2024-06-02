/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import com.viettel.cluster.agent.integration.Record;
import java.sql.Timestamp;

/**
 *
 * @author Sungroup
 */
public class ExecutePrizeObj implements Record {

    public static final String ID = "ID";
    public static final String PRIZE_ID = "PRIZE_ID";
    public static final String MSISDN = "MSISDN";
    public static final String INSERT_TIME = "INSERT_TIME";
    private long id;
    private long prizeId;
    private String msisdn;
    private Timestamp insertTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(long prizeId) {
        this.prizeId = prizeId;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public Timestamp getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Timestamp insertTime) {
        this.insertTime = insertTime;
    }

    @Override
    public String toString() {
        return msisdn + "|" + prizeId;
    }

    @Override
    public long getID() {
        return id;
    }
}
