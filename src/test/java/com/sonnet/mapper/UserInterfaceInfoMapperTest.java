package com.sonnet.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInterfaceInfoMapperTest {

    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Test
    void updateInvokeCount() {

        // 测试数据
        long interfaceInfoId = 1L;
        long userId = 1L;
        // 调用测试方法
        int result = userInterfaceInfoMapper.updateInvokeCount(interfaceInfoId, userId);
        // 断言结果
        Assertions.assertEquals(1, result);
        System.out.println(result);
    }
}