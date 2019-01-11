package com.typeng.hydrology.utils;

import com.typeng.hydrology.enums.SpiderType;
import com.typeng.hydrology.model.SpiderObject;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Tianying Peng
 * @date 2019/1/11 16:04
 */
public class SpiderObjectUtil {

    /**
     * 获取爬虫构造对象
     *
     * @param type
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public static SpiderObject getSpiderObject(SpiderType type, LocalDate startDate, LocalDate endDate, LocalTime[] times) throws Exception {
        SpiderObject spiderObject = new SpiderObject();
        // 根据参数构造
        spiderObject.setUrl(type.getUrl());
        spiderObject.setStationNames(type.getStationNames());
        spiderObject.setRiverBasins(type.getRiverBasins());
        // 开始日期为空则默认从 1992-1-1 开始
        if (null == startDate) {
            spiderObject.setStartDate(LocalDate.of(1992,1,1));
        } else {
            spiderObject.setStartDate(startDate);
        }
        // 截止日期为空则默认截至当前日期
        if (null == endDate) {
            spiderObject.setEndDate(LocalDate.now());
        } else {
            spiderObject.setEndDate(endDate);
        }
        if (spiderObject.getStartDate().isAfter(spiderObject.getEndDate())) {
            throw new Exception("开始日期大于截止日期！");
        }
        // 时间为空则默认爬取 08:00 数据
        if (null == times || times.length <= 0) {
            LocalTime[] theTimes = {LocalTime.of(8, 0)};
            times = theTimes;
        }
        spiderObject.setTimes(times);
        return spiderObject;
    }

}
