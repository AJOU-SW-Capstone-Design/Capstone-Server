package com.capstone.mapper;

import com.capstone.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface PostMapper {
    public List<MainPostDto> getPostCategory(MainPageDto mainPageDto);
    public List<MainPostDto> getPostAll(MainPageDto mainPageDto);
    public void createPost(PostDto postDto);
    public void createOrders(OrdersDto ordersDto);
    public  List<MainPostDto> searchPost(String keyword);
    public DetailPostDto getDetailPost(int pId);
    public void updateDonePost(int pId);
    public void updateNanumPlace(HashMap<String, Object> nanumPlaceInfo);
    public void updateShootingPost(ShootingInfoDto shootingInfoDto);
    public void updateShootingOrders(int pId);
    public void updateShootingUserOrders(int pId);
    public void updateTotalPoint(OrdersDto ordersDto);
    public int getTotalPoint(int pId);
    public String getOrderFee(int pId);
    public void updateTotalFee(HashMap<String, Object> totalFeeInfo);
    public PostDto getPostInfo(int pId);
    public void updateShootingPostFee(int pId);
    public List<OrdersDto> getUserList(int pId);
    public void updatePostFee(HashMap<String, Object> postFeeInfo);
    public List<Integer> getPostUserId(int pId);
    public void updateUserOrderFee(HashMap<String, Object> userOrderFeeInfo);
    public UserDto getShootingUserInfo(int pId);
    public List<RestaurantDto> getSearchRestaurantList(String searchKeyword);
    public RestaurantDto getSearchRestaurant(int rId);
    public List<NanumMemberPosDto> getNanumMemberPos(int pId);
    public OrdersDto getOrdersDto(OrdersDto ordersDto);
    public int getTotalFee(int pId);
    public int getUserFee(HashMap<String, Object> ordersInfo);
    public int getUserMenuPrice(HashMap<String, Object> ordersInfo);
    public int getUserPoint(int uId);
    public void updateUserPoint(HashMap<String, Object> nanumMemberInfo);
    public UserDto getUserInfo(int uId);
    public List<HashMap<String, Object>> getChatList(int uId);
    public int chargePoint(HashMap<String, Object> paymentInfo);
    public void chargeUserPoint(HashMap<String, Object> chargeInfo);
    public HashMap<String, Object> getChatListDetailInfo(int pId);
    public String getOrdererPhone(int pId);
    public String getOrderUrl(int pId);
    public List<HashMap> getOrderList(int pId);
    public List<OrdersDto> getOrders(int pId);
    public void updateOrders(OrdersDto ordersDto);
    public void updateOrdersMenu(OrdersDto ordersDto);
    public void updateOrdersPrice(OrdersDto ordersDto);
    public double getPLocationX(int pId);
    public double getPLocationY(int pId);
}