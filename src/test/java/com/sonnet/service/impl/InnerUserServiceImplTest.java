package com.sonnet.service.impl;

import com.sonnet.apicommon.model.entity.User;
import com.sonnet.apicommon.service.InnerUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class InnerUserServiceImplTest {


    @Resource
    private InnerUserService innerUserService;


    @Test
    void getInvokeUser() {
        User yvpi = innerUserService.getInvokeUser("yvpi");
        assert yvpi != null;
        System.out.println(yvpi);
    }
}