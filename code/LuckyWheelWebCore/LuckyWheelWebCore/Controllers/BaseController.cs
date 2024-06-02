using System;
using System.Collections.Generic;
using System.Data;
using System.Globalization;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Security.Cryptography;
using System.ServiceModel;
using System.Text;
using System.Threading.Tasks;
using LuckyWheelWebCore.Extensions;
using LuckyWheelWebCore.Source;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using Org.BouncyCastle.Crypto;
using Org.BouncyCastle.Crypto.Parameters;
using Org.BouncyCastle.Security;
using ServiceAPI;
using static LuckyWheelWebCore.Source.LuckyQuestionUtils;

namespace LuckyWheelWebCore.Controllers
{
    public class BaseController : Controller
    {

        private readonly log4net.ILog log = log4net.LogManager.GetLogger(typeof(Program));
        private readonly IWebHostEnvironment webHostEnvironment;

        protected IConfiguration configuration;

        public static String CountryCode = "";
        public static int numWs = 0;
        public static String FacebookAppId = "";
        public static String FacebookShareHref = "";
        public static String channel = "";
        public static String serviceId = "";
        public static String RootHref = "";
        public static String wsUser = "";
        public static String wsPassword = "";
        public static String wsUrl1 = "";
        public static int coin1Spin = 5;
        public static int testMode = 0;

        //public static String CountryCode = ConfigurationManager.AppSettings["countryCode"];
        //public static int numWs = int.Parse(ConfigurationManager.AppSettings["numWs"]);
        //public static String FacebookAppId = ConfigurationManager.AppSettings["FacebookAppId"];
        //public static String channel = ConfigurationManager.AppSettings["channel"];
        //public static String serviceId = ConfigurationManager.AppSettings["serviceId"];

        public BaseController() { }
        public BaseController(IConfiguration _configuration, IWebHostEnvironment hostEnvironment)
        {
            configuration = _configuration;
            webHostEnvironment = hostEnvironment;
            // load config
            CountryCode = configuration["countryCode"];
            numWs = int.Parse(configuration["numWs"]);
            FacebookAppId = configuration["FacebookAppId"];
            FacebookShareHref = configuration["FacebookShareHref"];
            channel = configuration["channel"];
            serviceId = configuration["serviceId"];
            wsUser = configuration["wsUser"];
            wsPassword = configuration["wsPassword"];
            wsUrl1 = configuration["wsUrl1"];
            RootHref = configuration["RootHref"];
            coin1Spin = int.Parse(configuration["coin1Spin"]);
            testMode = int.Parse(configuration["testMode"]);
        }

        public String GetParameter(String key)
        {
            return configuration.GetSection(key).Value;
        }

        public String GetParameter(String parentKey, String key)
        {
            var configs = configuration.GetSection(parentKey).GetChildren();
            foreach (IConfiguration config in configs)
            {
                if (config[key] != null)
                {
                    return config[key];
                }
            }
            return "";
        }

        public string RandomString(int size, bool lowerCase)
        {
            StringBuilder builder = new StringBuilder();
            Random random = new Random();
            char ch;
            for (int i = 0; i < size; i++)
            {
                ch = Convert.ToChar(Convert.ToInt32(Math.Floor(26 * random.NextDouble() + 65)));
                builder.Append(ch);
            }
            if (lowerCase)
                return builder.ToString().ToLower();
            return builder.ToString();
        }

        protected string convertToDateTimeServer(String date)
        {
            // date: 
            DateTime oDateFrom = DateTime.Parse(date);
            string hour = oDateFrom.Hour < 10 ? "0" + oDateFrom.Hour : oDateFrom.Hour.ToString();
            string minute = oDateFrom.Minute < 10 ? "0" + oDateFrom.Minute : oDateFrom.Minute.ToString();
            string second = oDateFrom.Second < 10 ? "0" + oDateFrom.Second : oDateFrom.Second.ToString();
            string month = oDateFrom.Month < 10 ? "0" + oDateFrom.Month : oDateFrom.Month.ToString();
            string day = oDateFrom.Day < 10 ? "0" + oDateFrom.Day : oDateFrom.Day.ToString();
            string fromCheck = day + "/" + month + "/" + oDateFrom.Year + " " + hour + ":" + minute + ":" + second;
            return fromCheck; //MM/dd/yyyy HH24:mm:ss
        }
        protected void CreateAuthToken(String account, Object userObj)
        {
            // create session authen
            // Create the random value we will use to secure the session.
            string authId = GenerateAuthId();

            // Store the value in both our Session and a Cookie.
            HttpContext.Session.SetString("AuthorizationCookieId", authId);
            string sessionValue = HttpContext.Session.GetString("AuthorizationCookieId");
            //CookieOptions option = new CookieOptions
            //{
            //    Expires = DateTime.Now.AddMinutes(1)
            //};
            //Response.Cookies.Append("Key Name", "Value", option);
            CookieOptions options = new CookieOptions()
            {
                //Path = "/",
                //HttpOnly = true,
                //Secure = false,
                //SameSite = SameSiteMode.None
                Expires = DateTime.Now.AddMinutes(60)
            };
            HttpContext.Response.Cookies.Append("AuthorizationCookie", authId, options);
            string cookieValue = HttpContext.Request.Cookies["AuthorizationCookie"];


            HttpContext.Session.SetString("account", account);
            HttpContext.Session.SetComplexData("userInfo", userObj);
        }

