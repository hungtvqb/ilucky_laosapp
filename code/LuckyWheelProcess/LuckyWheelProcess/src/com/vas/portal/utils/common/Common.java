/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.portal.utils.common;

import com.vas.portal.utils.object.Teams;
import com.vas.portal.utils.object.MpsConfigObj;
import com.vas.portal.utils.object.PrizeObj;
import com.vas.portal.utils.service.WSProcessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author kdvt_minhnh@viettel.com.vn
 * @since May 16, 2012
 * @version 1.0
 */
public class Common {

    //public static String SCHEMA_PRE;
    public static String CHANNEL;
    public static String COUNTRY_CODE;
    public static List<String[]> SCAN_MT_EXTEND_TIME;
    public static List<String[]> SCAN_RESULT_TIME;
    public static boolean iLoadConfig = true;
    public static int FREE_SPIN;
    public static int NUM_QUESTIONS;
    public static int EXPIRE_HOUR = 20;
//    public static int FEE_VOTING;
    public static int FEE;
    public static int FEE_BUYQUESTION;
//    public static int PRIZE_CORRECT;
    public static int PRIZE_WEEKLY;
//    public static List<Integer> HOUR_PAID_EXTEND;
    public static List<String> LIST_RECEIVE_REPORT;
    public static String SQL_REPORT;
    public static AtomicLong counterTransId = new AtomicLong(1000000);
    public static HashMap<Long, PrizeObj> mapPrize;
    public static HashMap<String, MpsConfigObj> mapMpsConfig;
    public static List<String> blacklist;

    // for recharge
    public static String FTP_RECHARGE_ADDRESS;
    public static String FTP_RECHARGE_ACCOUNT;
    public static String FTP_RECHARGE_PASS;
    public static String FTP_RECHARGE_PATH;
    public static String FTP_RECHARGE_DOWNLOADED_PATH;

    public static void loadConfig() {
        // load cau hinh 
        //Common.SCHEMA_PRE = MessageResponse.listMsg.get("SCHEMA_PRE") + ".";
        Common.CHANNEL = MessageResponse.listMsg.get("CHANNEL").toString();
        Common.COUNTRY_CODE = MessageResponse.listMsg.get("COUNTRY_CODE").toString();
        Common.FREE_SPIN = Integer.parseInt(MessageResponse.listMsg.get("FREE_SPIN").toString());
        iLoadConfig = false;
    }

    public class ErrCode {

        public static final String SUCCESS = "0";
        public static final String SYSTEM_FAIL = "-9";
        //Receiver 2.1.1
        public static final String WRONG_SYNTAX = "-1";
        public static final String DUPLICATE_REQUEST = "-2";
        public static final String SERVICE_INACTIVE = "-3";
        public static final String OVERLOAD = "-4";
        //
        public static final String DB_CM_ERROR = "1";
        public static final String DB_ERROR = "2";
        public static final String DB_NOT_PRE = "3";
        public static final String EXIST_CONFIRM = "4";
        public static final String NOT_ENOUGH_MONEY = "5";
        public static final String ALREADY_USE = "6";
        public static final String STA_DATETIME_NULL = "7";
        public static final String NO_SUPPORT = "8";
        public static final String PRO_ERROR = "9";
        public static final String CONFIRM_NOT_EXIST = "10";
        public static final String CONFIRM_NO = "11";
        public static final String NOT_ACTIVE_2WAYS = "12";
        public static final String NOT_IN_PROMOTION = "13";
        public static final String SUB_CHANGE_INFO = "14";
        public static final String NOT_ALLOW_CHECK = "15";
        public static final String NOT_FOUND_CONTRACT = "16";
        public static final String ANYPAY_NOT_FOUND = "17";
        public static final String OVER_REG_IN_DAY = "18";
        public static final String ALREADY_REGISTER = "19";
        public static final String NOT_REGISTER = "20";
        public static final String NOT_IN_TIME = "2000";
    }

    public class Message {

