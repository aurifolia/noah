package org.example.service;

import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/2
 **/
public interface BojiangService {
    void spider(List<String> rids);

    void searchZhubo(List<String> keywords);

    void spiderStat(List<String> rids);
}
