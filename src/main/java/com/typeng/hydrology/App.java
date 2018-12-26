package com.typeng.hydrology;

import com.typeng.hydrology.model.SpiderObject;
import com.typeng.hydrology.utils.Spider;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tianying Peng
 * @date 2018-12-24 21:29
 */
public class App {

    public static void main(String[] args) throws Exception {

        /*
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        String statement = "mapper.userMapper.getUser";

        HydrologicalInfo hydroInfo = new HydrologicalInfo();
        // TODO spider

        sqlSession.insert(statement, hydroInfo);


        sqlSession.close();*/

//        String url = "http://61.187.56.156/wap/hnsq_BB2.asp";
//        RiverBasinEnum[] riverBasins = {RiverBasinEnum.DONG_TING_HU};
//        String[] stationNames = {"新江口"};
//        SpiderObject spiderObject = new SpiderObject(url, riverBasins, stationNames, startDate);
        String url = "http://61.187.56.156/wap/zykz_BB2.asp";
        String[] stationNames = {"沙道观", "弥陀寺", "藕池(康)", "藕池(管)", "石门", "桃源", "桃江"};
        LocalDate startDate = LocalDate.of(2018, 12, 1);
        SpiderObject spiderObject = new SpiderObject(url, stationNames, startDate);
        // 开始日期为空则默认从 1992-1-1 开始
        if (null == spiderObject.getStartDate()) {
            spiderObject.setStartDate(LocalDate.parse("1992-1-1", DateTimeFormatter.ofPattern("yyyy-M-d")));
        }
        // 截止日期为空则默认截至当前日期
        if (null == spiderObject.getEndDate()) {
            spiderObject.setEndDate(LocalDate.now());
        }
        if (spiderObject.getStartDate().isAfter(spiderObject.getEndDate())) {
            throw new Exception("开始日期大于截止日期！");
        }
        // 时间为空则默认爬取 08:00 数据
        if (null == spiderObject.getTimes() || spiderObject.getTimes().length <= 0) {
            LocalTime[] theTimes = {LocalTime.parse("08:00")};
            spiderObject.setTimes(theTimes);
        }
        ExecutorService pool = Executors.newFixedThreadPool(1);

        pool.execute(new Spider(spiderObject));

        pool.shutdown();
    }
}
