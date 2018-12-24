package com.typeng.hydrology.model;

import java.sql.Time;
import java.util.Date;

/**
 * 水文信息.
 *
 * @author Tianying Peng
 * @date 2018-12-24 21:32
 */
public class HydrologicalInformation {

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
    private Date date;
    /**
     * 测量时间
     */
    private Time time;
    /**
     * 水位
     */
    private Double waterLevel;
    /**
     * 涨落
     */
    private String fluctuation;
    /**
     * 流量
     */
    private Integer flow;
    /**
     * 警戒水位
     */
    private Double warningWaterLevel;
    /**
     * 历史最高水位
     */
    private Double highestWaterLevel;
    /**
     * 最高水位发生日期
     */
    private Date dateOfHighest;
    /**
     * 所属地区
     */
    private String region;
    /**
     * 所在县
     */
    private String county;
    /**
     * 面积
     */
    private Integer area;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Double getWaterLevel() {
        return waterLevel;
    }

    public void setWaterLevel(Double waterLevel) {
        this.waterLevel = waterLevel;
    }

    public String getFluctuation() {
        return fluctuation;
    }

    public void setFluctuation(String fluctuation) {
        this.fluctuation = fluctuation;
    }

    public Integer getFlow() {
        return flow;
    }

    public void setFlow(Integer flow) {
        this.flow = flow;
    }

    public Double getWarningWaterLevel() {
        return warningWaterLevel;
    }

    public void setWarningWaterLevel(Double warningWaterLevel) {
        this.warningWaterLevel = warningWaterLevel;
    }

    public Double getHighestWaterLevel() {
        return highestWaterLevel;
    }

    public void setHighestWaterLevel(Double highestWaterLevel) {
        this.highestWaterLevel = highestWaterLevel;
    }

    public Date getDateOfHighest() {
        return dateOfHighest;
    }

    public void setDateOfHighest(Date dateOfHighest) {
        this.dateOfHighest = dateOfHighest;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }
}
