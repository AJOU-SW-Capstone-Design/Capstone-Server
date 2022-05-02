package com.capstone.dto;

import lombok.Getter;

@Getter
public class NanumMemberXYDto {
    private float uX;
    private float uY;

    @Override
    public String toString() {
        return "NanumMemberXYDto{" +
                "uX=" + uX +
                ", uY=" + uY +
                '}';
    }
}
