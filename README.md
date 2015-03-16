# netty 集成 spring mvc

将netty的HttpRequest和HttpResponse对象与servlet的对象进行相互转换。我采用了springtest提供的用来模拟HttpServletRequest和HttpServletResponse的类，这两个类也是它们的子类。
org.springframework.mock.web.MockHttpServletRequest;
org.springframework.mock.web.MockHttpServletResponse;

#### 项目功能

- 支持spring mvc的controller中，注解方式获取参数
- 支持返回json
- 支持post请求

#### 如何测试

- 下载项目，启动org.springframework.sandbox.netty.MyServer
- 浏览器访问localhost:8080

#### 如何使用

- 在org.springframework.sandbox.mvc.TestController中，你可以修改自己的业务