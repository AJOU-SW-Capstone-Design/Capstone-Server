package com.capstone.service;

import com.capstone.dto.DetailPostDto;
import com.capstone.dto.MainPostDto;
import com.capstone.dto.OrdersDto;
import com.capstone.dto.PostDto;
import java.util.List;

public interface PostService {
    List<PostDto> getPostCategory(String category);
    List<PostDto> getPostAll();
    void createPost(PostDto postDto);
    void createOrders(OrdersDto ordersDto);
    List<MainPostDto> searchPost(String keyword);
    public DetailPostDto getDetailPost(int pId);
    void updateDonePost(int pId);
}
