package com.gdczwlkj.controller;

import com.gdczwlkj.config.RedisCache;
import com.gdczwlkj.persistent.entity.UserDemo;
import com.gdczwlkj.persistent.mapper.UserMapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by franco.cheng on 2017/7/25.
 */
@RestController
public class DemoController {

    @Autowired
    RedisCache redisCache;

    @RequestMapping("/")
    String home() {
        return redisCache.getName();
    }

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/data")
    String data() {
        UserDemo userDemo = userMapper.getOne(2);
        return userDemo.getName();
    }

}
