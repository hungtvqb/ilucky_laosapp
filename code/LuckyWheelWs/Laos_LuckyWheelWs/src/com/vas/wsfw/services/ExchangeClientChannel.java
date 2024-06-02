/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.services;

import com.viettel.common.ObjectClientChannel;
import com.viettel.utility.PropertiesUtils;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.vas.wsfw.common.WebserviceManager;

/**
 * Quan ly thong tin Exchange client
 *
 * @author TungTT
 * @version 1.0
 * @since 01-03-2011
 */
public class ExchangeClientChannel {

    private static Logger logger = Logger.getLogger(ExchangeClientChannel.class);
    private static String loggerLabel = "ExchangeClientChannel: ";
    private static HashMap listExch;
    private int exchId;
//    private static HashMap<String, ExchangeClientChannel> hmInstance = new HashMap<String, ExchangeClientChannel>();
    private static ExchangeClientChannel instance;
    private int RECEIVER_TIME_OUT = -1;
    private static int exchCount = 0;
    //test ket noi
    public static String commandTest;
    public static String messageTypeTest;
    public static String processCodeTest;
    public static String msisdnTest;
    public static long periodCheck;

    public ExchangeClientChannel() throws Exception {
        logger.info(loggerLabel + "Init Exchange");
        String path = WebserviceManager.pathExch;

        String host;
        int port;
        String user;
        String pass;
        String type;
        listExch = new HashMap();

        try {
            PropertiesUtils pro = new PropertiesUtils();
            pro.loadPropertiesEpt(path);
            String[] properties = pro.getProperties();

            RECEIVER_TIME_OUT = Integer.parseInt(pro.getProperty("RECEIVER_TIME_OUT", "100000"));
            commandTest = pro.getProperty("EXCH_COMMAND_TEST", "IN_VIEWUSERINFO");
            messageTypeTest = pro.getProperty("MESSAGE_TYPE_TEST", "1900");
            processCodeTest = pro.getProperty("PROCESS_CODE_TEST", "030005");
            msisdnTest = pro.getProperty("MSISDN_TEST", "84985098588");
            periodCheck = Long.parseLong(pro.getProperty("PERIOD_CHECK", "30000"));

            int index = 0;
            String line = properties[index];
            exchId = 0;

            while (index <= properties.length) {
                host = "";
                port = -1;
                user = "";
                pass = "";
                type = "";

                // Get config
                while (index < properties.length && !line.trim().toUpperCase().equals("[CHANNEL]")) {
                    line = properties[index++];
                }
                if (index >= properties.length) {
                    return;
                }
                line = properties[index++];


                while (index <= properties.length && line.trim().toUpperCase().indexOf("[CHANNEL]") < 0) {
                    if (line.trim().length() > 0) {
                        String info[] = line.split("=");
                        if (info[0].trim().toUpperCase().equals("ADDRESS")) {
                            host = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("PORT")) {
                            port = Integer.parseInt(info[1].trim());
                        }
                        if (info[0].trim().toUpperCase().equals("USER")) {
                            user = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("PASS")) {
                            pass = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("TYPE")) {
                            type = info[1].trim();
                        }
                    }
                    if (index >= properties.length) {
                        break;
                    }
                    line = properties[index++];
                }
                // init client
                ObjectClientChannel channel = new ObjectClientChannel(host, port, user, pass, true);
                ExchangeChannel exChannel = new ExchangeChannel(channel, RECEIVER_TIME_OUT, exchId, path, type);
                listExch.put(exchId, exChannel);
                exchId++;

                logger.info(loggerLabel + "Init Exchange: " + exChannel.getInfor() + " success");
            }

            if (RECEIVER_TIME_OUT != -1) {
                logger.info(loggerLabel + "RECEIVER_TIME_OUT: " + RECEIVER_TIME_OUT);
            }

            logger.info(loggerLabel + "Init All Exchange success");
        } catch (Exception ex) {
            logger.error("ERROR Init Exchange, please check exchange_client.cfg", ex);
            listExch.clear();
        }
    }

