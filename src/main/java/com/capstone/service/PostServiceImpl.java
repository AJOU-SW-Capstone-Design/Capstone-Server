package com.capstone.service;

import com.capstone.dto.DetailPostDto;
import com.capstone.dto.MainPostDto;
import com.capstone.dto.OrdersDto;
import com.capstone.dto.PostDto;
import com.capstone.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PostServiceImpl {
    @Autowired
    private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    //카테고리별 post 찾기
    public List<PostDto> getPostCategory(String category) {
        return postMapper.getPostCategory(category);
    }

    public List<PostDto> getPostAll() {
        return postMapper.getPostAll();
    }

    public void createPost(PostDto postDto) {
        postMapper.createPost(postDto);
    }

    public void createOrders(OrdersDto ordersDto) { postMapper.createOrders(ordersDto);}

    public List<MainPostDto> searchPost(String keyword){ return postMapper.searchPost(keyword);}

    public DetailPostDto getDetailPost(int pId) { return postMapper.getDetailPost(pId);}

    public void updateDonePost(int pId){postMapper.updateDonePost(pId);}
    ;
}

