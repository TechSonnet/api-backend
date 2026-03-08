package com.sonnet.service.impl.inner;

import com.sonnet.apicommon.model.entity.InterfaceInfo;
import com.sonnet.apicommon.service.InnerInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InnerInterfaceInfoServiceImplTest {

    @Resource
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @Test
    void getInterfaceInfo() {

        InterfaceInfo info = innerInterfaceInfoService.getInterfaceInfo("https://v1.hitokoto.cn", "GET");
        Assertions.assertNotNull(info);
        System.out.println(info);

    }
}