package com.capstone.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NanumMemberPosDto {
    private int p_id;
    private int u_id;
    private double u_x;
    private double u_y;

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
