package com.eladmin.wechatmp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class EladminWechatMpApplication {

    public static void main(String[] args) {
        SpringApplication.run(EladminWechatMpApplication.class, args);
    }

}
