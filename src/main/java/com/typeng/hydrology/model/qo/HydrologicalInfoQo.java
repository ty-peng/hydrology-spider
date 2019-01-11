package com.typeng.hydrology.model.qo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Tianying Peng
 * @date 2018-12-26 22:32
 */
public class HydrologicalInfoQo {
    /**
     * id
     */
    private List<Integer> infoIds;
    /**
     * 流域
     */
    private List<String> riverBasins;
    /**
     * 河名
     */
    private List<String> riverNames;
    /**
     * 站名
     */
    private List<String> stationNames;
    /**
     * 查询的起始日期
     */
    private LocalDate startDate;
    /**
     * 查询的截止日期
     */
    private LocalDate endDate;
    /**
     * 测量时间
     */
    private LocalTime time;

    public List<Integer> getInfoIds() {
        return infoIds;
    }

    public void setInfoIds(List<Integer> infoIds) {
        this.infoIds = infoIds;
    }

    public List<String> getRiverBasins() {
        return riverBasins;
    }

    public void setRiverBasins(List<String> riverBasins) {
        this.riverBasins = riverBasins;
    }

    public List<String> getRiverNames() {
        return riverNames;
    }

    public void setRiverNames(List<String> riverNames) {
        this.riverNames = riverNames;
    }

    public List<String> getStationNames() {
        return stationNames;
    }

    public void setStationNames(List<String> stationNames) {
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

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
