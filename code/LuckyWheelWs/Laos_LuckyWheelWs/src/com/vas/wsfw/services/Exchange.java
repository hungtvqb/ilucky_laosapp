/*
 * @Exchange.java    version 1.0
 *
 * Created on May 3, 2010
 *
 * Copyright 2006 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.services;

import com.viettel.common.ExchMsg;
import java.util.Date;
import com.vas.wsfw.common.WebserviceManager;
import com.vas.wsfw.common.Common;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author: toanpd
 * @since: May 3, 2010
 */
public class Exchange extends ExchangeClientChannel {

    private Logger logger;
    private Logger detailLogger;
    private String loggerLabel = "Exchange: ";
    //private ExchangeChannel channel;
//    private String DATE_FORMAT = "yyyy-MM-dd";
//    private String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
//    private java.text.SimpleDateFormat sdf;
    private java.text.SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private java.text.SimpleDateFormat fullDf = new SimpleDateFormat("yyyyMMddHHmmss");
//    private SimpleDateFormat dateFormatExchVal = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
//    private SimpleDateFormat fsdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//    private Calendar c1;
    //
//    private static final String NOT_REG = "S-PPS-68002";
//    private static final String POS_REGISTER = "1";
//    private static final String POS_UNREGISTER = "0";
//    private static final String NOT_POST = "S-PRF-00025";
//    public static final String EXCHGW_RESULT_ERR_CODE_IDX = "EXCHGW_RESULT_ERR_CODE_IDX";
    //
    private long timeStart;
    private long timeExecute;
    private StringBuilder br = new StringBuilder();
    //
//    public static long IN_VIEWACNTINFO;
//    public static long IN_MODIACNTBAL_POSITIVE;
//    public static long IN_MODIACNTBAL;
//    public static long IN_MOD_VOICE_PRE;
//    public static long CHARGING_MODIPRICEPLAN;
//    public static long IN_ADDPRICE_FULL;
//    public static long IN_REMOVEPRICE;
//    public static long IN_MODIMBFREEBAL2;
//    public static long IN_MODIMBFREEBAL;
//    public static long IN_MODISMSBAL;
//    public static long PCRF_ADJUSTQUOTA;
//    public static long PCRF_GETALLQUOTA;
//    public static long PCRF_UPDATEQUOTA;
//    public static long CHARGING_QUERYACCTINFO;
//    public static long CHARGING_MODI_ALL_BALANCE;
//    public static long CHARGING_CREATE_OTCRD;
//    public static long PCRF_SUBSCRIBESERVICE;
//    public static long PCRF_UNSUBSCRIBESERVICE;
//    //
//    public static List ERR_PRE2POST = new ArrayList();
//    public static List ERR_NOT_ALLOW_REG = new ArrayList();
//    public static List ERR_PRE_PRICE_NOT_EXIST = new ArrayList();
//    public static List ERR_POS_PRICE_NOT_EXIST = new ArrayList();
//    public static List BALANCE_NOT_ENOUGH = new ArrayList();
//    public static List BALANCE_SUSPEND = new ArrayList();
//    public static List PRE_ALREADY_REG = new ArrayList();
//    public static List POS_ALREADY_REG = new ArrayList();
//    public static List ERR_PCRF = new ArrayList();
//    //tungnv: nang cap partycode
//    public static String PARTY_CODE_REG;
//    public static String PARTY_CODE_EXTEND;
//    public static String PARTY_CODE_ROLLBACK_REG;
//    public static String PARTY_CODE_ROLLBACK_EXTEND;
//    public static String PARTY_CODE_PROMOTION_MONEY;

    /**
     *
     * @param logger
     */
    public Exchange(Logger logger) throws Exception {
        this.logger = logger;
        this.detailLogger = Logger.getLogger("detail");
//        sdf = new java.text.SimpleDateFormat(DATE_FORMAT);
//        sdf2 = new java.text.SimpleDateFormat(DATE_FORMAT2);
//        c1 = Calendar.getInstance();
    }

