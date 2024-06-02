using LuckyWheelWebCore.Extensions;
using Microsoft.AspNetCore.Authentication.Cookies;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using System;
using WebSockets.Events;
using LuckyWheelWebCore.Source;
using log4net;
using System.Threading;
using LuckyWheelWebCore.Controllers;

namespace LuckyWheelWebCore
{
    public class Startup
    {
        private ILog logger = LogManager.GetLogger("ThreadLog");
        //private WebSocketLogger _logger;
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;

            GetHu(configuration);
            //_logger = new WebSocketLogger(logger);
            // start client socket
            //Thread clientThread = new Thread(new ParameterizedThreadStart(SocketClient));
            //clientThread.IsBackground = false; 

            //clientThread.Start(configuration["socketServer"]);

            Thread clientThread = new Thread(new ParameterizedThreadStart(UpdateHu));
            clientThread.Start(configuration);
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            //services.AddControllersWithViews();
            services.AddControllersWithViews().AddRazorRuntimeCompilation();
            //services.AddTransient<ISuperAdminRepo, SuperAdminRepo>();
            services.AddSingleton<IConfiguration>(Configuration);
            services.AddMvc();
            //.SetCompatibilityVersion(CompatibilityVersion.Version_2_1)
            //.AddJsonOptions(options => options.SerializerSettings.ContractResolver = new DefaultContractResolver());
            services.AddAntiforgery(o => o.HeaderName = "XSRF-TOKEN");
            services.AddDistributedMemoryCache(); // Adds a default in-memory implementation of IDistributedCache
            services.AddSession(options =>
            {
                options.IdleTimeout = TimeSpan.FromSeconds(600);
                options.Cookie.HttpOnly = true;
                options.Cookie.IsEssential = true;
            });
            //services.AddSession();
            services.AddAuthentication(CookieAuthenticationDefaults.AuthenticationScheme).AddCookie();
            services.AddHttpContextAccessor();

            services.AddMvc(options =>
            {
                options.Filters.Add(new ConfigAction(
                    Configuration.GetSection("MyConfig")
                ));
            });
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
                // The default HSTS value is 30 days. You may want to change this for production scenarios, see https://aka.ms/aspnetcore-hsts.
                app.UseHsts();
            }
            app.UseSession();
            app.UseCookiePolicy();
            app.UseHttpsRedirection();
            app.UseStaticFiles();

            app.UseRouting();

            app.UseAuthorization();

            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllerRoute(
                    name: "default",
                    pattern: "{controller=Home}/{action=Index}/{id?}");
            });
        }

        private void UpdateHu(object state)
        {
            IConfiguration configuration = (IConfiguration)state;
            string wsUser = configuration["wsUser"];
            string wsPassword = configuration["wsPassword"];
            string wsUrl = configuration["wsUrl1"];
            ServiceAPI.WsLuckyWheelClient client = new ServiceAPI.WsLuckyWheelClient();
            client.Endpoint.Address = new System.ServiceModel.EndpointAddress(wsUrl);

            while (true)
            {
                Thread.Sleep(60000);
                string coinTotal = LuckyQuestionUtils.totalCoin + "";
                var res = client.wsUpdateHuValue(wsUser, wsPassword, coinTotal);
                if (res.errorCode == "0")
                {
                    logger.InfoFormat("Updated coin total: {0}", coinTotal);
                }
                else
                {
                    logger.ErrorFormat("Error update coin value total: {0}", coinTotal);
                }
            }
        }

        private void GetHu(IConfiguration configuration)
        {
            string wsUser = configuration["wsUser"];
            string wsPassword = configuration["wsPassword"];
            string wsUrl = configuration["wsUrl1"];
            ServiceAPI.WsLuckyWheelClient client = new ServiceAPI.WsLuckyWheelClient();
            client.Endpoint.Address = new System.ServiceModel.EndpointAddress(wsUrl);

            var res = client.wsGetHuValue(wsUser, wsPassword);
            if (res.errorCode == "0")
            {
                LuckyQuestionUtils.totalCoin = res.huValue;
                logger.InfoFormat("Loaded coin total: {0}", res.huValue);
            }
            else
            {
                logger.ErrorFormat("Error get coin value total: {0}", res.content);
                return;
            }
        }


        //public void SocketClient(object state)
        //{
        //    try
        //    {
        //        string url = (string)state;
        //        ChatWebSocketClient.Init(true, _logger);
        //        var client = ChatWebSocketClient.GetInstance();


        //        Uri uri = new Uri(url);
        //        client.TextFrame += Client_TextFrame;
        //        client.ConnectionOpened += Client_ConnectionOpened;

        //        // test the open handshake
        //        client.OpenBlocking(uri);


        //        _logger.Information(typeof(Program), "Client finished, press any key");
        //    }
        //    catch (Exception ex)
        //    {
        //        _logger.Error(typeof(Program), ex.ToString());
        //        _logger.Information(typeof(Program), "Client terminated: Press any key");
        //    }

        //    Console.ReadKey();
        //}

        //private void Client_ConnectionOpened(object sender, EventArgs e)
        //{
        //    logger.Info(typeof(Program) + " Client: Connection Opened");
        //    var client = (ChatWebSocketClient)sender;

        //    // test sending a message to the server
        //    client.Send("GET");
        //}

        //private void Client_TextFrame(object sender, TextFrameEventArgs e)
        //{
        //    logger.Info(typeof(Program) + " Client: " + e.Text);

        //    try
        //    {
        //        LuckyQuestionUtils.totalCoin = long.Parse(e.Text);
        //    }
        //    catch { }
        //    //var client = (ChatWebSocketClient)sender;
        //    // lets test the close handshake
        //    //client.Dispose();
        //}
    }
}
