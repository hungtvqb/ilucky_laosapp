/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.obj;

import java.util.List;

/**
 *
 * @author
 */
public class Response {

    private String errorCode;
    private String resultCode;
    private String content;
    private List<Questions> listQuestion;
    private List<PrizeObj> listPrize;
    private List<MissionObj> listMission;
    private List<AdsObj> listAds;
    private List<RegInfoWs> listRegInfo;
    private List<UserPlayObj> listUserPrize;
    private SpinTotalObj spinTotal;
    private int role;
//    private long id;
    private long huValue;
    private long playId;
    private int rowOnPage;
    private int totalPage;
    private int totalRow;

    public List<AdsObj> getListAds() {
        return listAds;
    }

    public void setListAds(List<AdsObj> listAds) {
        this.listAds = listAds;
    }

    public int getTotalRow() {
        return totalRow;
    }

    public void setTotalRow(int totalRow) {
        this.totalRow = totalRow;
    }

    public int getRowOnPage() {
        return rowOnPage;
    }

    public void setRowOnPage(int rowOnPage) {
        this.rowOnPage = rowOnPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
 

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(long playId) {
        this.playId = playId;
    }

    public List<MissionObj> getListMission() {
        return listMission;
    }

    public void setListMission(List<MissionObj> listMission) {
        this.listMission = listMission;
    }

    public SpinTotalObj getSpinTotal() {
        return spinTotal;
    }

    public void setSpinTotal(SpinTotalObj spinTotal) {
        this.spinTotal = spinTotal;
    }

    public long getHuValue() {
        return huValue;
    }

    public void setHuValue(long huValue) {
        this.huValue = huValue;
    }

    public List<UserPlayObj> getListUserPrize() {
        return listUserPrize;
    }

    public void setListUserPrize(List<UserPlayObj> listUserPrize) {
        this.listUserPrize = listUserPrize;
    }

    public List<RegInfoWs> getListRegInfo() {
        return listRegInfo;
    }

    public void setListRegInfo(List<RegInfoWs> listRegInfo) {
        this.listRegInfo = listRegInfo;
    }

    public List<PrizeObj> getListPrize() {
        return listPrize;
    }

    public void setListPrize(List<PrizeObj> listPrize) {
        this.listPrize = listPrize;
    }

//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<Questions> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Questions> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return errorCode + "|" + content;
    }
}
