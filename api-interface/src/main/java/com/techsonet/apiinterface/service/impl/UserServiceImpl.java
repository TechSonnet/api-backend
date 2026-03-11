package com.techsonet.apiinterface.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.techsonet.apiinterface.domain.User;
import com.techsonet.apiinterface.mapper.UserMapper;
import com.techsonet.apiinterface.service.UserService;
import org.springframework.stereotype.Service;

/**
* @author chang
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2026-02-13 11:14:55
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




