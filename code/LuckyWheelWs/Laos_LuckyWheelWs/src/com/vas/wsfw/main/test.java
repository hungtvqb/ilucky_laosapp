/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.main;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

/**
 *
 * @author minhnh@viettel.com.vn
 * @since Jun 3, 2013
 * @version 1.0
 */
public class test {

    public static void main(String[] args) throws Exception {
        // Khoi dong MoProcess        
//        ScanProcessManager.getInstance().start();
        RegularExpression regex = new RegularExpression("[Ee][Vv][Nn]\\s+[Nn][Tt]\\s+\\w+");
        String str = "EvN nt 123456";
        boolean match = regex.matches(str);
        System.out.print(match);
    }
}
