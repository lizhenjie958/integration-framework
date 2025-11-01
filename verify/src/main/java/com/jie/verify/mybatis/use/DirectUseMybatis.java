package com.jie.verify.mybatis.use;

import com.jie.verify.mybatis.context.DynamicTableContextUtil;
import com.jie.verify.mybatis.plugin.TableNameChangeInterceptor;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author: lizhenjie
 * @date:2025/6/4
 */
public class DirectUseMybatis {
    public static void main(String[] args) throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("mybatis-config.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder()
                .build(resourceAsStream);
        DynamicTableContextUtil.setTableName("qxkj");
        sqlSessionFactory.getConfiguration().addInterceptor(new TableNameChangeInterceptor());
        SqlSession sqlSession = sqlSessionFactory.openSession();
        Object o = sqlSession.selectOne("com.jie.verify.mybatis.mapper.UserInfoMapper.selectOneByPhone", "ZSWVZHZO6w3CHVh7AB4JCw==");
        System.err.println(o);
    }
}
