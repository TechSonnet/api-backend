package com.techsonet.apiinterface.client;

import com.techsonet.apiinterface.domain.User;

public class Test {

    public static void main(String[] args) {

        final String accessKey = "0aa00d375d34cdcdba0a6201692fc5f0";
        final String secretKey = "c6b1e5e953cc920fdb2c12bc9c78ed81";

        ApiClient apiClient = new ApiClient(accessKey, secretKey);

        User user = new User();
        user.setUserName("name-03");
        user.setUserAccount("admin");
        String userNameByPost = apiClient.getUserNameByPost(user);


        System.out.println("userNameByPost: " + userNameByPost);
    }
}
