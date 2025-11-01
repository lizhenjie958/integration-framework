package com.jie.verify.mybatis.handler;

import com.jie.verify.mybatis.model.UserConfigDO;

/**
 * @author: lizhenjie
 * @date:2025/6/3
 */
public class UserConfigJsonTypeHandler extends JsonTypeHandler<UserConfigDO> {
    @Override
    public Class<UserConfigDO> getClassType() {
        return UserConfigDO.class;
    }
}
