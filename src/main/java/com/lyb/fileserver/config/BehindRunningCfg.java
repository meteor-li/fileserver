package com.lyb.fileserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

/**
 * Created by  on 22:37 2020/9/21.
 */
@Configuration
public class BehindRunningCfg implements CommandLineRunner {

    @Value("${root.path}")
    private String root_path;

    @Value("${server.adminpassword}")
    private String server_adminpassword;

    @Value("${server.port}")
    private String server_port;

    @Value("${funnyftp.maxfilesize}")
    private String funnyftp_maxfilesize;







    @Override
    public void run(String... args) {
        System.out.println("#################################################################################################");
        System.out.println("# 项目访问地址：http://localhost:"+server_port);
        System.out.println("# 文件管理的文件根目录（例如：/usr/local 或者 D://）："+root_path);
        System.out.println("# ###############################################################################################");
        System.out.println("# 管理密码："+server_adminpassword);
        System.out.println("# 文件下载最大文件配置："+funnyftp_maxfilesize);
        System.out.println("#################################################################################################");
    }
}
