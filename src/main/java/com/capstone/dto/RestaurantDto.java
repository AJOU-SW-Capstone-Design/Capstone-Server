package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class RestaurantDto {
    private int r_id;
    private String r_name;
    private String category;
    private int min_price;
    private String order_fee;

    @Override
    public String toString() {
        return "RestaurantDto{" +
                "rId=" + r_id +
                ", rName='" + r_name + '\'' +
                ", category='" + category + '\'' +
                ", minPrice=" + min_price +
                ", orderFee='" + order_fee + '\'' +
                '}';
    }
}
