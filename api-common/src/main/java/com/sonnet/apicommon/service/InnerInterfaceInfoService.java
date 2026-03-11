package com.sonnet.apicommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sonnet.apicommon.model.entity.InterfaceInfo;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-06-30 16:28:51
*/
public interface InnerInterfaceInfoService {

    /**
     * 从数据库中查询接口是否存在
     * @param url
     * @param method
     * @return
     */
    InterfaceInfo getInterfaceInfo(String url, String method);

}
