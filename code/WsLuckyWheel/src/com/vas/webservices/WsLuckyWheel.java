/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vas.webservices;

import com.vas.wsfw.common.Common;
import com.vas.wsfw.common.Encrypt;
import com.vas.wsfw.common.MessageResponse;
import com.vas.wsfw.common.WebserviceAbstract;
import static com.vas.wsfw.common.WebserviceAbstract.EXCEPTION;
import static com.vas.wsfw.common.WebserviceAbstract.PARAM_NOT_ENOUGH;
import static com.vas.wsfw.common.WebserviceAbstract.SUCCESS;
import static com.vas.wsfw.common.WebserviceAbstract.WRONG_PASSWORD;
import com.vas.wsfw.common.WebserviceManager;
import com.vas.wsfw.database.WsProcessUtils;
import com.vas.wsfw.obj.AccountInfo;
import com.vas.wsfw.obj.AdsObj;
import com.vas.wsfw.obj.ChargeLog;
import com.vas.wsfw.obj.ConfirmOtpObj;
import com.vas.wsfw.obj.MissionObj;
import com.vas.wsfw.obj.PrizeObj;
import com.vas.wsfw.obj.Questions;
import com.vas.wsfw.obj.Request;
import com.vas.wsfw.obj.Response;
import com.vas.wsfw.obj.SmsMtObj;
import com.vas.wsfw.obj.SpinTotalObj;
import com.vas.wsfw.obj.UserInfo;
import com.vas.wsfw.obj.UserPlayObj;
import com.vas.wsfw.services.WSProcessor;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 *
 * @author Sungroup
 */
@WebService
public class WsLuckyWheel extends WebserviceAbstract {

    private WsProcessUtils db;
    private StringBuilder br = new StringBuilder();
    //private Exchange exchange;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat fullDf = new SimpleDateFormat("yyyyMMddHHmmss");
    private SimpleDateFormat reqDf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private WSProcessor ws;
    private Random ran = new Random();

    public WsLuckyWheel() throws Exception {
        super("WsLuckyDraw");
        db = new WsProcessUtils("dbProcess", logger);
        ws = new WSProcessor(logger, "../etc/webservice.cfg", "../etc/database.xml");
        if (Common.iLoadConfig) {
            Common.listConfig = db.getConfig("PROCESS");
            MessageResponse.setMessage(Common.listConfig);
            logger.info("LIST CONFIG:\n" + Common.listConfig);
//            Common.listProduct = db.iLoadPackage();
            Common.mapPrize = db.loadPrize(-1, -1);
            Common.padPrize.clear();
            Common.padPrize.addAll(db.loadPrize(-1, 1).values());
            Common.mapMpsConfig = db.loadMpsConfig();
            Common.loadConfig();
        }
    }

