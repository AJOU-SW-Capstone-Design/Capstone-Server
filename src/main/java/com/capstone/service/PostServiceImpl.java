package com.capstone.service;

import com.capstone.dto.*;
import com.capstone.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class PostServiceImpl {
    @Autowired
    private final PostMapper postMapper;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    //카테고리별 post 찾기
    public List<MainPostDto> getPostCategory(String category) {
        return postMapper.getPostCategory(category);
    }

    public List<MainPostDto> getPostAll() {
        return postMapper.getPostAll();
    }

    public void createPost(PostDto postDto) {
        postMapper.createPost(postDto);
    }

    public void createOrders(OrdersDto ordersDto) { postMapper.createOrders(ordersDto);}

    public List<MainPostDto> searchPost(String keyword){ return postMapper.searchPost(keyword);}

    public DetailPostDto getDetailPost(int pId) { return postMapper.getDetailPost(pId);}

    public void updateDonePost(int pId){postMapper.updateDonePost(pId);}

    public void updateNanumPlace(HashMap<String, Object> nanumPlaceInfo){postMapper.updateNanumPlace(nanumPlaceInfo);}

    public void updateShootingPost(ShootingInfoDto shootingInfoDto){postMapper.updateShootingPost(shootingInfoDto);}

    public void updateShootingOrders(int pId){postMapper.updateShootingOrders(pId);}

    public void updateShootingUserOrders(int pId){postMapper.updateShootingUserOrders(pId);}

    public void updateTotalPoint(OrdersDto ordersDto){postMapper.updateTotalPoint(ordersDto);}

    public int getTotalPoint(int pId) { return postMapper.getTotalPoint(pId);}

    public String getOrderFee(int pId){ return postMapper.getOrderFee(pId);}

    public void updateTotalFee(HashMap<String, Object> totalFeeInfo){postMapper.updateTotalFee(totalFeeInfo);}

    public PostDto getPostInfo(int pId){ return postMapper.getPostInfo(pId);}

    public void updateShootingPostFee(int pId){postMapper.updateShootingPostFee(pId);}

    public List<OrdersDto> getUserList(int pId){ return postMapper.getUserList(pId);}

    public void updatePostFee(HashMap<String, Object> postFeeInfo){postMapper.updatePostFee(postFeeInfo);}

    public List<Integer> getPostUserId(int pId){ return postMapper.getPostUserId(pId);}

    public void updateUserOrderFee(HashMap<String, Object> userOrderFeeInfo){postMapper.updateUserOrderFee(userOrderFeeInfo);}

    public UserDto getShootingUserInfo(int pId){ return postMapper.getShootingUserInfo(pId);}

    public List<RestaurantDto> getSearchRestaurantList(String searchKeyword){ return postMapper.getSearchRestaurantList(searchKeyword);}

    public RestaurantDto getSearchRestaurant(int rId){ return postMapper.getSearchRestaurant(rId);}

    public List<NanumMemberPosDto> getNanumMemberPos(int pId){ return postMapper.getNanumMemberPos(pId);}
    ;
}

