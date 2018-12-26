package com.typeng.hydrology.model.qo;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Tianying Peng
 * @date 2018-12-26 22:32
 */
public class HydrologicalInfoQo {
    /**
     * id
     */
    private Integer infoId;
    /**
     * 流域
     */
    private String riverBasin;
    /**
     * 河名
     */
    private String riverName;
    /**
     * 站名
     */
    private String stationName;
    /**
     * 查询日期（时间）
     */
    private LocalDate date;
    /**
     * 测量时间
     */
    private LocalTime time;

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

    public String getRiverBasin() {
        return riverBasin;
    }

    public void setRiverBasin(String riverBasin) {
        this.riverBasin = riverBasin;
    }

    public String getRiverName() {
        return riverName;
    }

    public void setRiverName(String riverName) {
        this.riverName = riverName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
