package com.capstone.controller;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import com.capstone.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @PutMapping("/signup")
    public int insertUser(@RequestBody UserDto userDto){
        userService.insertUser(userDto);
        return userDto.getU_id();
    }

    @PostMapping("/login")
    @ResponseBody
    public UserDto checkUser(@RequestBody LoginUserDto loginUserDto) {
        return userService.checkUser(loginUserDto);
    }

    @PutMapping("/neighbor")
    public void insertUserNeighbor(@RequestBody UserNeighborDto userNeighborDto){
        userService.insertUserNeighbor(userNeighborDto);
    }

    @GetMapping("/signup/certification")
    public String sendCertificationMessage(@RequestParam String phoneNumber) {
        return userService.sendCertificationMessage(phoneNumber);
    }
}
