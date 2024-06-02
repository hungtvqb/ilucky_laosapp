/*
 * Copyright (C) 2010 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.common;

import com.vas.wsfw.database.ConnectionPoolManager;
import com.vas.wsfw.services.ExchangeClientChannel;
import com.viettel.mmserver.base.ProcessThreadMX;
import com.viettel.ussdfw.log.ProcessTransLog;
import com.viettel.utility.PropertiesUtils;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.Endpoint;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.Config;

/**
 *
 * @author minhnh@viettel.com.vn
 * @since Jun 3, 2013
 * @version 1.0
 */
public class WebserviceManager extends ProcessThreadMX {

    private static WebserviceManager instance;
    //
    private static List<WebserviceObject> listWS;
    public static HashMap<String, Integer> listWSname;
    private static List<Endpoint> listEndpoint;
    private int[] idLogTransThread;
    private String connectorsFile;
    // Config
    public static String processClass;
    public static String appId;
//    public static int maxRow;
    public static int queryDbTimeout;
    public static int breakQuery;
    public static int dbTimeOut;
    public static boolean exchangeEnable;
    public static boolean enableQueryDbTimeout;
    public static String pathDatabase;
    public static String pathExch;
    public static long[] timesDbLevel;
    public static long[] timesOcsLevel;
    public static long minTimeDb;
    public static long minTimeOcs;
    public static HashMap loggerDbMap;
    public static HashMap loggerOcsMap;

    public static boolean logToDatabase;
    public static int numThreadLogTrans;
    public static String logToDatabaseId;
    public static String logDatabaseConf;
    private boolean exception = false;
    //
    private StringBuffer br = new StringBuffer();
//    private boolean exception = false;
    //
    //nang cap retry db
    public static int BREAK_INSERT_REGISTER;
    public static int RETRY_INSERT_REGISTER;
    public static int LOG_RETRY_INSERT_REGISTER;
    public static int BREAK_UPDATE_REGISTER;
    public static int RETRY_UPDATE_REGISTER;
    public static int LOG_RETRY_UPDATE_REGISTER;
    public static int BREAK_DELETE_MO;
    public static int RETRY_DELETE_MO;
    public static int LOG_RETRY_DELETE_MO;
    public static int DELAY_RETRY_DB;
    public static long PROCESS_TIMEOUT;
    // ma quoc gia
    public static String countryCode;
    // =====CHECK_PRODUCT==========================
    // so tien trinh check product
    public static int NUM_THREAD_CHECK_PRODUCT;
    // thoi gian timeout khi xu ly check product
    public static long TIMEOUT_THREAD_CHECK_PRODUCT;
    public static int NUM_PRODUCT_PER_THREAD;
    public static String MESSAGE;
    public static String CHANNEL;
    //=============================================

    public static WebserviceManager getInstance() throws Exception {
        if (instance == null) {
            instance = new WebserviceManager();
        }
        return instance;
    }

