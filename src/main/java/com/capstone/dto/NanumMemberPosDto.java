package com.capstone.dto;

import lombok.Getter;

@Getter
public class NanumMemberPosDto {
    private int p_id;
    private int u_id;
    private float u_x;
    private float u_y;

    @Override
    public String toString() {
        return "NanumMemberPosDto{" +
                "pId=" + p_id +
                ", uId=" + u_id +
                ", uX=" + u_x +
                ", uY=" + u_y +
                '}';
    }
}
