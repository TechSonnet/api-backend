package com.sonnet.service.impl;

import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.service.InnerInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InnerInterfaceInfoServiceImplTest {

    @Resource
    InnerInterfaceInfoService interfaceInfoService;

    @Test
    void getInterfaceInfo() {

        InterfaceInfo info = interfaceInfoService.getInterfaceInfo("www.courtney-kassulke.net", "POST");
        System.out.println(info);

    }
}