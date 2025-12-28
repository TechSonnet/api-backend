package com.sonnet.controller;

import com.google.gson.Gson;
import com.sonnet.annotation.AuthCheck;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.model.entity.User;
import com.sonnet.common.*;
import com.sonnet.constant.UserConstant;
import com.sonnet.exception.BusinessException;
import com.sonnet.exception.ThrowUtils;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.sonnet.model.enums.InterfaceInfoStatusEnum;
import com.sonnet.service.InterfaceInfoService;
import com.sonnet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.apiinterfacesdk.client.ApiClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *  接口管理
 */

@RestController
@RequestMapping("/interfaceInfo")
@Slf4j
public class InterfaceInfoController {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Resource
    private UserService userService;

    @Autowired
    private ApiClient apiClient;


    // region 增删改查

    /**
     * 创建
     *
     * @param interfaceInfoAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addInterfaceInfo(@RequestBody InterfaceInfoAddRequest interfaceInfoAddRequest, HttpServletRequest request) {
        if (interfaceInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoAddRequest, interfaceInfo);
        // 接口校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, true);
        User loginUser = userService.getLoginUser(request);
        interfaceInfo.setUserId(loginUser.getId());
        boolean result = interfaceInfoService.save(interfaceInfo);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        long newInterfaceInfoId = interfaceInfo.getId();
        return ResultUtils.success(newInterfaceInfoId);
    }

    /**
     * 删除
     *
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteInterfaceInfo(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getLoginUser(request);
        long id = deleteRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        // 仅本人或管理员可删除
        if (!oldInterfaceInfo.getUserId().equals(user.getId()) && !userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean b = interfaceInfoService.removeById(id);
        return ResultUtils.success(b);
    }

    /**
     * 更新（仅管理员）
     *
     * @param interfaceInfoUpdateRequest
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateInterfaceInfo(@RequestBody InterfaceInfoUpdateRequest interfaceInfoUpdateRequest) {
        if (interfaceInfoUpdateRequest == null || interfaceInfoUpdateRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoUpdateRequest, interfaceInfo);
        // 参数校验
        interfaceInfoService.validInterfaceInfo(interfaceInfo, false);
        long id = interfaceInfoUpdateRequest.getId();
        // 判断是否存在
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(oldInterfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        boolean result = interfaceInfoService.updateById(interfaceInfo);
        return ResultUtils.success(result);
    }


    /**
     * 上线接口
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/online")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> onlineInterface(@RequestBody IDRequest idRequest,
                                                 HttpServletRequest request){
        // 基本参数校验
        if (idRequest == null || idRequest.getId() < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取需上线接口 ID，判断接口是否存在
        Long interfaceID = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceID);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // todo 判断接口是否可用，借用开发的SDK，模拟调用


        // 修改其对应状态
        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(interfaceID);
        newInterfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        boolean result = interfaceInfoService.updateById(newInterfaceInfo);

        // 返回修改结果
        return ResultUtils.success(result);
    }


    /**
     * 下线接口
     * @param idRequest
     * @param request
     * @return
     */
    @PostMapping("/offline")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> offlineInterface(@RequestBody IDRequest idRequest,
                                                 HttpServletRequest request){
        // 基本参数校验
        if (idRequest == null || idRequest.getId() < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取需上线接口 ID，判断接口是否存在
        Long interfaceID = idRequest.getId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceID);
        if (interfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // todo 判断接口是否可用，借用开发的SDK，模拟调用

        // 修改其对应状态
        InterfaceInfo interfaceInfo01 = new InterfaceInfo();
        interfaceInfo01.setId(interfaceID);
        interfaceInfo01.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        boolean result = interfaceInfoService.updateById(interfaceInfo01);

        // 返回修改结果
        return ResultUtils.success(result);
    }

    /**
     * 调用
     * @param interfaceInfoInvokeRequest
     * @param request
     * @return
     */
    @PostMapping("/invoke")
    public BaseResponse<String> invokeInterface(@RequestBody InterfaceInfoInvokeRequest interfaceInfoInvokeRequest,
                                                 HttpServletRequest request){

        // 基本参数校验
        if (interfaceInfoInvokeRequest == null || interfaceInfoInvokeRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 获取请求中的接口参数并进行校验
        Long id = interfaceInfoInvokeRequest.getId();
        String requestParams = interfaceInfoInvokeRequest.getRequestParams();
        InterfaceInfo oldInterfaceInfo = interfaceInfoService.getById(id);
        if (oldInterfaceInfo == null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        if (oldInterfaceInfo.getStatus() == InterfaceInfoStatusEnum.OFFLINE.getValue()){
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "this interface is closed");
        }

        // 获取用户信息
        User loginUser = userService.getLoginUser(request);

        // 解析前端参数，反序列化
        Gson gson = new Gson();
        org.example.apiinterfacesdk.model.User sdkUser = gson.fromJson(requestParams, org.example.apiinterfacesdk.model.User.class);
        sdkUser.setUserAccount(loginUser.getUserAccount());
        sdkUser.setAccessKey(loginUser.getAccessKey());
        sdkUser.setSecretKey(loginUser.getSecretKey());
        sdkUser.setUserPassword(loginUser.getUserPassword());

        // 接口调用
        String userNameByPost = apiClient.getUserNameByPost(sdkUser);

        // 返回调用结果
        return ResultUtils.success(userNameByPost);
    }

}
