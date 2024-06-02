/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

/**
 *
 * @author MinhNH
 */
public class Route {
    
    private String name;
    private String prefixMsisdn;
    private String listDbName;
    
    public String getListDbName() {
        return listDbName;
    }
    
    public void setListDbName(String listDbName) {
        this.listDbName = listDbName;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getPrefixMsisdn() {
        return prefixMsisdn;
    }
    
    public void setPrefixMsisdn(String prefixMsisdn) {
        this.prefixMsisdn = prefixMsisdn;
    }
    
    public String getDbNamebyMsisdn(String msisdn) {
        String result = "";
        String[] listDb = listDbName.split(",");
        String[] listPrefix = prefixMsisdn.split(",");
        for (int i = 0; i < listPrefix.length; i++) {
            if (msisdn.substring(0, listPrefix[i].length()).equals(listPrefix[i])) {
                int tail = Integer.parseInt(msisdn.substring(msisdn.length() - 2, msisdn.length()));
                int mod = tail % listDb.length;
                result = listDb[mod];
                break;
            }
        }
        return result;
    }
}
