using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace LuckyWheelWebCore.Models
{
    public class JwtData
    {
        public String iss { get; set; }
        public String username { get; set; }
        public String msisdn { get; set; }
        public String language { get; set; }
        public String avatarUrl { get; set; }
        public bool unitel { get; set; }
        public long exp { get; set; }
    }
}