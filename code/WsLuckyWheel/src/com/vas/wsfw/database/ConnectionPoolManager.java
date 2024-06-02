/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.viettel.utility.PropertiesUtils;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 *
 * @author MinhNH
 */
public class ConnectionPoolManager {

    private static Logger logger = Logger.getLogger(ConnectionPoolManager.class);
    private static HashMap<String, ComboPooledDataSource> connectionMap = null;

    /**
     * *
     *
     * @param dbConfigXml
     * @throws Exception
     */
    public static void loadConfig(String dbConfigXml) throws Exception {

        if (connectionMap == null) {
            connectionMap = new HashMap();
            try {
                PropertiesUtils proUtils = new PropertiesUtils();
                proUtils.loadPropertiesEpt(dbConfigXml);
                StringBuilder build = new StringBuilder();
                String[] proStr = proUtils.getProperties();
                //
                for (int i = 0; i < proStr.length; i++) {
                    build.append(proStr[i]);
                }
                InputSource is = new InputSource(new StringReader(build.toString()));
                DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = df.newDocumentBuilder();
                Document dc = db.parse(is);
                Element root = dc.getDocumentElement();
                HashMap connectors = new HashMap();
                int index = 0;
                //load cac time out
                NodeList listTimeOut = root.getElementsByTagName("timeout-config");
                for (int i = 0; i < listTimeOut.getLength(); i++) {
                    Element ele = (Element) listTimeOut.item(i);
                    NodeList listProperty = ele.getElementsByTagName("property");
                    for (int j = 0; j < listProperty.getLength(); j++) {
                        Element elePro = (Element) listProperty.item(j);
                        String name = elePro.getAttribute("name");
                        String value = elePro.getTextContent();
                        if (name.equals("queryDbTimeout")) {
                            try {
                                DbProcessorAbstract.QUERY_TIMEOUT = Integer.parseInt(value);
                            } catch (Exception e) {
                                DbProcessorAbstract.QUERY_TIMEOUT = 60;
                            }

                        } else if (name.equals("timeBreak")) {
                            try {
                                DbProcessorAbstract.TIME_BREAK = Integer.parseInt(value);
                            } catch (Exception e) {
                                DbProcessorAbstract.TIME_BREAK = 90000;
                            }
                        } else if (name.equals("timeBreakDeleteRecordTimeOut")) {
                            try {
                                DbProcessorAbstract.TIME_BREAK_DELETE_RECORD_TIMEOUT = Integer.parseInt(value);
                            } catch (Exception e) {
                                DbProcessorAbstract.TIME_BREAK_DELETE_RECORD_TIMEOUT = 120000;
                            }
                        }
                    }
                }

                //
                NodeList list = root.getElementsByTagName("named-config");
                if (list.getLength() < 1) {
                    throw new Exception("No named-config");
                }
                //
                for (int i = 0; i < list.getLength(); ++i) {
                    Element ele = (Element) list.item(i);
                    String dbName = ele.getAttribute("name");

                    NodeList listProperty = ele.getElementsByTagName("property");
                    if (listProperty.getLength() < 1) {
                        throw new Exception("No property");
                    }
                    //doc list
                    Properties pro = new Properties();
                    for (int j = 0; j < listProperty.getLength(); j++) {
                        Element elePro = (Element) listProperty.item(j);
                        String name = elePro.getAttribute("name");
                        String value = elePro.getTextContent();
                        pro.setProperty(name, value);
                    }

                    // init pool Database
                    ConnectionPoolObject obj = new ConnectionPoolObject(pro);
                    obj.setId(dbName);
                    try {
                        connectionMap.put(dbName, obj.createPoolConnection());
                    } catch (Exception e) {
                        logger.error("==> ERROR WHEN INIT DATABASE ID=" + dbName, e);
                    }
                }
            } catch (Exception ex) {
                logger.error("==> ERROR INIT DATABASE LIST", ex);
                throw ex;
            }
        }
    }

    public static void reLoadConfig(String dbConfigXml) throws Exception {
        connectionMap = new HashMap();
        try {
            PropertiesUtils proUtils = new PropertiesUtils();
            proUtils.loadPropertiesEpt(dbConfigXml);
            StringBuilder build = new StringBuilder();
            String[] proStr = proUtils.getProperties();
            //
            for (int i = 0; i < proStr.length; i++) {
                build.append(proStr[i]);
            }
            InputSource is = new InputSource(new StringReader(build.toString()));
            DocumentBuilderFactory df = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = df.newDocumentBuilder();
            Document dc = db.parse(is);
            Element root = dc.getDocumentElement();
            HashMap connectors = new HashMap();
            int index = 0;
            //
            NodeList list = root.getElementsByTagName("named-config");
            if (list.getLength() < 1) {
                throw new Exception("No named-config");
            }
            //
            for (int i = 0; i < list.getLength(); ++i) {
                Element ele = (Element) list.item(i);
                String dbName = ele.getAttribute("name");

                NodeList listProperty = ele.getElementsByTagName("property");
                if (listProperty.getLength() < 1) {
                    throw new Exception("No property");
                }
                //doc list
                Properties pro = new Properties();
                for (int j = 0; j < listProperty.getLength(); j++) {
                    Element elePro = (Element) listProperty.item(j);
                    String name = elePro.getAttribute("name");
                    String value = elePro.getTextContent();
                    pro.setProperty(name, value);
                }

                // init pool Database
                ConnectionPoolObject obj = new ConnectionPoolObject(pro);
                obj.setId(dbName);
                try {
                    connectionMap.put(dbName, obj.createPoolConnection());
                } catch (Exception e) {
                    logger.error("==> ERROR WHEN INIT DATABASE ID=" + dbName, e);
                }
            }
        } catch (Exception ex) {
            logger.error("==> ERROR INIT DATABASE LIST", ex);
            throw ex;
        }
    }

    /**
     * @param id
     * @return **
     *
     */
    public static synchronized Connection getConnection(String id) {
        Connection conn = null;
        try {
            if (connectionMap.containsKey(id)) {
                conn = ((ComboPooledDataSource) connectionMap.get(id)).getConnection();
            } else {
                logger.warn("PooledDataSource don't have ID: " + id);
            }
        } catch (SQLException ex) {
            logger.error("Can't connect DB, with ID: " + id, ex);
        }

        return conn;
    }

    public static HashMap<String, ComboPooledDataSource> getConnectionMap() {
        return connectionMap;
    }

    public static void setConnectionMap(HashMap<String, ComboPooledDataSource> connectionMap) {
        ConnectionPoolManager.connectionMap = connectionMap;
    }

    public static List<String> getListDatabaseName() {

        List<String> listTemp = new ArrayList<String>();
        List<String> listReturn = new ArrayList<String>();
        for (Map.Entry<String, ComboPooledDataSource> entry : connectionMap.entrySet()) {
            String databaseName = entry.getKey();
            listTemp.add(databaseName);
        }
        //dao nguoc chuoi cho dung thu tu
        for (int i = listTemp.size(); i > 0; i--) {
            listReturn.add(listTemp.get(i - 1));
        }
        return listReturn;
    }
}
