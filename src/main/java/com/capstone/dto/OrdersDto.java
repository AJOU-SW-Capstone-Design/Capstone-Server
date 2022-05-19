package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdersDto {
    private int u_id;
    private int p_id;
    private String menu;
    private int price;
    private String request;
    private int fee;

    @Override
    public String toString() {
        return "OrderDto{" +
                "uId=" + u_id +
                ", pId='" + p_id + '\'' +
                ", menu='" + menu + '\'' +
                ", price=" + price +
                ", request='" + request + '\'' +
                ", fee=" + fee +
                '}';
    }
}
