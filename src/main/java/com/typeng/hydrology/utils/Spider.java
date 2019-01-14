package com.typeng.hydrology.utils;

import com.typeng.hydrology.enums.RiverBasinEnum;
import com.typeng.hydrology.mapper.HnswMapper;
import com.typeng.hydrology.model.HydrologicalInfo;
import com.typeng.hydrology.model.SpiderObject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * 爬虫.
 *
 * @author Tianying Peng
 * @date 2018-12-24 21:32
 */
public class Spider implements Runnable {

    private static final Logger log = Logger.getLogger("spiderLogger");

    // 应用唯一
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 爬虫构建对象
    private SpiderObject spiderObject;

    public Spider(SpiderObject spiderObject) {
        this.spiderObject = spiderObject;
    }

    public void run() {
        StringBuffer url = new StringBuffer(spiderObject.getUrl());
        int index = url.length();
        for (LocalDate date = spiderObject.getStartDate(); date.isBefore(spiderObject.getEndDate().plusDays(1)); date = date.plusDays(1)) {
            for (LocalTime time : spiderObject.getTimes()) {
                url.append("?printflag=1&nian=").append(date.getYear())
                        .append("&yue=").append(date.getMonthValue())
                        .append("&ri=").append(date.getDayOfMonth())
                        .append("&shi=").append(time.toString());
                // 是否选择流域, 带流域（全省查询（默认湘江））, 不带流域（控制站查询）
                if (null != spiderObject.getRiverBasins() && spiderObject.getRiverBasins().length > 0) {
                    // 不同流域一次接口只能查询一个
                    for (RiverBasinEnum riverBasin : spiderObject.getRiverBasins()) {
                        int basin = riverBasin.getKey();
                        int index2 = url.length();
                        url.append("&liuyu=").append(basin);
                        try {
                            doSpider(url.toString(), date);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 重置额外请求路径
                        url.delete(index2, url.length());
                    }
                } else {
                    try {
                        doSpider(url.toString(), date);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 重置请求路径
                url.delete(index, url.length());
            }
        }

    }

    /**
     * 爬取页面
     *
     * @param url
     * @param date
     */
    private void doSpider(String url, LocalDate date) {
        // 每个线程一个 sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            Document doc = Jsoup.connect(url)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0")
                    .header("Connection", "keep-alive")
                    .timeout(10000)
                    .get();
            // 选择元素
            Elements trs = doc.select("table tr");
            // 遍历每一行
            for (Element tr : trs) {
                Elements elems;
                int[] index;
                if (url.contains("hnsq_BB2.asp")) {
                    // 获取每一列
                    elems = tr.select("td");
                    // 对应数据元素位置索引数组
                    index = new int[]{1, 2, 3, 4, 6, 7};
                } else if (url.contains("zykz_BB2.asp")) {
                    // 跳过首行标题
                    if (tr.equals(trs.first())) {
                        // Elements tds = tr.select("td p b");
                        continue;
                    }
                    elems = tr.select("td p");
                    index = new int[]{0, 1, 2, 3, 4, 5};
                } else {
                    break;
                }
                // 获取填充
                HydrologicalInfo hydrologicalInfo = getAndFillData(elems, index);
                if (null == hydrologicalInfo) continue;
                hydrologicalInfo.setDate(date);
                // 存入数据库
                // 获取 HnswMapper 接口
                HnswMapper hnswMapper = sqlSession.getMapper(HnswMapper.class);
                int result = hnswMapper.insertInfo(hydrologicalInfo);

            }
            log.info(date.toString() + " over");
        } catch (Exception e) {
            //e.printStackTrace();
            // 输出爬取错误的记录信息
            log.error("retry " + date.toString() + " [" + e.getMessage() + "] url: " + url);
            doSpider(url, date);
        } finally {
            sqlSession.close();
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
        String riverName = elems.eq(index[i++]).text().replace((char) 12288, ' ').trim();
        String stationName = elems.eq(index[i++]).text().replace((char) 12288, ' ').trim();
        String tempStr = elems.eq(index[i++]).text().replace((char) 12288, ' ').trim();
        LocalTime checkTime = null;
        if (isStrNotBlank(tempStr)) {
            checkTime = LocalTime.parse(tempStr);
        }
        tempStr = elems.eq(index[i++]).text().replace((char) 12288, ' ').replace(",", "").trim();
        Double waterLevel = null;
        if (isStrNotBlank(tempStr)) {
            waterLevel = Double.parseDouble(tempStr);
        }
        String fluctuation = elems.eq(index[i++]).text().replace((char) 12288, ' ').trim();
        tempStr = elems.eq(index[i++]).text().replace((char) 12288, ' ').replace(",", "").trim();
        Integer flow = null;
        if (isStrNotBlank(tempStr)) {
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
     * 检查字符串是否不为空.
     *
     * @param tempStr
     * @return
     */
    private boolean isStrNotBlank(String tempStr) {
        return tempStr != null && !tempStr.equals("") && tempStr.length() > 0;
    }

}
