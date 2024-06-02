/*
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.vas.wsfw.services;

import com.viettel.common.ExchMsg;
import com.viettel.common.ObjectClientChannel;
import com.viettel.common.ViettelMsg;
import com.viettel.common.ViettelService;
import com.vas.wsfw.common.WebserviceManager;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

/**
 * Thong tin ObjectClientChannel va moduleId
 *
 * @author TungTT
 * @version 1.0
 * @since 01-03-2011
 */
public class ExchangeChannel {

    private String host;
    private int port;
    private String user;
    private String pass;
    private ObjectClientChannel channel;
    private int id;
    private String pathEx;
    private int status;
    private String type;
    //
    private Logger logger = Logger.getLogger(ExchangeChannel.class);
    private String loggerLabel;
    //checker
    private ExchangeChannel.ChannelChecker checker;
    private ExecutorService executor = Executors.newCachedThreadPool();
    private boolean startChecker = false;

    public ExchangeChannel(ObjectClientChannel channel, long receiverTimeOut, int id, String pathEx, String type) {
        this.channel = channel;
        this.channel.setReceiverTimeout(receiverTimeOut);
        this.id = id;
        this.host = channel.getIp();
        this.port = channel.getPort();
        this.user = channel.getUsername();
        this.pass = channel.getPassword();
        this.pathEx = pathEx;
        this.loggerLabel = "ExchangeChannel" + this.getInfor();
        this.type = type;
        this.checker = new ExchangeChannel.ChannelChecker();
    }

    public String getInfor() {
        return host + ":" + port;
    }

    public String getFullInfo() {
        return host + ":" + port + "/" + user + "@" + pass;
    }

    public ObjectClientChannel getChannel() {
        return channel;
    }

