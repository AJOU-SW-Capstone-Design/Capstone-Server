package com.capstone.service;

import com.capstone.dto.*;
import com.capstone.mapper.PostMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.coyote.Response;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;


import java.util.*;

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

    private List<NameValuePair> convertParameter(Map<String,String> paramMap){
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        Set<Map.Entry<String,String>> entries = paramMap.entrySet();
        for(Map.Entry<String,String> entry : entries) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return paramList;
    }

    public int chargePoint(HashMap<String, Object> paymentInfo){
        String impUid = (String) paymentInfo.get("imp_uid");
        String merchantUid = (String) paymentInfo.get("merchant_uid");

        //access token 발급
        String url = "https://api.iamport.kr/users/getToken";

        String result = "";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        Map<String,String> m =new HashMap<String,String>();
        m.put("imp_key", imp_key);
        m.put("imp_secret", imp_secret);
        try {
            post.setEntity(new UrlEncodedFormEntity(convertParameter(m)));
            HttpResponse res = client.execute(post);
            ObjectMapper mapper = new ObjectMapper();
            String body = EntityUtils.toString(res.getEntity());
            JsonNode rootNode = mapper.readTree(body);
            JsonNode resNode = rootNode.get("response");
            result = resNode.get("access_token").asText();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //결제 정보 조회
        String access_token = result;
        url = "https://api.iamport.kr/payments/" + impUid;

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", access_token);
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

        JSONObject jsonObject = new JSONObject(response.getBody());
        JSONObject paymentData = jsonObject.getJSONObject("response");

        System.out.println("paymentData = " + paymentData);

        if(paymentData.get("status") == "failed")
            return 0;

        HashMap<String, Object> chargeInfo = new HashMap<String, Object>();
        chargeInfo.put("uId", paymentInfo.get("uId"));
        chargeInfo.put("point", paymentData.get("amount"));
        postMapper.chargeUserPoint(chargeInfo);

        return postMapper.getUserPoint((Integer) paymentInfo.get("uId"));
        //결제 정보 저장
        /*
        HashMap<String, Object> chargeInfo = new HashMap<String, Object>();
        chargeInfo.put("uId", paymentInfo.get("uId"));
        chargeInfo.put("point", paymentData.);
        postMapper.chargeUserPoint(chargeInfo);

        boolean success = false;
        String status = paymentData.;
        switch (status) {
            case "ready": // 가상계좌 발급
                // DB에 가상계좌 발급 정보 저장
                String vbankNum = paymentData.;
                String vbankDate = paymentData.;
                String vbankName = paymentData.;
                HashMap<String, Object> vbankInfo = new HashMap<String, Object>();
                vbankInfo.put("vbankNum", vbankNum);
                vbankInfo.put("vbankDate", vbankDate);
                vbankInfo.put("vbankName", vbankName);
                postMapper.createVbank(vbankInfo);
                break;
            case "paid": // 결제 완료
                success = true;
                break;
        }
        return success;
        */
    }

    public HashMap<String, Object> getChatListDetailInfo(int pId) {return postMapper.getChatListDetailInfo(pId);}
    ;
}

