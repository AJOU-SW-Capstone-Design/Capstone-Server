package com.capstone.dto;

import lombok.Getter;

@Getter
public class ShootingInfoDto {
    private int p_id;
    private int u_id;

    @Override
    public String toString() {
        return "ShootingInfoDto{" +
                "p_id=" + p_id +
                ", u_id=" + u_id +
                '}';
    }
}
