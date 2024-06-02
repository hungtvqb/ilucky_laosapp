/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.wsfw.common;

import com.vas.wsfw.obj.Route;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import utils.Config;

/**
 *
 * @author MinhNH
 */
public class RouteManager {

    private static RouteManager instance;
    private static List<Route> listRoute;
    private Logger logger;

    public RouteManager() throws Exception {

        logger = Logger.getLogger(RouteManager.class);
        String connectorsFile = Config.configDir + File.separator + "route.xml";
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document dc = db.parse(connectorsFile);
        Element root = dc.getDocumentElement();

        NodeList list = root.getElementsByTagName("route");
        if (list.getLength() < 1) {
            throw new Exception("No webservice to publish");
        }
        listRoute = new ArrayList<Route>();
        for (int i = 0; i < list.getLength(); ++i) {
            Element element = (Element) list.item(i);

            String name = element.getAttribute("name");


            Route route = new Route();

            logger.info("===> get config for route to: " + name);
            route.setName(name);
            route.setListDbName(element.getAttribute("listDbName"));
            route.setPrefixMsisdn(element.getAttribute("prefixMsisdn"));
            listRoute.add(route);
        }
    }

    public static RouteManager getInstance() throws Exception {
        if (instance == null) {
            instance = new RouteManager();
        }
        return instance;
    }

    public String getDbNamebyMsisdn(String msisdn) {
        String result = "";
        for (Route route : listRoute) {
            result = route.getDbNamebyMsisdn(msisdn);
            if (!result.equals("")) {
                break;
            }
        }
        return result;
    }
}
