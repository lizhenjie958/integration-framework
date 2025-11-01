package com.jie.verify.mybatis.context;

/**
 * @author: lizhenjie
 * @date:2025/6/3
 */
public final class DynamicTableContextUtil {
    private static final ThreadLocal<String> TABLE_NAME_CONTEXT = new ThreadLocal<>();

    public static void setTableName(String tableName) {
        TABLE_NAME_CONTEXT.set(tableName);
    }

    public static String getTableName() {
        return TABLE_NAME_CONTEXT.get();
    }
}
