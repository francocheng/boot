package com.gdczwlkj.config;

import java.util.ArrayList;
import java.util.List;

import com.gdczwlkj.persistent.entity.UserDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by franco.cheng on 2017/7/25.
 */
@Component
public class RedisCache {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    public String getName(){
        return stringRedisTemplate.opsForValue().get("10086");
    }


}