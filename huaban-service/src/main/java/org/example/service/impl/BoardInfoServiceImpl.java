package org.example.service.impl;

import org.example.entity.BoardInfo;
import org.example.mapper.BoardInfoMapper;
import org.example.service.IBoardInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author VNElinpe
 * @since 2023-07-09
 */
@Service
public class BoardInfoServiceImpl extends ServiceImpl<BoardInfoMapper, BoardInfo> implements IBoardInfoService {

}