    @WebMethod(operationName = "wsLoginReport")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsLoginReport(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "password") String password,
            @WebParam(name = "username") String username) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsLoginReport Params:\n").
                    append("msisdn:").append(username);
            logger.info(br);

            Request request = new Request();
            request.setUser(username);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (username == null || username.length() == 0) {
                logger.info("username is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("username is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                    return response;
                } else {
                    // check existed
                    String msisdn = formatMsisdn(username);
                    AccountInfo accountInfo = db.iGetAccountByMsisdn(msisdn);
                    if (accountInfo == null) {
                        logger.error("Error check exist account: " + msisdn);
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                        response.setContent("Error database!");
                        return response;
                    } else if (accountInfo.getMsisdn() == null) {
                        logger.info("Account not existed username: " + msisdn);
                        response.setErrorCode(Common.ErrorCode.ACCOUNT_NOT_EXISTED);
                        response.setContent("Wrong account or password!");
                        return response;
                    }

                    String encyptedPassword = Encrypt.getHashCode(username, password, logger);

                    if (encyptedPassword.equals(accountInfo.getPassword())) {
                        // return
                        logger.info("Login success: " + username);
                        response.setErrorCode(Common.ErrorCode.SUCCESS);
//                        response.setRole(accountInfo.getRole());

                        return response;
                    } else {
                        logger.info("Login failed: " + username + ", encyptedPassword: " + encyptedPassword);
                        response.setErrorCode(Common.ErrorCode.AUTHENTICATE_FAIL);
                        String message = MessageResponse.getDefaultMessage(Common.Message.LOGIN_FAIL, logger);
                        response.setContent("".equals(message) ? "Authen failed!" : message);
                        return response;
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsReloadPrize")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsReloadPrize(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsReloadPrize:\n").append(wsUser);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    Common.mapPrize = db.loadPrize(-1, -1);
                    Common.padPrize.clear();
                    Common.padPrize.addAll(db.loadPrize(-1, 1).values());
                    List<PrizeObj> listPrize = new ArrayList();
                    listPrize.addAll(Common.mapPrize.values());
                    // return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                    response.setListPrize(listPrize);

                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsReloadMission")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsReloadMission(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsReloadPrize  :\n").append(wsUser);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    List<MissionObj> listMission = db.loadMission();
                    // return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                    response.setListMission(listMission);

                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsReloadAds")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsReloadAds(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsReloadAds  :\n").append(wsUser);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    List<AdsObj> listAds = db.loadAds();
                    // return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                    response.setListAds(listAds);

                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsGetSubInfo")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsGetSubInfo(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "msisdn") String msisdn) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsGetSubInfo Params:\n").
                    append("msisdn:").append(msisdn);
            logger.info(br);

            Request request = new Request();
            request.setUser(msisdn);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (msisdn == null || msisdn.length() == 0) {
                logger.info("msisdn is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("msisdn is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                msisdn = formatMsisdn(msisdn);
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                    return response;
                } else {
                    // check existed
//                    AccountInfo accountInfo = db.iGetAccountByMsisdn(msisdn);
//                    if (accountInfo == null) {
//                        logger.error("Error check exist account: " + msisdn);
//                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
//                        response.setContent("Error database!");
//                        return response;
//                    } else if (accountInfo.getMsisdn() == null) {
//                        logger.info("Account not existed msisdn: " + msisdn);
//                        response.setErrorCode(Common.ErrorCode.ACCOUNT_NOT_EXISTED);
//                        response.setContent(MessageResponse.get(Common.Message.ACCOUNT_NOT_EXISTED, logger));
//                        return response;
//                    }

                    // return
                    logger.info("Get info success: " + msisdn);
                    response.setErrorCode(Common.ErrorCode.SUCCESS);

                    // get reg_info
//                    List<RegisterInfo> listReg = db.getRegisterInfoAll(msisdn);
//                    if (listReg != null && listReg.size() > 0) {
//                        List<RegInfoWs> listRegWs = new ArrayList();
//                        RegisterInfo regInfo = listReg.get(0);
//                        RegInfoWs regWs = new RegInfoWs();
//                        regWs.setProductName(regInfo.getProductName());
//                        regWs.setRegisterTime(fullDf.format(regInfo.getRegisterTime()));
//                        regWs.setExpireTime(fullDf.format(regInfo.getExpireTime()));
//                        regWs.setPlayedTimes(regInfo.getPlayedTimes());
//                        regWs.setFreeQuestion(regInfo.getNumberSpin());
//                        regWs.setRemainQuestion(regInfo.getNumberSpin() - regInfo.getPlayedTimes());
//                        listRegWs.add(regWs);
//
//                        response.setListRegInfo(listRegWs);
//                    }
                    SpinTotalObj spinTotal = db.getSpin(msisdn);
                    response.setSpinTotal(spinTotal);

                    return response;

                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsPreSpin")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsPreSpin(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "account") String account,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "programId") String programId) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsPreSpin Params:\n").
                    append(" WsUser:").append(wsUser).
                    append("\n account:").append(account).
                    append("\n msisdn:").append(msisdn).
                    append("\n programId:").append(programId);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (account == null || account.length() == 0) {
                logger.info("account is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("account is null");
            } else if (programId == null || programId.length() == 0) {
                logger.info("programId is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("programId is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    // check current & remain spin
                    SpinTotalObj spinInfo = db.getSpin(msisdn);
                    if (spinInfo == null) {
                        logger.error("Error query spin: " + msisdn);
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                        return response;
                    }

                    //
                    logger.error("Spin info: " + msisdn
                            + ", normal spin: " + spinInfo.getSpinNum()
                            + ", gifted spin: " + spinInfo.getSpinGift()
                            + ", last action: " + spinInfo.getLastAction());
                    if (spinInfo.getSpinGift() + spinInfo.getSpinNum() <= 0) {
                        response.setErrorCode(Common.ErrorCode.NOMORE_SPIN);
                        return response;
                    }

                    // check last mission
                    if (spinInfo.getLastAction() != null
                            && (spinInfo.getLastAction().startsWith(Common.ActionCode.MISSION)
                            || spinInfo.getLastAction().startsWith(Common.ActionCode.INVITE))) {
                        // need do mission before spin 
                        logger.info("Need complete mission to continue: " + msisdn);
                        response.setErrorCode(Common.ErrorCode.MISSION_NOT_FISNISH);
                        return response;
                    }

                    // 
                    int isGift = 0;
                    if (spinInfo.getSpinGift() > 0) {
                        isGift = 1;
                    }
                    //
                    int prizeIndex = ran.nextInt(Common.listPrizePercent.size());
                    PrizeObj selectedPrize = Common.listPrizePercent.get(prizeIndex);
                    int money = 0;
                    // check prize
                    if (selectedPrize.getActionCode().split("_")[0].equals(Common.ActionCode.ADDMONEY)) {
                        money = Integer.parseInt(selectedPrize.getActionCode().split("_")[1]);
                    }

                    logger.info("Selected prize: " + msisdn + ", prize: " + selectedPrize.toString());
                    long playId = db.actionSpin(msisdn, selectedPrize.getActionCode(), isGift, selectedPrize.getId(), money);
                    if (playId > 0) {
                        logger.info("Spin success: " + msisdn + ", prize: " + selectedPrize.toString());
                        response.setErrorCode(Common.ErrorCode.SUCCESS);
                        response.setResultCode(selectedPrize.getId() + "");
                        response.setContent(selectedPrize.getPrizeName());
                        response.setPlayId(playId);
                    } else {
                        logger.info("Spin failed: " + msisdn + ", prize: " + selectedPrize.toString());
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsDoMission")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsDoMission(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "account") String account,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "actionCode") String actionCode,
            @WebParam(name = "programId") String programId) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsDoMission Params:\n").
                    append(" WsUser:").append(wsUser).
                    append("\n account:").append(account).
                    append("\n msisdn:").append(msisdn).
                    append("\n actionCode:").append(actionCode).
                    append("\n programId:").append(programId);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (account == null || account.length() == 0) {
                logger.info("account is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("account is null");
            } else if (programId == null || programId.length() == 0) {
                logger.info("programId is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("programId is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    // check current & remain spin
                    SpinTotalObj spinInfo = db.getSpin(msisdn);
                    if (spinInfo == null) {
                        logger.error("Error query spin: " + msisdn);
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                        return response;
                    }

                    //
                    logger.error("Spin info: " + msisdn
                            + ", normal spin: " + spinInfo.getSpinNum()
                            + ", gifted spin: " + spinInfo.getSpinGift()
                            + ", last action: " + spinInfo.getLastAction());

//                    if (!actionCode.equals(spinInfo.getLastAction())) {
//                        // need do mission before spin
//                        response.setErrorCode(Common.ErrorCode.MISSION_NOT_FISNISH);
//                        return response;
//                    }
                    // mark done 
                    db.completeMission(spinInfo.getMsisdn());
                    response.setErrorCode(Common.ErrorCode.SUCCESS);
                    return response;
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsStartPlay")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsStartPlay(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "fullname") String fullname,
            @WebParam(name = "age") String age,
            @WebParam(name = "showroom") String showroom,
            @WebParam(name = "programId") String programId) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsStartPlay  :").
                    append("\n msisdn").append(msisdn).
                    append("\n fullname").append(fullname).
                    append("\n age").append(age).
                    append("\n showroom").append(showroom).
                    append("\n programId").append(programId);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    long id = db.getSequence("user_play");
                    db.insertUserPlay(id, fullname, msisdn, age, showroom, Integer.parseInt(programId));
                    //return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                    response.setPlayId(id);
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsGetQuestion")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsGetQuestion(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "numQuestion") String numQuestion) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsGetQuestion  :\n").append(numQuestion);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    List<Questions> questionList = db.getRandomQuestion(Integer.parseInt(numQuestion));

                    // return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                    response.setListQuestion(questionList);

                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsAnswerQuestion")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsAnswerQuestion(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "playId") String playId,
            @WebParam(name = "questionId") String questionId,
            @WebParam(name = "correct") String correct) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsAnswerQuestion  :").
                    append("\n playId").append(playId).
                    append("\n questionId").append(questionId).
                    append("\n correct").append(correct);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    db.insertQuestionPlay(Long.parseLong(playId), Long.parseLong(questionId), Integer.parseInt(correct));
                    //return
                    response.setErrorCode(Common.ResultCode.SUCCESS);
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    // PRIZE
    @WebMethod(operationName = "wsConfirmPrize")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsConfirmPrize(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "account") String account,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "playId") String playId) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsConfirmPrize Params:\n").
                    append(" WsUser:").append(wsUser).
                    append("\n account:").append(account).
                    append("\n msisdn:").append(msisdn).
                    append("\n playId:").append(playId);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (account == null || account.length() == 0) {
                logger.info("account is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("account is null");
            } else if (playId == null || playId.length() == 0) {
                logger.info("playId is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("playId is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    logger.info("Confirm prize: " + msisdn + ", playId: " + playId);
                    boolean rs = db.confirmPrize(Long.parseLong(playId));
                    if (rs) {
                        response.setErrorCode(Common.ErrorCode.SUCCESS);
                    } else {
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsGetMyPrize")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsGetMyPrize(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "account") String account,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "isConfirmed") String isConfirmed,
            @WebParam(name = "page") String page,
            @WebParam(name = "rowOnPage") String rowOnPage) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsConfirmPrize Params:\n").
                    append(" WsUser:").append(wsUser).
                    append("\n account:").append(account).
                    append("\n msisdn:").append(msisdn).
                    append("\n isConfirmed:").append(isConfirmed).
                    append("\n page:").append(page).
                    append("\n rowOnPage:").append(rowOnPage);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (account == null || account.length() == 0) {
                logger.info("account is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("account is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    boolean confirmed = false;
                    if (isConfirmed != null && isConfirmed.equals("1")) {
                        confirmed = true;
                    }
                    List<UserPlayObj> result = db.getUserPrize(msisdn, confirmed, Integer.parseInt(page), Integer.parseInt(rowOnPage));

                    int totalRow = db.countUserPrize(msisdn, confirmed);
                    int totalPage = (int) Math.ceil((double) totalRow / Integer.parseInt(rowOnPage));
                    response.setTotalPage(totalPage);
                    response.setTotalRow(totalRow);

                    response.setListUserPrize(result);
                    response.setErrorCode(Common.ErrorCode.SUCCESS);
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsGetHuValue")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsGetHuValue(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsGetHuValue Params:\n").
                    append(" WsUser:").append(wsUser);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    long huValue = db.getHuValue();

                    response.setHuValue(huValue);
                    response.setErrorCode(Common.ErrorCode.SUCCESS);
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsUpdateHuValue")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsUpdateHuValue(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "huValue") String huValue) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsGetHuValue Params:\n").
                    append(" WsUser:").append(wsUser).
                    append(",huValue:").append(huValue);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            }

            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {
                    long val = Long.parseLong(huValue);

                    if (db.updateHuValue(val)) {
                        logger.info("Update hu success: " + huValue);
                        response.setErrorCode(Common.ErrorCode.SUCCESS);
                    } else {
                        response.setErrorCode(Common.ErrorCode.QUERY_ERROR);
                        logger.info("Update hu failed: " + huValue);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    // OTP CHARGE BUY TURN
    @WebMethod(operationName = "wsRequestOtp")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsRequestOtp(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "msisdn") String msisdn,
            @WebParam(name = "password") String password,
            @WebParam(name = "packgName") String packgName,
            @WebParam(name = "channel") String channel,
            @WebParam(name = "ServiceId") String serviceId,
            @WebParam(name = "action") String action) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);
//        Timestamp reqTime = new Timestamp(System.currentTimeMillis());

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsRequestOtp Params:\n").
                    append("User:").append(wsUser).
                    append("\n msisdn:").append(msisdn).
                    append("\n password:").append(password).
                    append("\n packgName:").append(packgName).
                    append("\n channel:").append(channel).
                    append("\n serviceId:").append(serviceId).
                    append("\n action:").append(action);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (msisdn == null || msisdn.length() == 0) {
                logger.info("msisdn is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("account is null");
            }
//            else if (packgName == null || packgName.length() == 0) {
//                logger.info("packgName is null");
//                response.setErrorCode(PARAM_NOT_ENOUGH);
//                response.setContent("packgName is null");
//            }

            if (SUCCESS.equals(response.getErrorCode())) {
                msisdn = formatMsisdn(msisdn);
                if (packgName != null) {
                    packgName = packgName.toUpperCase();
                }
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                    return response;
                } else {
                    // send OTP/password
                    String REQ = reqDf.format(new Date()) + "1" + msisdn.substring(Common.COUNTRY_CODE.length());

                    String otp = generateValidateCode(4);
                    long svId = Long.parseLong(serviceId);

                    // check wait confirm 
                    ConfirmOtpObj confirmOtp = db.getConfirmOtp(msisdn, svId, Integer.parseInt(action));
                    logger.info("Create otp " + msisdn);

                    // delete old confirm otp
                    if (confirmOtp != null) {
                        db.deleteConfirmOtp(confirmOtp);
                    }
                    // insert confirm otp (timeout 120s)
                    confirmOtp = new ConfirmOtpObj();
                    confirmOtp.setMsisdn(msisdn);
                    confirmOtp.setRequestId(REQ);
                    confirmOtp.setOtp(otp);
                    confirmOtp.setPassword(password);
                    confirmOtp.setProductName(packgName);
                    confirmOtp.setAction(Integer.parseInt(action));
                    confirmOtp.setSvId(svId);
                    confirmOtp.setExpireTime(new Timestamp(System.currentTimeMillis() + Common.OTP_TIMEOUT));
                    db.insertConfirmOtp(confirmOtp);

                    // insert activate account
                    //db.insertActiveAccount(msisdn, password, Common.ValidateCode.ACTIVE_CODE);
                    String message = "";
                    switch (confirmOtp.getAction()) {
                        default: {
                            // common case (charge)
                            message = MessageResponse.getDefaultMessage(Common.Message.GET_OTP_SUCCESS, logger);
                            message = message.replaceAll("%expire%", sdf.format(confirmOtp.getExpireTime()));
                            message = message.replaceAll("%otp%", otp);
                            SmsMtObj mt = new SmsMtObj();
                            mt.setMsisdn(msisdn);
                            mt.setChannel(Common.CHANNEL);
                            mt.setMessage(message);
                            db.insertMt(mt);
                            break;
                        }
                    }
                    response.setErrorCode(Common.ErrorCode.SUCCESS);
                    response.setContent(message);
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    @WebMethod(operationName = "wsBuyTurnConfirm")
    @SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
    public Response wsBuyTurnConfirm(
            @WebParam(name = "WsUser") String wsUser,
            @WebParam(name = "WsPass") String wsPass,
            @WebParam(name = "Msisdn") String msisdn,
            @WebParam(name = "ProductId") String productId,
            @WebParam(name = "Otp") String otp,
            @WebParam(name = "ServiceId") String serviceId) {

        Response response = new Response();
        response.setErrorCode(SUCCESS);

        try {
            String ip = getIpClient();
            UserInfo userInfo = null;
            br.setLength(0);
            br.append("wsBuyTurnConfirm Params:\n").
                    append("WsUser:").append(wsUser).
                    append("\n Msisdn:").append(msisdn).
                    append("\n productId:").append(productId);
            logger.info(br);

            Request request = new Request();
            request.setUser(wsUser);
//            request.setPass(password);
            if (wsUser == null || wsUser.length() == 0) {
                logger.info("wsUser is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsUser is null");
            } else if (wsPass == null || wsPass.length() == 0) {
                logger.info("wsPass is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("wsPass is null");
            } else if (msisdn == null || msisdn.length() == 0) {
                logger.info("msisdn is null");
                response.setErrorCode(PARAM_NOT_ENOUGH);
                response.setContent("msisdn is null");
            }
            if (SUCCESS.equals(response.getErrorCode())) {
                // kiem tra thong tin dang nhap
                userInfo = authenticate(wsUser, wsPass, ip);
                if (userInfo == null || userInfo.getWsUserId() <= 0) {
                    response.setErrorCode(WRONG_PASSWORD);
                    response.setContent("Authenticate fail!");
                } else {

                    String resultPaid = Common.ResultCode.SUCCESS;
                    int fee = Common.ADD_SPIN_FEE;
                    // check wait confirm
                    ConfirmOtpObj confirmOtp = db.getConfirmOtp(msisdn, Long.parseLong(serviceId), Common.ActionType.BUY_TURN);
                    if (confirmOtp == null || confirmOtp.getExpireTime().getTime() < System.currentTimeMillis()) {
                        // no wait confirm or timeout
                        response.setErrorCode(Common.ErrorCode.NOT_IN_TIME);
                        String message = MessageResponse.getDefaultMessage(Common.Message.TIMEOUT_CONFIRM_OTP, logger);
                        response.setContent(message);
                        return response;
                    }
                    if (!confirmOtp.getOtp().equals(otp)) {
                        // wrong otp
                        response.setErrorCode(Common.ErrorCode.WRONG_REGISTER_CODE);
                        String message = MessageResponse.getDefaultMessage(Common.Message.WRONG_OTP, logger);
                        response.setContent(message);
                        return response;
                    }
                    // charge
                    if (fee > 0) {
                        resultPaid = ws.chargeFee(msisdn, fee, "iLucky Buy turn");
                    }

                    if (WSProcessor.LIST_SUCCESS_RES.contains(resultPaid)) {
                        // delete confirm
                        db.deleteConfirmOtp(confirmOtp);

                        //insert chargelog
                        if (fee > 0) {
                            ChargeLog chargeLog = new ChargeLog();
                            chargeLog.setFee(fee);
                            chargeLog.setChargeTime(new Timestamp(System.currentTimeMillis()));
                            chargeLog.setMsisdn(msisdn);
                            chargeLog.setDescription("Buy turn");
                            chargeLog.setPackgName("BUY");
                            db.iInsertChargeLog(chargeLog);
                        }

                        // send message
                        String message = MessageResponse.getDefaultMessage(Common.Message.BUY_SPIN_UNSUB_SUCCESS, logger);
                        message = message.replaceAll("%fee%", fee + "");
                        SmsMtObj mt = new SmsMtObj();
                        mt.setMsisdn(msisdn);
                        mt.setChannel(Common.CHANNEL);
                        mt.setMessage(message);
                        db.insertMt(mt);

                        response.setErrorCode(Common.ErrorCode.SUCCESS);
                        response.setContent(message);
                    } else {
                        response.setErrorCode(Common.ErrorCode.NOT_ENOUGH_MONEY);
                        response.setContent("Charge fail");
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error processing", e);
            response.setErrorCode(EXCEPTION);
            response.setContent("Transaction fail");
        }

        return response;
    }

    private String generateValidateCode(int length) {
        String output = "";
        String input = "1234567890";
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            output = output + input.charAt(rnd.nextInt(input.length()));
        }
        return output;
    }

    private String formatMsisdn(String msisdn) {
        if (msisdn.startsWith(WebserviceManager.countryCode)) {
            return msisdn;
        } else if (msisdn.startsWith("0")) {
            return WebserviceManager.countryCode + msisdn.substring(1);
        }
        return WebserviceManager.countryCode + msisdn;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
//        MessageDigest md = null;
//        String seq = "123456a@" + "32323232";
//        md = MessageDigest.getInstance("SHA-256"); //step 2
//        md.update(seq.getBytes("UTF-8")); //step 3

//        byte raw[] = md.digest(); //step 4
//        String hash = (new BASE64Encoder()).encode(raw); //step 5
//        System.out.println("hash" + hash);
//        String pass = "1QAaz@123@";
//        Pattern pattern = Pattern.compile("abcdefghijklmnopqrstuvwxyz");
        //boolean match = list.matches(".");
//        System.out.println("hash " + pass.matches(".*[A-Z].*"));
//        String output = "";
//        String input = "1234567890";
//        Random rnd = new Random();
//        for (int i = 0; i < 8; i++) {
//            output = output + input.charAt(rnd.nextInt(input.length()));
//        }
//        System.out.println("output = " + output);
//        double x = 1.1;
//        System.out.println("output = " + (((x * 3 / 2) * 100) / 100));
//        System.out.println("output = " + (x * 3 / 2));
        int point = Integer.parseInt("116000.0");
        int money = point / 10;
        System.out.println("Poinnt " + point + ", money: " + money);
    }

    @Override
    public UserInfo authenticate(String userName, String password, String ipAddress) {
        // validate thong tin user
        // validate thong tin user
        UserInfo userInfo = db.iGetUser(userName);
        if (userInfo == null || userInfo.getWsUserId() <= 0) {
            logger.info("User not existed: " + userName);
            userInfo = new UserInfo();
            userInfo.setWsUserId(-1);
            return userInfo;
        }

        if (userInfo.getIp() != null && userInfo.getIp().length() > 0) {
            // Check IP
            String ip[] = userInfo.getIp().split(",");
            boolean pass = false;
            for (String ipConfig : ip) {
                if (pair(ipAddress, ipConfig.trim())) {
                    pass = true;
                    break;
                }
            }
            if (!pass) {
                logger.info("IP address not allowed: " + ipAddress);
                userInfo.setWsUserId(-2);
                return userInfo;
            }
        }
        // Check password
        if (userInfo.getPass() != null && userInfo.getPass().trim().length() > 0) {
            if (password.equals(userInfo.getPass())) {
                return userInfo;
            }

            String passEncript = "";
            passEncript = Encrypt.getHashCode(userName, password, logger);
            if (passEncript.equals(userInfo.getPass())) {
                return userInfo;
            }
            logger.info("Password incorrect: user=" + userName);
            userInfo.setWsUserId(-3);
            return userInfo;
        }
        return null;//tam fix
    }
}
