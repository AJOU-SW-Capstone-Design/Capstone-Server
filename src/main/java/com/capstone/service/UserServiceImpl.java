package com.capstone.service;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
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

    public int insertUser(UserDto userDto) {
       return userMapper.insertUser(userDto);
    }

    public UserDto checkUser(LoginUserDto loginUserDto) {
        UserDto tempLoginUserDto = userMapper.checkUser(loginUserDto);
        if (tempLoginUserDto == null)
            return null;
        else
            return tempLoginUserDto;
    }

    public void insertUserNeighbor(UserNeighborDto userNeighborDto){userMapper.insertUserNeighbor(userNeighborDto);}
}
