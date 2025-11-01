package com.jie.verify.mybatis.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizhenjie
 * @date 8/24/23 11:45 上午
 */
@Data
public class UserDataDO implements Serializable {

    private static final long serialVersionUID = 5178183072486188715L;

    /**
     * 昵称
     */
    private String nickName;
}
