package com.sonnet.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sonnet.model.entity.UserInterfaceInfo;

/**
* @author chang
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2025-08-20 11:53:14
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean b);

    boolean invokeCount(Long interfaceInfoId, Long userId);

}
