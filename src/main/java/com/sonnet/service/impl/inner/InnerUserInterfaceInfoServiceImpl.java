package com.sonnet.service.impl.inner;

import com.sonnet.apicommon.service.InnerUserInterfaceInfoService;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.mapper.UserInterfaceInfoMapper;
import com.sonnet.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;


//@Service
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public boolean invokeCount(long interfaceInfoId, long userId) {

        // 基本校验
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 执行更新操作
        int affectRows = userInterfaceInfoMapper.updateInvokeCount(interfaceInfoId, userId);
        if (affectRows <= 0){
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "更新数据失败，剩余次数补足或无权限");
        }

        // 返回更新结果
        return true;
    }
}
