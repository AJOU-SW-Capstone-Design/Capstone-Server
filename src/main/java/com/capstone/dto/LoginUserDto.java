package com.capstone.dto;

public class LoginUserDto {
    private String pw;
    private String phone;

    @Override
    public String toString() {
        return "UserDto{" +
                ", pw='" + pw + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
