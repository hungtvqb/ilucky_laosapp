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
public class VotingObj implements VotedObj {

    public static final String ID = "ID";
    public static final String MSISDN = "MSISDN";
    public static final String VOTED_ID = "VOTE_ID";
    public static final String VOTED_CODE = "VOTE_CODE";
    public static final String VOTE_VALUE = "VOTE_VALUE";
    public static final String VOTE_TIME = "VOTE_TIME";
    public static final String PRO_CODE = "PRO_CODE";
    public static final String CHANNEL = "CHANNEL";
    private long id;
    private String msisdn;
    private long votedId;
    private String votedCode;
    private long voteValue;
    private Timestamp voteTime;
    private String proCode;
    private String channel;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
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

    public void setVotedId(long votedId) {
        this.votedId = votedId;
    }

    public void setVotedCode(String votedCode) {
        this.votedCode = votedCode;
    }

    public long getVoteValue() {
        return voteValue;
    }

    public void setVoteValue(long voteValue) {
        this.voteValue = voteValue;
    }

    public Timestamp getVoteTime() {
        return voteTime;
    }

    public void setVoteTime(Timestamp voteTime) {
        this.voteTime = voteTime;
    }

    @Override
    public long getVotedId() {
        return votedId;
    }

    @Override
    public String getVotedCode() {
        return votedCode;
    }

}
