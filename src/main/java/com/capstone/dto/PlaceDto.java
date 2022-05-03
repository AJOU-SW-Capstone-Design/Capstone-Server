package com.capstone.dto;

import lombok.Getter;

@Getter
public class PlaceDto {
    private String pl_name;
    private String pl_address;
    private double x;
    private double y;

    @Override
    public String toString() {
        return "PlaceDto{" +
                "plName='" + pl_name + '\'' +
                ", plAddress='" + pl_address + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
