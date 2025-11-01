package com.jie.verify.mybatis.use;

import com.alibaba.druid.pool.DruidDataSource;
import com.jie.verify.mybatis.plugin.ExecutorInterceptor;
import com.jie.verify.mybatis.plugin.SensitiveParamTransferInterceptor;
import com.jie.verify.mybatis.plugin.SensitiveResultTransferInterceptor;
import com.jie.verify.mybatis.plugin.TableNameChangeInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;

/**
 * @author: lizhenjie
 * @date:2025/6/4
 */
@Configuration
@MapperScan(basePackages = {"com.jie.verify.mybatis.mapper"})
public class SpringUseMybatisConfiguration {
    @Bean(name = "dataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return new DruidDataSource();
    }

    @Bean(name = "dataSqlSessionFactory")
    @Primary
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource ds, org.apache.ibatis.session.Configuration configuration) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

        Resource[] resources = resourcePatternResolver.getResources("classpath:/mapper/*.xml");
        bean.setMapperLocations(resources);
        bean.setDataSource(ds);
        bean.setPlugins(
                new ExecutorInterceptor(),
                new TableNameChangeInterceptor(),
                new SensitiveParamTransferInterceptor(),
                new SensitiveResultTransferInterceptor());
        bean.setConfiguration(configuration);
        return bean.getObject();
    }

    @Bean
    public org.apache.ibatis.session.Configuration configuration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        configuration.setLogImpl(org.apache.ibatis.logging.stdout.StdOutImpl.class);
        return configuration;
    }


    @Bean(name = "dataTransactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(@Qualifier("dataSource") DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }
}
