package com.sonnet.service.impl.inner;

import com.sonnet.apicommon.service.InnerUserInterfaceInfoService;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


//@Service
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        // 1. 基本校验
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // 2. 增加特定接口的调用次数
        // 3. 返回操作结果
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }
}
