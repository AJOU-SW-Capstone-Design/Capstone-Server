package com.capstone.controller;

import com.capstone.dto.*;
import com.capstone.service.NanumServiceImpl;
import com.capstone.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private NanumServiceImpl nanumService;

    @GetMapping("/main")
    public List<MainPostDto> getPost(@RequestParam @Nullable String category){
        //category 별 post 분류(category가 있으면 not null이라고 가정)
        if (category!=null)
            return postService.getPostCategory(category);
        else
            return postService.getPostAll();
    }

    @PostMapping("/post")
    public PostDto insertPost(@RequestBody PostDto postDto){
        postService.createPost(postDto);
        return postDto;
    }

    @PutMapping("/post")
    public OrdersDto createOrders(@RequestBody OrdersDto ordersDto){
        postService.createOrders(ordersDto);
        return ordersDto;
    }

    @GetMapping( "/main/search")
    public List<MainPostDto> searchPost(@RequestParam String keyword){
        return postService.searchPost(keyword);
    }

    @GetMapping("/main/detail")
    public DetailPostDto getDetailPost(@RequestParam int pId){ return postService.getDetailPost(pId);}

    @GetMapping("/chat")
    public List<NanumMemberPosDto> setNanumPlace(@RequestParam int pId){
        ArrayList<Double> center;
        List<NanumMemberPosDto> nanumMemberPosDtoList;

        //모집 마감 처리
        postService.updateDonePost(pId);
        //나눔 멤버 리스트 조회
        nanumMemberPosDtoList =nanumService.getNanumMembersPos(pId);
        //중심점 계산
        center=nanumService.setMembersCenter(nanumMemberPosDtoList);
        System.out.println(center);
        return nanumMemberPosDtoList;
    }

}
