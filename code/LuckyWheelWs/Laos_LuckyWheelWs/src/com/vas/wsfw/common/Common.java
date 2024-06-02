/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.common;

import com.vas.wsfw.obj.MpsConfigObj;
import com.vas.wsfw.obj.PrizeObj;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author: TungTT
 * @version 2.0
 * @since: April 18, 2011
 */
public class Common {

    public static HashMap listConfig;
    public static boolean iLoadConfig = true;
    public static String CHANNEL;
    public static String COUNTRY_CODE;
    public static AtomicLong counterTransId = new AtomicLong(1000000);
    public static int ADD_SPIN_FEE;
//    public static int ADD_SPIN_TIMES;
//    public static int MAX_WIN;
//    public static int REGISTER_FEE;
//    public static double SPIN_FEE;
//    public static int INVITE_POINT;
//    public static int MONEY_LIMIT;
//    public static int PERCENT_LIMIT;
//    public static List<ProductInfo> listProduct;
    public static LinkedHashMap<Integer, PrizeObj> mapPrize;
    public static List<PrizeObj> listPrizePercent;
    public static List<PrizeObj> padPrize = new ArrayList();
//    public static Date END_DATE_FREE;
//    public static int MAX_PRESENT_PERDAY;
    public static HashMap<String, MpsConfigObj> mapMpsConfig;
//    public static String COUNTRY_CODE;
    public static int START_DAY = 7;
    public static int END_DAY = 17;
    public static int OTP_TIMEOUT;

    public static void loadConfig() throws ParseException {
        // load cau hinh
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        iLoadConfig = false;
        Common.CHANNEL = Common.listConfig.get("CHANNEL").toString();
        Common.COUNTRY_CODE = Common.listConfig.get("COUNTRY_CODE").toString();
        Common.OTP_TIMEOUT = Integer.parseInt(Common.listConfig.get("OTP_TIMEOUT").toString());
        Common.ADD_SPIN_FEE = Integer.parseInt(Common.listConfig.get("ADD_SPIN_FEE").toString());
//        Common.INVITE_POINT = Integer.parseInt(Common.listConfig.get("INVITE_POINT").toString());
//        Common.ADD_SPIN_TIMES = Integer.parseInt(Common.listConfig.get("ADD_SPIN_TIMES").toString());
//        try {
//            MONEY_LIMIT = Integer.parseInt(Common.listConfig.get("MONEY_LIMIT").toString());
//        } catch (Exception ex) {
//            MONEY_LIMIT = 3000;
//        }
        try {
            START_DAY = Integer.parseInt(Common.listConfig.get("START_DAY").toString());
        } catch (Exception ex) {
            START_DAY = 7;
        }

        try {
            END_DAY = Integer.parseInt(Common.listConfig.get("END_DAY").toString());
        } catch (Exception ex) {
            END_DAY = 17;
        }

        listPrizePercent = new ArrayList();
        for (PrizeObj prize : mapPrize.values()) {
            for (int i = 0; i < prize.getNumberPrize(); i++) {
                listPrizePercent.add(prize);
            }
        }
//        try {
//            PERCENT_LIMIT = Integer.parseInt(Common.listConfig.get("PERCENT_LIMIT").toString());
//        } catch (Exception ex) {
//            PERCENT_LIMIT = 10;
//        }
//        try {
//            MAX_PRESENT_PERDAY = Integer.parseInt(Common.listConfig.get("MAX_PRESENT_PERDAY").toString());
//        } catch (Exception ex) {
//            MAX_PRESENT_PERDAY = 0;// not limit
//        }
//        END_DATE_FREE = sdf.parse(Common.listConfig.get("END_DATE_FREE").toString());

//        WSProcessor.ACCOUNT_BLOCKED_RES = Arrays.asList(Common.listConfig.get("ACCOUNT_BLOCKED_RES").toString().split(","));
//        WSProcessor.LIST_SUCCESS_RES = Arrays.asList(Common.listConfig.get("LIST_SUCCESS_RES").toString().split(","));
//        WSProcessor.LIST_CANCEL_SUCCESS_RES = Arrays.asList(Common.listConfig.get("LIST_CANCEL_SUCCESS_RES").toString().split(","));
//        WSProcessor.NOT_ENOUGH_MONEY_RES = Arrays.asList(Common.listConfig.get("NOT_ENOUGH_MONEY_RES").toString().split(","));
    }

    public class Constant {

        public static final long STATUS_ACTIVE = 1L; //trang thai co hieu luc
        public static final long STATUS_INACTIVE = 0L; //trang thai khong hieu luc
        public static final double AMOUNT_RATE = 10000;
        public static final double AMOUNT_RATE_IM = 11000;
        public static final String MAX_ANYPAY_AMOUNT = "MAX_ANYPAY_AMOUNT";
    }

