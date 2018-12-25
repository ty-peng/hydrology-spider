package com.typeng.hydrology.utils;

import com.typeng.hydrology.enums.RiverBasinEnum;
import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.SpiderObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 爬虫.
 *
 * @author Tianying Peng
 * @date 2018-12-24 21:32
 */
public class Spider implements Runnable {

    private SpiderObject spiderObject;

    public Spider(SpiderObject spiderObject) {
        this.spiderObject = spiderObject;
    }

    public void run() {
        StringBuffer url = new StringBuffer(spiderObject.getUrl() + "?printflag=1");
        // 是否选择流域（默认湘江）
        // 带流域（全省查询）  不带流域（控制站查询）
        if (null != spiderObject.getRiverBasins() && spiderObject.getRiverBasins().length > 0) {
            for (RiverBasinEnum riverBasin : spiderObject.getRiverBasins()) {
                int riverBasinKey = riverBasin.getKey();
                url.append("&liuyu=" + riverBasinKey);
                try {
                    doSpider(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                doSpider(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * url 初始化.
     *
     * @param url
     * @return
     */
    public StringBuffer urlInit(StringBuffer url) throws Exception {
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
        return url;
    }

    /**
     * 爬虫解析获取数据.
     *
     * @param url
     */
    public void doSpider(StringBuffer url) throws Exception {

        urlInit(url);

        // 开始到截止日期迭代
        for (LocalDate date = spiderObject.getStartDate(); date.isBefore(spiderObject.getEndDate()); date = date.plusDays(1)) {
            url.append("&nian=" + date.getYear() + "&yue=" + date.getMonthValue() + "&ri=" + date.getDayOfMonth());
            for (LocalTime time : spiderObject.getTimes()) {
                url.append("&shi=" + time.getHour());
                // 解析页面获取数据
                try {
                    Document doc = Jsoup.connect(url.toString()).timeout(10000).get();
                    // 选择元素
                    Elements trs = doc.select("table tr");
                    // 不同url属性值选择器不一样
                    // 全省
                    if (url.toString().contains("hnsq_BB2.asp")) {
                        for (Element tr : trs) {
                            Elements tds = tr.select("td");
                            // 获取数据
                            String riverBasint = tds.eq(0).html().trim();
                            String riverName = tds.eq(1).html().trim();
                            String stationName = tds.eq(2).html().trim();
                            LocalTime checkTime = LocalTime.parse(tds.eq(3).html().trim());
                            Double waterLevel = Double.parseDouble(tds.eq(4).html().trim());
                            // index = 5 时为涨落箭头，跳过
                            String fluctuation = tds.eq(6).html().trim();
                            Integer flow = Integer.parseInt(tds.eq(7).html().trim());
                            // 填充数据
                            HydrologicalInfo hydrologicalInfo = fillHydrologicalInfo(
                                    riverName, stationName, time, waterLevel, fluctuation, flow);
                            hydrologicalInfo.setRiverBasin(riverBasint);
                            // TODO 存入数据库

                            System.out.println(Thread.currentThread().getName() + " ok ");
                        }
                        System.out.println(Thread.currentThread() + " over");
                        // 控制站
                    } else if (url.toString().contains("zykz_BB2.asp")) {
                        for (Element tr : trs) {
                            if (tr.equals(trs.first())) {
                                // Elements tds = tr.select("td p b");
                                continue;
                            }
                            Elements tdps = tr.select("td p");
                            // 获取数据
                            String riverName = tdps.eq(0).html().trim();
                            String stationName = tdps.eq(1).html().trim();
                            LocalTime checkTime = LocalTime.parse(tdps.eq(2).html().trim());
                            Double waterLevel = Double.parseDouble(tdps.eq(3).html().trim());
                            // index = 5 时为涨落箭头，跳过
                            String fluctuation = tdps.eq(4).html().trim();
                            Integer flow = Integer.parseInt(tdps.eq(5).html().trim());
                            // 填充数据
                            HydrologicalInfo hydrologicalInfo = fillHydrologicalInfo(
                                    riverName, stationName, time, waterLevel, fluctuation, flow);
                            // TODO 存入数据库

                            System.out.println(Thread.currentThread().getName() + " ok ");
                        }
                        System.out.println(Thread.currentThread() + " over");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 填充 HydrologicalInfo 对象.
     *
     * @param riverName
     * @param stationName
     * @param time
     * @param waterLevel
     * @param fluctuation
     * @param flow
     * @return
     */
    private HydrologicalInfo fillHydrologicalInfo(String riverName, String stationName, LocalTime time, Double waterLevel, String fluctuation, Integer flow) {
        HydrologicalInfo hydrologicalInfo = new HydrologicalInfo();
        hydrologicalInfo.setRiverName(riverName);
        hydrologicalInfo.setStationName(stationName);
        hydrologicalInfo.setTime(time);
        hydrologicalInfo.setWaterLevel(waterLevel);
        hydrologicalInfo.setFluctuation(fluctuation);
        hydrologicalInfo.setFlow(flow);
        return hydrologicalInfo;
    }
}
