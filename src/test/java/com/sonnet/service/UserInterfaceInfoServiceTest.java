package com.sonnet.service;

import com.sonnet.model.entity.UserInterfaceInfo;
import com.sonnet.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserInterfaceInfoServiceTest {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    /**
     * 测试获取用户信息表所有信息
     */
    @Test
    void testFindAll(){
        List<UserInterfaceInfo> list = userInterfaceInfoService.list();
        assertNotNull(list, "The result is not null!");
//        assertEquals(2, list.size(), "the count of res is not fitted");
    }


    @Test
    void testInvokeCount() {
//        boolean res = userInterfaceInfoService.invokeCount(2001L, 1001L);
//        assertTrue(res);
//        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getById("1");
//        assertEquals(6, userInterfaceInfo.getTotalNum());
//        assertEquals(1, userInterfaceInfo.getLeftNum());
    }
}