    public class Message {

        public static final String SYSTEM_FAIL = "SYSTEM_FAIL";
        public static final String SYNTAX_ERROR = "SYNTAX_ERROR";
        public static final String LOGIN_FAIL = "LOGIN_FAIL";
        public static final String GET_ACTIVE_CODE = "GET_ACTIVE_CODE";
        public static final String GET_REGISTER_CODE = "GET_REGISTER_CODE";
        public static final String ACTIVATE_SUCCESS = "ACTIVATE_SUCCESS";
        public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
        public static final String DISABLE_EXTEND_SUCCESS = "DISABLE_EXTEND_SUCCESS";
        public static final String ENABLE_EXTEND_SUCCESS = "ENABLE_EXTEND_SUCCESS";
        public static final String RESET_PASSWORD_SUCCESS = "RESET_PASSWORD_SUCCESS";
        public static final String REGISTER_SUCCESS_USSD = "REGISTER_SUCCESS_USSD";
        public static final String CREATE_ACCOUNT_SUCCESS = "CREATE_ACCOUNT_SUCCESS";
        public static final String RENEW_SUCCESS = "RENEW_SUCCESS";
        public static final String BUY_SPIN_UNSUB_SUCCESS = "BUY_SPIN_UNSUB_SUCCESS";
        public static final String BUY_SPIN_SUB_SUCCESS = "BUY_SPIN_SUB_SUCCESS";
        public static final String DESTROY_ACCOUNT_SUCCESS = "DESTROY_ACCOUNT_SUCCESS";
        public static final String NOT_ENOUGH_POINT = "NOT_ENOUGH_POINT";
        public static final String CHANGE_POINT_SUCCESS = "CHANGE_POINT_SUCCESS";
        public static final String OVER_MAX_POINT_CHANGE = "OVER_MAX_POINT_CHANGE";
        public static final String AREADY_MAX_POINT_CHANGE = "AREADY_MAX_POINT_CHANGE";

        public static final String INVITE_FRIEND_REGISTERED = "INVITE_FRIEND_REGISTERED";
        public static final String INVITED_FRIEND = "INVITED_FRIEND";
        //
        public static final String NOT_ENOUGH_BALANCE = "NOT_ENOUGH_BALANCE";
        public static final String ALREADY_REGISTERED = "ALREADY_REGISTERED";
        public static final String ACCOUNT_NOT_EXISTED = "ACCOUNT_NOT_EXISTED";
        public static final String ADD_FREE_GIFT_SUCCESS = "ADD_FREE_GIFT_SUCCESS";
        public static final String ADD_SPIN_RECHARGE_SUCCESS = "ADD_SPIN_RECHARGE_SUCCESS";
        public static final String CHECK_SPIN_RECHARGE_SUCCESS = "CHECK_SPIN_RECHARGE_SUCCESS";
        public static final String RETURN_WIN_ADD_POINT = "RETURN_WIN_ADD_POINT";
        public static final String RETURN_WIN_MULTIPLE_POINT = "RETURN_WIN_MULTIPLE_POINT";
        public static final String RETURN_WIN_INVITE_FRIEND = "RETURN_WIN_INVITE_FRIEND";
        public static final String RETURN_INVITE_FRIEND = "RETURN_INVITE_FRIEND";
        public static final String RETURN_WAIT_INVITE_FRIEND = "RETURN_WAIT_INVITE_FRIEND";
        public static final String RECEIVED_INVITATION = "RECEIVED_INVITATION";
//        public static final String RETURN_WIN_LOSE_TURN = "RETURN_WIN_LOSE_TURN";
        public static final String RETURN_WIN_LOSE_SPIN = "RETURN_WIN_LOSE_SPIN";
        public static final String RETURN_WIN_ADD_SPIN = "RETURN_WIN_ADD_SPIN";
        public static final String RETURN_WIN_RESET_SPIN = "RETURN_WIN_RESET_SPIN";
        public static final String RETURN_WIN_MULTIPLE_SPIN = "RETURN_WIN_MULTIPLE_SPIN";
        public static final String RETURN_WIN_ADD_TIMES_UNSUB = "RETURN_WIN_ADD_TIMES_UNSUB";
        public static final String RETURN_WIN_ADD_MONEY_UNSUB = "RETURN_WIN_ADD_MONEY_UNSUB";
        public static final String RETURN_WIN_LOSE_TURN_UNSUB = "RETURN_WIN_LOSE_TURN_UNSUB";

        // for MPS
        public static final String NOT_REGISTERED = "NOT_REGISTERED";
        public static final String REGISTER_WAIT_CONFIRM = "REGISTER_WAIT_CONFIRM";
        public static final String CHARGE_WAIT_CONFIRM = "CHARGE_WAIT_CONFIRM";
        public static final String CHARGE_FEE_SUCCESS = "CHARGE_FEE_SUCCESS";
        
