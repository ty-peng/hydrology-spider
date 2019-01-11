package com.typeng.hydrology.enums;

/**
 * @author Tianying Peng
 * @date 2019/1/11 15:44
 */
public enum SpiderType {
    TYPE_1("http://61.187.56.156/wap/hnsq_BB2.asp", new String[]{"新江口"}),
    TYPE_2("http://61.187.56.156/wap/zykz_BB2.asp", new String[]{"沙道观", "弥陀寺", "藕池(康)", "藕池(管)", "石门", "桃源", "桃江", "湘潭", "城陵矶"});

    private String url;
    private RiverBasinEnum[] riverBasins;
    private String[] stationNames;

    SpiderType(String url, String[] stationNames) {
        this.url = url;
        this.riverBasins = null;
        this.stationNames = stationNames;
    }

    public String getUrl() {
        return url;
    }

    public RiverBasinEnum[] getRiverBasins() {
        return riverBasins;
    }

    public String[] getStationNames() {
        return stationNames;
    }

    public SpiderType setRiverBasins(RiverBasinEnum[] riverBasins) {
        this.riverBasins = riverBasins;
        return this;
    }
}
