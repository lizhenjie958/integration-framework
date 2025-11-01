package com.jie.verify.mybatis.model;

import com.jie.verify.mybatis.annotation.SensitiveField;
import lombok.Data;

import java.util.Date;

@Data
public class UserInfoDO {
    /**
     * 主键ID
     */
    private Long id;
    /**
     * 用户ID
     */
    private Long userId;

    @SensitiveField
    private String userName;

    @SensitiveField
    private String phone;

    @SensitiveField
    private String idNumber;

    private Date lastLoginTime;
    private Date registerTime;
    private Integer status;
    private Date gmtCreate;
    private Date gmtModify;

    /**
     * 微信openId
     */
    private String wxOpenId;
    /**
     * 用户数据
     */
    private UserDataDO userDataDO;
    /**
     * 用户配置
     */
    private UserConfigDO userConfigDataDO;
}