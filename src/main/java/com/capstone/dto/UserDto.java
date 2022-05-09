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
    private String road_address;
    private String address;
    private String phone;
    private int point;
    private double u_x;
    private double u_y;

    @Override
    public String toString() {
        return "UserDto{" +
                "uId=" + u_id +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", road_address='" + road_address + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", u_x=" + u_x +
                ", u_y=" + u_y +
                '}';
    }
}
