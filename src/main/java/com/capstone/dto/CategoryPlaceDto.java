package com.capstone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@Data
@AllArgsConstructor
@Setter
public class CategoryPlaceDto implements Comparable<CategoryPlaceDto>{
    private String place_name;
    private String category_group_code;
    private String road_address_name;
    private String address_name;
    private double x;
    private double y;
    //중심점과의 거리
    private double distance;

    @Override
    public String toString() {
        return "CategoryPlaceDto{" +
                "place_name='" + place_name + '\'' +
                ", category_group_code='" + category_group_code + '\'' +
                ", road_address_name='" + road_address_name + '\'' +
                ", address_name='" + address_name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", distance=" + distance +
                '}';
    }

    @Override
    public int compareTo(CategoryPlaceDto o) {
        if (this.distance < o.getDistance()) {
            return -1;
        } else if (this.distance > o.getDistance()) {
            return 1;
        }
        return 0;
    }
}
