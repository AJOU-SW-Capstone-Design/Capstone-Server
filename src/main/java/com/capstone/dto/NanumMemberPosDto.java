package com.capstone.dto;

import lombok.Getter;

@Getter
public class NanumMemberPosDto {
    private int pId;
    private int uId;
    private float uX;
    private float uY;

    @Override
    public String toString() {
        return "NanumMemberPosDto{" +
                "pId=" + pId +
                ", uId=" + uId +
                ", uX=" + uX +
                ", uY=" + uY +
                '}';
    }
}
