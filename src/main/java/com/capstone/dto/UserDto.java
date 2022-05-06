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
    private String phone;
    private int point;
    private Double u_x;
    private Double u_y;
    private String road_address;
    private String address;

    @Override
    public String toString() {
        return "UserDto{" +
                "u_id=" + u_id +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", u_x=" + u_x +
                ", u_y=" + u_y +
                ", road_address='" + road_address + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
