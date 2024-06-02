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
public class AdsObj {

    public static final String ID = "ID";
    public static final String ADS_NAME = "ADS_NAME";
    public static final String VIDEO_URL = "VIDEO_URL";
    public static final String IMAGE_URL = "IMAGE_URL";
    public static final String TIMEOUT = "TIMEOUT";
//    public static final String CREATED_TIME = "CREATED_TIME";
    public static final String STATUS = "STATUS";
    private long id;
    private String adsName;
    private String videoUrl;
    private String imageUrl;
    private int timeout;
//    private Timestamp createdTime;
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAdsName() {
        return adsName;
    }

    public void setAdsName(String adsName) {
        this.adsName = adsName;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
