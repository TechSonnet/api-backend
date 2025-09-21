package com.sonnet.service;

import com.sonnet.apicommon.model.entity.InterfaceInfo;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InterfaceInfoServiceTest {

    @Resource
    InterfaceInfoService interfaceInfoService;

    @Test
    void testFindAll() {
        List<InterfaceInfo> interfaceInfoList = interfaceInfoService.list();
        assertNotNull(interfaceInfoList);
    }

}