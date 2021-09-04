package com.user.service.impl;

import com.user.entity.TUserFriendRelation;
import com.user.mapper.TUserFriendRelationMapper;
import com.user.service.ITUserFriendRelationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jiabao
 * @since 2021-07-27
 */
@Service
@Transactional
public class TUserFriendRelationServiceImpl extends ServiceImpl<TUserFriendRelationMapper, TUserFriendRelation> implements ITUserFriendRelationService {

    @Override
    public void saveRelation(TUserFriendRelation relation) {
        boolean save = this.save(relation);
    }
}
