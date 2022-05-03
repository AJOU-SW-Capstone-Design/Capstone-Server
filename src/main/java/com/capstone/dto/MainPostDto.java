package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MainPostDto {
    private int p_id;
    private String title;
    private String r_name;
    private LocalTime order_time;
    private String category;
    private int min_price;
    private int post_fee;
}
