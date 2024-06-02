/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

import java.util.HashMap;
import java.util.Map;

/**
 * HashMap descriptions code return for CP
 *
 * @version 1.0
 * @author langiac
 * @since 10 10 2015
 */
public class StaticCode {

    private static final String code0 = "Transaction success";
    private static final String code1 = "msisdn not found";
    private static final String code4 = "IP Cllient was wrong";
    private static final String code11 = "missed parameter";
    private static final String code13 = "missed parameter";
    private static final String code14 = "cp request id not found";
    private static final String code15 = "value is not found";
    private static final String code16 = "aes key is not found";
    private static final String code17 = "name item is not found";
    private static final String code18 = "category item is not found";
    private static final String code22 = "CP code is not valid or not active";
    private static final String code23 = "Payment is not valid";
    private static final String code24 = "transaction not confirm before";
    private static final String code25 = "CP Request Id is not valid";
    private static final String code503 = "MPS was error";
    private static final String code101 = "transaction was error";
    private static final String code102 = "transaction was error when register";
    private static final String code103 = "got error when pay by mobile account";
    private static final String code104 = "Error when cancelling service";
    private static final String code201 = "Signal is not valid";
    private static final String code202 = "transaction was error or password not valid";
    private static final String code207 = "Conflict service when register";
    private static final String code401 = "not enough balance";
    private static final String code402 = "Thuê bao chưa đăng ký thanh toán";
    private static final String code403 = "msisdn not existed";
    private static final String code404 = "msisdn is not valid";
    private static final String code405 = "msisdn was changed owner";
    private static final String code406 = "not found mobile";
    private static final String code407 = "missing parameters";
    private static final String code408 = "msisdn is using service";
    private static final String code409 = "msisdn was two ways blocked";
    private static final String code410 = "msisdn is not valid";
    private static final String code411 = "msisdn canceled service";
    private static final String code412 = "msisdn not using service";
    private static final String code413 = "parameter is not valid";
    private static final String code414 = "msisdn in period recharge.(recharge time < next charge time)";
    private static final String code415 = "OTP code is not valid";
    private static final String code416 = "OTP not exist or timeout";
    private static final String code417 = "Error USSD time out";
    private static final String code440 = "System error";
    private static final String code501 = "msisdn not register";
    private static final String code203 = "MPS account not exist (msisdn/password not valid)";
    private static final String code204 = "MPS acount is exist but msisdn not register CP service";
    private static final String code205 = "MPS acount is exist and registed CP service";
    private static final String code100 = "Transaction was proccessed ";
    private static final Map<String, String> dictcode = new HashMap();

    static {
        dictcode.put("0", code0);
        dictcode.put("100", code100);
        dictcode.put("1", code1);
        dictcode.put("4", code4);
        dictcode.put("11", code11);
        dictcode.put("13", code13);
        dictcode.put("14", code14);
        dictcode.put("15", code15);
        dictcode.put("16", code16);
        dictcode.put("17", code17);
        dictcode.put("18", code18);
        dictcode.put("22", code22);
        dictcode.put("23", code23);
        dictcode.put("24", code24);
        dictcode.put("25", code25);
        dictcode.put("101", code101);
        dictcode.put("102", code102);
        dictcode.put("103", code103);
        dictcode.put("104", code104);
        dictcode.put("201", code201);
        dictcode.put("202", code202);
        dictcode.put("207", code207);
        dictcode.put("401", code401);
        dictcode.put("402", code402);
        dictcode.put("403", code403);
        dictcode.put("405", code405);
        dictcode.put("406", code406);
        dictcode.put("407", code407);
        dictcode.put("408", code408);
        dictcode.put("404", code404);
        dictcode.put("440", code440);
        dictcode.put("410", code410);
        dictcode.put("411", code411);
        dictcode.put("412", code412);
        dictcode.put("413", code413);
        dictcode.put("414", code414);
        dictcode.put("415", code415);
        dictcode.put("416", code416);
        dictcode.put("417", code417);
        dictcode.put("503", code503);
        dictcode.put("204", code204);
        dictcode.put("205", code205);
        dictcode.put("501", code501);
        dictcode.put("203", code203);
        dictcode.put("409", code409);
    }

    public static String getdiscription(String code) {
        return dictcode.get(code);
    }
}
