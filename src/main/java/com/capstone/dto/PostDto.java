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
    private String p_location;
    private int u_id;
    private int r_id;

    @Override
    public String toString() {
        return "PostDto{" +
                "pId=" + p_id +
                ", title='" + title + '\'' +
                ", orderTime='" + order_time + '\'' +
                ", postTime='" + post_time + '\'' +
                ", shootingUser='" + shooting_user + '\'' +
                ", pLocation='" + p_location + '\'' +
                ", uId=" + u_id +
                ", rId=" + r_id +
                '}';
    }
}
