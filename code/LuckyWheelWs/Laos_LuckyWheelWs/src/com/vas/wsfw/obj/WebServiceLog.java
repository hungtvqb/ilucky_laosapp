/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.sql.Timestamp;

/**
 *
 * @author tungnv6
 */
public class WebServiceLog {

    long Id;
    double Fee;
    String IpClient;
    String Msisdn;
    String Request;
    String Response;
    String ResponseCode;
    Timestamp RequestTime;
    Timestamp ResponseTime;
    String Type;
    long UserId;
    String Channel;
    String UserName;
    String WsName;

    public String getWsName() {
        return WsName;
    }

    public void setWsName(String WsName) {
        this.WsName = WsName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String Channel) {
        this.Channel = Channel;
    }

    public double getFee() {
        return Fee;
    }

    public void setFee(double Fee) {
        this.Fee = Fee;
    }

    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
    }

    public String getIpClient() {
        return IpClient;
    }

    public void setIpClient(String IpClient) {
        this.IpClient = IpClient;
    }

    public String getMsisdn() {
        return Msisdn;
    }

    public void setMsisdn(String Msisdn) {
        this.Msisdn = Msisdn;
    }

    public String getRequest() {
        return Request;
    }

    public void setRequest(String Request) {
        this.Request = Request;
    }

    public Timestamp getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(Timestamp RequestTime) {
        this.RequestTime = RequestTime;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResponseCode() {
        return ResponseCode;
    }

    public void setResponseCode(String ResponseCode) {
        this.ResponseCode = ResponseCode;
    }

    public Timestamp getResponseTime() {
        return ResponseTime;
    }

    public void setResponseTime(Timestamp ResponseTime) {
        this.ResponseTime = ResponseTime;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public long getUserId() {
        return UserId;
    }

    public void setUserId(long UserId) {
        this.UserId = UserId;
    }
}
