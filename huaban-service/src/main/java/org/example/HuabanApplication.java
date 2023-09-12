package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/9
 **/
@SpringBootApplication
@MapperScan("org.example.mapper")
public class HuabanApplication {
    public static void main(String[] args) throws IOException {
//        List<String> all = Files.readAllLines(Paths.get("D:\\Workspace\\Java\\idea\\product\\noah\\huaban-service\\src\\main\\resources\\all.txt"), StandardCharsets.UTF_8).stream().filter(StringUtils::hasText).distinct().collect(Collectors.toList());
//        Set<String> done = Files.readAllLines(Paths.get("D:\\Workspace\\Java\\idea\\product\\noah\\huaban-service\\src\\main\\resources\\allDone.txt")).stream().filter(StringUtils::hasText).collect(Collectors.toSet());
//        all = all.stream().filter(item -> !done.contains(item)).collect(Collectors.toList());
//        List<String>[] part = new List[2];
//        part[0] = new LinkedList<>();
//        part[1] = new LinkedList<>();
//        for (int i = 0; i < all.size(); i++) {
//            if (i % 2 == 0) {
//                part[0].add(all.get(i));
//            }
//            else {
//                part[1].add(all.get(i));
//            }
//        }
//        String join = String.join("\n", part[0]);
//        String join1 = String.join("\n", part[1]);
//        System.out.println();
                SpringApplication.run(HuabanApplication.class, args);
    }
}
