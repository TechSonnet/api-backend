package com.techsonet.apisdktest;

import com.techsonet.apiclientsdk.client.ApiClient;
import com.techsonet.apiclientsdk.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ApiSdkTestApplicationTests {

    @Resource
    private ApiClient apiClient;


    @Test
    void contextLoads() {

        String nameByGet = apiClient.getNameByGet("name-01");
        System.out.println(nameByGet);

        User user = new User();
        user.setUserName("name-02");
        user.setUserAccount("admin");
        String userNameByPost = apiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);

    }

}
