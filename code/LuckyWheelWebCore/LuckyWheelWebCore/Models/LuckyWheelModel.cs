using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using ServiceAPI;

namespace LuckyWheelWebCore.Models
{
    public class LuckyWheelModel
    {
        public userPlayObj[] listPrize { get; set; }
        public string page { get; set; } = "1";
        public string pageSize { get; set; } = "10";
        public int totalPage { get; set; }
        public int totalRow { get; set; }
        public string confirmed { get; set; }
    }
}
