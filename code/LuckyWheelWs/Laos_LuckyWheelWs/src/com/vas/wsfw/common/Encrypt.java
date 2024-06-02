/*
 * @Encrypt.java	version 1.0	29/03/2010
 *
 * Copyright 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.log4j.Logger;
import sun.misc.BASE64Encoder;

/**
 *
 * @author TungTT
 */
public class Encrypt {

    private static String convertToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String MD5(String text)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("MD5");
        byte[] md5hash = new byte[32];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5hash = md.digest();
        return convertToHex(md5hash);
    }

    /**
     *
     * @param securityCode
     * @param salt
     * @return
     */
    public static String getHashCode(String salt, String securityCode, Logger logger) {
        MessageDigest md = null;
        try {
            String seq = securityCode + salt;
            md = MessageDigest.getInstance("SHA-256"); //step 2
            md.update(seq.getBytes("UTF-8")); //step 3
        } catch (Exception ex) {
            logger.error(ex.toString(), ex);
        }
        byte raw[] = md.digest(); //step 4
        String hash = (new BASE64Encoder()).encode(raw); //step 5
        return hash;
    }

    public static void main(String[] args) {
        MessageDigest md = null;
        String[] salt = new String[]{
            "856123456",
            "856123457"

        };
        String[] securityCode = new String[]{
            "123456",
            "123456"
        };
        try {
            for (int i = 0; i < salt.length; i++) {

                String seq = securityCode[i] + salt[i];
                md = MessageDigest.getInstance("SHA-256"); //step 2
                md.update(seq.getBytes("UTF-8")); //step 3

                byte raw[] = md.digest(); //step 4
                String hash = (new BASE64Encoder()).encode(raw); //step 5
                System.out.println(hash);
            }
        } catch (Exception ex) {
//            logger.error(ex.toString(), ex);
        }
    }
}
