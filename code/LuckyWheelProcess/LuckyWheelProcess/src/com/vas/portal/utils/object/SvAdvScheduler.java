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
public class SvAdvScheduler {

    public static final String ID = "ID";
    public static final String SCHEDULE_ID = "SCHEDULE_ID";
    public static final String SV_ADV_ID = "SV_ADV_ID";
//    public static final String FROM_DATE = "FROM_DATE";
//    public static final String TO_DATE = "TO_DATE";
//    public static final String SCHEDULE_NAME = "SCHEDULE_NAME";
    public static final String PERCENT = "PERCENT";

    private int id;
    private int scheduleId;
    private int svAdvId;
    private int percent;
    private SvAdv svAdv;

    public SvAdv getSvAdv() {
        return svAdv;
    }

    public void setSvAdv(SvAdv svAdv) {
        this.svAdv = svAdv;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getSvAdvId() {
        return svAdvId;
    }

    public void setSvAdvId(int svAdvId) {
        this.svAdvId = svAdvId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return svAdv.getSvCode() + ": " + percent + "%";
    }
}
