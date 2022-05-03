package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class UserDto {
    private int u_id;
    private String pw;
    private String name;
    private String bank;
    private String account;
    private String location;
    private String phone;
    private int point;
    private Double u_x;
    private Double u_y;

    @Override
    public String toString() {
        return "UserDto{" +
                "uId=" + u_id +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", uX=" + u_x +
                ", uY=" + u_y +
                '}';
    }
}
