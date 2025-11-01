package com.jie.verify.mybatis.plugin;

import com.jie.verify.mybatis.context.DynamicTableContextUtil;
import com.jie.verify.mybatis.annotation.DynamicTableName;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author lizhenjie
 * @date 7/7/23 9:55 上午
 * 表名替换插件、实现动态表名
 */
@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Component
public class TableNameChangeInterceptor extends AbstractInterceptor {
    protected static final String DELEGATE_MAPPED_STATEMENT = "delegate.mappedStatement";
    protected static final String DELEGATE_BOUND_SQL_SQL = "delegate.boundSql.sql";
    protected static final String H_TARGET = "h.target";

    @Override
    public Object intercept(Invocation invocation) {
        try {
            StatementHandler statementHandler = realTarget(invocation.getTarget());
            MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
            MappedStatement mappedStatement = (MappedStatement) metaObject.getValue(DELEGATE_MAPPED_STATEMENT);
            Class<?> mapperClass = getMapperClass(mappedStatement);
            if (mapperClass.isAnnotationPresent(DynamicTableName.class)) {
                doChangeTableName(statementHandler, metaObject, mapperClass);
            }
            return invocation.proceed();
        }catch (Exception e) {
            throw new RuntimeException("分表失败");
        }
    }

    private void doChangeTableName(StatementHandler handler, MetaObject metaStatementHandler, Class<?> mapperClass) {
        DynamicTableName annotation = mapperClass.getAnnotation(DynamicTableName.class);
        BoundSql boundSql = handler.getBoundSql();
        String originalSql = boundSql.getSql();
        if (StringUtils.isNotEmpty(originalSql) && originalSql.contains(annotation.originName())) {
            String newTableName = generatorNewTableName(annotation.originName());
            String newSql = originalSql.replace(annotation.originName(), newTableName);
            metaStatementHandler.setValue(DELEGATE_BOUND_SQL_SQL, newSql);
        }
    }

    /**
     * 获取新表名
     *
     * @param originTableName
     * @return
     */
    public String generatorNewTableName(String originTableName) {
        return originTableName + "_" + DynamicTableContextUtil.getTableName();
    }

    protected static <T> T realTarget(Object target) {
        if (Proxy.isProxyClass(target.getClass())) {
            MetaObject metaObject = SystemMetaObject.forObject(target);
            return realTarget(metaObject.getValue(H_TARGET));
        }
        return (T) target;
    }

}
