package org.example.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.common.collect.Lists;
import org.example.entity.Danmu;
import org.example.entity.Stat;
import org.example.entity.Zhubo;
import org.example.mapper.DanmuMapper;
import org.example.mapper.StatMapper;
import org.example.mapper.ZhuboMapper;
import org.example.service.BojiangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/2
 **/
@Component
public class BojiangServiceImpl implements BojiangService {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @Value("${bojiang.search-zhubo-url-pattern}")
    private String searchZhuboUrlPattern;
    @Value("${bojiang.url-pattern}")
    private String urlPattern;
    @Value("${bojiang.stat-url-pattern}")
    private String statUrlPattern;
    @Value("${bojiang.start-date}")
    private String startDate;

    @Value("${bojiang.end-date:}")
    private String endDate;

    @Autowired
    private DanmuMapper danmuMapper;
    @Autowired
    private ZhuboMapper zhuboMapper;
    @Autowired
    private StatMapper statMapper;

    @Override
    public void spider(List<String> rids) {
        if (CollectionUtils.isEmpty(rids)) {
            return;
        }
        rids.forEach(this::spiderByRid);
    }

    @Override
    public void searchZhubo(List<String> keywords) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = getHttpHeaders();
        for (String keyword : keywords) {
            String url = searchZhuboUrlPattern.replace("{keyword}", URLEncoder.encode((keyword), StandardCharsets.UTF_8));
            RequestEntity requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, URI.create(url));
            JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
            JSONArray rows = body.getJSONObject("data").getJSONArray("rows");
            if (CollectionUtils.isEmpty(rows)) {
                System.out.println(String.format("%s is not fount", keyword));
            }
            else {
                JSONObject jsonObject = rows.getJSONObject(0);
                zhuboMapper.saveOne(jsonObject.to(Zhubo.class));
                System.out.println(String.format("%s  -->  %s", keyword, jsonObject.getString("name")));
            }
        }
    }

    @Override
    public void spiderStat(List<String> rids) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = getHttpHeaders();
        ExecutorService statPool = Executors.newFixedThreadPool(4);
        for (String rid : rids) {
            statPool.execute(() -> {
                LocalDate startDate = LocalDate.parse(this.startDate, DATE_TIME_FORMATTER);
                LocalDate endDate = LocalDate.now();
                while (!startDate.isAfter(endDate)) {
                    String url = statUrlPattern.replace("{rid}", rid)
                            .replace("{date}", startDate.format(DATE_TIME_FORMATTER));
                    RequestEntity requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, URI.create(url));
                    JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
                    if (body != null) {
                        JSONObject data = body.getJSONObject("data");
                        if (data != null) {
                            Stat stat = new Stat();
                            stat.setRid(rid);
                            stat.setDate(data.getString("date"));
                            stat.setData(data.toJSONString());
                            statMapper.saveOne(stat);
                        }
                    }
                    startDate = startDate.plusDays(1);
                }
                System.out.println(rid + " finished");
            });

        }
        statPool.shutdown();
        try {
            statPool.awaitTermination(1, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("all finished");
    }

    private void spiderByRid(String rid) {
        LocalDate startLocalDate = LocalDate.parse(startDate, DATE_TIME_FORMATTER);
        LocalDate endLocalDate = StringUtils.hasText(endDate) ? LocalDate.parse(endDate, DATE_TIME_FORMATTER) : LocalDate.now();
        while (!startLocalDate.isAfter(endLocalDate)) {
            // 获取数据
            List<Danmu> danmuList = getDanmu(rid, startLocalDate);
            // 保存数据
            if (!danmuList.isEmpty()) {
                for (List<Danmu> danmus : Lists.partition(danmuList, 1000)) {
                    danmuMapper.saveAll(danmus);
                }
            }
            System.out.println("get " + startLocalDate.format(DATE_TIME_FORMATTER) + " " + danmuList.size());
            startLocalDate = startLocalDate.plusDays(1);
        }
        System.out.println(rid + " finished");
    }

    private List<Danmu> getDanmu(String rid, LocalDate date) {
        List<Danmu> result = new LinkedList<>();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = getHttpHeaders();
        int pageNum = 1;
        do {
            String url = urlPattern.replace("{rid}", rid)
                    .replace("{date}", date.format(DATE_TIME_FORMATTER))
                    .replace("{pageNum}", String.valueOf(pageNum));
            RequestEntity requestEntity = new RequestEntity<>(httpHeaders, HttpMethod.GET, URI.create(url));
            JSONObject body = restTemplate.exchange(requestEntity, JSONObject.class).getBody();
            JSONObject page = body.getJSONObject("data").getJSONObject("page");
            if (page == null) {
                break;
            }
            result.addAll(page.getList("rows", Danmu.class));
            pageNum++;
        } while (true);
        return result;
    }

    public HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36");
        httpHeaders.set("Cookie", "bojiangerid=A264497EB2B02049BB548C915679124D; Hm_lvt_afde8320c8c6fa5cb4193fae2f492fd3=1689541169,1689589728,1689592580,1689635003; Hm_lpvt_afde8320c8c6fa5cb4193fae2f492fd3=1689635003");
        httpHeaders.set("Host", "www.bojianger.com");
        httpHeaders.set("Sec-Ch-Ua", "\"Not.A/Brand\";v=\"8\", \"Chromium\";v=\"114\", \"Google Chrome\";v=\"114\"");
        httpHeaders.set("Sec-Ch-Ua-Mobile", "?0");
        httpHeaders.set("Sec-Ch-Ua-Platform", "\"Windows\"");
        httpHeaders.set("Sec-Fetch-Dest", "empty");
        httpHeaders.set("Sec-Fetch-Mode", "cors");
        httpHeaders.set("Sec-Fetch-Site", "same-origin");
        httpHeaders.set("Token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxOTUwNTI4MTc1OCIsImlhdCI6MTY4OTU4OTgwMCwiZXhwIjoxNjkwMTk0NjAwfQ.cbRwvuYPJVcGfQtuco8jLQstq331YkDDFZ5XbA-vgJHyQYruALba-NurOlzx1SFXX3RayRwLoA0CS4E3RTP18w");
        return httpHeaders;
    }
}
