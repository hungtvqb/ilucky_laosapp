/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.portal.utils.common;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author TungTT
 * @since Oct 25, 2010
 */
public class MessageResponse {

    public static HashMap listMsg;
    public static boolean init = false;
    private static final Object lockMessage = new Object();
    public static List<RegularExpression> listRegexSyntaxTopup;
    public static List<RegularExpression> listRegexSyntaxCheck;

    public static void setMessage(HashMap listMsg) {
        synchronized (lockMessage) {
            MessageResponse.listMsg = listMsg;
            String listSyntax = (String) listMsg.get("LIST_SYNTAX_TOPUP");
            if (listSyntax != null && !listSyntax.isEmpty()) {
                listRegexSyntaxTopup = new ArrayList();
                for (String syntax : listSyntax.split(",")) {
                    RegularExpression regex = new RegularExpression(syntax);
                    listRegexSyntaxTopup.add(regex);
                }
            }
            listSyntax = (String) listMsg.get("LIST_SYNTAX_CHECK");
            if (listSyntax != null && !listSyntax.isEmpty()) {
                listRegexSyntaxCheck = new ArrayList();
                for (String syntax : listSyntax.split(",")) {
                    RegularExpression regex = new RegularExpression(syntax);
                    listRegexSyntaxCheck.add(regex);
                }
            }
            init = true;
        }
    }

    public static String get(String key, String defaultKey, Logger logger) {
        synchronized (lockMessage) {
            String value = (String) listMsg.get(key);
            if (value == null || value.length() == 0) {
                logger.error("Not yet configured: PARAM_NAME=" + key);
                if (defaultKey != null) {
                    value = (String) listMsg.get(defaultKey);
                } else {
                    return "";
                }
            } else {
                logger.info("Tin nhan tra ve trong bang CONFIG: PARAM_NAME=" + key);
                return value;
            }

            if (value == null || value.length() == 0) {
                logger.error("Not yet configured: PARAM_NAME=" + defaultKey);
                value = "";
            } else {
                logger.info("Not yet configured: PARAM_NAME=" + defaultKey);
                return value;
            }
            return value;
        }
    }

    public static String getMessage(String key, Logger logger) {
        synchronized (lockMessage) {
            String value = (String) listMsg.get(key);
            if (value == null || value.length() == 0) {
                logger.error("Chua cau hinh trong bang CONFIG: PARAM_NAME=" + key);
                return "";
            } else {
                logger.info("Get message " + key);
                return value;
            }
        }
    }
}
