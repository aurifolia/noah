package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.cursor.Cursor;
import org.example.entity.Danmu;

import java.util.List;

/**
 * TODO
 *
 * @author VNElinpe
 * @since 2023/7/2
 **/
@Mapper
public interface DanmuMapper {
    int saveAll(@Param("danmuList") List<Danmu> damuList);

    Cursor<Danmu> list();
}
