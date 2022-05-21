package com.capstone.controller;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import com.capstone.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

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
        Random rand  = new Random();
        String certificationNum = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certificationNum+=ran;
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + certificationNum);
        userService.sendCertificationMessage(phoneNumber,certificationNum);
        return certificationNum;
    }
}
