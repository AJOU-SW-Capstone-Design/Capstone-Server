package com.capstone.service;

import com.capstone.dto.*;
import com.capstone.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;

@Service
public class PostServiceImpl {
    @Autowired
    private final PostMapper postMapper;

    @Value("${imp_key}")
    private String imp_key;
    @Value("${imp_secret}")
    private String imp_secret;

    public PostServiceImpl(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    //카테고리별 post 찾기
    public List<MainPostDto> getPostCategory(MainPageDto mainPageDto) {
        return postMapper.getPostCategory(mainPageDto);
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

    public OrdersDto getOrdersDto(OrdersDto ordersDto){ return postMapper.getOrdersDto(ordersDto);}

    public int getTotalFee(int pId){return postMapper.getTotalFee(pId);}

    public int getUserFee(HashMap<String, Object> ordersInfo){return postMapper.getUserFee(ordersInfo);}

    public int getUserMenuPrice(HashMap<String, Object> ordersInfo){return postMapper.getUserMenuPrice(ordersInfo);}

    public int getUserPoint(int uId){ return postMapper.getUserPoint(uId);}

    public void updateUserPoint(HashMap<String, Object> nanumMemberInfo){postMapper.updateUserPoint(nanumMemberInfo);}

    public UserDto getUserInfo(int uId) {return postMapper.getUserInfo(uId);}

    public List<MainPostDto> getPostAll(MainPageDto mainPageDto){ return postMapper.getPostAll(mainPageDto);}

    public List<HashMap<String, Object>> getChatList(int uId){ return postMapper.getChatList(uId);}

    public void chargePoint(HashMap<String, Object> paymentInfo){
        String impUid = (String) paymentInfo.get("imp_uid");
        String merchantUid = (String) paymentInfo.get("merchant_uid");

        //access token 발급
        String url = "https://api.iamport.kr/users/getToken";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("imp_key", imp_key);
        body.add("imp_secret", imp_secret);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        HttpEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        System.out.println(response.toString());

        //결제 정보 조회
        /*
        String access_token = response.getBody();
        url = "https://api.iamport.kr/payments/" + impUid;

        RestTemplate restTemplate2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();
        headers2.add("Authorization", access_token);
        HttpEntity request = new HttpEntity(headers2);

        ResponseEntity<String> response2 = restTemplate.exchange(url, HttpMethod.GET, request, String.class );

        String paymentData = response2.getBody();
        */
        //결제 검증

    }
    ;
}

