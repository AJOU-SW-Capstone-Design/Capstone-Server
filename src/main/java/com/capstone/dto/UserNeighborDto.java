package com.capstone.dto;

import lombok.Getter;

@Getter
public class UserNeighborDto {
    private Integer u_id;
    private double u_x;
    private double u_y;
    private String road_address;
    private String address;

    @Override
    public String toString() {
        return "UserNeighborDto{" +
                "u_id=" + u_id +
                ", u_x=" + u_x +
                ", u_y=" + u_y +
                ", road_address='" + road_address + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
