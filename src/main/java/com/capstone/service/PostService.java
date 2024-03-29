package com.capstone.service;

import com.capstone.dto.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

public interface PostService {
    List<MainPostDto> getPostCategory(MainPageDto mainPageDto);
    void createPost(PostDto postDto);
    void createOrders(OrdersDto ordersDto);
    List<MainPostDto> searchPost(String keyword);
    public DetailPostDto getDetailPost(int pId);
    void updateDonePost(int pId);
    void updateNanumPlace(HashMap<String, Object> nanumPlaceInfo);
    void updateShootingPost(ShootingInfoDto shootingInfoDto);
    void updateShootingOrders(int pId);
    void updateShootingUserOrders(int pId);
    void updateTotalPoint(OrdersDto ordersDto);
    int getTotalPoint(int pId);
    String getOrderFee(int pId);
    void updateTotalFee(HashMap<String, Object> totalFeeInfo);
    PostDto getPostInfo(int pId);
    void updateShootingPostFee(int pId);
    List<OrdersDto> getUserList(int pId);
    void updatePostFee(HashMap<String, Object> postFeeInfo);
    List<Integer> getPostUserId(int pId);
    void updateUserOrderFee(HashMap<String, Object> userOrderFeeInfo);
    UserDto getShootingUserInfo(int pId);
    List<RestaurantDto> getSearchRestaurantList(String searchKeyword);
    RestaurantDto getSearchRestaurant(int rId);
    List<NanumMemberPosDto> getNanumMemberPos(int pId);
    OrdersDto getOrdersDto(OrdersDto ordersDto);
    int getTotalFee(int pId);
    int getUserFee(HashMap<String, Object> ordersInfo);
    int getUserMenuPrice(HashMap<String, Object> ordersInfo);
    int getUserPoint(int uId);
    void updateUserPoint(HashMap<String, Object> nanumMemberInfo);
    UserDto getUserInfo(int uId);
    List<MainPostDto> getPostAll(MainPageDto mainPageDto);
    List<HashMap<String, Object>> getChatList(int uId);
    int chargePoint(HashMap<String, Object> paymentInfo);
    HashMap<String, Object> getChatListDetailInfo(int pId);
    String getOrdererPhone(int pId);
    String getOrderUrl(int pId);
    List<HashMap> getOrderList(int pId);
    List<OrdersDto> getOrders(int pId);         //전체 멤버 주문서 조회
    void updateOrders(OrdersDto ordersDto);     //주문서 수정
    void updateOrdersMenu(OrdersDto ordersDto);
    void updateOrdersPrice(OrdersDto ordersDto);
    double getPLocationX(int pId);
    double getPLocationY(int pId);

}
