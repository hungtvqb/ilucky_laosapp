/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

import java.sql.Timestamp;

/**
 *
 * @author Sungroup
 */
public class DonatingObj {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String TEAM_ID = "TEAM_ID";
    public static final String TEAM_CODE = "TEAM_CODE";
    public static final String DONATE_VALUE = "DONATE_VALUE";
    public static final String DONATE_TIME = "DONATE_TIME";
    private long id;
    private String msisdn;
    private long teamId;
    private String teamCode;
    private long donateValue;
    private Timestamp donateTime;

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

    public long getTeamId() {
        return teamId;
    }

    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public long getDonateValue() {
        return donateValue;
    }

    public void setDonateValue(long donateValue) {
        this.donateValue = donateValue;
    }

    public Timestamp getDonateTime() {
        return donateTime;
    }

    public void setDonateTime(Timestamp donateTime) {
        this.donateTime = donateTime;
    }

}
