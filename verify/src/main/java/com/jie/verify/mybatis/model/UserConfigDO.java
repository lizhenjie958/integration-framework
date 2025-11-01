package com.jie.verify.mybatis.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizhenjie
 * @date 8/24/23 11:44 上午
 */
@Data
public class UserConfigDO implements Serializable {

    private static final long serialVersionUID = -314764997201904894L;

    /**
     * 服务协议版本
     */
    private String userAgreementVersion;

    /**
     * 隐私协议版本
     */
    private String privacyPolicyVersion;
}
