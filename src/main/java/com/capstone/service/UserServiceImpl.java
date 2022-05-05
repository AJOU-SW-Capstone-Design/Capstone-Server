package com.capstone.service;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {
    @Autowired
    private final UserMapper userMapper;
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void insertUser(UserDto userDto) {
       userMapper.insertUser(userDto);
    }

    public Boolean checkUser(LoginUserDto loginUserDto) {
        LoginUserDto tempLoginUserDto = userMapper.checkUser(loginUserDto);
        if (tempLoginUserDto == null)
            return false;
        else
            return true;
    }
}
