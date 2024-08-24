package com.lyb.fileserver;
import com.lyb.fileserver.filters.UrlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
/**
 * Created by  on 15:11 2019/6/26.
 */
@Configuration
public class SpringConfig {

    /**
     * 编程方式：在Spring Boot的配置类中，使用FilterRegistrationBean来注册Filter。
     * @param urlFilter
     * @return
     */
    @Bean
    public FilterRegistrationBean<UrlFilter> filterFilterRegistrationBean(UrlFilter urlFilter) {
        //自定义过滤器
        FilterRegistrationBean<UrlFilter> filterFilterRegistrationBean = new FilterRegistrationBean<>();

        //拦截路径配置
        List<String> uriList = new ArrayList<>(10);
        uriList.add("/file/*");
        uriList.add("/page/info");
        uriList.add("/");
        //设置要注册的过滤器实例
        filterFilterRegistrationBean.setFilter(urlFilter);
        //启用过滤器
        filterFilterRegistrationBean.setEnabled(true);
        //设置过滤器拦截的url模式
        filterFilterRegistrationBean.setUrlPatterns(uriList);
        //设置过滤器名称
        filterFilterRegistrationBean.setName("baseFilter");
        //值越小，优先级越高
        filterFilterRegistrationBean.setOrder(1);
        //设置过滤器的初始化参数(这些初始化参数将在过滤器初始化时被传递，并可以在过滤器的init方法中获取。)
        filterFilterRegistrationBean.setInitParameters(new HashMap<>());
        return filterFilterRegistrationBean;
    }

}
