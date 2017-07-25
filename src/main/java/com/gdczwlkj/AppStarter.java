package com.gdczwlkj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
/**
 * Created by franco.cheng on 2017/7/25.
 */
@EnableAutoConfiguration
@ComponentScan(basePackages={"com.gdczwlkj"})
@MapperScan("com.gdczwlkj.persistent.mapper")
public class AppStarter {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AppStarter.class, args);
    }

}
