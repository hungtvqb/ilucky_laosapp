/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

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
            init = true;
        }
    }

    public static String get(String key, Logger logger) {
        synchronized (lockMessage) {
            String value = (String) listMsg.get(key);
            if (value == null || value.length() == 0) {
                logger.error("Not yet config message " + key);
                return "";
            } else {
                logger.info("Return message " + key);
                return value;
            }
        }
    }

    public static String get(String key, String defaultKey, Logger logger) {
        synchronized (lockMessage) {
            String value = (String) listMsg.get(key);
            if (value == null || value.length() == 0) {
                logger.error("Not yet config message " + key);
                if (defaultKey != null) {
                    value = (String) listMsg.get(defaultKey);
                } else {
                    return "";
                }
            } else {
                logger.info("Return message " + key);
                return value;
            }

            if (value == null || value.length() == 0) {
                logger.error("Not yet config message " + defaultKey);
                value = "";
            } else {
                logger.info("Return message " + defaultKey);
                return value;
            }
            return value;
        }
    }

    public static String getDefaultMessage(String key, Logger logger) {
        synchronized (lockMessage) {
            String value = (String) listMsg.get(key);
            if (value == null || value.length() == 0) {
                logger.error("Not yet config message " + key);

                return "";
            } else {
                return value;
            }

        }
    }
}
