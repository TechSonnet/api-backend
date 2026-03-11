package com.sonnet.controller;

import com.sonnet.annotation.AuthCheck;
import com.sonnet.common.BaseResponse;
import com.sonnet.common.ResultUtils;
import com.sonnet.model.vo.InterfaceInfoVO;
import com.sonnet.service.InterfaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据分析
 */

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController {

    @Resource
    private InterfaceInfoService interfaceInfoService;


    /**
     * 获取最热门的接口信息
     *
     * @return
     */
    @GetMapping("/top/interface/invoke")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<InterfaceInfoVO>> listTopInvokeInterfaceInfo() {

        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfoService.listTopInvokeInterfaceInfos(3);

        return ResultUtils.success(interfaceInfoVOS);
    }
}
