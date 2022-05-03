package com.capstone.dto;

import lombok.Getter;

@Getter
public class NanumMemberDto {
    private int p_id;
    private int u_id;
    private String pw;
    private String name;
    private String bank;
    private String account;
    private String location;
    private String phone;
    private int point;
    private float u_x;
    private float u_y;
    private String menu;
    private int price;
    private String request;
    private int fee;

    @Override
    public String toString() {
        return "NanumMemberDto{" +
                "pId=" + p_id +
                ", uId=" + u_id +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", uX=" + u_x +
                ", uY=" + u_y +
                ", menu='" + menu + '\'' +
                ", price=" + price +
                ", request='" + request + '\'' +
                ", fee=" + fee +
                '}';
    }
}
