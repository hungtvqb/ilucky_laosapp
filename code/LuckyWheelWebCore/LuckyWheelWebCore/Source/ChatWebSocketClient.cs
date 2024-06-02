using log4net;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSockets.Client;
using WebSockets.Common;

namespace LuckyWheelWebCore.Source
{
    public class ChatWebSocketClient : WebSocketClient
    {
        private static ChatWebSocketClient _client;
        private ILog logger = LogManager.GetLogger("ChatWebSocketClient");

        public static ChatWebSocketClient GetInstance()
        {
            if (_client == null)
            {
                Init(true, new WebSocketLogger());
            }
            return _client;
        }

        public static void Init(bool noDelay, IWebSocketLogger logger)
        {
            _client = new ChatWebSocketClient(noDelay, logger);
        }

        public ChatWebSocketClient(bool noDelay, IWebSocketLogger logger) : base(noDelay, logger)
        {

        }

        public new void Send(string text)
        {
            logger.Info("Client send: " + text);
            byte[] buffer = Encoding.UTF8.GetBytes(text);
            base.Send(WebSocketOpCode.TextFrame, buffer);
        }
    }
}
