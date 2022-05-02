package com.capstone.mapper;

import com.capstone.dto.DetailPostDto;
import com.capstone.dto.MainPostDto;
import com.capstone.dto.OrdersDto;
import com.capstone.dto.PostDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PostMapper {
    public List<PostDto> getPostCategory(String category);
    public List<PostDto> getPostAll();
    public void createPost(PostDto postDto);
    public void createOrders(OrdersDto ordersDto);
    public  List<MainPostDto> searchPost(String keyword);
    public DetailPostDto getDetailPost(int pId);
    public void updateDonePost(int pId);
}