package org.example;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.opencsv.CSVWriter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.example.entity.Danmu;
import org.example.entity.Stat;
import org.example.entity.Zhubo;
import org.example.mapper.DanmuMapper;
import org.example.mapper.StatMapper;
import org.example.mapper.ZhuboMapper;
import org.example.service.BojiangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/2
 **/
@SpringBootApplication
public class BoJiangSpiderApplication implements ApplicationRunner {
    @Autowired
    private BojiangService bojiangService;

    public static void main(String[] args) {
        SpringApplication.run(BoJiangSpiderApplication.class, args);
    }

    @Autowired
    private ZhuboMapper zhuboMapper;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private StatMapper statMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        List<String> collect = Arrays.asList("11185673", "4457056");
//        bojiangService.spiderStat(collect);


        SqlSession sqlSession = sqlSessionFactory.openSession();
        DanmuMapper mapper = sqlSession.getMapper(DanmuMapper.class);
        CSVWriter csvWriter = new CSVWriter(Files.newBufferedWriter(Paths.get("C:/csv/danmu.csv"), StandardCharsets.UTF_8));
        csvWriter.writeNext(new String[] {
            "num",
            "uid",
            "rid",
            "date",
            "uname",
            "uavator",
            "level",
            "nl",
            "bl",
            "brid",
            "txt",
            "update_time",
            "anchorName",
            "anchorAvator",
            "anchorCate"
        });
        csvWriter.flush();
        List<String[]> cache = new LinkedList<>();
        for (Danmu danmu : mapper.list()) {
            cache.add(danmuToStringArr(danmu));
            if (cache.size() > 1000) {
                csvWriter.writeAll(cache);
                cache.clear();
                System.out.println(1000);
            }
        }
        if (!cache.isEmpty()) {
            csvWriter.writeAll(cache);
        }
        csvWriter.close();
        System.out.println("finish");



//        List<Stat> list = statMapper.list();
//        List<JSONObject> collect = list.stream().map(Stat::getData).map(item -> JSON.parseObject(item)).collect(Collectors.toList());
//        BufferedWriter bufferedWriter = Files.newBufferedWriter(Paths.get("C:/csv/zhubo_stat_all.json"), StandardCharsets.UTF_8);
//        bufferedWriter.write("[");
//        for (int i = 0; i < collect.size(); i++) {
//            bufferedWriter.write(JSON.toJSONString(collect.get(i)));
//            if (i + 1 != collect.size()) {
//                bufferedWriter.write(",");
//            }
//            bufferedWriter.flush();
//        }
//        bufferedWriter.write("]");
//        bufferedWriter.flush();
//        bufferedWriter.close();
    }

    private String[] danmuToStringArr(Danmu danmu) {
        String[] arr = new String[15];
        if (danmu.getNum() != null) {
            arr[0] = String.valueOf(danmu.getNum());
        }
        if (danmu.getUid() != null) {
            arr[1] = String.valueOf(danmu.getUid());
        }
        if (danmu.getRid() != null) {
            arr[2] = String.valueOf(danmu.getRid());
        }
        if (danmu.getDate() != null) {
            arr[3] = String.valueOf(danmu.getDate());
        }
        if (danmu.getUname() != null) {
            arr[4] = String.valueOf(danmu.getUname());
        }
        if (danmu.getUavator() != null) {
            arr[5] = String.valueOf(danmu.getUavator());
        }
        if (danmu.getLevel() != null) {
            arr[6] = String.valueOf(danmu.getLevel());
        }
        if (danmu.getNl() != null) {
            arr[7] = String.valueOf(danmu.getNl());
        }
        if (danmu.getBl() != null) {
            arr[8] = String.valueOf(danmu.getBl());
        }
        if (danmu.getBrid() != null) {
            arr[9] = String.valueOf(danmu.getBrid());
        }
        if (danmu.getTxt() != null) {
            arr[10] = String.valueOf(danmu.getTxt());
        }
        if (danmu.getUpdateTime() != null) {
            arr[11] = String.valueOf(danmu.getUpdateTime());
        }
        if (danmu.getAnchorName() != null) {
            arr[12] = String.valueOf(danmu.getAnchorName());
        }
        if (danmu.getAnchorAvator() != null) {
            arr[13] = String.valueOf(danmu.getAnchorAvator());
        }
        if (danmu.getAnchorCate() != null) {
            arr[14] = String.valueOf(danmu.getAnchorCate());
        }
        return arr;
    }
}
