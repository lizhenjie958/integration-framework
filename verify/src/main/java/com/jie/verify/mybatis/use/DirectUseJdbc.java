package com.jie.verify.mybatis.use;

import com.mysql.jdbc.Driver;
import org.apache.ibatis.io.Resources;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @author: lizhenjie
 * @date:2025/6/5
 */
public class DirectUseJdbc {
    public static void main(String[] args) throws Exception {
        Properties resourceAsProperties = Resources.getResourceAsProperties("application-dev.properties");
        String url = resourceAsProperties.getProperty("spring.datasource.url");
        String user = resourceAsProperties.getProperty("spring.datasource.username");
        String password = resourceAsProperties.getProperty("spring.datasource.password");
        String driverClassName = resourceAsProperties.getProperty("spring.datasource.driver-class-name");
        Class<?> clazz = Class.forName(driverClassName);
        Driver driver = (Driver) clazz.newInstance();
        DriverManager.registerDriver(driver);  // 注册驱动到DriverManager
        Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement preparedStatement = conn.prepareStatement("select * from qx_user_info_qxkj where user_id = ?");
        preparedStatement.setLong(1, 850403531777L);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            long aLong = resultSet.getLong("id");
            String string1 = resultSet.getString("user_name");
            String string = resultSet.getString("phone");
            String dataJson = resultSet.getString("user_data_json");
            System.err.println(aLong + " " + string1 + " " + string + " " + dataJson);
        }
        resultSet.close();
        conn.close();
    }
}
