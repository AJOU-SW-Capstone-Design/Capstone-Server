package com.capstone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;

@Getter
public class PostDto {
    private int p_id;
    private String title;
    private LocalTime order_time;
    private LocalDateTime post_time;
    private String shooting_user;
    private double p_location_x;
    private double p_location_y;
    private int u_id;
    private int r_id;
    private int post_fee;
    private int total_point;
    private boolean done;
    private int total_fee;

    @Override
    public String toString() {
        return "PostDto{" +
                "p_id=" + p_id +
                ", title='" + title + '\'' +
                ", order_time=" + order_time +
                ", post_time=" + post_time +
                ", shooting_user='" + shooting_user + '\'' +
                ", p_location_x='" + p_location_x + '\'' +
                ", p_location_y='" + p_location_y + '\'' +
                ", u_id=" + u_id +
                ", r_id=" + r_id +
                ", post_fee=" + post_fee +
                ", total_point=" + total_point +
                ", done=" + done +
                ", total_fee=" + total_fee +
                '}';
    }
}
