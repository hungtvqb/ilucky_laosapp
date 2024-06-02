/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.obj;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author BinhBM
 */
public class Subscriber
{
    protected Long subId;
    protected String isdn;
    protected String imsi;
    protected String serial;
    protected Long custId;
    protected String custName;
    protected String subName;
    protected Date birthDate;
    protected String gender;
    protected String productCode;
    protected String orgProductCode;
    protected String actStatus;
    protected Date validDatetime;
    protected Date staDatetime;
    protected Date endDatetime;
    protected Long status;
    protected Long finishReasonId;
    protected String userCreated;
    protected String shopCode;
    protected String firstShopCode;
    protected Long staffId;
    protected String subType;
    protected String vip;
    protected String subCosHuawei;
    protected String subCosZte;
    protected String startMoney;
    protected String passWord;
    protected Long quota;
    protected String promotionCode;
    protected String regType;
    protected Long reasonDepositId;
    protected String ipStatic;
    protected Long isNewSub;
    protected Long offerId;
    protected String offerName;
    protected Date changeDatetime;
    Map resourceMap = new HashMap();
    protected Map attributeMap = new HashMap();
    protected Map vasMap = new HashMap();
    protected Map vasCheckBoxMap = new HashMap();
    protected String message;
    protected String address;
    protected String province;
    protected String district;
    protected String precinct;

    protected Long contractId;

    // add by TuanTM2 - 17/12/2009
    protected Long numResetZone;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPrecinct() {
        return precinct;
    }

    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getNumResetZone() {
        return numResetZone;
    }

    public void setNumResetZone(Long numResetZone) {
        this.numResetZone = numResetZone;
    }
    

    public Map getAttributeMap() {
        return attributeMap;
    }

    public void setAttributeMap(Map attributeMap) {
        this.attributeMap = attributeMap;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public Date getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(Date endDatetime) {
        this.endDatetime = endDatetime;
    }

    public Long getFinishReasonId() {
        return finishReasonId;
    }

    public void setFinishReasonId(Long finishReasonId) {
        this.finishReasonId = finishReasonId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIpStatic() {
        return ipStatic;
    }

    public void setIpStatic(String ipStatic) {
        this.ipStatic = ipStatic;
    }

    public Long getIsNewSub() {
        return isNewSub;
    }

    public void setIsNewSub(Long isNewSub) {
        this.isNewSub = isNewSub;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    

    public Long getQuota() {
        return quota;
    }

    public void setQuota(Long quota) {
        this.quota = quota;
    }

    public Long getReasonDepositId() {
        return reasonDepositId;
    }

    public void setReasonDepositId(Long reasonDepositId) {
        this.reasonDepositId = reasonDepositId;
    }

//    public Long getRegReasonId() {
//        return regReasonId;
//    }
//
//    public void setRegReasonId(Long regReasonId) {
//        this.regReasonId = regReasonId;
//    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getRegType() {
        return regType;
    }

    public void setRegType(String regType) {
        this.regType = regType;
    }

    public Map getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map resourceMap) {
        this.resourceMap = resourceMap;
    }

    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    public Date getStaDatetime() {
        return staDatetime;
    }

    public void setStaDatetime(Date staDatetime) {
        this.staDatetime = staDatetime;
    }    

    public Long getSubId() {
        return subId;
    }

    public void setSubId(Long subId) {
        this.subId = subId;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getUserCreated() {
        return userCreated;
    }

    public void setUserCreated(String userCreated) {
        this.userCreated = userCreated;
    }

    public Map getVasCheckBoxMap() {
        return vasCheckBoxMap;
    }

    public void setVasCheckBoxMap(Map vasCheckBoxMap) {
        this.vasCheckBoxMap = vasCheckBoxMap;
    }

    public Map getVasMap() {
        return vasMap;
    }

    public void setVasMap(Map vasMap) {
        this.vasMap = vasMap;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(String startMoney) {
        this.startMoney = startMoney;
    }

    public String getSubCosHuawei() {
        return subCosHuawei;
    }

    public void setSubCosHuawei(String subCosHuawei) {
        this.subCosHuawei = subCosHuawei;
    }

    public String getSubCosZte() {
        return subCosZte;
    }

    public void setSubCosZte(String subCosZte) {
        this.subCosZte = subCosZte;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getActStatus() {
        return actStatus;
    }

    public void setActStatus(String actStatus) {
        this.actStatus = actStatus;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getIsdn() {
        return isdn;
    }

    public void setIsdn(String isdn) {
        this.isdn = isdn;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }
    
   

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public Date getValidDatetime()
    {
        return validDatetime;
    }

    public void setValidDatetime(Date validDatetime)
    {
        this.validDatetime = validDatetime;
    }

    public String getOrgProductCode()
    {
        return orgProductCode;
    }

    public void setOrgProductCode(String orgProductCode)
    {
        this.orgProductCode = orgProductCode;
    }

    public Long getStaffId()
    {
        return staffId;
    }

    public void setStaffId(Long staffId)
    {
        this.staffId = staffId;
    }

    public String getFirstShopCode()
    {
        return firstShopCode;
    }

    public void setFirstShopCode(String firstShopCode)
    {
        this.firstShopCode = firstShopCode;
    }

    public Date getChangeDatetime() {
        return changeDatetime;
    }

    public void setChangeDatetime(Date changeDatetime) {
        this.changeDatetime = changeDatetime;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }
}
