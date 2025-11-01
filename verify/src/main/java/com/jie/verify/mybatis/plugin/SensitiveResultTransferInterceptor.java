package com.jie.verify.mybatis.plugin;


import com.jie.verify.mybatis.annotation.EnableSensitiveDataAutoTransfer;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lizhenjie
 * @date 7/16/23 4:36 下午
 * 返回的结果集
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})
})
@Component
public class SensitiveResultTransferInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object returnValue = invocation.proceed();
        if (!(returnValue instanceof ArrayList<?>)) {
            return returnValue;
        }
        ResultSetHandler resultSetHandler = (ResultSetHandler) invocation.getTarget();
        MetaObject metaResultSetHandler = SystemMetaObject.forObject(resultSetHandler);
        MappedStatement mappedStatement = (MappedStatement) metaResultSetHandler.getValue("mappedStatement");
        Class<?> mapperClass = super.getMapperClass(mappedStatement);
        if (!mapperClass.isAnnotationPresent(EnableSensitiveDataAutoTransfer.class)) {
            return returnValue;
        }
        List<?> list = (ArrayList<?>) returnValue;
        if (CollectionUtils.isEmpty(list)) {
            return returnValue;
        }

        Method mapperMethod = super.getMapperMethod(mappedStatement);
        Class<?> returnItemClass = list.get(0).getClass();

        if (!super.isDaoPackage(returnItemClass)) {
            return returnValue;
        }
        Map<String, Field> sensitiveFiledMap = super.getSensitiveFiledMap(returnItemClass);
        if (CollectionUtils.isEmpty(sensitiveFiledMap)) {
            return returnValue;
        }

        for (Object returnItem : list) {
            MetaObject metaReturnItem = SystemMetaObject.forObject(returnItem);
            for (Map.Entry<String, Field> stringStringEntry : sensitiveFiledMap.entrySet()) {
                String filedName = stringStringEntry.getKey();
                Object fieldValue = metaReturnItem.getValue(filedName);
                // 只提供string类型自动转化
                if (Objects.nonNull(fieldValue) && fieldValue instanceof String) {
                    String realValue = (String) fieldValue;
                    if (StringUtils.isNotBlank(realValue)) {
                        String decrypt = super.doDecrypt((String) fieldValue);
                        metaReturnItem.setValue(filedName, decrypt);
                    }
                }
            }
        }
        return returnValue;
    }
}
