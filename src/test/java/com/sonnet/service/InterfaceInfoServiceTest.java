package com.sonnet.service;

import com.sonnet.model.vo.InterfaceInfoVO;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterfaceInfoServiceTest {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    @Test
    void listTopInvokeInterfaceInfos() {

        List<InterfaceInfoVO> interfaceInfoVOS = interfaceInfoService.listTopInvokeInterfaceInfos(3);
        if (interfaceInfoVOS != null){
            System.out.println(interfaceInfoVOS.get(0));
        }

    }
}