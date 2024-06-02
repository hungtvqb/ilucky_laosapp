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
public class ServicesObj {

    public static final String ID = "ID";
    public static final String SV_CODE = "SV_CODE";
    public static final String SV_NAME = "SV_NAME";
    public static final String STATUS = "STATUS";
    private int id;
    private String svCode;
    private String svName;
    private int status;
    private SvAdv svAdv;
    private SvReport svReport;

    public SvAdv getSvAdv() {
        return svAdv;
    }

    public void setSvAdv(SvAdv svAdv) {
        this.svAdv = svAdv;
    }

    public SvReport getSvReport() {
        return svReport;
    }

    public void setSvReport(SvReport svReport) {
        this.svReport = svReport;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSvCode() {
        return svCode;
    }

    public void setSvCode(String svCode) {
        this.svCode = svCode;
    }

    public String getSvName() {
        return svName;
    }

    public void setSvName(String svName) {
        this.svName = svName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
