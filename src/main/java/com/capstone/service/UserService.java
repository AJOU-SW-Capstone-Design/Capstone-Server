package com.capstone.service;

import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import org.springframework.stereotype.Service;


public interface UserService {
    void insertUser(UserDto userDto);
    void insertUserNeighbor(UserNeighborDto userNeighborDto);

}
