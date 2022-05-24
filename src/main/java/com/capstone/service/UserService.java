package com.capstone.service;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import org.springframework.stereotype.Service;

public interface UserService {
    int insertUser(UserDto userDto);
    UserDto checkUser(LoginUserDto loginUserDto);
    void insertUserNeighbor(UserNeighborDto userNeighborDto);
    String sendCertificationMessage(String phoneNumber);
}
