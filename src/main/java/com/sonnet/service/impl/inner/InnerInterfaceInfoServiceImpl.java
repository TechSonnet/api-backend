package com.sonnet.service.impl.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.service.InnerInterfaceInfoService;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

// @Service 注意这个注解的位置，不在 InnerInterfaceInfoService。但是使用的时候，只需要注入 InnerInterfaceInfoService 即可。
@DubboService
public class InnerInterfaceInfoServiceImpl implements InnerInterfaceInfoService {

    @Resource
    InterfaceInfoMapper interfaceInfoMapper;

    @Override
    public InterfaceInfo getInterfaceInfo(String url, String method) {

        // 基本测试代码
        if (StringUtils.isBlank(url) || StringUtils.isBlank(method)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("url", url);
        queryWrapper.eq("method", method);

        return interfaceInfoMapper.selectOne(queryWrapper);
    }
}
