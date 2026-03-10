package com.sonnet.mapper;

import com.sonnet.model.entity.UserInterfaceInfo;
import com.sonnet.model.vo.InterfaceInfoVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.List;

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

    @Test
    void listTopInvokeInterfaceInfos() {

        int limit = 3;
        List<InterfaceInfoVO> list = userInterfaceInfoMapper.listTopInvokeInterfaceInfos(limit);
        System.out.println(list.get(0).toString());
        Assertions.assertNotNull(list);

    }
}