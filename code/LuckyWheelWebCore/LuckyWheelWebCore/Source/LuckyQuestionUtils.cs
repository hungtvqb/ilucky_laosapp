using LuckyWheelWebCore.Models;
using System;
using System.Collections.Generic;

namespace LuckyWheelWebCore.Source
{
    public class LuckyQuestionUtils
    {
        public static String ProgramId = "1";


        public static long totalCoin = 0;


        public static Dictionary<string, JwtData> mapJwt = new Dictionary<string, JwtData>();
        public class segment
        {
            public string text { get; set; }
            public string fillStyle { get; set; }
            public string textFontFamily { get; set; }
            public segment() { }
            public segment(string text)
            {
                this.text = text;
            }
        }

        public class Period
        {

            public static int DAILY = 1;
            public static int WEEKLY = 2;
            public static int MONTHLY = 3;
        }

        public class Status
        {

            public static int NOT_REDEEM = 0;
            public static int REDEEM = 1;
            public static int PROCESSED = 2;
        }

        public class Channel
        {

            public static int MOCHA = 1;
            public static int SMS = 2;
            public static int USSD = 3;
            public static int WEB = 4;
        }


        public class ActionType
        {
            public static String CHANGE_PASS = "5";
            public static String HELP = "10";
            public static String CREATE_ACCOUNT = "1";
            public static String REGISTER_SUB = "2";
            public static String BUY_TURN = "3";
        }


        public class ActionCode
        {
            public static String INVITE = "INVITE";
            public static String MISSION = "MISSION";
            public static String ADD = "ADD";
        }
    }
}