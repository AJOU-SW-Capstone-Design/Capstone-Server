package com.capstone.dto;

import lombok.Getter;

@Getter
public class NanumMemberDto {
    private int pId;
    private int uId;
    private String pw;
    private String name;
    private String bank;
    private String account;
    private String location;
    private String phone;
    private int point;
    private float uX;
    private float uY;
    private String menu;
    private int price;
    private String request;
    private int fee;

    @Override
    public String toString() {
        return "NanumMemberDto{" +
                "pId=" + pId +
                ", uId=" + uId +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                ", bank='" + bank + '\'' +
                ", account='" + account + '\'' +
                ", location='" + location + '\'' +
                ", phone='" + phone + '\'' +
                ", point=" + point +
                ", uX=" + uX +
                ", uY=" + uY +
                ", menu='" + menu + '\'' +
                ", price=" + price +
                ", request='" + request + '\'' +
                ", fee=" + fee +
                '}';
    }
}
