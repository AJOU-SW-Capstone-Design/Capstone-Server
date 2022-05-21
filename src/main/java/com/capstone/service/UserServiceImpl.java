package com.capstone.service;

import com.capstone.dto.LoginUserDto;
import com.capstone.dto.UserDto;
import com.capstone.dto.UserNeighborDto;
import com.capstone.mapper.UserMapper;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

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

    public void sendCertificationMessage(String phoneNumber, String certificationNum) {
        String api_key = "NCSSGVGDAUAGAT3W";
        String api_secret = "6IH1UPF3MYKG520ZRF1NRVGMLQ2GQQBX";
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);  // 수신전화번호
        params.put("from", "01090604074"); // 발신전화번호
        params.put("type", "SMS");
        params.put("text", "삼삼오오 휴대폰인증 메시지 : 인증번호는" + "["+certificationNum+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

    }
}