        // OTP
        public static final String GET_OTP_SUCCESS = "GET_OTP_SUCCESS";
        public static final String TIMEOUT_CONFIRM_OTP = "TIMEOUT_CONFIRM_OTP";
        public static final String WRONG_OTP = "WRONG_OTP";
    }

    /**
     * Dinh nghia ma check dieu kien ban dau
     */
    public class ValidateCode {

        public static final int ACTIVE_CODE = 1;
        public static final int REGISTER_CODE = 2;
    }

    /**
     * Dinh nghia ma loi MO_HIS
     */
    public class ErrorCode {

        public static final String SUCCESS = "0";
        public static final String NOT_REGISTERED = "100";
        public static final String QUERY_ERROR = "200";
        public static final String NOMORE_SPIN = "300";
        public static final String NOT_IN_TIME = "400";
        public static final String AUTHENTICATE_FAIL = "500";
        public static final String INVALID_PINCODE = "600";
        public static final String ACCOUNT_EXISTED = "700";
        public static final String MSISDN_EXISTED = "800";
        public static final String ACCOUNT_ACTIVATED = "900";
        public static final String ACCOUNT_NOT_EXISTED = "1000";
        public static final String ACCOUNT_IS_LOCKED = "1100";
        public static final String ACCOUNT_NOT_ACTIVATED = "1200";
        public static final String ACCOUNT_NOT_REGISTERED = "1300";
        public static final String WRONG_REGISTER_CODE = "1400";
        public static final String NOT_ENOUGH_MONEY = "1500";
        public static final String MSISDN_BLOCKED = "1600";
        public static final String DISABLED_AUTO_EXTEND = "1700";
        public static final String ENABLED_AUTO_EXTEND = "1800";
        public static final String CHARGE_ERROR = "1900";
        public static final String ALREADY_REGISTER = "2000";
        public static final String NOT_ENOUGH_POINT = "2100";
        public static final String ALREADY_INVITED = "2200";
        public static final String OVER_MAX_POINT_CHANGE = "2300";
        public static final String MISSION_NOT_FISNISH = "2400";
    }

    public class ResultCode {

        public static final String OK = "OK";
        public static final String NOK = "NOK";
        public static final String LOGIN = "LOGIN";
        public static final String SIGNUP = "SIGNUP";
        public static final String INVALID_COUNTERID = "INVALID COUNTERID";
        public static final String MANY_FAIL_TOPUP = "TOO MANY FAILED ATTEMPTS";
        public static final String SUCCESS = "0";
        public static final String NOT_ENOUGHT = "S-ACT-00049";
        public static final String ERROR = "9999";
        public static final String EXIT_PRICEPLAN = "8";
        public static final String NOT_ENOUGH = "9";
    }

    public class ActionType {

        public static final int CHANGE_PASS = 5;
        public static final int HELP = 10;
        public static final int CREATE_ACCOUNT = 1;
        public static final int REGISTER_SUB = 2;
        public static final int BUY_TURN = 3;
    }

    public class RankType {

        public static final int BOUGHT = 1;
        public static final int SPIN = 0;
    }

    public class ExtendStatus {

        public static final int SUCCESS = 0;
        public static final int NOT_ENOUGH_MONEY = 1;
        public static final int ERROR = 2;
    }

    public class ServiceCode {

//        public static final String Million = "MLNRE";
//        public static final String GamePortal = "GAMEP";
//        public static final String LuckySpin = "LUCSPIN";
        public static final String LuckyWheel = "ILUCKY";
    }

    public class ChargeType {

        public static final String CHARGE_REGISTER = "CHARGE_REGISTER";
        public static final String CHARGE_INVITE = "CHARGE_INVITE";
        public static final String CHARGE_PLAY = "CHARGE_PLAY";
        public static final String DAILY_FEE = "DAILY_FEE";
        public static final String SPIN_FEE = "SPIN_FEE";
    }

    public class SubType {

        public static final int SUB = 1;
        public static final int UNSUB = 0;
    }

    public class PeriodPrize {

        public static final int DAILY = 0;
        public static final int WEEKLY = 1;
        public static final int MONTHLY = 2;
    }

    public class PrizeType {

        public static final int INVITE = 1;
        public static final int ADD_POINT = 2;
        //public static final int ADD_MONEY = 3;
        public static final int ADD_SPIN = 4;
    }

    public class ActionCode {

        public static final String MISSION = "MISSION";
        public static final String NOHU = "NOHU";
        public static final String ADDMB = "ADDMB";
        public static final String ADDMONEY = "ADDMONEY";
        public static final String SPIN = "SPIN";
        public static final String INVITE = "INVITE";
    }
}
