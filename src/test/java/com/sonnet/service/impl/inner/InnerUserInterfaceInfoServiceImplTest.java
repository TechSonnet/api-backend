package com.sonnet.service.impl.inner;

import com.sonnet.apicommon.service.InnerUserInterfaceInfoService;
import com.sonnet.service.InterfaceInfoService;
import com.sonnet.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InnerUserInterfaceInfoServiceImplTest {

    @Resource
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    @Transactional
    void invokeCount() {

        boolean result = innerUserInterfaceInfoService.invokeCount(1L, 1L);
        assertTrue(result);

    }
}