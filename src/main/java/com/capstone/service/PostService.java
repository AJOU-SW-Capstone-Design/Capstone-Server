package com.capstone.service;

import com.capstone.dto.DetailPostDto;
import com.capstone.dto.MainPostDto;
import com.capstone.dto.OrdersDto;
import com.capstone.dto.PostDto;
import java.util.List;

public interface PostService {
    List<MainPostDto> getPostCategory(String category);
    List<MainPostDto> getPostAll();
    void createPost(PostDto postDto);
    void createOrders(OrdersDto ordersDto);
    List<MainPostDto> searchPost(String keyword);
    public DetailPostDto getDetailPost(int p_id);
    void updateDonePost(int p_id);
}
