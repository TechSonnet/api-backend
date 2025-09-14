package com.sonnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.mapper.UserInterfaceInfoMapper;
import com.sonnet.model.entity.UserInterfaceInfo;
import com.sonnet.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

/**
* @author chang
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
* @createDate 2025-08-20 11:53:14
*/
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
    implements UserInterfaceInfoService{

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 如果是某一种操作，比如说添加操作
        if (b) {
            if (userInterfaceInfo.getInterfaceInfoId() <= 0 || userInterfaceInfo.getUserId() <= 0){
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }

        if (userInterfaceInfo.getLeftNum() < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不可小于0");
        }

    }

    /**
     * 调用接口计数方法
     * @param interfaceInfoId
     * @param userId
     * @return
     */
    @Override
    public boolean invokeCount(Long interfaceInfoId, Long userId) {
        // 基本校验
        if (interfaceInfoId <= 0 || userId <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 构造更新条件，并执行更新操作
        UpdateWrapper<UserInterfaceInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("interfaceInfoId", interfaceInfoId);
        updateWrapper.eq("userId", userId);
        updateWrapper.setSql("leftNum = leftNum - 1, totalNum = totalNum + 1");
        // 返回更新结果
        return this.update(updateWrapper);
    }
}




