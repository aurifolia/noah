package org.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/5/22
 **/
@RequestMapping("/lsar")
@RestController
public class LsarController {
    @GetMapping("/douyu/{roomId}")
    public String get(@PathVariable String roomId) {
        try {
            Process process = Runtime.getRuntime().exec(String.format("/opt/node-v18.16.0-linux-x64/bin/lsar douyu %s", roomId));
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String lineMes;
            List<String> result = new ArrayList<>();
            while ((lineMes = br.readLine()) != null) {
                result.add(lineMes);
            }

            //检查命令是否执行失败。
            if (process.waitFor() != 0) {
                if (process.exitValue() == 1) {//0表示正常结束，1：非正常结束
                    return "";
                }
            }
            br.close();
            return String.join("\n", result);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
