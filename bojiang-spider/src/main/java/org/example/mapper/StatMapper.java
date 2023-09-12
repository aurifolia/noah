package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Stat;

import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/5
 **/
@Mapper
public interface StatMapper {
    void saveOne(Stat stat);

    List<Stat> list();
}