    public ExchangeClientChannel(String pathEx) throws Exception {
        logger.info(loggerLabel + "Init Exchange");
        String path = pathEx;

        String host;
        int port;
        String user;
        String pass;
        String type;
        listExch = new HashMap();

        try {
            PropertiesUtils pro = new PropertiesUtils();
            pro.loadPropertiesEpt(path);
            String[] properties = pro.getProperties();

            RECEIVER_TIME_OUT = Integer.parseInt(pro.getProperty("RECEIVER_TIME_OUT", "100000"));
            commandTest = pro.getProperty("EXCH_COMMAND_TEST", "IN_VIEWUSERINFO");
            messageTypeTest = pro.getProperty("MESSAGE_TYPE_TEST", "1900");
            processCodeTest = pro.getProperty("PROCESS_CODE_TEST", "030005");
            msisdnTest = pro.getProperty("MSISDN_TEST", "");
            periodCheck = Long.parseLong(pro.getProperty("PERIOD_CHECK", "30000"));

            int index = 0;
            String line = properties[index];
            exchId = 0;

            while (index <= properties.length) {
                host = "";
                port = -1;
                user = "";
                pass = "";
                type = "";

                // Get config
                while (index < properties.length && !line.trim().toUpperCase().equals("[CHANNEL]")) {
                    line = properties[index++];
                }
                if (index >= properties.length) {
                    return;
                }
                line = properties[index++];


                while (index <= properties.length && line.trim().toUpperCase().indexOf("[CHANNEL]") < 0) {
                    if (line.trim().length() > 0) {
                        String info[] = line.split("=");
                        if (info[0].trim().toUpperCase().equals("ADDRESS")) {
                            host = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("PORT")) {
                            port = Integer.parseInt(info[1].trim());
                        }
                        if (info[0].trim().toUpperCase().equals("USER")) {
                            user = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("PASS")) {
                            pass = info[1].trim();
                        }
                        if (info[0].trim().toUpperCase().equals("TYPE")) {
                            type = info[1].trim();
                        }
                    }
                    if (index >= properties.length) {
                        break;
                    }
                    line = properties[index++];
                }
                // init client
                ObjectClientChannel channel = new ObjectClientChannel(host, port, user, pass, true);
                ExchangeChannel exChannel = new ExchangeChannel(channel, RECEIVER_TIME_OUT, exchId, pathEx, type);
                listExch.put(exchId, exChannel);
                exchId++;

                logger.info(loggerLabel + "Init Exchange: " + exChannel.getInfor() + " success");
            }

            if (RECEIVER_TIME_OUT != -1) {
                logger.info(loggerLabel + "RECEIVER_TIME_OUT: " + RECEIVER_TIME_OUT);
            }

            logger.info(loggerLabel + "Init All Exchange success");
        } catch (Exception ex) {
            logger.error("ERROR Init Exchange, please check exchange_client.cfg", ex);
            listExch.clear();
        }
    }

    public static ExchangeClientChannel getInstance() throws Exception {
        if (instance == null) {
            instance = new ExchangeClientChannel();
        }
        return instance;
    }

    public synchronized static ExchangeChannel getChannel() {
        int getAcount = 0;
        try {
            while (true) {
                if (getAcount > listExch.size() - 1) {
                    logger.info("All ExchangeChannel is blocked => set active status for all");
                    //Tat ca cac Channel trong list deu bi block. Thuc hien giai phong 
                    for (int i = 0; i < listExch.size(); i++) {
                        ((ExchangeChannel) listExch.get(i)).setStatus(0);
                    }
                    getAcount = 0;
                }

                if (exchCount > listExch.size() - 1) {
                    //Kiem tra exchCount co lon hon so luong chanle trong list khong.
                    exchCount = 0;
                }

                if (((ExchangeChannel) listExch.get(exchCount)).getStatus() == 0) {
                    //Lay channel ra, tang exchCount roi return
                    ExchangeChannel channel = (ExchangeChannel) listExch.get(exchCount);
                    exchCount++;
                    return channel;
                } else {
                    getAcount++;
                    exchCount++;
                }
            }
        } catch (Exception ex) {
            logger.error("Get channel in exchange channel list fails, return Null:.........", ex);
            return null;
        }
    }

    public int getExchId() {
        return exchId;
    }

    public void setExchId(int exchId) {
        this.exchId = exchId;
    }

    public HashMap getListExch() {
        return listExch;
    }

    public void setListExch(HashMap listExch) {
        ExchangeClientChannel.listExch = listExch;
    }

    public int getRECEIVER_TIME_OUT() {
        return RECEIVER_TIME_OUT;
    }

    public void setRECEIVER_TIME_OUT(int RECEIVER_TIME_OUT) {
        this.RECEIVER_TIME_OUT = RECEIVER_TIME_OUT;
    }
}
