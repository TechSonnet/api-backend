package com.techsonet.apiinterface.service;

import com.techsonet.apiinterface.domain.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    public void testSaveUser() {
        // 1. Prepare test data
        User user = new User();
        user.setUserAccount("testUser");
        user.setUserPassword("testPassword");
        user.setAccessKey("testAccessKey");
        user.setSecretKey("testSecretKey");

        // 2. Execute the service method
        boolean isSaved = userService.save(user);

        // 3. Verify the result
        Assertions.assertTrue(isSaved, "User should be saved successfully");
        Assertions.assertNotNull(user.getId(), "MyBatis-Plus should auto-fill the ID");
    }

    @Test
    public void testGetAllUser() {

        List<User> list = userService.list();
        list.forEach(System.out::println);
        Assertions.assertFalse(list.isEmpty(), "List should not be empty");

    }

}