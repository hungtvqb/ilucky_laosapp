using Microsoft.AspNetCore.Mvc.Filters;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace LuckyWheelWebCore.Extensions
{
    public class ConfigAction : IAsyncActionFilter
    {
        private MyConfig _options;
        public ConfigAction(IConfiguration configuration)
        {

            _options = new MyConfig();
            configuration.Bind(_options);
        }

        public async Task OnActionExecutionAsync(ActionExecutingContext context, ActionExecutionDelegate next)
        {
            ((Microsoft.AspNetCore.Mvc.Controller)context.Controller).ViewBag.MyConfig = _options;
            await next();
        }
    }

    public class MyConfig
    {
        public string MyValue { get; set; }
    }
}
