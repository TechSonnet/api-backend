package com.sonnet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.model.vo.InterfaceInfoVO;
import com.sonnet.common.ErrorCode;
import com.sonnet.exception.BusinessException;
import com.sonnet.mapper.InterfaceInfoMapper;
import com.sonnet.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
* @author Administrator
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2025-06-30 16:28:51
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

//    保留这个代码模板，这是最基础的请求校验模板，可以以后借用
//
//    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
//        if (interfaceInfo == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        // 创建时，参数不能为空
//        if (add) {
//            if (StringUtils.isBlank()) {
//                throw new BusinessException(ErrorCode.PARAMS_ERROR);
//            }
//        }
//        // 有参数则校验
//        if (StringUtils.isNotBlank(content) && content.length() > 8192) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
//        }
//    }
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
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "the name of interfaceInfo is empty");
            }
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(interfaceName) && interfaceName.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "内容过长");
        }
    }

    /**
     * 转换 vo 对象
     * @param interfaceInfo
     * @param request
     * @return
     */
    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {
        // 转换为 VO 对象
        return InterfaceInfoVO.objToVo(interfaceInfo);
    }


}




