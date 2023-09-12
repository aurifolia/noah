package org.example.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class BojiangServiceImplTest {
    public static void main(String[] args) throws IOException {
        Set<String> set = new HashSet<>();
        Pattern compile = Pattern.compile("^(\\d+).*");
        for (String readAllLine : Files.readAllLines(Paths.get("D:\\Workspace\\Java\\idea\\product\\noah\\bojiang-spider\\src\\main\\resources\\a.json"), StandardCharsets.UTF_8)) {
            Matcher matcher = compile.matcher(readAllLine);
            if (matcher.find()) {
                set.add(matcher.group(1));
            }
        }

        set.forEach(System.out::println);
    }

    public static class Test001 {
        public static void main(String[] args) {
            System.out.println(Integer.MIN_VALUE);
        }
    }

}