        public static final String MSG_DAILY_CODE = "MSG_DAILY_CODE";
        public static final String MSG_NOT_LUCKY = "MSG_NOT_LUCKY";
        public static final String MSG_LUCKY_PRIZE = "MSG_LUCKY_PRIZE";
        public static final String ALREADY_REGISTERED = "ALREADY_REGISTERED";
        public static final String NOT_REGISTERED = "NOT_REGISTERED";
        public static final String NOT_ENOUGH_BALANCE = "NOT_ENOUGH_BALANCE";
        public static final String SYSTEM_FAIL = "SYSTEM_FAIL";
        public static final String REGISTER_SUCCESS = "REGISTER_SUCCESS";
        public static final String REGISTER_SUCCESS_NOCODE = "REGISTER_SUCCESS_NOCODE";
        public static final String DESTROY_ACCOUNT_SUCCESS = "DESTROY_ACCOUNT_SUCCESS";
        public static final String WRONG_SYNTAX = "WRONG_SYNTAX";
        public static final String BUY_QUESTION_SUCCESS = "BUY_QUESTION_SUCCESS";
        public static final String SEND_QUESTION = "SEND_QUESTION";
        public static final String SEND_NEXT_QUESTION = "SEND_NEXT_QUESTION";
        public static final String SEND_QUESTION_BUY = "SEND_QUESTION_BUY";
        public static final String CONFIRM_BUY = "CONFIRM_BUY";
        public static final String NOT_IN_TIME = "NOT_IN_TIME";
        public static final String TIMEOUT = "TIMEOUT";
        public static final String CORRECT_ANSWER = "CORRECT_ANSWER";
        public static final String WRONG_ANSWER = "WRONG_ANSWER";
        public static final String INTRODUCE_MESSAGE = "INTRODUCE_MESSAGE";
        public static final String MSG_WEEKLY_PRIZE = "MSG_WEEKLY_PRIZE";
        public static final String MSG_GOT_LUCKY_CODE = "MSG_GOT_LUCKY_CODE";
        public static final String MSG_PRIZE_WINNER = "MSG_PRIZE_WINNER";
        public static final String MSG_CODE_NO_PRIZE = "MSG_CODE_NO_PRIZE";
        public static final String RECHARGE_GIFT = "RECHARGE_GIFT";
        public static final String RECHARGE_INTRODUCE = "RECHARGE_INTRODUCE";
        public static final String RECHARGE_ADD_CODE = "RECHARGE_ADD_CODE";
        public static final String WRONG_PARAM_1 = "WRONG_PARAM_1";
        public static final String WRONG_PARAM_2 = "WRONG_PARAM_2";
        public static final String DONATE_WRONG_PARAM_1 = "DONATE_WRONG_PARAM_1";
        public static final String DONATE_WRONG_PARAM_2 = "DONATE_WRONG_PARAM_2";
        public static final String VOTE_SUCCESS = "VOTE_SUCCESS";
        public static final String DONATE_SUCCESS = "DONATE_SUCCESS";
        public static final String PREDICT_SUCCESS = "PREDICT_SUCCESS";
    }

    public class ResultCode {

        public static final String SUCCESS = "0";
        public static final String SUCCESS_HW = "405000000";
        public static final String EXCEPTION = "0000";
        public static final String ERROR = "ERROR";
        public static final String NOT_ENOUGH_MONEY = "1500";
        public static final String MSISDN_BLOCKED = "1600";
    }

    public class Constant {

        public static final String ACTIVE_2_WAY_PRE = "00";
        public static final String ACTIVE_2_WAY_POST = "000";
        public static final int PRE_PAID = 1;
        public static final int POST_PAID = 0;
    }

    public class ExtendStatus {

        public static final int SUCCESS = 0;
        public static final int NOT_ENOUGH_MONEY = 1;
        public static final int ERROR = 2;
    }

    public class ServiceCode {

        public static final String Million = "MLNRE";
        public static final String GamePortal = "LUCKYCARD";
        public static final String LuckySpin = "LUCSPIN";
    }

    public class ChargeType {

        public static final String REGISTER = "REGISTER";
        public static final String CHARGED400 = "CHARGED400";
        public static final String CHARGED = "CHARGED";
    }

    public class MessageType {

        public static final int TEXT_SMS = 0;
        public static final int FLASH_SMS = 1;
        public static final int PUSH_USSD = 200;
        public static final int FLASH_USSD = 201;
    }
}