    public WebserviceManager() throws Exception {
        super("WebserviceManager");
        registerAgent("WebserviceFW:type=WebserviceManager");
        // get config
        String config = Config.configDir + File.separator + "app.conf";
        FileReader fileReader = null;
        fileReader = new FileReader(config);
        Properties pro = new Properties();
        pro.load(fileReader);

        br.setLength(0);
        // Ma ung dung
        try {
            appId = pro.getProperty("APP_ID").toUpperCase();
        } catch (Exception ex) {
            br.append("APP_ID not found in app.conf\n");
//            exception = true;
        }

        // Query db break
        try {
            breakQuery = 1000 * Integer.parseInt(pro.getProperty("breakQuery"));
        } catch (Exception ex) {
            breakQuery = 10000;
            br.append("breakQuery not found in app.conf--> default: 60s\n");
        }

        // Query db break
        try {
            queryDbTimeout = Integer.parseInt(pro.getProperty("queryDbTimeout"));
        } catch (Exception ex) {
            queryDbTimeout = 18000;
            br.append("queryDbTimeout not found in app.conf --> default: 18s\n");
        }

        // Ma quoc gia
        try {
            countryCode = pro.getProperty("COUNTRY_CODE");
            if (countryCode == null || countryCode.length() == 0) {
                br.append("COUNTRY_CODE not found in app.conf ==> default=84");
                countryCode = "84";
            }
        } catch (Exception ex) {
            br.append("COUNTRY_CODE not found in app.conf ==> default=84");
            countryCode = "84";
        }

        // thoi gian break insert register
        try {
            BREAK_INSERT_REGISTER = Integer.parseInt(pro.getProperty("BREAK_INSERT_REGISTER"));
        } catch (Exception ex) {
            br.append("BREAK_INSERT_REGISTER not found in app.conf\n");
        }

        // thoi gian break update register
        try {
            BREAK_UPDATE_REGISTER = Integer.parseInt(pro.getProperty("BREAK_UPDATE_REGISTER"));
        } catch (Exception ex) {
            br.append("BREAK_UPDATE_REGISTER not found in app.conf\n");
        }

        // so lan retry insert register
        try {
            RETRY_INSERT_REGISTER = Integer.parseInt(pro.getProperty("RETRY_INSERT_REGISTER"));
        } catch (Exception ex) {
            br.append("RETRY_INSERT_REGISTER not found in app.conf\n");
        }

        // so lan retry de ghi log
        try {
            LOG_RETRY_INSERT_REGISTER = Integer.parseInt(pro.getProperty("LOG_RETRY_INSERT_REGISTER"));
        } catch (Exception ex) {
            br.append("LOG_RETRY_INSERT_REGISTER not found in app.conf\n");
        }

        // so lan retry update register
        try {
            RETRY_UPDATE_REGISTER = Integer.parseInt(pro.getProperty("RETRY_UPDATE_REGISTER"));
        } catch (Exception ex) {
            br.append("RETRY_UPDATE_REGISTER not found in app.conf\n");
        }

        // so lan retry de ghi log
        try {
            LOG_RETRY_UPDATE_REGISTER = Integer.parseInt(pro.getProperty("LOG_RETRY_UPDATE_REGISTER"));
        } catch (Exception ex) {
            br.append("LOG_RETRY_UPDATE_REGISTER not found in app.conf\n");
        }

        // thoi gian delay truoc khi retry db (s)
        try {
            DELAY_RETRY_DB = 1000 * Integer.parseInt(pro.getProperty("DELAY_RETRY_DB"));
        } catch (Exception ex) {
            br.append("DELAY_RETRY_DB not found in app.conf\n");
        }

        // thoi gian timeout xu ly (s)
        try {
            PROCESS_TIMEOUT = 1000 * Long.parseLong(pro.getProperty("PROCESS_TIMEOUT"));
        } catch (Exception ex) {
            br.append("PROCESS_TIMEOUT not found in app.conf\n");
        }

        //So tien trinh kiem tra danh sach goi duoc dang ky
        try {
            NUM_THREAD_CHECK_PRODUCT = Integer.parseInt(pro.getProperty("NUM_THREAD_CHECK_PRODUCT"));
        } catch (Exception ex) {
            br.append("NUM_THREAD_CHECK_PRODUCT not found in app.conf ==> default = 1\n");
            NUM_THREAD_CHECK_PRODUCT = 1;
        }
        try {
            NUM_PRODUCT_PER_THREAD = Integer.parseInt(pro.getProperty("NUM_PRODUCT_PER_THREAD"));
        } catch (Exception ex) {
            br.append("NUM_PRODUCT_PER_THREAD not found in app.conf ==> default = 100\n");
            NUM_PRODUCT_PER_THREAD = 100;
        }
        //Thoi gian timeout khi kiem tra product duoc dang ky
        try {
            TIMEOUT_THREAD_CHECK_PRODUCT = 1000 * Integer.parseInt(pro.getProperty("TIMEOUT_THREAD_CHECK_PRODUCT"));
        } catch (Exception ex) {
            br.append("TIMEOUT_THREAD_CHECK_PRODUCT not found in app.conf ==> default = 30000ms\n");
            TIMEOUT_THREAD_CHECK_PRODUCT = 30000;
        }
        //
        // thoi gian break delete mo
        try {
            BREAK_DELETE_MO = Integer.parseInt(pro.getProperty("BREAK_DELETE_MO"));
        } catch (Exception ex) {
            br.append("BREAK_DELETE_MO not found in app.conf\n");
        }

        // so lan retry delete mo
        try {
            RETRY_DELETE_MO = Integer.parseInt(pro.getProperty("RETRY_DELETE_MO"));
        } catch (Exception ex) {
            br.append("RETRY_DELETE_MO not found in app.conf\n");
        }

        // so lan retry ghi log
        try {
            LOG_RETRY_DELETE_MO = Integer.parseInt(pro.getProperty("LOG_RETRY_DELETE_MO"));
        } catch (Exception ex) {
            br.append("LOG_RETRY_DELETE_MO not found in app.conf\n");
        }

        /**
         * EXCHANGE
         */
        try {
            exchangeEnable = Boolean.parseBoolean(pro.getProperty("EXCHANGE_ENABLE"));
        } catch (Exception ex) {
            br.append("EXCHANGE_ENABLE not found in app.conf => Default value: false\n");
            exchangeEnable = false;
        }
        try {
            MESSAGE = pro.getProperty("MESSAGE");
        } catch (Exception ex) {
            br.append("MESSAGE not found in app.conf ==> default = null\n");
            MESSAGE = null;
        }
        try {
            CHANNEL = pro.getProperty("CHANNEL");
        } catch (Exception ex) {
            br.append("CHANNEL not found in app.conf ==> default = null\n");
            CHANNEL = "154";
        }
        if (exchangeEnable) {
            try {
                pathExch = "../etc/exchange_client.cfg";
            } catch (Exception ex) {
                br.append("PATH_EXCH not found in app.conf\n");
//                exception = true;
            }
        }
        try {
            logToDatabase = Boolean.parseBoolean(pro.getProperty("LOG_TO_DATABASE"));
        } catch (Exception ex) {
            this.br.append("LOG_TO_DATABASE not found in app.conf, default=false \n");
            logToDatabase = false;
        }
        if (logToDatabase) {
            try {
                logToDatabaseId = pro.getProperty("LOG_TO_DATABASE_ID");
                if ((logToDatabaseId == null) || (logToDatabaseId.length() <= 0)) {
                    this.br.append("LOG_TO_DATABASE_ID need config in app.conf\n");
                }
            } catch (Exception ex) {
                this.br.append("LOG_TO_DATABASE_ID not found in app.conf\n");
                this.exception = true;
            }
            try {
                logDatabaseConf = pro.getProperty("LOG_DATABASE_CONF");
                if ((logDatabaseConf == null) || (logDatabaseConf.length() <= 0)) {
                    this.br.append("LOG_DATABASE_CONF need config in app.conf\n");
                    this.exception = true;
                }
            } catch (Exception ex) {
                this.br.append("LOG_DATABASE_CONF not found in app.conf\n");
                this.exception = true;
            }

            try {
                numThreadLogTrans = Integer.parseInt(pro.getProperty("NUM_THREAD_LOG_TRANS"));
                if (numThreadLogTrans <= 0) {
                    this.br.append("NUM_THREAD_LOG_TRANS need config in app.conf\n");
                    numThreadLogTrans = 1;
                }
            } catch (Exception ex) {
                this.br.append("NUM_THREAD_LOG_TRANS not found in app.conf,default=1\n");
                numThreadLogTrans = 1;
            }

        }

        if (this.exception) {
            throw new Exception(this.br.toString());
        }
        if (this.br.length() > 0) {
            this.logger.warn(this.br);
        }

        /**
         * DATABASE
         */
        pathDatabase = "../etc/database.xml";

        fileReader.close();
        // Load log warning
        loadLogLevelWarnning();
        connectorsFile = Config.configDir + File.separator + "webservices.xml";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dc = db.parse(connectorsFile);
        Element root = dc.getDocumentElement();

        NodeList list = root.getElementsByTagName("webservice");
        if (list.getLength() < 1) {
            throw new Exception("No webservice to publish");
        }

        listWS = new ArrayList<WebserviceObject>();
        listWSname = new HashMap<String, Integer>();

        for (int i = 0; i < list.getLength(); ++i) {
            Element element = (Element) list.item(i);

            String name = element.getAttribute("name");

            if (listWSname.containsKey(name)) {
                throw new Exception("same webservice name: " + name);
            }
            WebserviceObject webserviceObject = new WebserviceObject();

            logger.info("===> get config for webservice: " + name);
            webserviceObject.setName(name);
            webserviceObject.setIp(element.getAttribute("ip"));
            webserviceObject.setPort(element.getAttribute("port"));
            webserviceObject.setPath(element.getAttribute("path"));
            webserviceObject.setImplementClass(element.getAttribute("implementClass"));
            webserviceObject.makeUrl();
            listWSname.put(name, 1);
            listWS.add(webserviceObject);
        }
        ConnectionPoolManager.loadConfig(pathDatabase);
        if (exchangeEnable) {
            ExchangeClientChannel.getInstance();
        }

        this.idLogTransThread = new int[WebserviceManager.numThreadLogTrans];
        for (int i = 0; i < WebserviceManager.numThreadLogTrans; i++) {
            ProcessTransLog log = new ProcessTransLog("ProcessTransLog" + i,
                    WebserviceManager.logToDatabase, WebserviceManager.logDatabaseConf, WebserviceManager.logToDatabaseId);
            this.idLogTransThread[i] = log.getId().intValue();
        }

    }

