/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author Sungroup
 */
public class UserReport {

    public static final String USERNAME = "USERNAME";
    public static final String PASSWORD = "PASSWORD";
    public static final String WRONGTIME = "WRONGTIME";
    public static final String LOGERROR = "LOGERROR";
    public static final String LOCKED = "LOCKED";
    public static final String STATUS = "STATUS";
    public static final String ROLE = "ROLE";
    private String username;
    private String password;
    private int wrongtime;
    private String logerror;
    private int status;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWrongtime() {
        return wrongtime;
    }

    public void setWrongtime(int wrongtime) {
        this.wrongtime = wrongtime;
    }

    public String getLogerror() {
        return logerror;
    }

    public void setLogerror(String logerror) {
        this.logerror = logerror;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
