package com.gdczwlkj.persistent.mapper;

import com.gdczwlkj.persistent.entity.UserDemo;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * Created by franco.cheng on 2017/7/25.
 */
public interface UserMapper {

    /*@Select("SELECT * FROM test_user WHERE id = #{id}")
    @Results({
        @Result(property = "id", column = "id"),
        @Result(property = "name", column = "name")
    })*/
    UserDemo getOne(Integer id);


}