    public void setChannel(ObjectClientChannel channel) {
        this.channel = channel;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public synchronized void startChecker() {
        if (!startChecker) {
            startChecker = true;
            this.executor.execute(checker);
        }
    }

    public ViettelMsg send(ViettelMsg request) throws IOException {
        long timeSt = System.currentTimeMillis();
        try {
            if (this.status == 0) {
                ViettelMsg response = channel.send(request);
                return response;
            } else {
                throw new IOException("ExchangeChannel is blocked because It has many timeout transactions...");
            }
        } catch (IOException ex) {
            this.status = 1;
            //khoi tao tien trinh kiem tra ket noi o day
            this.startChecker();
            throw ex;
        } finally {
            logTime("Time to send request", timeSt);
        }
    }

    /**
     *
     * @param request
     * @param commandTimeOut
     * @param changeClientWhenErr
     * @param logger
     * @return
     * @throws IOException
     */
    public ViettelMsg sendAll(ViettelMsg request, Long commandTimeOut, boolean changeClientWhenErr) throws Exception {
        ViettelMsg response = null;
        try {
            if (commandTimeOut != null) {
                request.set("ClientTimeout", String.valueOf(commandTimeOut));
            }
            response = this.send(request);
        } catch (Exception ex) {
            logger.error(loggerLabel + "ERROR send request " + this.getInfor(), ex);
            this.status = 1;
            //khoi tao tien trinh kiem tra ket noi o day
            this.startChecker();
            if (changeClientWhenErr) {
                int mainId = this.getId();
                ExchangeChannel newChannel = ExchangeClientChannel.getChannel();
                if (mainId != newChannel.getId()) {
                    logger.info(loggerLabel + "Change to Exchange " + newChannel.getInfor());
                    return processRequestOther(mainId, newChannel, request);
                } else {
                    logger.error(loggerLabel
                            + "All Client Exchange error, not process request: ERROR " + request.getCommand());
                    return response;
                }
            }
        }
        return response;
    }

    private ViettelMsg processRequestOther(int mainChannelId, ExchangeChannel channelOther, ViettelMsg request) {
        ViettelMsg response = null;
        try {
            response = channelOther.send(request);
        } catch (Exception ex) {
            logger.error(loggerLabel + "ERROR send request in Client: " + channelOther.getInfor(), ex);
            channelOther.setStatus(1);
            //khoi tao tien trinh kiem tra ket noi o day
            channelOther.startChecker();
            ExchangeChannel newChannel = ExchangeClientChannel.getChannel();

            if (mainChannelId != newChannel.getId()) {
                logger.info(loggerLabel + "Change to Exchange " + newChannel.getInfor());
                return processRequestOther(mainChannelId, newChannel, request);
            } else {
                logger.error(loggerLabel
                        + "All Client Exchange error, not process request: ERROR " + request.getCommand());
                return response;
            }
        } finally {
        }
        return response;
    }

    public void logTime(String strLog, long timeSt) {
        long timeEx = System.currentTimeMillis() - timeSt;
        if (timeEx >= WebserviceManager.minTimeOcs && WebserviceManager.loggerOcsMap != null) {
            logger.warn(loggerLabel
                    + WebserviceManager.getTimeLevelOcs(timeEx)
                    + ": " + strLog + ": " + timeEx + " ms\n");
        } else {
            logger.info(loggerLabel + strLog + ": " + timeEx + " ms");
        }
    }

    private class ChannelChecker implements Runnable {

        private ChannelChecker() {
        }

        @Override
        public void run() {
            String label = ExchangeChannel.this.getInfor() + ":";
            ExchangeChannel.this.logger.info(label + "Start thread check channel " + ExchangeChannel.this.getInfor());
            while (true) {
                try {
                    Thread.sleep(ExchangeClientChannel.periodCheck);
                } catch (Exception e) {
                }

                if (ExchangeChannel.this.status != 0) {
                    if (ExchangeChannel.this.type.equalsIgnoreCase("exchange")) {
                        ExchMsg request = new ExchMsg();
                        ExchMsg response = null;
                        request.setCommand(ExchangeClientChannel.commandTest);
                        request.set("MSISDN", ExchangeClientChannel.msisdnTest);
                        request.set("ClientTimeout", "25000");
                        try {
                            ExchangeChannel.this.logger.info(label + "Test send command  ");
                            response = (ExchMsg) ExchangeChannel.this.send(request);
                            if (response != null) {
                                ExchangeChannel.this.logger.info(label + "Test send command success");
                                ExchangeChannel.this.status = 0;
                                ExchangeChannel.this.startChecker = false;
                                return;
                            } else {
                                ExchangeChannel.this.logger.error(label + "Test send command error");
                            }
                        } catch (Exception e) {
                            ExchangeChannel.this.logger.error(label + "Test send command error");
                        }
                    } else if (ExchangeChannel.this.type.equalsIgnoreCase("service")) {
                        ViettelService request = new ViettelService();
                        ViettelService response = null;
                        request.setMessageType(ExchangeClientChannel.messageTypeTest);
                        request.setProcessCode(ExchangeClientChannel.processCodeTest);
                        request.set("MSISDN", ExchangeClientChannel.msisdnTest);
                        request.set("ClientTimeout", "25000");
                        try {
                            ExchangeChannel.this.logger.info(label + "Test send command  ");
                            response = (ViettelService) ExchangeChannel.this.send(request);
                            if (response != null) {
                                ExchangeChannel.this.logger.info(label + "Test send command   success");
                                ExchangeChannel.this.status = 0;
                                ExchangeChannel.this.startChecker = false;
                                return;
                            } else {
                                ExchangeChannel.this.logger.error(label + "Test send command  error");
                            }
                        } catch (Exception e) {
                            ExchangeChannel.this.logger.error(label + "Test send command  error");
                        }
                    }
                } else {
                    ExchangeChannel.this.logger.info(label + " no need check, status=" + ExchangeChannel.this.status);
                    ExchangeChannel.this.startChecker = false;
                    return;
                }
            }
        }
    }
}
