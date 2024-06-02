/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.main;

import com.vas.wsfw.common.WebserviceManager;
import org.apache.log4j.Logger;

/**
 *
 * @author minhnh@viettel.com.vn
 * @since Jun 3, 2013
 * @version 1.0
 */
public class ScanProcessManager {

    private static ScanProcessManager instance;
    private Logger logger;

    public ScanProcessManager() throws Exception {
        logger = Logger.getLogger(ScanProcessManager.class);
    }

    public static ScanProcessManager getInstance() throws Exception {
        if (instance == null) {
            instance = new ScanProcessManager();
        }
        return instance;
    }

    public void start() {

        logger.info("+++  SYSTEM IS STARTING  +++");
        try {
            WebserviceManager.getInstance().start();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public void stop() {

        logger.info("+++  SYSTEM IS STOPPING  +++");
        try {
            WebserviceManager.getInstance().stop();
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
}