    /**
     *
     * @param msisdn
     * @param money
     * @param balanceId
     * @return
     */
//    public String addBalanceBasicPreAll(long money, String balanceId, Date expireDate,
//            String productName) {
////        if (money == 0) {
////            return FreeTalk.ResultCode.SUCCESS;
////        }
//
//        br.setLength(0);
//        br.append(loggerLabel).
//                append("Cong tien tai khoan: MSISDN=").append(cdrRecord.getMsisdn()).
//                append(" - ADD_BALANCES=").append(money).
//                append(" - BALANCE_ID=").append(balanceId).
//                append(" - EXPIRE_DATE=").append(sdf2.format(expireDate));
//        logger.info(br);
//
//        ExchMsg response = null;
//        try {
//            ExchMsg request = new ExchMsg();
//            request.setSynchronous(true);
//
//            request.setCommand("IN_MOD_SMS_PRE");
//            request.set("MSISDN", cdrRecord.getMsisdn());
//            request.set("ADD_SMS", String.valueOf(money));//So tien cong
//            if (balanceId != null) {//Neu co tai khoan truyen vao. (De null, cong vao tai khoan goc)
//                request.set("ACCT_REST_ID", balanceId);//Ma tai khoan
//            }
//            request.set("EXPIRE_DATE", sdf2.format(expireDate));//Thoi gian het han
////            request.set("PARTY_CODE", PARTY_CODE_PROMOTION_MONEY + "@" + productName);
//            logger.info(request);
//
//            timeStart = System.currentTimeMillis();
//            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            logTime("Time to IN_MOD_SMS_PRE");
//
//            String errCode = response.getError();
//
//            if (!errCode.equals(Common.ResultCode.SUCCESS)) {
//                br.setLength(0);
//                br.append(loggerLabel).
//                        append("ERROR execute IN_MOD_SMS_PRE: MSISDN=").append(cdrRecord.getMsisdn()).
//                        append(" - DESCRIPTION=").append(response.getDescription());
//                logger.error(br);
//                return "IN_MOD_SMS_PRE-" + errCode + "-" + response.getDescription();
//            }
//
//            // tao cdr partycode
////            CdrPartyCode cdr = new CdrPartyCode();
////            cdr.setSender("VTFREE");
////            cdr.setMsisdn(cdrRecord.getMsisdn());
////            cdr.setActionId("3");
////            cdr.setReqDate(fullDf.format(new Date()));
////            cdr.setMoney(money);
////            cdr.setDescription("Cong tien khuyen mai");
////            cdr.setBackup1(PARTY_CODE_PROMOTION_MONEY + "@" + productName);
////            cdr.setStatus(0);
////            cdr.setFileTypeId(FreeTalk.CdrFileTypeId.PREPAID);
////            listCdrPartyCode.add(cdr);
//
//            try {
//                br.setLength(0);
//                br.append(WebserviceManager.appId).append("||").
//                        append("ProcessCdrTopup").append("||").
//                        append(fullDf.format(new Date(timeStart))).append("||").
//                        append(fullDf.format(new Date())).append("||").
//                        append(ExchangeClientChannel.getChannel().getUser()).append("||").
//                        append(Inet4Address.getLocalHost().getHostAddress()).append("||").
//                        append("ProcessCdrTopup").append("||").
//                        append("Cong tien KM").append("||").
//                        append(cdrRecord.getMsisdn()).append("||").
//                        append("MSISDN:").append(cdrRecord.getMsisdn()).append("|").
//                        append("AMOUNT:").append(money).append("|").
//                        append("EXPIRE:").append(sdf2.format(expireDate)).append("|").
//                        append("BALANCE_ID:").append(balanceId);
//                detailLogger.info(br);
//            } catch (Exception ex) {
//                logger.warn("Loi ghi log", ex);
//            }
//
//            return Common.ResultCode.SUCCESS;
//        } catch (Exception ex) {
//            if (response != null) {
//                br.setLength(0);
//                br.append(loggerLabel).
//                        append("ERROR execute IN_MOD_SMS_PRE: MSISDN=").append(cdrRecord.getMsisdn()).
//                        append("\n").append(response);
//                logger.error(br, ex);
//            } else {
//                br.setLength(0);
//                br.append(loggerLabel).
//                        append("ERROR execute IN_MOD_SMS_PRE: Response is null, MSISDN=").append(cdrRecord.getMsisdn());
//                logger.error(br, ex);
//            }
//        }
//        return Common.ResultCode.ERROR;
//    }
    public String addPromotion(String msisdn, String amount, String addDays, String accId)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        if (!msisdn.startsWith(WebserviceManager.countryCode)) {
            msisdn = WebserviceManager.countryCode + msisdn;
        }
        request.setCommand("IN_MOD_SMS_PRE");
        request.set("ADD_DAYS", addDays);
        request.set("ADD_SMS", amount);
        request.set("MSISDN", msisdn);
        request.set("ACCT_REST_ID", accId);
        this.logger.info("Add promotion subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
            this.logger.debug("Result add promotion subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute IN_MOD_SMS_PRE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                return response.getDescription();
            }
            return Common.ResultCode.SUCCESS;
        } catch (Exception ex) {
            this.logger.error("Exception when add promotion for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public String topup(String msisdn, String amount, String addDays, String accId)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        if (!msisdn.startsWith(WebserviceManager.countryCode)) {
            msisdn = WebserviceManager.countryCode + msisdn;
        }
        request.setCommand("CHARGING_MOD_MONEY_PRE");
        request.set("ADD_DAYS", addDays);
        request.set("ADD_BALANCES", amount);
        request.set("MSISDN", msisdn);
        request.set("ACCT_REST_ID", accId);
        this.logger.info("charging subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
            this.logger.debug("Result charging for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute CHARGING_MOD_MONEY_PRE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                return response.getDescription();
            }
            return Common.ResultCode.SUCCESS;
        } catch (Exception ex) {
            this.logger.error("Exception when charging for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public String reset(String msisdn, String amount, String addDays, String accId)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        if (!msisdn.startsWith(WebserviceManager.countryCode)) {
            msisdn = WebserviceManager.countryCode + msisdn;
        }
        request.setCommand("IN_MOD_BASIC2_PRE");
        request.set("ADD_DAYS", addDays);
        request.set("ADD_BALANCES", amount);
        request.set("MSISDN", msisdn);
        request.set("ACCT_REST_ID", accId);
        request.set("RESET_MONEY", "0");
        this.logger.info("charging subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
            this.logger.debug("Result charging for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute CHARGING_MOD_MONEY_PRE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                return response.getDescription();
            }
            return Common.ResultCode.SUCCESS;
        } catch (Exception ex) {
            this.logger.error("Exception when charging for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public String addPricePlan(String msisdn, String pricePlan, Date expireDate) {
        if (pricePlan == null) {
            return Common.ResultCode.SUCCESS;
        } else {
            br.setLength(0);
            br.append("Add ma gia phu: MSISDN=").append(msisdn).
                    append(" - PRICE_PLAN=").append(pricePlan).
                    append(" - EXPIRE_DATE=").append(sdf2.format(expireDate));
            logger.info(br);

            ExchMsg response = null;
            try {
                ExchMsg request = new ExchMsg();
                request.setSynchronous(true);
                if (!msisdn.startsWith(WebserviceManager.countryCode)) {
                    msisdn = WebserviceManager.countryCode + msisdn;
                }
                request.setCommand("IN_ADDPRICE_FULL");
                request.set("MSISDN", msisdn);
                request.set("PricePlanId", pricePlan);//So tien cong
                request.set("EXP_PRICEPLAN", sdf2.format(expireDate));//Thoi gian het han
                request.set("ACTION_ADD_PRICEPLAN", "1");//action add ma gia
//                setTimeSt(System.currentTimeMillis());
//                response = (ExchMsg) send(request, msisdn);
                response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
                logTime("Time to IN_ADDPRICE_FULL");

                String errCode = response.getError();
                if ("S-PRF-00109".equals(errCode)) {
                    return Common.ResultCode.EXIT_PRICEPLAN;
                }


                if (!errCode.equals(Common.ResultCode.SUCCESS)) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("ERROR execute IN_ADDPRICE_FULL: MSISDN=").append(msisdn).
                            append(" - DESCRIPTION=").append(response.getDescription());
                    logger.error(br);
                    return "IN_ADDPRICE_FULL-" + errCode + "-" + response.getDescription();
                }

                return Common.ResultCode.SUCCESS;
            } catch (Exception ex) {
                if (response != null) {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("ERROR execute IN_ADDPRICE_FULL: MSISDN=").append(msisdn).
                            append("\n").append(response);
                    logger.error(br, ex);
                } else {
                    br.setLength(0);
                    br.append(loggerLabel).
                            append("ERROR execute IN_ADDPRICE_FULL: Response is null, MSISDN=").append(msisdn);
                    logger.error(br, ex);
                }
            }
            return Common.ResultCode.ERROR;
        }
    }

//    public Response getBalance(String msisdn, Response resp)
//            throws Exception {
//        ExchMsg response = new ExchMsg();
//        ExchMsg request = new ExchMsg();
//        msisdn = "" + msisdn;
//        request.setCommand("CHARGING_QUERY_ACCT_BAL");
//        request.set("MSISDN", msisdn);
//        this.logger.info("View getEABalance subscriber <" + msisdn + "> \n" + request);
//        try {
//            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
////            response = (ExchMsg) send(request, msisdn);
//            logTime("Time to CHARGING_QUERY_ACCT_BAL");
//            this.logger.debug("Result balace for subscriber <" + msisdn + "> \n" + response);
//            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
//                br.setLength(0);
//                br.append(loggerLabel).
//                        append("ERROR execute CHARGING_QUERY_ACCT_BAL: MSISDN=").append(msisdn).
//                        append(" - DESCRIPTION=").append(response.getDescription());
//                logger.error(br);
//                resp.setErrorCode("99");
//                return resp;
//            }
//            String accId = response.get("ACCT_REST_ID") != null ? (String) response.get("ACCT_REST_ID") : null;
//            String blStr = response.get("BALANCE") != null ? (String) response.get("BALANCE") : null;
//            String exStr = response.get("EXPIRE_DATE") != null ? (String) response.get("EXPIRE_DATE") : null;
//            String names = response.get("ACCT_REST_NAME") != null ? (String) response.get("ACCT_REST_NAME") : null;
//            resp.setLstAccId(accId);
//            resp.setLstAccName(names);
//            resp.setLstBalance(blStr);
//            resp.setLstExpire(exStr);
//        } catch (Exception ex) {
//            this.logger.error("Exception when view balance for sub <" + msisdn + "> detail following \n", ex);
//        }
//        return resp;
//    }
    public String chargingEMoney(String msisdn, String amount)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("CHARGING_MOD_MONEY_PRE");
        request.set("ADD_DAYS", "0");
        request.set("ADD_BALANCES", amount);
        request.set("MSISDN", msisdn);
        request.set("ACCT_REST_ID", "88");
        this.logger.info("chargingEMoney subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            response = (ExchMsg) send(request, msisdn);
            logTime("Time to CHARGING_MOD_MONEY_PRE");
            this.logger.debug("Result chargingEMoney for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute CHARGING_MOD_MONEY_PRE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                result = response.getDescription();
            }
        } catch (Exception ex) {
            this.logger.error("Exception when chargingEMoney for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public String getBalance(String msisdn, String balanceId)
            throws Exception {
        String balance = "0";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("IN_VIEWACNTINFO");
        request.set("MSISDN", msisdn);
        this.logger.info("View getEABalance subscriber <" + msisdn + "> \n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            response = (ExchMsg) send(request, msisdn);
            logTime("Time to IN_VIEWACNTINFO");
            this.logger.debug("Result balace for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute IN_VIEWACNTINFO: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                return balance;
            }
            String accId = response.get("BALANCE_TYPE_IDS") != null ? (String) response.get("BALANCE_TYPE_IDS") : null;
            String blStr = response.get("BALANCES") != null ? (String) response.get("BALANCES") : null;
            if (accId != null && blStr != null) {
                String[] accIdArr = accId.split("&");
                String[] blStrArr = blStr.split("&");
                if (accIdArr.length != blStrArr.length) {
                    return balance;
                } else {
                    HashMap bleMap = new HashMap();
                    for (int i = 0; i < accIdArr.length; i++) {
                        bleMap.put(accIdArr[i], blStrArr[i]);
                    }
                    try {
                        balance = bleMap.get(balanceId).toString();
                        balance = balance.substring(0, balance.length() - 4);
                    } catch (Exception e) {
                        balance = "0";
                    }
                    return balance;
                }
            } else {
                return balance;
            }
        } catch (Exception ex) {
            this.logger.error("Exception when view balance for sub <" + msisdn + "> detail following \n", ex);
        }
        return balance;
    }

    public String addMoney(String msisdn, String amount, String balanceId)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("IN_MOD_BASIC2_PRE");
        request.set("ADD_DAYS", "0");
        request.set("ADD_BALANCES", amount);
        request.set("MSISDN", msisdn);
        request.set("ACCT_REST_ID", balanceId);
        this.logger.info("chargingEMoney subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            response = (ExchMsg) send(request, msisdn);
            logTime("Time to IN_MOD_BASIC2_PRE");
            this.logger.debug("Result chargingEMoney for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute IN_MOD_BASIC2_PRE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                result = response.getDescription();
            } else {
                result = Common.ResultCode.SUCCESS;
            }
        } catch (Exception ex) {
            this.logger.error("Exception when chargingEMoney for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public String chargingMoney(String msisdn, String amount)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("IN_MODIACNTBAL_POSITIVE");

        request.set("MSISDN", msisdn);
        request.set("CHARGE_VALUE", amount);
        this.logger.info("chargingEMoney subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            response = (ExchMsg) send(request, msisdn);
            logTime("Time to IN_MODIACNTBAL_POSITIVE");
            this.logger.debug("Result chargingEMoney for subscriber <" + msisdn + "> \n" + response);
            if (Common.ResultCode.NOT_ENOUGHT.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                result = Common.ResultCode.NOT_ENOUGHT;
            }
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute IN_MODIACNTBAL_POSITIVE: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                result = response.getDescription();
            } else {
                result = Common.ResultCode.SUCCESS;
            }
        } catch (Exception ex) {
            this.logger.error("Exception when chargingEMoney for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

//    public String getBalance(String msisdn)
//            throws Exception {
//        String balance = "0";
//        ExchMsg response = new ExchMsg();
//        ExchMsg request = new ExchMsg();
//        msisdn = "" + msisdn;
//        request.setCommand("CHARGING_QUERY_ACCT_BAL");
//        request.set("MSISDN", msisdn);
//        this.logger.info("View getEABalance subscriber <" + msisdn + "> \n" + request);
//        try {
//            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
////            response = (ExchMsg) send(request, msisdn);
//            logTime("Time to CHARGING_QUERY_ACCT_BAL");
//            this.logger.debug("Result balace for subscriber <" + msisdn + "> \n" + response);
//            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
//                br.setLength(0);
//                br.append(loggerLabel).
//                        append("ERROR execute CHARGING_QUERY_ACCT_BAL: MSISDN=").append(msisdn).
//                        append(" - DESCRIPTION=").append(response.getDescription());
//                logger.error(br);
//                return balance;
//            }
//            String accId = response.get("ACCT_REST_ID") != null ? (String) response.get("ACCT_REST_ID") : null;
//            String blStr = response.get("BALANCE") != null ? (String) response.get("BALANCE") : null;
//            if (accId != null && blStr != null) {
//                String[] accIdArr = accId.split("&");
//                String[] blStrArr = blStr.split("&");
//                if (accIdArr.length != blStrArr.length) {
//                    return balance;
//                } else {
//                    HashMap bleMap = new HashMap();
//                    for (int i = 0; i < accIdArr.length; i++) {
//                        bleMap.put(accIdArr[i], blStrArr[i]);
//                    }
//                    try {
//                        balance = bleMap.get("1").toString();
//                        balance = balance.substring(0, balance.length() - 4);
//                    } catch (Exception e) {
//                        balance = "0";
//                    }
//                    return balance;
//                }
//            } else {
//                return balance;
//            }
//        } catch (Exception ex) {
//            this.logger.error("Exception when view balance for sub <" + msisdn + "> detail following \n", ex);
//        }
//        return balance;
//    }
    public String topupByEMoney(String msisdn, String amount)
            throws Exception {
        String result = "";
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("CHARGING_IN_PAYMENT");
        request.set("VALUE", amount);
        request.set("MSISDN", msisdn);
        this.logger.info("topupByEMoney subscriber <" + msisdn + ">amount<" + amount + ">\n" + request);
        try {
//            response = (ExchMsg) send(request, msisdn);
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
            logTime("Time to CHARGING_IN_PAYMENT");
            this.logger.debug("Result topupByEMoney subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute CHARGING_IN_PAYMENT: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                result = response.getDescription();
            }
        } catch (Exception ex) {
            this.logger.error("Exception when topupByEMoney for sub <" + msisdn + "> detail following \n", ex);
            result = ex.toString();
        }
        return result;
    }

    public void logTime(String strLog) {
        timeExecute = System.currentTimeMillis() - timeStart;
        if (timeExecute >= WebserviceManager.minTimeOcs && WebserviceManager.loggerOcsMap != null) {
            br.setLength(0);
            br.append(loggerLabel).
                    append(WebserviceManager.getTimeLevelOcs(timeExecute)).append(": ").
                    append(strLog).
                    append(": ").
                    append(timeExecute).
                    append(" ms");

            logger.warn(br);
        } else {
            br.setLength(0);
            br.append(loggerLabel).
                    append(strLog).
                    append(": ").
                    append(timeExecute).
                    append(" ms");

            logger.info(br);
        }
    }

    public String[] getAllBalance(String msisdn)
            throws Exception {
        String[] balance = new String[4];
        ExchMsg response = new ExchMsg();
        ExchMsg request = new ExchMsg();
        msisdn = "" + msisdn;
        request.setCommand("IN_VIEWACNTINFO");
        request.set("MSISDN", msisdn);
        this.logger.info("View getEABalance subscriber <" + msisdn + "> \n" + request);
        try {
            response = (ExchMsg) ExchangeClientChannel.getChannel().sendAll(request, 30000L, true);
//            response = (ExchMsg) send(request, msisdn);
            logTime("Time to IN_VIEWACNTINFO");
            this.logger.debug("Result balace for subscriber <" + msisdn + "> \n" + response);
            if (!Common.ResultCode.SUCCESS.equals(response.getError())) {
                br.setLength(0);
                br.append(loggerLabel).
                        append("ERROR execute IN_VIEWACNTINFO: MSISDN=").append(msisdn).
                        append(" - DESCRIPTION=").append(response.getDescription());
                logger.error(br);
                return null;
            }
            //String accId = response.get("BALANCE_TYPE_IDS") != null ? (String) response.get("BALANCE_TYPE_IDS") : null;
            String accName = response.get("BALANCE_NAMES") != null ? (String) response.get("BALANCE_NAMES") : null;
            String blStr = response.get("BALANCES") != null ? (String) response.get("BALANCES") : null;
            String exStr = response.get("EXPIRE_DATES") != null ? (String) response.get("EXPIRE_DATES") : null;
            String blType = response.get("BALANCE_TYPE_IDS") != null ? (String) response.get("BALANCE_TYPE_IDS") : null;
            balance[0] = accName;
            balance[1] = blStr;
            balance[2] = exStr;
            balance[3] = blType;

        } catch (Exception ex) {
            this.logger.error("Exception when view balance for sub <" + msisdn + "> detail following \n", ex);
            return null;
        }
        return balance;
    }
}
