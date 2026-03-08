package com.sonnet.service.impl.inner;

import com.sonnet.apicommon.model.entity.User;
import com.sonnet.apicommon.service.InnerUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InnerUserServiceImplTest {

    @Resource
    private InnerUserService innerUserService;

    @Test
    void getInvokeUser() {

        User invokeUser = innerUserService.getInvokeUser("0aa00d375d34cdcdba0a6201692fc5f0");
        assertNotNull(invokeUser);

    }
}