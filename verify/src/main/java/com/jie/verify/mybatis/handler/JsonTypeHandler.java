package com.jie.verify.mybatis.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: lizhenjie
 * @date:2025/6/3
 */
public abstract class JsonTypeHandler<T> extends BaseTypeHandler<T> {

    abstract Class<T> getClassType();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {

    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        T t = JSONObject.parseObject(string, getClassType());
        return t;
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        T t = JSONObject.parseObject(string, getClassType());
        return t;
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        T t = JSONObject.parseObject(string, getClassType());
        return t;
    }
}
