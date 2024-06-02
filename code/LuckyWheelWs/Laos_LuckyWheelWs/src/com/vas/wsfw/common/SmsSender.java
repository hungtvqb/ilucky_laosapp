/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

import com.viettel.utility.PropertiesUtils;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import utils.Config;
import org.apache.log4j.Logger;

/**
 *
 * @author tungnv6
 */
public class SmsSender {
    // Lay danh sach listStub

    private int totalSmsWs;
    private static int totalStub;
    private ArrayList<SmsWs> listSmsWs;
    private static final Object lock = new Object();
    private static int roll = 0;
    private static SmsSender instance;

    public SmsSender() throws Exception {
        // Lay danh sach listStub
        ArrayList<SmsWs> listSmsInfo = initSmsWsInfo();

        //So luong thread gui tin
        totalStub = 1;

        // <editor-fold defaultstate="expanded" desc="Khoi tao danh sach SmsWs">
        int index = 0;
        listSmsWs = new ArrayList<SmsWs>();
        for (int i = 0; i < totalStub; i++) {
            SmsWs smsWs = listSmsInfo.get(index);
            listSmsWs.add(new SmsWs(smsWs.getId(), smsWs.getUrl(), smsWs.getXmlns(),
                    smsWs.getUser(), smsWs.getPass(), true));

            index++;
            if (index >= listSmsInfo.size()) {
                index = 0;
            }
        }
        // </editor-fold>
    }

    public static SmsSender getInstance() throws IOException, Exception {
        if (instance == null) {
            instance = new SmsSender();
        }
        return instance;
    }

    // Ham load tham so smsgateway
    private ArrayList<SmsWs> initSmsWsInfo() throws Exception {
        ArrayList<SmsWs> listSmsInfo;
        PropertiesUtils pro = new PropertiesUtils();
        pro.loadPropertiesEpt(Config.configDir + File.separator + "smsgateway.cfg");
        String[] properties = pro.getProperties();
        int index = 0;
        String line = properties[index++];

        String url;
        String user;
        String pass;

        listSmsInfo = new ArrayList<SmsWs>();
        while (index <= properties.length) {
            url = "";
            user = "";
            pass = "";

            // Get config
            while (index < properties.length && !line.trim().toUpperCase().equals("[SMSWS]")) {
                line = properties[index++];
            }
            if (index >= properties.length) {
                return listSmsInfo;
            }
            line = properties[index++];

            while (index <= properties.length && line.trim().toUpperCase().indexOf("[SMSWS]") < 0) {
                if (line.trim().length() > 0) {
                    String info[] = line.split("=");
                    if (info[0].trim().toUpperCase().equals("URL")) {
                        url = info[1].trim();
                    }
                    if (info[0].trim().toUpperCase().equals("USERNAME")) {
                        user = info[1].trim();
                    }
                    if (info[0].trim().toUpperCase().equals("PASSWORD")) {
                        pass = info[1].trim();
                    }
                }
                if (index >= properties.length) {
                    break;
                }
                line = properties[index++];
            }
            // init client
            SmsWs stub1 = new SmsWs(listSmsInfo.size(), url, "http://tempuri.org/", user, pass, false);
            listSmsInfo.add(stub1);
        }
        totalSmsWs = listSmsInfo.size();
        return listSmsInfo;
    }

    private int increaseRoll() {
        synchronized (lock) {
            roll++;
            if (roll >= listSmsWs.size()) {
                roll = 0;
            }
        }
        return roll;
    }

    public int sendSmsAll(String message, String msisdn, String fromChannel, String msgType, Logger logger) {

        int error = -1;

        int index = increaseRoll();

        //long timeSt = System.currentTimeMillis();
        error = listSmsWs.get(index).send("0", "warning", fromChannel, msisdn, msgType, message, "1");
        //logTime("Time to send sms", timeSt, listSmsWs.get(index), logger);
        if (error == 0) {
            return error;
        }

        List<Integer> listId = new ArrayList();
        listId.add(listSmsWs.get(index).getId());

        // <editor-fold defaultstate="expanded" desc="Loi, tiep tuc gui thong qua SMSGW tiep theo">
        while (listId.size() < totalSmsWs) {
            logger.error("Sender using SMSWS error: " + listSmsWs.get(index).getInfor());

            index = increaseRoll();
            int idNext = listSmsWs.get(index).getId();
            while (listId.indexOf(idNext) >= 0) {
                // Neu SMSWS tiep theo la SMSWS da gui thi lay SMSWS tiep theo
                index = increaseRoll();
                idNext = listSmsWs.get(index).getId();
            }

            //timeSt = System.currentTimeMillis();
            error = listSmsWs.get(idNext).send("0", "warning", fromChannel, msisdn, "0", message, "1");
            //logTime("Time to send sms", timeSt, listSmsWs.get(index), logger);
            if (error == 0) {
                return error;
            }

            // Add to list chua id SMSWS da gui
            listId.add(listSmsWs.get(idNext).getId());
        }
        // </editor-fold>
        return error;
    }
}
