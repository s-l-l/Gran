package com.shill.gran.common.user.dao;

import com.shill.gran.common.user.domain.entity.UserBackpack;
import com.shill.gran.common.user.mapper.UserBackpackMapper;
import com.shill.gran.common.user.service.IUserBackpackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户背包表 服务实现类
 * </p>
 *
 * @author shill
 * @since 2023-11-08
 */
@Service
public class UserBackpackDao extends ServiceImpl<UserBackpackMapper, UserBackpack> implements IUserBackpackService {

}
