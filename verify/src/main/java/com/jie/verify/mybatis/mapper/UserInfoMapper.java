package com.jie.verify.mybatis.mapper;

import com.jie.verify.mybatis.annotation.DynamicTableName;
import com.jie.verify.mybatis.annotation.EnableSensitiveDataAutoTransfer;
import com.jie.verify.mybatis.annotation.SensitiveField;
import com.jie.verify.mybatis.model.UserInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
@DynamicTableName(originName = "qx_user_info")
@EnableSensitiveDataAutoTransfer
public interface UserInfoMapper {

    /**
     * 通过手机号查询
     *
     * @param phone 手机号
     * @return 用户信息
     */
    UserInfoDO selectOneByPhone(@Param("phone") @SensitiveField String phone);
}
