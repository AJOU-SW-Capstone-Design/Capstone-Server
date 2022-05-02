package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserDto {
    @JsonProperty("u_id")
    private int uId;
    private String pw;
    private String name;
    private String bank;
    private String account;
    private String location;
    private String phone;
    private int point;
    private Double uX;
    private Double uY;

    @Override
    public String toString() {
        return "UserDto{" +
                "uId=" + uId +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", uX=" + uX +
                ", uY=" + uY +
                '}';
    }
}
