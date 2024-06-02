using log4net;
using LuckyWheelWebCore.Source;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using ServiceAPI;
using System;
using LuckyWheelWebCore.Extensions;
using LuckyWheelWebCore.Models;
using Microsoft.Extensions.Primitives;
using Newtonsoft.Json;

namespace LuckyWheelWebCore.Controllers
{
    public class HomeController : BaseController
    {

        private ILog log = LogManager.GetLogger("HomeController");

        public HomeController(IConfiguration _configuration, IWebHostEnvironment hostEnvironment) : base(_configuration, hostEnvironment)
        {
            // init
        }
        public IActionResult Index()
        {

            var account = IdentifyMsisdn();

            if (account == null)
            {
                return Redirect("/Home/NotSupport");
            }
            ReloadUserInfo(account);

            StringValues jwtData;
            HttpContext.Request.Headers.TryGetValue("jwt_data", out jwtData);
            if (!StringValues.IsNullOrEmpty(jwtData))
            {
                String jwt = jwtData[0];
                try
                {
                    string json = Decode(jwt);

                    log.Info("JWT data: " + json);

                    JwtData jwtObj = JsonConvert.DeserializeObject<JwtData>(json);
                    String msisdnJwt = validateMsisdn(jwtObj.msisdn);
                    if (!string.IsNullOrEmpty(msisdnJwt))
                    {
                        if (LuckyQuestionUtils.mapJwt.ContainsKey(msisdnJwt))
                        {
                            LuckyQuestionUtils.mapJwt.Remove(msisdnJwt);
                        }
                        LuckyQuestionUtils.mapJwt.Add(msisdnJwt, jwtObj);
                    }
                }
                catch (Exception ex)
                {
                    log.Error("Error parse jwt data: " + jwt, ex);
                }
            }

            return View();
        }

        public IActionResult Login()
        {
            return View();
        }

        public IActionResult Logout()
        {
            try
            {
                ClearCache();
                return Redirect("/Home/Login");
            }
            catch (Exception ex)
            {
                log.Error("Exception " + ex);
                return Redirect("/Shared/Error");
            }
        }

        public IActionResult NotSupport()
        {
            return View();
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public JsonResult LoginShowroom(String username, String password)
        {
            username = validateMsisdn(username);
            String wsUser = configuration["wsUser"];
            String wsPassword = configuration["wsPassword"];
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, username);
            var res = wsClient.wsLoginReport(wsUser, wsPassword, password, username);

            if (res.errorCode == "0")
            {
                HttpContext.Session.SetString("account", username);

                ReloadUserInfo(username);

                return Json(new
                {
                    error = "0",
                    content = "Success",
                    href = "/Home/Index"
                });
            }
            else
            {
                return Json(new
                {
                    error = res.errorCode,
                    content = res.content
                });
            }
            //return Json(new
            //{
            //    error = "0",
            //    question = listQuestion[0],
            //    msisdn = msisdn.Substring(3)
            //});
        }

        private void ReloadUserInfo(String username)
        {
            // store session
            UserInfo userInfo = new UserInfo();
            userInfo.msisdn = username;
            userInfo.username = username;
            //userInfo.remain_spin = 3;

            String wsUser = configuration["wsUser"];
            String wsPassword = configuration["wsPassword"];
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, username);

            // get sub info
            response resSub = wsClient.wsGetSubInfo(wsUser, wsPassword, username);
            userInfo.spinTotal = resSub.spinTotal;
            userInfo.total_spin = userInfo.spinTotal.spinNum + userInfo.spinTotal.spinGift;
            //
            if (resSub.listRegInfo != null && resSub.listRegInfo.Length > 0)
            {
                userInfo.regInfo = resSub.listRegInfo[0];
                userInfo.total_spin = userInfo.total_spin + userInfo.regInfo.remainQuestion;
            }

            // store
            HttpContext.Session.SetComplexData("userInfo", userInfo);
        }

        public IActionResult LuckyDraw()
        {
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Redirect("/Home/Index");
            }

