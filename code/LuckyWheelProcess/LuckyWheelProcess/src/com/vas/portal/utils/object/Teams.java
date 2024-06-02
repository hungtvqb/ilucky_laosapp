/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

/**
 *
 * @author Sungroup
 */
public class Teams implements VotedObj {

    private long id;
    private String teamName;
    private String teamCode;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(String teamCode) {
        this.teamCode = teamCode;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return teamName;
    }

    @Override
    public long getVotedId() {
        return id;
    }

    @Override
    public String getVotedCode() {
        return teamCode;
    }
}
