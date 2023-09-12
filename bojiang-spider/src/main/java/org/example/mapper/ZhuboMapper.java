package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.entity.Zhubo;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/4
 **/
@Mapper
public interface ZhuboMapper {
    void saveOne(Zhubo zhubo);
}
