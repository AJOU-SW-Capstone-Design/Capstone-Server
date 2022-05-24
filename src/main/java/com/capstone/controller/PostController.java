package com.capstone.controller;

import com.capstone.configuration.properties.KakaoProperties;
import com.capstone.dto.*;
import com.capstone.service.NanumServiceImpl;
import com.capstone.service.PostService;
import com.capstone.service.PostServiceImpl;
import org.apache.maven.model.Model;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@RestController
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @PostMapping("/main")
    public List<MainPostDto> getPost(@RequestBody MainPageDto mainPageDto) {
        //category 별 post 분류(category가 있으면 not null이라고 가정)
        if (mainPageDto.getCategory()!=null)
            return postService.getPostCategory(mainPageDto);
        else
            return postService.getPostAll(mainPageDto);
    }

    @PostMapping("/post")
    public PostDto insertPost(@RequestBody PostDto postDto){
        postService.createPost(postDto);
        return postDto;
    }

    @PutMapping("/post")
    public OrdersDto createOrders(@RequestBody OrdersDto ordersDto){
        postService.createOrders(ordersDto);

        //현재 모인 총 금액(total_point) 업데이트
        int pId = ordersDto.getP_id();
        postService.updateTotalPoint(ordersDto);
        int totalPoint = postService.getTotalPoint(pId);
        int totalFee = 0;
        int userOrderFee = 0;   //현재 주문서 작성한 참여자가 내야할 배달비
        //해당 나눔 게시글의 식당 배달비 정보 반환
        String orderFee = postService.getOrderFee(pId);
        String[] orderFeeList = orderFee.split("\\.");
        int listSize = orderFeeList.length;
        for(int i=0; i<listSize; i++){
            String[] tmpOrderFee = orderFeeList[i].split(",");
            int price = Integer.parseInt(tmpOrderFee[0].substring(1));  //주문금액
            int fee = Integer.parseInt(tmpOrderFee[1].substring(0,tmpOrderFee[1].length()-1));     //배달비
            if (totalPoint >= price) {
                //모인 금액에 따른 현재 배달비(total_fee) 업데이트
                HashMap<String, Object> totalFeeInfo = new HashMap<String, Object>();
                totalFeeInfo.put("pId", pId);
                totalFeeInfo.put("totalFee", fee);
                totalFee = fee;
                postService.updateTotalFee(totalFeeInfo);
            }
        }

        //현재 나눔 참여시 내야할 배달비(postFee), 현재 각 나눔 참여자가 부담하는 배달비(userOrderFee) 업데이트
        PostDto postDto = postService.getPostInfo(pId);
        if(postDto.getShooting_user() != null)
            postService.updateShootingPostFee(pId);     //shooting_user 있을 시 postFee 업데이트
        else{
            //postFee 업데이트
            List<OrdersDto> ordersDtoList = postService.getUserList(pId);
            int userNum = ordersDtoList.size();
            userOrderFee = totalFee / userNum;
            int postFee = totalFee / (userNum+1);
            HashMap<String, Object> postFeeInfo = new HashMap<String, Object>();
            postFeeInfo.put("pId", pId);
            postFeeInfo.put("postFee", postFee);
            postService.updatePostFee(postFeeInfo);

            // userOrderFee 업데이트
            List<Integer> postUserIdList = postService.getPostUserId(pId);  //해당 게시글에 참여한 사용자 목록
            int postUserIdListSize = postUserIdList.size();
            for(int i=0; i<postUserIdListSize; i++) {
                HashMap<String, Object> userOrderFeeInfo = new HashMap<String, Object>();
                userOrderFeeInfo.put("pId", pId);
                userOrderFeeInfo.put("uId", postUserIdList.get(i));
                userOrderFeeInfo.put("userOrderFee", userOrderFee);
                postService.updateUserOrderFee(userOrderFeeInfo);
            }
        }

        return postService.getOrdersDto(ordersDto);
    }

    @GetMapping("/post")
    public List<RestaurantDto> getSearchRestaurantList(@RequestParam String rName) {
        //검색한 키워드가 포함된 식당 정보 리스트 돌려주기
        return postService.getSearchRestaurantList(rName);
    }

    @GetMapping( "/main/search")
    public List<MainPostDto> searchPost(@RequestParam String keyword){
        return postService.searchPost(keyword);
    }

    @GetMapping("/main/detail")
    public DetailPostDto getDetailPost(@RequestParam int pId){ return postService.getDetailPost(pId);}

    @PostMapping("/payments/complete")
    public int chargePoint(@RequestBody HashMap<String, Object> paymentInfo) {
        return postService.chargePoint(paymentInfo);
    }

    @GetMapping("/payments/complete/mobile")
    public String orderCompleteMobile(
            @RequestParam(required = false) String imp_uid
            , @RequestParam(required = false) String merchant_uid
            , Model model
            , Locale locale
            , HttpSession session) throws IOException
    {

        System.out.println("hi");
        return "home";
    }

}
