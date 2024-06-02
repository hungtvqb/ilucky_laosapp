using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using WebSockets.Common;
using System.Diagnostics;
using log4net;

namespace LuckyWheelWebCore.Source
{
    internal class WebSocketLogger : IWebSocketLogger
    {
        private ILog logger;
        public WebSocketLogger()
        {
            logger = LogManager.GetLogger(typeof(WebSocketLogger));
        }

        public WebSocketLogger(ILog _logger)
        {
            this.logger = _logger;
        }

        public void Information(Type type, string format, params object[] args)
        {
            logger.InfoFormat(format);
        }

        public void Warning(Type type, string format, params object[] args)
        {
            logger.WarnFormat(format);
        }

        public void Error(Type type, string format, params object[] args)
        {
            logger.ErrorFormat(format, args);
        }

        public void Error(Type type, Exception exception)
        {
            Error(type, "{0}", exception);
        }
    }
}
