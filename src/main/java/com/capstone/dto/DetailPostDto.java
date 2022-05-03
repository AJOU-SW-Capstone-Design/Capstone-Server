package com.capstone.dto;

import lombok.Getter;

import java.time.LocalTime;

@Getter
public class DetailPostDto {
    private String title;
    private String category;
    private String name;
    private String r_name;
    private LocalTime order_time;
    private int min_price;

}
