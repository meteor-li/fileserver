package com.lyb.fileserver.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 方式一：添加@Component或@WebFilter注解，以便将其识别为Spring Bean，并注册到应用程序上下文中。
 * 方式二：可以使用@WebFilter和 @Order 注解来声明和配置过滤器 @Order(1) @WebFilter(urlPatterns = "/*", filterName = "logFilter2")
 *        通过在Spring Boot的主配置类上添加@ServletComponentScan注解来启用注解扫描，以便识别并注册使用了@WebFilter注解的过滤器。
 */
@Component
public class UrlFilter implements Filter {

    @Value("${server.adminpassword}")
    private String adminpassword;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("初始化管理员密码："+adminpassword);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = req.getRequestURI();
        System.out.println("--------------------->过滤请求地址" + requestURI);
        String adminpass = (String) req.getSession().getAttribute("adminpass");
        if (adminpass == null || (!(adminpassword.equals(adminpass)))) {
            response.sendRedirect("/page/login");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    /**
     * Filter对象创建后会驻留在内存，当web应用移除或服务器停止时调用destroy()方法进行销毁。在Web容器卸载 Filter 对象之前被调用。
     * destroy()方法在Filter的生命周期中仅执行一次。通过destroy()方法，可以释放过滤器占用的资源。
     */
    @Override
    public void destroy() {
        System.out.println("...................");
    }
}
