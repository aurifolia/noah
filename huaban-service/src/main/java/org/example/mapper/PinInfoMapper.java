package org.example.mapper;

import org.example.entity.PinInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author VNElinpe
 * @since 2023-07-09
 */
public interface PinInfoMapper extends BaseMapper<PinInfo> {
    void saveAllWithIngnore(List<PinInfo> pinInfos);
}
