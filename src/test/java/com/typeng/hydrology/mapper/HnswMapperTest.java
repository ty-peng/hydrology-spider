package com.typeng.hydrology.mapper;

import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.qo.HydrologicalInfoQo;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础测试类
 *
 * @author Tianying Peng
 * @date 2019/1/9 17:03
 */
public class HnswMapperTest extends BaseMapperTest{

    @Test
    public void testSelectById() {
        try(SqlSession sqlSession = getSqlSession()) {
            // 获取 HnswMapper 接口
            HnswMapper hnswMapper = sqlSession.getMapper(HnswMapper.class);
            // 调用方法
            HydrologicalInfo info = hnswMapper.selectById(1);
            // 判断不为空
            //Assert.assertNotNull(info);
            // 其他判断正误
            //Assert.assertEquals("测试河", info.getRiverName());
            //Assert.assertEquals("测试站", info.getStationName());
        }
    }

    @Test
    public void testSelectByQo() {
        try(SqlSession sqlSession = getSqlSession()) {
            // 获取 HnswMapper 接口
            HnswMapper hnswMapper = sqlSession.getMapper(HnswMapper.class);
            HydrologicalInfoQo qo = new HydrologicalInfoQo();
            List<String> riverNames = new ArrayList<String>();
            //riverNames.add("测试河");
            //riverNames.add("沅水");
            qo.setRiverNames(riverNames);
            List<String> stationNames = new ArrayList<String>();
            //stationNames.add("测试站");
            //stationNames.add("桃江");
            qo.setStationNames(stationNames);
            // 调用方法
            List<HydrologicalInfo> infos = hnswMapper.selectByQo(qo);
            // 判断不为空
            //Assert.assertNotNull(infos);
            // 其他判断正误
            printHydrologicalInfos(infos);
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            // 获取 HnswMapper 接口
            HnswMapper hnswMapper = sqlSession.getMapper(HnswMapper.class);
            HydrologicalInfo info = new HydrologicalInfo();
            info.setStationName("测试站1");
            int result = hnswMapper.insertInfo(info);
            Assert.assertEquals(1, result);

        } finally {
            // 回滚不提交数据库，以免影响以后的测试结果
            sqlSession.rollback();
            // 默认不提交，要手动提交到数据库
            //sqlSession.commit();
            // 关闭 sqlSession
            sqlSession.close();
        }

    }

    @Test
    public void test() {
        StringBuffer str = new StringBuffer("&ri=1&shi=");
        str.append("8:00");
        System.out.println(str);
        str.delete(str.lastIndexOf("i=") + 2,str.length());
    }


    private void printHydrologicalInfos(List<HydrologicalInfo> hydrologicalInfos) {
        for (HydrologicalInfo info : hydrologicalInfos) {
            System.out.printf("%s  %s\n", info.getStationName(), info.getDate());
        }
    }


}
