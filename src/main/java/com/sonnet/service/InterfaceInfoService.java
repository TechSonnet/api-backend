package com.sonnet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sonnet.model.vo.InterfaceInfoVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2025-06-30 16:28:51
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean b);



    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    /**
     * 获取最热门的接口信息
     * @param count
     * @return
     */
    List<InterfaceInfoVO> listTopInvokeInterfaceInfos(int count);
}
