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
public class MissionObj {

    public static final String ID = "ID";
    public static final String MISSION_NAME = "MISSION_NAME";
    public static final String URL = "URL";
    public static final String IMAGE = "IMAGE";
    public static final String DESCRIPTION = "DESCRIPTION";
//    public static final String CREATED_TIME = "CREATED_TIME";
    public static final String STATUS = "STATUS";
    private long id;
    private String missionName;
    private String url;
    private String image;
    private String description;
//    private Timestamp createdTime;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public Timestamp getCreatedTime() {
//        return createdTime;
//    }
//
//    public void setCreatedTime(Timestamp createdTime) {
//        this.createdTime = createdTime;
//    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