    @Override
    protected void process() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException ex) {
        }
    }

    @Override
    public void start() {
        super.start();
        ClassLoader cl = new ClassLoader() {
        };
        listEndpoint = new ArrayList<Endpoint>();
        for (WebserviceObject webserviceObject : listWS) {
            try {

                Class c = cl.loadClass(webserviceObject.getImplementClass());
                logger.info("===> Load class: " + c.getName());
                WebserviceAbstract webserviceAbstract = (WebserviceAbstract) c.newInstance();
                Endpoint service = Endpoint.publish(webserviceObject.getUrl(), webserviceAbstract);

                logger.info("Publish service " + webserviceObject.getName() + " success!");
                logger.info("URL: " + webserviceObject.getUrl() + "?wsdl");
                listEndpoint.add(service);
            } catch (Exception e) {
                logger.error("Publish service " + webserviceObject.getName() + " error!", e);
            }

        }

        for (int i = 0; i < this.idLogTransThread.length; i++) {
            com.viettel.mmserver.base.ProcessManager.getInstance().getMmProcess(Integer.valueOf(this.idLogTransThread[i])).start();
        }

        logger.info("+++ SYSTEM PROCESS STARTED  +++");
    }

    @Override
    public void stop() {

        for (Endpoint endpoint : listEndpoint) {
            try {
                endpoint.stop();
            } catch (Exception e) {
                logger.error("Stop endpoint " + endpoint.getClass().toString() + " error!", e);
            }
        }

        for (int i = 0; i < this.idLogTransThread.length; i++) {
            com.viettel.mmserver.base.ProcessManager.getInstance().getMmProcess(Integer.valueOf(this.idLogTransThread[i])).stop();
        }

        super.stop();
        logger.info("+++ SYSTEM PROCESS STOPPED  +++");
    }

    /**
     * Doc thong tin file loglevel.conf
     */
    private void loadLogLevelWarnning() throws Exception {
        PropertiesUtils pros = new PropertiesUtils();
        pros.loadProperties("../etc/loglevel.conf", false);
        try {
            String[] dbTimes = pros.getProperty("DB_TIMES").split(",");
            String[] dbKey = pros.getProperty("DB_MESSAGE_KEY").split(",");

            loggerDbMap = new HashMap();
            timesDbLevel = new long[dbTimes.length];
            minTimeDb = Long.parseLong(dbTimes[0].trim());
            for (int i = 0; i < dbTimes.length; i++) {
                timesDbLevel[i] = Long.parseLong(dbTimes[i].trim());
                loggerDbMap.put(i, dbKey[i].trim());
            }
        } catch (Exception ex) {
            logger.error("Loi lay thong tin DB_TIMES, DB_MESSAGE_KEY trong loglevel.conf");
            loggerDbMap = null;
            throw ex;
        }

        if (exchangeEnable) {
            try {
                String[] ocsTimes = pros.getProperty("OCS_TIMES").split(",");
                String[] ocsKey = pros.getProperty("OCS_MESSAGE_KEY").split(",");

                loggerOcsMap = new HashMap();
                timesOcsLevel = new long[ocsTimes.length];
                minTimeOcs = Long.parseLong(ocsTimes[0].trim());
                for (int i = 0; i < ocsTimes.length; i++) {
                    timesOcsLevel[i] = Long.parseLong(ocsTimes[i].trim());
                    loggerOcsMap.put(i, ocsKey[i].trim());
                }
            } catch (Exception ex) {
                logger.error("Loi lay thong tin OCS_TIMES, OCS_MESSAGE_KEY trong loglevel.conf");
                loggerOcsMap = null;
                throw ex;
            }
        }
    }

    /**
     * Log cham database
     *
     * @param times
     * @return
     */
    public static String getTimeLevelDb(long times) {
        if (loggerDbMap != null) {
            int key = Arrays.binarySearch(timesDbLevel, times);
            if (key < 0) {
                key = -key - 2;
            }

            String label = (String) loggerDbMap.get(key);

            return (label == null) ? "-" : label;
        }
        return null;
    }

    /**
     * Log cham ocs, hlr
     *
     * @param times
     * @return
     */
    public static String getTimeLevelOcs(long times) {
        if (loggerOcsMap != null) {
            int key = Arrays.binarySearch(timesOcsLevel, times);
            if (key < 0) {
                key = -key - 2;
            }

            String label = (String) loggerOcsMap.get(key);
            return (label == null) ? "-" : label;
        }
        return null;
    }
}
