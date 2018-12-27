package com.typeng.hydrology.utils;

import com.typeng.hydrology.dao.HnswDao;
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
import java.util.ArrayList;
import java.util.List;

/**
 * 爬虫.
 *
 * @author Tianying Peng
 * @date 2018-12-24 21:32
 */
public class Spider implements Runnable {

    private SpiderObject spiderObject;
    private List<HydrologicalInfo> hydrologicalInfoList = new ArrayList<>();
    private HnswDao hnswDao;

    public Spider(SpiderObject spiderObject) {
        this.spiderObject = spiderObject;
    }

    public void run() {
        String baseUrl = spiderObject.getUrl() + "?printflag=1";
        StringBuffer fullUrl;
        StringBuffer basinsUrl;
        // 是否选择流域（默认湘江）
        // 带流域（全省查询）  不带流域（控制站查询）
        if (null != spiderObject.getRiverBasins() && spiderObject.getRiverBasins().length > 0) {
            for (RiverBasinEnum riverBasin : spiderObject.getRiverBasins()) {
                int riverBasinKey = riverBasin.getKey();
                fullUrl = new StringBuffer(baseUrl);
                basinsUrl = new StringBuffer("&liuyu=").append(riverBasinKey);
                try {
                    doSpider(fullUrl.append(basinsUrl));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            fullUrl = new StringBuffer(baseUrl);
            try {
                doSpider(fullUrl);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 爬虫解析获取数据.
     *
     * @param theUrl run()方法里fullUrl的引用
     */
    private void doSpider(StringBuffer theUrl) {
        StringBuffer fullUrl = new StringBuffer(theUrl);
        StringBuffer url;
        StringBuffer timeUrl;
        // 开始到截止日期迭代
        for (LocalDate date = spiderObject.getStartDate(); date.isBefore(spiderObject.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            url = new StringBuffer().append("&nian=").append(date.getYear())
                    .append("&yue=").append(date.getMonthValue())
                    .append("&ri=").append(date.getDayOfMonth());
            for (LocalTime time : spiderObject.getTimes()) {
                timeUrl = new StringBuffer().append("&shi=").append(time.toString());
                // 解析页面获取数据
                try {
                    Document doc = Jsoup.connect(fullUrl.append(url).append(timeUrl).toString()).timeout(10000).get();
                    // 选择元素
                    Elements trs = doc.select("table tr");
                    if (fullUrl.toString().contains("hnsq_BB2.asp")) {    // 全省页面
                        // 遍历每一行
                        for (Element tr : trs) {
                            // 获取每一列
                            Elements tds = tr.select("td");
                            // 对应数据元素位置索引数组
                            int[] index = {1, 2, 3, 4, 6, 7};
                            // 获取填充
                            HydrologicalInfo hydrologicalInfo = getAndFillData(tds, index);
                            if (null == hydrologicalInfo) continue;
                            hydrologicalInfo.setDate(date);
                            hydrologicalInfoList.add(hydrologicalInfo);
                        }
                    } else if (fullUrl.toString().contains("zykz_BB2.asp")) {   // 控制站页面
                        for (Element tr : trs) {
                            // 跳过首行标题
                            if (tr.equals(trs.first())) {
                                // Elements tds = tr.select("td p b");
                                continue;
                            }
                            Elements tdps = tr.select("td p");
                            int[] index = {0, 1, 2, 3, 4, 5};
                            HydrologicalInfo hydrologicalInfo = getAndFillData(tdps, index);
                            if (null == hydrologicalInfo) continue;
                            hydrologicalInfo.setDate(date);
                            hydrologicalInfoList.add(hydrologicalInfo);
                        }
                    }
                    fullUrl = new StringBuffer(theUrl);
                    // TODO 存入数据库
                    // HnswDao
                    for (HydrologicalInfo hydrologicalInfo : hydrologicalInfoList) {
                        System.out.println(hydrologicalInfo.getStationName() + hydrologicalInfo.getDate().toString());
                    }
                    System.out.println(Thread.currentThread() + " over");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    /**
     * 获取 html 中的数据并填充对象.
     *
     * @param elems
     * @param index
     * @return
     */
    private HydrologicalInfo getAndFillData(Elements elems, int[] index) {
        // 获取数据
        // 无流域信息
        int i = 0;
        String riverName = elems.eq(index[i++]).html().replace((char) 12288, ' ').trim();
        String stationName = elems.eq(index[i++]).html().replace((char) 12288, ' ').trim();
        String tempStr = elems.eq(index[i++]).html().replace((char) 12288, ' ').trim();
        LocalTime checkTime = null;
        if (!isStrBlank(tempStr)) {
            checkTime = LocalTime.parse(tempStr);
        }
        tempStr = elems.eq(index[i++]).html().replace((char) 12288, ' ').replace(",", "").trim();
        Double waterLevel = null;
        if (!isStrBlank(tempStr)) {
            waterLevel = Double.parseDouble(tempStr);
        }
        String fluctuation = elems.eq(index[i++]).html().replace((char) 12288, ' ').trim();
        tempStr = elems.eq(index[i++]).html().replace((char) 12288, ' ').replace(",", "").trim();
        Integer flow = null;
        if (!isStrBlank(tempStr)) {
            flow = Integer.parseInt(tempStr);
        }
        // 填充数据
        HydrologicalInfo hydrologicalInfo = new HydrologicalInfo();
        hydrologicalInfo.setRiverName(riverName);
        hydrologicalInfo.setStationName(stationName);
        hydrologicalInfo.setTime(checkTime);
        hydrologicalInfo.setWaterLevel(waterLevel);
        hydrologicalInfo.setFluctuation(fluctuation);
        hydrologicalInfo.setFlow(flow);
        // 未指定站名
        if (null == spiderObject.getStationNames() || spiderObject.getStationNames().length < 1) {
            return hydrologicalInfo;
        }
        // 指定站名
        for (String name : spiderObject.getStationNames()) {
            if (hydrologicalInfo.getStationName().equals(name)) {
                return hydrologicalInfo;
            }
        }
        return null;
    }



    /**
     * 检查字符串是否为空.
     *
     * @param tempStr
     * @return
     */
    private boolean isStrBlank(String tempStr) {
        return tempStr.equals("") || tempStr.length() <= 0;
    }

}
