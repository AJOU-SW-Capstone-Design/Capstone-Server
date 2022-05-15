package com.capstone.mapper;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public int insertUser(UserDto userDto);

    public UserDto checkUser(LoginUserDto loginUserDto);
    public void insertUserNeighbor(UserNeighborDto userNeighborDto);
}
