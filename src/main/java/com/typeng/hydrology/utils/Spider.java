package com.typeng.hydrology.utils;

import com.typeng.hydrology.model.HydrologicalInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;

/**
 * @author Tianying Peng
 * @date 2018-12-24 21:32
 */
public class Spider implements Runnable {

    private String url;
    private List<HydrologicalInfo> lists;

    public Spider(String url, String[] staa) {
        this.url = url;
        this.lists = lists;
    }

    public void run() {
        try {
            Document doc = Jsoup.connect(url).timeout(10000).get();

            // TODO change
            Elements elms = doc.select(".grid_view .item");
            for (Element e : elms) {
                HydrologicalInfo hydrologicalInfo = new HydrologicalInfo();

                hydrologicalInfo.setRiverBasin(e.select("").text());


                System.out.println(Thread.currentThread().getName() + " ok ");
            }
            System.out.println(Thread.currentThread() + " over");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
