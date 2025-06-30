package com.yupi.springbootinit.service;

import com.yupi.springbootinit.model.entity.InterfaceInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-06-30 16:28:51
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);
}
