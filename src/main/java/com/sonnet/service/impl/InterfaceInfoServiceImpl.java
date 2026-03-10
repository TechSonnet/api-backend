package com.sonnet.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.mapper.InterfaceInfoMapper;
import com.sonnet.mapper.UserInterfaceInfoMapper;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sonnet.model.vo.InterfaceInfoVO;
import com.sonnet.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2025-06-30 16:28:51
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取待校验数据，此处仅仅校验接口名字是否为空
        String interfaceName = interfaceInfo.getName();
        // 创建时，参数不能为空
        if (add) {
            if (StringUtils.isBlank(interfaceName)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "The name of interfaceInfo is empty!");
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(interfaceName) && interfaceName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The content is too long!");
        }
    }



    /**
     * 获取查询条件
     * @param interfaceInfoQueryRequest
     * @return
     */
    @Override
    public Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {

        if (interfaceInfoQueryRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Query params is null!");
        }

        Long id = interfaceInfoQueryRequest.getId();
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String url = interfaceInfoQueryRequest.getUrl();
        Integer status = interfaceInfoQueryRequest.getStatus();
        String method = interfaceInfoQueryRequest.getMethod();
        Long userId = interfaceInfoQueryRequest.getUserId();
        String requestParams = interfaceInfoQueryRequest.getRequestParams();
        return new QueryWrapper<InterfaceInfo>()
                .like(StringUtils.isNotBlank(name), "name", name)
                .like(StringUtils.isNotBlank(description), "description", description)
                .like(StringUtils.isNotBlank(url), "url", url)
                .eq(status != null, "status", status)
                .like(StringUtils.isNotBlank(method), "method", method)
                .like(StringUtils.isNotBlank(requestParams), "requestParams", requestParams);
    }

    @Override
    public List<InterfaceInfoVO> listTopInvokeInterfaceInfos(int count) {

        // 基本校验
        if (count <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The count is invalid!");
        }
        // 查询信息
        List<InterfaceInfoVO> interfaceInfoVOS = userInterfaceInfoMapper.listTopInvokeInterfaceInfos(count);
        if (interfaceInfoVOS != null){
            return interfaceInfoVOS;
        }

        return Collections.emptyList();
    }


}




