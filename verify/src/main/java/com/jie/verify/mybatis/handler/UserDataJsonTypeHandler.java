package com.jie.verify.mybatis.handler;

import com.jie.verify.mybatis.model.UserDataDO;

/**
 * @author: lizhenjie
 * @date:2025/6/3
 */
public class UserDataJsonTypeHandler extends JsonTypeHandler<UserDataDO> {
    @Override
    public Class<UserDataDO> getClassType() {
        return UserDataDO.class;
    }
}
