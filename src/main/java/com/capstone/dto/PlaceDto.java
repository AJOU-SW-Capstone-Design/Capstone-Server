package com.capstone.dto;

import lombok.Getter;

@Getter
public class PlaceDto {
    private String plName;
    private String plAddress;
    private double x;
    private double y;

    @Override
    public String toString() {
        return "PlaceDto{" +
                "plName='" + plName + '\'' +
                ", plAddress='" + plAddress + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
