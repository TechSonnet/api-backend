package com.sonnet.apicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sonnet.apicommon.model.entity.User;

/**
 * 用户服务
 *
 */
public interface InnerUserService {

    /**
     * 数据库中查询是否已分配给用户秘钥
     * @param accessKey
     */
    User getInvokeUser(String accessKey);
}
