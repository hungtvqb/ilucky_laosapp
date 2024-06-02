/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.object;

/**
 *
 * @author sungroup
 */
public class MpsConfigObj {

    public static final String SERVICE = "SERVICE";
    public static final String SUB_SERVICE = "SUB_SERVICE";
    public static final String CP_NAME = "CP_NAME";
    public static final String COMMAND = "COMMAND";
    public static final String CATEGORY = "CATEGORY";
    public static final String PRICE = "PRICE";
    public static final String ACTION_CODE = "ACTION_CODE";
    public static final String KEY_PATH = "KEY_PATH";
    private String service;
    private String subService;
    private String cpName;
    private String command;
    private String category;
    private String price;
    private String actionCode;
    private String keyPath;

    public String getKeyPath() {
        return keyPath;
    }

    public void setKeyPath(String keyPath) {
        this.keyPath = keyPath;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSubService() {
        return subService;
    }

    public void setSubService(String subService) {
        this.subService = subService;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

}
