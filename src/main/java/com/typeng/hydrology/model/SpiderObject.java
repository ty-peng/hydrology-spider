package com.typeng.hydrology.model;

import com.typeng.hydrology.enums.RiverBasinEnum;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * 用于构造爬虫对象.
 *
 * @author Tianying Peng
 * @date 2018-12-25 21:01
 */
public class SpiderObject {

    /**
     * 爬取链接
     */
    private String url;
    /**
     * 流域
     */
    private RiverBasinEnum[] riverBasins;
    /**
     * 河名
     */
    private String[] riverNames;
    /**
     * 站名数组
     */
    private String[] stationNames;
    /**
     * 开始日期
     */
    private LocalDate startDate;
    /**
     * 截止日期
     */
    private LocalDate endDate;
    /**
     * 时间点
     */
    private LocalTime[] times;
    /**
     * 结果列表（一般不需要用到直接存入数据库）
     */
    private List<HydrologicalInfo> resultList;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public RiverBasinEnum[] getRiverBasins() {
        return riverBasins;
    }

    public void setRiverBasins(RiverBasinEnum[] riverBasins) {
        this.riverBasins = riverBasins;
    }

    public String[] getRiverNames() {
        return riverNames;
    }

    public void setRiverNames(String[] riverNames) {
        this.riverNames = riverNames;
    }

    public String[] getStationNames() {
        return stationNames;
    }

    public void setStationNames(String[] stationNames) {
        this.stationNames = stationNames;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime[] getTimes() {
        return times;
    }

    public void setTimes(LocalTime[] times) {
        this.times = times;
    }

    public List<HydrologicalInfo> getResultList() {
        return resultList;
    }

    public void setResultList(List<HydrologicalInfo> resultList) {
        this.resultList = resultList;
    }
}
