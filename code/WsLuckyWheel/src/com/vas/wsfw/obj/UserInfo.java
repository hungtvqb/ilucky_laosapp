/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author truongnk1
 */
public class UserInfo {

    public static final String WS_USER_ID = "WS_USER_ID";
    public static final String USER = "USERNAME";
    public static final String PASS = "PASSWORD";
    public static final String IP = "IP_ADDRESS";
//    public static final String CHANNEL = "CHANNEL";
//    public static final String IS_ADMIN = "IS_ADMIN";
    private long wsUserId;
    private String user;
    private String pass;
    private String ip;

//    public boolean isAdmin() {
//        return admin;
//    }
//
//    public void setAdmin(boolean admin) {
//        this.admin = admin;
//    }

//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    //    private String channel;
//    private boolean admin;
//        return admin;
//    }
//
//    public void setAdmin(boolean admin) {
//        this.admin = admin;
//    }

//    public String getChannel() {
//        return channel;
//    }
//
//    public void setChannel(String channel) {
//        this.channel = channel;
//    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;

    public long getWsUserId() {
        return wsUserId;
    }

    public void setWsUserId(long wsUserId) {
        this.wsUserId = wsUserId;
    }
//    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    @Override
    public String toString() {
        StringBuilder br = new StringBuilder();
        br.append("<user>").append(user == null ? "" : user).append("</user>").append("\n");
        //br.append("<pass>").append(errOcs).append("</pass>").append("\n");
        br.append("<ip>").append(ip == null ? "" : ip).append("</ip>").append("\n");
//        br.append("<channel>").append(channel == null ? "" : channel).append("</channel>").append("\n");
//        br.append("<isAdmin>").append(admin).append("</isAdmin>").append("\n");

        return br.toString();
    }
}
