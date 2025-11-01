package com.jie.verify;

import com.jie.verify.mybatis.context.DynamicTableContextUtil;
import com.jie.verify.mybatis.mapper.UserInfoMapper;
import com.jie.verify.mybatis.model.UserInfoDO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class TestUseMybatis {

    @Resource
    private UserInfoMapper userInfoMapper;

    @Test
    public void testUserMapper(){
        DynamicTableContextUtil.setTableName("qxkj");
        UserInfoDO userInfoDO = userInfoMapper.selectOneByPhone("18595520444");
        System.err.println(userInfoDO);
    }
}
