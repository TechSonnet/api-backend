package com.sonnet.controller;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.sonnet.annotation.AuthCheck;
import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.model.entity.User;
import com.sonnet.common.*;
import com.sonnet.constant.CommonConstant;
import com.sonnet.constant.UserConstant;
import com.sonnet.exception.BusinessException;
import com.sonnet.exception.ThrowUtils;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoAddRequest;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoInvokeRequest;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoQueryRequest;
import com.sonnet.model.dto.interfaceinfo.InterfaceInfoUpdateRequest;
import com.sonnet.model.dto.user.UserQueryRequest;
import com.sonnet.model.enums.InterfaceInfoStatusEnum;
import com.sonnet.model.vo.UserVO;
import com.sonnet.service.InterfaceInfoService;
import com.sonnet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.apiinterfacesdk.client.ApiClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
        // 根据 ID 获取接口信息
        // 发送地址进行模拟调用
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

        // 为了展示功能，这里使用一些真实可调用的接口
//        // 解析前端参数，反序列化
//        Gson gson = new Gson();
//        org.example.apiinterfacesdk.model.User sdkUser = gson.fromJson(requestParams, org.example.apiinterfacesdk.model.User.class);
//        sdkUser.setUserAccount(loginUser.getUserAccount());
//        sdkUser.setAccessKey(loginUser.getAccessKey());
//        sdkUser.setSecretKey(loginUser.getSecretKey());
//        sdkUser.setUserPassword(loginUser.getUserPassword());
//        // 接口调用
//        String userNameByPost = apiClient.getUserNameByPost(sdkUser);

        // 获取接口的请求地址，使用 hutool 工具实际调用接口
        String targetUrl = oldInterfaceInfo.getUrl();

        // 第一步：把自己伪装成真实的 Chrome 浏览器发请求
        HttpResponse response = HttpRequest.get(targetUrl)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .header("Accept", "application/json, text/plain, */*")
                .timeout(5000) // 必须设置超时时间，防止对方服务器卡死导致你的程序一直等待
                .execute();

        String result = response.body();

        // 第二步：检查有没有被对方的“反爬虫”或者“JS 重定向”卡住
        if (result != null && result.contains("window.location.replace")) {

            // 利用 Hutool 的正则工具 (ReUtil)，把那段 HTML 里的真实新网址抠出来
            // 正则表达式会精准提取 replace(" 这里面的真实网址 ")
            String realUrl = ReUtil.get("window\\.location\\.replace\\([\"'](.*?)[\"']\\)", result, 1);

            if (realUrl != null && !realUrl.isEmpty()) {
                // 第三步：拿着抠出来的真实网址，再发一次请求！
                result = HttpRequest.get(realUrl)
                        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .header("Accept", "application/json")
                        .timeout(5000)
                        .execute()
                        .body();
            }
        }
        // 到这里，result 里面装的就绝对是你想要的纯净 JSON 数据了！
        System.out.println("最终拿到的结果：" + result);

        // 返回调用结果
        return ResultUtils.success(result);
    }

    /**
     * 根据 id 获取接口信息（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<InterfaceInfo> getInterfaceInfoById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(id);
        ThrowUtils.throwIf(interfaceInfo == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(interfaceInfo);
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param interfaceInfoQueryRequest
     * @param request
     * @return
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InterfaceInfo>> listInterfaceInfoByPage(InterfaceInfoQueryRequest interfaceInfoQueryRequest,
                                                   HttpServletRequest request) {
        if (interfaceInfoQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        InterfaceInfo interfaceInfoQuery = new InterfaceInfo();
        BeanUtils.copyProperties(interfaceInfoQueryRequest, interfaceInfoQuery);
        long current = interfaceInfoQueryRequest.getCurrent();
        long size = interfaceInfoQueryRequest.getPageSize();
        String sortField = interfaceInfoQueryRequest.getSortField();
        String sortOrder = interfaceInfoQueryRequest.getSortOrder();
        String description = interfaceInfoQuery.getDescription();
        // description 需支持模糊搜索
        interfaceInfoQuery.setDescription(null);
        // 限制爬虫
        if (size > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> queryWrapper = new QueryWrapper<>(interfaceInfoQuery);
        queryWrapper.like(StringUtils.isNotBlank(description), "description", description);
        queryWrapper.orderBy(StringUtils.isNotBlank(sortField),
                sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        Page<InterfaceInfo> interfaceInfoPage = interfaceInfoService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(interfaceInfoPage);
    }





}
