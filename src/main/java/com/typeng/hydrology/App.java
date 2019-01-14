package com.typeng.hydrology;

import com.typeng.hydrology.enums.RiverBasinEnum;
import com.typeng.hydrology.enums.SpiderType;
import com.typeng.hydrology.utils.Spider;
import com.typeng.hydrology.utils.SpiderObjectUtil;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Tianying Peng
 * @date 2018-12-24 21:29
 */
public class App {

    public static void main(String[] args) throws Exception {

        int threads = 8;
        // 开始和截止日期
        LocalDate startDate = LocalDate.of(2019, 1, 1);
        LocalDate endDate = LocalDate.now();
        // 计算每个线程爬取的天数
        long days = endDate.toEpochDay() - startDate.toEpochDay() + 1;
        long intervalDays = days / threads;
        // 创建线程池
        if (threads > days) {
            threads = (int) days;
        }
        // 两个任务同时执行时记得改线程数，间隔天数和threads有关，注意其中关联
        ExecutorService pool = Executors.newFixedThreadPool(threads * 2);
        // 给每个线程分配日期段任务
        // 类型一爬取全省页面可设置流域
        RiverBasinEnum[] riverBasins = {RiverBasinEnum.DONG_TING_HU};
        for (LocalDate start = startDate, end; start.isBefore(endDate.plusDays(1)); start = end.plusDays(1)) {
            if (start.plusDays(intervalDays).isAfter(endDate)) {
                end = endDate;
            } else {
                end = start.plusDays(intervalDays);
            }
            pool.execute(new Spider(SpiderObjectUtil.getSpiderObject(SpiderType.TYPE_2, start, end, null)));
            pool.execute(new Spider(SpiderObjectUtil.getSpiderObject(SpiderType.TYPE_1.setRiverBasins(riverBasins), start, end, null)));
        }
        // 销毁线程池
        pool.shutdown();
    }

}
