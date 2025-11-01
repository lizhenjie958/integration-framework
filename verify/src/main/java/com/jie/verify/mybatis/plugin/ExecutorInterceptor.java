package com.jie.verify.mybatis.plugin;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

/**
 * @author lizhenjie
 * @date 7/15/23 3:36 下午
 * 敏感数据转换拦截器
 */
@Intercepts({
        @Signature(
                type = Executor.class,
                method = "query",
                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
        ),
        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
@Component
public class ExecutorInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return invocation.proceed();
    }

}
