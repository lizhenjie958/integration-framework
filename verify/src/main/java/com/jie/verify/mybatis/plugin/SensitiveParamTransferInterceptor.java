package com.jie.verify.mybatis.plugin;

import com.jie.verify.mybatis.annotation.EnableSensitiveDataAutoTransfer;
import com.jie.verify.mybatis.annotation.SensitiveField;
import io.micrometer.core.instrument.util.StringUtils;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.parameter.ParameterHandler;
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
import java.lang.reflect.Parameter;
import java.sql.PreparedStatement;
import java.util.Map;
import java.util.Objects;

/**
 * @author lizhenjie
 * @date 7/15/23 3:36 下午
 * 敏感数据转换拦截器
 */
@Intercepts({
        @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class)
})
@Component
public class SensitiveParamTransferInterceptor extends AbstractInterceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取拦截器拦截的设置参数对象DefaultParameterHandler
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();

        // 通过mybatis的反射来获取对应的值
        MetaObject metaResultSetHandler = SystemMetaObject.forObject(parameterHandler);
        MappedStatement mappedStatement = (MappedStatement) metaResultSetHandler.getValue("mappedStatement");
        Object parameterObject = metaResultSetHandler.getValue("parameterObject");
        MetaObject param = SystemMetaObject.forObject(parameterObject);
        Object originalObject = param.getOriginalObject();

        // id字段对应执行的SQL的方法的全路径，包含类名和方法名
        Class<?> mapperClass = super.getMapperClass(mappedStatement);
        // 基本类型单个参数，不支持数据转换
        if (!mapperClass.isAnnotationPresent(EnableSensitiveDataAutoTransfer.class)) {
            return invocation.proceed();
        }
        if (originalObject instanceof MapperMethod.ParamMap) {
            MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) originalObject;
            if (CollectionUtils.isEmpty(paramMap)) {
                return invocation.proceed();
            }

            Method mapperMethod = getMapperMethod(mappedStatement);
            boolean needEncrypt = false;
            for (Parameter parameter : mapperMethod.getParameters()) {
                boolean annotationPresent = parameter.isAnnotationPresent(SensitiveField.class);
                if (annotationPresent) {
                    String paramName = parameter.getName();
                    Object paramValue = paramMap.get(paramName);
                    if (Objects.nonNull(paramValue) && paramValue instanceof String) {
                        String realValue = (String) paramValue;
                        if (StringUtils.isNotBlank(realValue)) {
                            paramMap.put(paramName, super.doEncrypt(realValue));
                        }
                    }
                    needEncrypt = true;
                }
            }
            if (needEncrypt) {
                originalObject = paramMap;
                metaResultSetHandler.setValue("parameterObject", parameterObject);
            }
            return invocation.proceed();
        }
        if (!isDaoPackage(originalObject.getClass())) {
            return invocation.proceed();
        }
        Map<String, Field> sensitiveFiledMap = super.getSensitiveFiledMap(originalObject.getClass());
        if (CollectionUtils.isEmpty(sensitiveFiledMap)) {
            return invocation.proceed();
        }

        // 解密
        for (Map.Entry<String, Field> stringStringEntry : sensitiveFiledMap.entrySet()) {
            String filedName = stringStringEntry.getKey();
            Object value = param.getValue(filedName);
            if (Objects.nonNull(value) && value instanceof String) {
                String realValue = (String) value;
                if (StringUtils.isNotBlank(realValue)) {
                    param.setValue(filedName, super.doEncrypt(realValue));
                }
            }
        }
        metaResultSetHandler.setValue("parameterObject", parameterObject);
        return invocation.proceed();
    }

}
