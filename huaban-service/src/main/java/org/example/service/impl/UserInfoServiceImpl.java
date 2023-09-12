package org.example.service.impl;

import org.example.entity.UserInfo;
import org.example.mapper.UserInfoMapper;
import org.example.service.IUserInfoService;
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
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

}
