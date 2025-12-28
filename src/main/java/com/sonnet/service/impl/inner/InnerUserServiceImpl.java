package com.sonnet.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.sonnet.apicommon.model.entity.User;
import com.sonnet.apicommon.service.InnerUserService;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.ThrowUtils;
import com.sonnet.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {

        // 1. 基本校验
        ThrowUtils.throwIf(StringUtils.isBlank(accessKey), ErrorCode.SYSTEM_ERROR);

        // 2. 封装请求条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey", accessKey);

        // 3. 返回结果
        return userMapper.selectOne(queryWrapper);
    }
}