        protected bool ClearCache()
        {
            HttpContext.Session.Clear();
            foreach (var cookieKey in HttpContext.Request.Cookies.Keys)
            {
                HttpContext.Response.Cookies.Delete(cookieKey);
            }
            return true;
        }

        private string GenerateAuthId()
        {
            using (RandomNumberGenerator rng = new RNGCryptoServiceProvider())
            {
                byte[] tokenData = new byte[32];
                rng.GetBytes(tokenData);
                return Convert.ToBase64String(tokenData);
            }
        }

        public void SetWsClient(ref ServiceAPI.WsLuckyWheelClient wsClient, String sessionId)
        {
            int lastNum = sessionId[sessionId.Length - 1];
            int wsId = lastNum % numWs + 1;
            wsClient.Endpoint.Address = new EndpointAddress(configuration["wsUrl" + wsId]);
        }

        public String validateMsisdn(String input)
        {
            if (input == null || input.Length == 0)
            {
                return "";
            }

            // check is number
            try
            {
                input = long.Parse(input) + "";
            }
            catch
            {
                return "";
            }
            //
            if (input.StartsWith("0"))
            {
                input = CountryCode + input.Substring(1);
            }
            else if (!input.StartsWith(CountryCode))
            {
                input = CountryCode + input;
            }

            return input.Trim();
        }

        public static List<prizeObj> listPrize;
        public static List<missionObj> listMission;
        public static List<adsObj> listAds;

        public static List<prizeObj> ListPrize()
        {
            if (listPrize != null)
            {
                return listPrize;
            }
            String wsUser = BaseController.wsUser;
            String wsPassword = BaseController.wsPassword;
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            wsClient.Endpoint.Address = new EndpointAddress(wsUrl1);
            response res = wsClient.wsReloadPrize(wsUser, wsPassword);
            listPrize = res.listPrize.OrderBy(o => o.id).ToList();
            return listPrize;
        }

        public static List<adsObj> ListAds()
        {
            if (listAds != null)
            {
                return listAds;
            }
            String wsUser = BaseController.wsUser;
            String wsPassword = BaseController.wsPassword;
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            wsClient.Endpoint.Address = new EndpointAddress(wsUrl1);
            response res = wsClient.wsReloadAds(wsUser, wsPassword);
            listAds = res.listAds.OrderBy(o => o.id).ToList();
            return listAds;
        }

        public static Dictionary<int, prizeObj> MapPrize()
        {
            if (listPrize == null)
            {
                listPrize = ListPrize();
            }
            Dictionary<int, prizeObj> mapPrize = new Dictionary<int, prizeObj>();
            foreach (prizeObj prize in listPrize)
            {
                mapPrize.Add(prize.id, prize);
            }
            return mapPrize;
        }

        public static List<missionObj> ListMission()
        {
            if (listMission != null)
            {
                return listMission;
            }
            ServiceAPI.WsLuckyWheelClient wsClient = new ServiceAPI.WsLuckyWheelClient();
            wsClient.Endpoint.Address = new EndpointAddress(wsUrl1);
            response res = wsClient.wsReloadMissionAsync(wsUser, wsPassword).Result.@return;
            listMission = res.listMission.OrderBy(o => o.id).ToList();
            return listMission;
        }

        public static List<segment> ListSegments()
        {
            List<segment> result = new List<segment>();
            foreach (prizeObj prize in ListPrize())
            {
                result.Add(new segment(prize.prizeName));
            }
            return result;
        }

        public String IdentifyMsisdn()
        {
            try
            {
                if (testMode == 1)
                {
                    string msisdn1 = "8562091442617";
                    HttpContext.Session.SetString("account", msisdn1);
                    return msisdn1;
                }
                String msisdn = HttpContext.Session.GetString("account");
                String uuid = HttpContext.Request.Query["uuid"];
                if (msisdn == null || (uuid != null && uuid != "" && uuid != (HttpContext.Session.GetString("uuid"))))
                {
                    //if (uuid == null || uuid.Length == 0)
                    //{
                    //    return null;
                    //}
                    GetMsisdnReq req = new GetMsisdnReq();
                    req.username = configuration["mocha_user"];
                    req.password = configuration["mocha_pass"];
                    req.uuid = uuid;
                    string result = SendPostParam(log, req, "urlMochaMsisdn");
                    //GetMsisdnResult res = new GetMsisdnResult(result);
                    GetMsisdnResult res = JsonConvert.DeserializeObject<GetMsisdnResult>(result);
                    //
                    if (res != null && res.data != null && res.data.msisdn != null)
                    {
                        msisdn = validateMsisdn(res.data.msisdn);
                        HttpContext.Session.SetString("account", msisdn);
                        HttpContext.Session.SetString("uuid", uuid);
                        return msisdn;
                    }
                    else
                    {
                        return null;
                    }
                }
                return msisdn;

            }
            catch (Exception ex)
            {
                log.Error("Error identify msisdn: " + ex.Message, ex);
                return null;
            }
        }

