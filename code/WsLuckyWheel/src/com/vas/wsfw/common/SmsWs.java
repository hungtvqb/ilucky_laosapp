/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

import sendmt.MtStub;
/**
 *
 * @author tungnv6
 */
public class SmsWs {

    private int id;
    private String url;
    private String xmlns;
    private String user;
    private String pass;
    private MtStub stub;

    public SmsWs() {
    }

    public SmsWs(int id, String url, String xmlns, String user, String pass, boolean initStub) {
        this.id = id;
        this.url = url;
        this.xmlns = xmlns;
        this.user = user;
        this.pass = pass;
        if (initStub) {
            this.stub = new MtStub(url, xmlns, user, pass);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public MtStub getStub() {
        return stub;
    }

    public void setStub(MtStub stub) {
        this.stub = stub;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }

    /**
     * Ham gui sms
     * @param sessionId
     * @param serviceId
     * @param sender
     * @param receiver
     * @param contentType
     * @param content
     * @param status
     * @return
     */
    public int send(String sessionId, String serviceId,
            String sender, String receiver, String contentType, String content, String status) {
        return stub.send(sessionId, serviceId, sender, receiver, contentType, content, status);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("URL = ");
        buffer.append(url);
        buffer.append("\r\n");
        buffer.append("USER = ");
        buffer.append(user);
        buffer.append("\r\n");
        return buffer.toString();
    }

    public String getInfor() {
        return url;
    }
}