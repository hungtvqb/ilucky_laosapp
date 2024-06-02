using ServiceAPI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web; 

namespace LuckyWheelWebCore.Source
{
    public class UserInfo
    {
        public string msisdn { get; set; }
        public string username { get; set; }
        public int age { get; set; }
        public int current_question { get; set; }
        public int correct_question { get; set; }

        //public int remain_spin { get; set; }
        //public int gift_spin { get; set; }
        public int total_spin { get; set; }
        //public string last_action { get; set; }

        public long play_id { get; set; }
        //public questions[] list_question { get; set; }
        //public List<prizeObj> list_prize { get; set; } = new List<prizeObj>();
        public regInfoWs regInfo { get; set; }
        public spinTotalObj spinTotal { get; set; }
    }
}