            //if (userInfo.remain_spin == 0)
            //{
            //    return Redirect("/Home/Prize");
            //}
            return View();
        }

        public IActionResult UserPrize(String confirmed, String page)
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Redirect("/Home/Index");
            }

            // get user prize
            page = page ?? "1";
            confirmed = confirmed ?? "0";
            String rowOnPage = configuration["pageSize"];

            String wsUser = configuration["wsUser"];
            String wsPassword = configuration["wsPassword"];
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, account);
            response res = wsClient.wsGetMyPrize(wsUser, wsPassword, account, userInfo.msisdn, confirmed, page, rowOnPage);

            // model
            LuckyWheelModel model = new LuckyWheelModel();
            model.confirmed = confirmed;
            model.page = page;
            model.pageSize = rowOnPage;
            model.totalPage = res.totalPage;
            model.totalRow = res.totalRow;
            model.listPrize = res.listUserPrize;
            return View(model);
        }


        public IActionResult Guide()
        {
            return View();
        }


        public IActionResult PrizeConfirm()
        {
            return View();
        }

        public IActionResult PrizeInfo([FromQuery(Name = "prizeId")] string prizeId)
        {
            PrizeObj model = new PrizeObj();
            model.prizeId = prizeId;
            return View(model);
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public JsonResult SpinAction()
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout",
                    href = "/Home/Index"
                });
            }
            if (userInfo.total_spin > 0)
            {
                String wsUser = configuration["wsUser"];
                String wsPassword = configuration["wsPassword"];
                ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
                SetWsClient(ref wsClient, account);
                response res = wsClient.wsPreSpin(wsUser, wsPassword, account, userInfo.msisdn, LuckyQuestionUtils.ProgramId + "");

                if (res.errorCode == "0")
                {

                    if (userInfo.spinTotal.spinGift > 0)
                    {
                        userInfo.spinTotal.spinGift--;
                    }
                    else
                    {
                        userInfo.spinTotal.spinNum--;
                    }
                    userInfo.total_spin--;
                    // check prize add spin
                    String prizeId = res.resultCode;
                    prizeObj prizeObj = ListPrize()[int.Parse(prizeId) - 1];
                    HttpContext.Session.SetComplexData("userInfo", userInfo);
                    // get total coin
                    //ChatWebSocketClient.GetInstance().Send("ADD:" + coin1Spin);
                    LuckyQuestionUtils.totalCoin += coin1Spin;

                    return Json(new
                    {
                        error = "0",
                        content = "Success",
                        prize = prizeObj,
                        remainSpin = userInfo.total_spin,
                        lastAction = prizeObj.actionCode,
                        playId = res.playId,
                        totalCoin = LuckyQuestionUtils.totalCoin
                    });
                }
                else
                {
                    return Json(new
                    {
                        error = res.errorCode,
                        content = res.content
                    });
                }
            }
            else
            {
                return Json(new
                {
                    error = "1",
                    content = "Out of spin"
                });
            }
        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public JsonResult BuyTurnOtp()
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout"
                });
            }

            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, account);
            response res = wsClient.wsRequestOtp(wsUser, wsPassword, account, null, null, channel, serviceId, LuckyQuestionUtils.ActionType.BUY_TURN);

            if (res.errorCode == "0")
            {
                return Json(new
                {
                    error = "0",
                    content = "Success"
                });
            }
            else
            {
                return Json(new
                {
                    error = res.errorCode,
                    content = res.content
                });
            }

        }

        [HttpPost]
        [ValidateAntiForgeryToken]
        public JsonResult BuyTurnConfirm(String otp)
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout"
                });
            }

            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, account);
            response res = wsClient.wsBuyTurnConfirm(wsUser, wsPassword, account, null, otp, serviceId);

            if (res.errorCode == "0")
            {
                return Json(new
                {
                    error = "0",
                    content = "Success"
                });
            }
            else
            {
                return Json(new
                {
                    error = res.errorCode,
                    content = res.content
                });
            }

        }

        [HttpPost]
        public IActionResult DoMission(String url, String actionCode)
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout",
                    href = "/Home"
                });
            }

            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, account);
            response res = wsClient.wsDoMission(wsUser, wsPassword, account, userInfo.msisdn, actionCode, LuckyQuestionUtils.ProgramId + "");
            log.Info("Do mission: " + userInfo.msisdn + ", " + actionCode + ", " + url);

            // return
            if (res.errorCode == "0")
            {
                return Json(new
                {
                    error = "0",
                    content = "Success",
                    href = url
                });
            }
            else
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout",
                    href = "/Home"
                });
            }
        }


        [HttpPost]
        public IActionResult ConfirmPrize(String playId)
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout",
                    href = "/Home"
                });
            }

            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            SetWsClient(ref wsClient, account);
            response res = wsClient.wsConfirmPrize(wsUser, wsPassword, account, userInfo.msisdn, playId);

            // return
            if (res.errorCode == "0")
            {
                return Json(new
                {
                    error = "0",
                    content = "Success"
                });
            }
            else
            {
                return Json(new
                {
                    error = res.errorCode,
                    content = res.content
                });
            }
        }


        [HttpPost]
        [ValidateAntiForgeryToken]
        public JsonResult RefreshHu()
        {
            String account = HttpContext.Session.GetString("account");
            UserInfo userInfo = HttpContext.Session.GetComplexData<UserInfo>("userInfo");
            if (userInfo == null)
            {
                return Json(new
                {
                    error = "-1",
                    content = "Timeout"
                });
            }

            // get total coin
            //ChatWebSocketClient.GetInstance().Send("GET");

            return Json(new
            {
                error = "0",
                totalCoin = LuckyQuestionUtils.totalCoin
            });

        }
    }
}
