package com.capstone.dto;

import lombok.Getter;

@Getter
public class LoginUserDto {
    private String pw;
    private String phone;

    @Override
    public String toString() {
        return "LoginUserDto{" +
                "pw='" + pw + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