        public String SendPostParam(log4net.ILog log, GetMsisdnReq obj, String type)
        {
            StringBuilder sb = new StringBuilder();
            sb.Append("Send post param " + type + ": ").Append("\n");
            sb.Append("\tRequest:" + JsonConvert.SerializeObject(obj)).Append("\n");
            String result = SendPostParam(obj, type);
            sb.Append("\tResponse:" + result);
            log.Info(sb);
            return result;
        }
        public String SendPostParam(GetMsisdnReq obj, String type)
        {
            //var json = JsonConvert.SerializeObject(obj);
            //var data = new StringContent(json, Encoding.UTF8, "application/json");

            var url = configuration[type] + AppendUrlParam(obj);
            using (var client = new HttpClient())
            {
                var response = client.PostAsync(url, null).Result;

                if (response.IsSuccessStatusCode)
                {
                    var responseContent = response.Content;

                    // by calling .Result you are synchronously reading the result
                    string responseString = responseContent.ReadAsStringAsync().Result;

                    return responseString;
                }
                return response.StatusCode.ToString();
            }
        }

        public String AppendUrlParam(Object obj)
        {
            Type type = obj.GetType();
            String append = "?";

            foreach (var f in type.GetProperties())
            {
                append += f.Name + "=" + f.GetValue(obj) + "&";
            }
            return append.Substring(0, append.Length - 1);
        }


        public string Decode(string token, bool verify = true)
        {
            string key = configuration["PublicKeyMocha"];
            string[] parts = token.Split('.');
            string header = parts[0];
            string payload = parts[1];
            byte[] crypto = Base64UrlDecode(parts[2]);

            string headerJson = Encoding.UTF8.GetString(Base64UrlDecode(header));
            JObject headerData = JObject.Parse(headerJson);

            string payloadJson = Encoding.UTF8.GetString(Base64UrlDecode(payload));
            JObject payloadData = JObject.Parse(payloadJson);

            if (verify)
            {
                var keyBytes = Convert.FromBase64String(key); // your key here

                AsymmetricKeyParameter asymmetricKeyParameter = PublicKeyFactory.CreateKey(keyBytes);
                RsaKeyParameters rsaKeyParameters = (RsaKeyParameters)asymmetricKeyParameter;
                RSAParameters rsaParameters = new RSAParameters();
                rsaParameters.Modulus = rsaKeyParameters.Modulus.ToByteArrayUnsigned();
                rsaParameters.Exponent = rsaKeyParameters.Exponent.ToByteArrayUnsigned();
                RSACryptoServiceProvider rsa = new RSACryptoServiceProvider();
                rsa.ImportParameters(rsaParameters);

                SHA256 sha256 = SHA256.Create();
                byte[] hash = sha256.ComputeHash(Encoding.UTF8.GetBytes(parts[0] + '.' + parts[1]));

                RSAPKCS1SignatureDeformatter rsaDeformatter = new RSAPKCS1SignatureDeformatter(rsa);
                rsaDeformatter.SetHashAlgorithm("SHA256");
                if (!rsaDeformatter.VerifySignature(hash, FromBase64Url(parts[2])))
                    throw new ApplicationException(string.Format("Invalid signature"));
            }

            return payloadData.ToString();
        }
        private byte[] FromBase64Url(string base64Url)
        {
            string padded = base64Url.Length % 4 == 0
                ? base64Url : base64Url + "====".Substring(base64Url.Length % 4);
            string base64 = padded.Replace("_", "/")
                                  .Replace("-", "+");
            return Convert.FromBase64String(base64);
        }

        // from JWT spec
        private string Base64UrlEncode(byte[] input)
        {
            var output = Convert.ToBase64String(input);
            output = output.Split('=')[0]; // Remove any trailing '='s
            output = output.Replace('+', '-'); // 62nd char of encoding
            output = output.Replace('/', '_'); // 63rd char of encoding
            return output;
        }

        // from JWT spec
        private byte[] Base64UrlDecode(string input)
        {
            var output = input;
            output = output.Replace('-', '+'); // 62nd char of encoding
            output = output.Replace('_', '/'); // 63rd char of encoding
            switch (output.Length % 4) // Pad with trailing '='s
            {
                case 0: break; // No pad chars in this case
                case 2: output += "=="; break; // Two pad chars
                case 3: output += "="; break; // One pad char
                default: throw new System.Exception("Illegal base64url string!");
            }
            var converted = Convert.FromBase64String(output); // Standard base64 decoder
            return converted;
        }
    }
}
