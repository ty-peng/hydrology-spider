package com.typeng.hydrology;

import com.typeng.hydrology.enums.RiverBasinEnum;
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

        App app = new App();
        int threads = 8;

        LocalDate startDate = LocalDate.of(2019, 1, 5);
        LocalDate endDate = LocalDate.now();


        long days = endDate.toEpochDay() - startDate.toEpochDay();
        long intervalDays = days / threads;
        LocalDate start = startDate;
        LocalDate end = start.plusDays(intervalDays);

        if (days < threads) {
            threads = (int) days;
        }
        ExecutorService pool = Executors.newFixedThreadPool(threads);

        for (int i = 0; i < threads; i++) {
            pool.execute(new Spider(app.toSpider(2, start, end)));
            start = end.plusDays(1);
            if (start.plusDays(intervalDays).isAfter(LocalDate.now())) {
                end = LocalDate.now();
            } else {
                end = start.plusDays(intervalDays);
            }

        }

        pool.shutdown();
    }

    public SpiderObject toSpider(int type, LocalDate startDate, LocalDate endDate) throws Exception {
        SpiderObject spiderObject;
        String url;
        String[] stationNames;
        if (1 == type) {
            url = "http://61.187.56.156/wap/hnsq_BB2.asp";
            RiverBasinEnum[] riverBasins = {RiverBasinEnum.DONG_TING_HU};
            String[] temp = {"新江口"};
            stationNames = temp;
            spiderObject = new SpiderObject(url, riverBasins ,stationNames, startDate, endDate);
        } else if (2 == type) {
            url = "http://61.187.56.156/wap/zykz_BB2.asp";
            String[] temp = {"沙道观", "弥陀寺", "藕池(康)", "藕池(管)", "石门", "桃源", "桃江", "湘潭", "城陵矶"};
            stationNames = temp;
            spiderObject = new SpiderObject(url, null, stationNames, startDate, endDate);
        } else {
            throw new Exception("爬取类型有误！");
        }
        // 开始日期为空则默认从 1992-1-1 开始
        if (null == spiderObject.getStartDate()) {
            spiderObject.setStartDate(LocalDate.parse("1992-1-1", DateTimeFormatter.ofPattern("yyyy-M-d")));
        }
        // 截止日期为空则默认截至当前日期
        if (null == spiderObject.getEndDate()) {
            spiderObject.setEndDate(LocalDate.now());
        } else {
            spiderObject.setEndDate(endDate);
        }
        if (spiderObject.getStartDate().isAfter(spiderObject.getEndDate())) {
            throw new Exception("开始日期大于截止日期！");
        }
        // 时间为空则默认爬取 08:00 数据
        if (null == spiderObject.getTimes() || spiderObject.getTimes().length <= 0) {
            LocalTime[] theTimes = {LocalTime.parse("08:00")};
            spiderObject.setTimes(theTimes);
        }

        return spiderObject;
    }

}
