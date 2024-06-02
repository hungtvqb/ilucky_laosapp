namespace LuckyWheelWebCore.Source
{
    public class GetMsisdnReq
    {
        public string username { get; set; }
        public string password { get; set; }
        public string uuid { get; set; }
    }

    public class DataRes
    {
        public string msisdn { get; set; }
        public string uuid { get; set; }
    }

    public class GetMsisdnResult
    {
        public int code { get; set; }
        public string desc { get; set; }
        public int errorCode { get; set; }
        public DataRes data { get; set; }
    }
}