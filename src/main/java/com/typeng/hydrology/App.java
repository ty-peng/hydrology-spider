package com.typeng.hydrology;

import com.typeng.hydrology.model.HydrologicalInfo;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Tianying Peng
 * @date 2018-12-24 21:29
 */
public class App {

    public static void main(String[] args) throws IOException {

        String resource = "mybatis/mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String statement = "mapper.userMapper.getUser";

        HydrologicalInfo hydroInfo = new HydrologicalInfo();
        // TODO spider

        sqlSession.insert(statement, hydroInfo);


        sqlSession.close();
    }
}
