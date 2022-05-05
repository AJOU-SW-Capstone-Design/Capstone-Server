package com.capstone.controller;

import com.capstone.configuration.properties.KakaoProperties;
import com.capstone.dto.*;
import com.capstone.service.NanumServiceImpl;
import com.capstone.service.PostServiceImpl;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import java.util.ArrayList;
import java.util.Collections;
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
    public UserPlaceDto setNanumPlace(@RequestParam int pId) throws JSONException {
        ArrayList<Double> center;
        List<NanumMemberPosDto> nanumMemberPosDtoList;

        //모집 마감 처리
        postService.updateDonePost(pId);

        //나눔 멤버 리스트 조회
        nanumMemberPosDtoList =nanumService.getNanumMembersPos(pId);

        //멤버 없는 경우
        if (nanumMemberPosDtoList.size()==0)
            return null;

        //중심점 계산
        center=nanumService.setMembersCenter(nanumMemberPosDtoList);

        //카테고리별 장소 검색
        ArrayList<CategoryPlaceDto> categoryPlaceDtos= nanumService.getCategoryPlace(center.get(0),center.get(1));
        Collections.sort(categoryPlaceDtos);

        //CategoryPlaceDtoList 내의 객체들 -> UserPlaceDto 객체 타입으로 변환
        ArrayList<UserPlaceDto> userPlaceDtoList = new ArrayList<>();
        int len = categoryPlaceDtos.size();
        for(int i=0; i<len; i++) {
            UserPlaceDto userPlaceDto = new UserPlaceDto();
            CategoryPlaceDto categoryPlaceDto = categoryPlaceDtos.get(i);
            userPlaceDto.setPl_name(categoryPlaceDto.getPlace_name());
            userPlaceDto.setPl_address(categoryPlaceDto.getAddress_name());
            userPlaceDto.setX(categoryPlaceDto.getX());
            userPlaceDto.setY(categoryPlaceDto.getY());
            userPlaceDtoList.add(userPlaceDto);
        }

        UserPlaceDto nanumPlace = nanumService.setPlace(nanumMemberPosDtoList, userPlaceDtoList);

        return nanumPlace;

    }

}
