package com.capstone.controller;

import com.capstone.dto.*;
import com.capstone.service.NanumServiceImpl;
import com.capstone.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class PostController {
    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private NanumServiceImpl nanumService;

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

    @PostMapping("/chat")
    public CategoryPlaceDto setNanumPlace(@RequestBody Map<String, String> param) throws JSONException {
        int pId = Integer.parseInt(param.get("pId"));;
        ArrayList<Double> center;
        List<NanumMemberPosDto> nanumMemberPosDtoList;

        //모집 마감 처리
        postService.updateDonePost(pId);

        //나눔 멤버 리스트 조회
        nanumMemberPosDtoList = nanumService.getNanumMembersPos(pId);

        //멤버 없는 경우
        if (nanumMemberPosDtoList.size()==0)
            return null;


        //포인트 차감
        int memberListSize = nanumMemberPosDtoList.size();
        for(int i=0; i<memberListSize; i++){
            HashMap<String, Object> nanumMemberInfo = new HashMap<String, Object>();
            nanumMemberInfo.put("uId", nanumMemberPosDtoList.get(i).getU_id());
            HashMap<String, Object> orderInfo = new HashMap<String, Object>();
            orderInfo.put("pId", pId);
            orderInfo.put("uId", nanumMemberPosDtoList.get(i).getU_id());
            int userFee = postService.getUserFee(orderInfo);
            int userMenuPrice = postService.getUserMenuPrice(orderInfo);
            int userPoint = postService.getUserPoint(nanumMemberPosDtoList.get(i).getU_id());
            nanumMemberInfo.put("resultFee", userPoint - (userFee + userMenuPrice));
            postService.updateUserPoint(nanumMemberInfo);
        }

        //멤버 한 명인 경우
        if (nanumMemberPosDtoList.size()==1){
            UserDto userDto = postService.getUserInfo(nanumMemberPosDtoList.get(0).getU_id());
            CategoryPlaceDto categoryPlaceDto = new CategoryPlaceDto(
                    userDto.getName() + " 사용자의 위치",
                    "",
                    userDto.getRoad_address(),
                    "",
                    userDto.getU_x(),
                    userDto.getU_y(),
                    0
            );
            return categoryPlaceDto;
        }

        //shooting_user 있는 경우 > 나눔 위치 계산 X
        PostDto postDto = postService.getPostInfo(pId);
        if(postDto.getShooting_user() != null){
            UserDto shootingUserDto = postService.getShootingUserInfo(pId);
            CategoryPlaceDto categoryPlaceDto = new CategoryPlaceDto(
                    shootingUserDto.getName() + " 사용자의 위치",
                    "",
                    shootingUserDto.getRoad_address(),
                    "",
                    shootingUserDto.getU_x(),
                    shootingUserDto.getU_y(),
                    0
            );
            return categoryPlaceDto;
        }

        //중심점 계산
        center=nanumService.setMembersCenter(nanumMemberPosDtoList);

        //카테고리별 장소 검색
        ArrayList<CategoryPlaceDto> categoryPlaceDtos= nanumService.getCategoryPlace(center.get(0),center.get(1));
        Collections.sort(categoryPlaceDtos);

        CategoryPlaceDto nanumPlace = nanumService.setPlace(nanumMemberPosDtoList, categoryPlaceDtos);

        // DB에 저장
        HashMap<String, Object> nanumPlaceInfo = new HashMap<String, Object>();
        nanumPlaceInfo.put("pId", pId);
        nanumPlaceInfo.put("x", nanumPlace.getX());
        nanumPlaceInfo.put("y", nanumPlace.getY());
        postService.updateNanumPlace(nanumPlaceInfo);

        return nanumPlace;
    }

    @PutMapping("/chat")
    public HashMap<String, Object>  setShooting(@RequestBody ShootingInfoDto shootingInfoDto) {
        //내가 쏜다 기능
        postService.updateShootingPost(shootingInfoDto);                    //post 테이블 업데이트
        postService.updateShootingOrders(shootingInfoDto.getP_id());        //orders 테이블 업데이트 - shooting_user가 아닌 참여자들의 fee를 0으로
        postService.updateShootingUserOrders(shootingInfoDto.getP_id());    //orders 테이블 업데이트 - shooting_user인 참여자의 fee를 total_fee로

        HashMap<String, Object> ResponseHashMap = new HashMap<String, Object>();
        ResponseHashMap.put("uId", shootingInfoDto.getU_id());
        ResponseHashMap.put("total_fee", postService.getTotalFee(shootingInfoDto.getP_id()));
        return ResponseHashMap;
    }

    @GetMapping("/chat")
    public List<NanumMemberPosDto> getNanumMemberPos(@RequestParam int pId) {
        return postService.getNanumMemberPos(pId);
    }

    @GetMapping("/chat/orders")
    public List<OrdersDto> getNanumOrders(@RequestParam int pId){
        return nanumService.getNanumOrders(pId);
    }

    @GetMapping("/chat/members")
    public List<UserDto> getAllNanumMembers(@RequestParam int pId){
        return nanumService.getAllNanumMembers(pId);
    }

    @PutMapping("/chat/orders")
    public OrdersDto updateNanumOrders(@RequestBody OrdersDto ordersDto){
        // 주문서 수정
        HashMap<String, Object> userOrderInfo = new HashMap<String, Object>();
        userOrderInfo.put("pId", ordersDto.getP_id());
        userOrderInfo.put("uId", ordersDto.getU_id());
        int prevPrice = postService.getUserMenuPrice(userOrderInfo);
        int prevTotalFee = postService.getTotalFee(ordersDto.getP_id());

        nanumService.updateNanumOrders(ordersDto);  //price, menu, request 업데이트

        int tempPrice = postService.getUserMenuPrice(userOrderInfo);

        ordersDto.setPrice(-prevPrice);
        postService.updateTotalPoint(ordersDto);
        ordersDto.setPrice(tempPrice);
        postService.updateTotalPoint(ordersDto);    //total_point 업데이트

        int totalPoint = postService.getTotalPoint(ordersDto.getP_id());
        int totalFee = 0;
        int userOrderFee = 0;   //현재 주문서 작성한 참여자가 내야할 배달비
        //해당 나눔 게시글의 식당 배달비 정보 반환
        String orderFee = postService.getOrderFee(ordersDto.getP_id());
        String[] orderFeeList = orderFee.split("\\.");
        int listSize = orderFeeList.length;
        for(int i=0; i<listSize; i++){
            String[] tmpOrderFee = orderFeeList[i].split(",");
            int price = Integer.parseInt(tmpOrderFee[0].substring(1));  //주문금액
            int fee = Integer.parseInt(tmpOrderFee[1].substring(0,tmpOrderFee[1].length()-1));     //배달비
            if (totalPoint >= price) {
                //모인 금액에 따른 현재 배달비(total_fee) 업데이트
                HashMap<String, Object> totalFeeInfo = new HashMap<String, Object>();
                totalFeeInfo.put("pId", ordersDto.getP_id());
                totalFeeInfo.put("totalFee", fee);
                totalFee = fee;
                postService.updateTotalFee(totalFeeInfo);
            }
        }

        if(prevTotalFee == totalFee)
            return ordersDto;

        //현재 나눔 참여시 내야할 배달비(postFee), 현재 각 나눔 참여자가 부담하는 배달비(userOrderFee) 업데이트
        PostDto postDto = postService.getPostInfo(ordersDto.getP_id());
        if(postDto.getShooting_user() != null)
            postService.updateShootingPostFee(ordersDto.getP_id());     //shooting_user 있을 시 postFee 업데이트
        else{
            //postFee 업데이트
            List<OrdersDto> ordersDtoList = postService.getUserList(ordersDto.getP_id());
            int userNum = ordersDtoList.size();
            userOrderFee = totalFee / userNum;
            int postFee = totalFee / (userNum+1);
            HashMap<String, Object> postFeeInfo = new HashMap<String, Object>();
            postFeeInfo.put("pId", ordersDto.getP_id());
            postFeeInfo.put("postFee", postFee);
            postService.updatePostFee(postFeeInfo);

            // userOrderFee 업데이트
            List<Integer> postUserIdList = postService.getPostUserId(ordersDto.getP_id());  //해당 게시글에 참여한 사용자 목록
            int postUserIdListSize = postUserIdList.size();
            for(int i=0; i<postUserIdListSize; i++) {
                HashMap<String, Object> userOrderFeeInfo = new HashMap<String, Object>();
                userOrderFeeInfo.put("pId", ordersDto.getP_id());
                userOrderFeeInfo.put("uId", postUserIdList.get(i));
                userOrderFeeInfo.put("userOrderFee", userOrderFee);
                postService.updateUserOrderFee(userOrderFeeInfo);
            }
        }
        return ordersDto;
    }

    @GetMapping("/chat/fee")
    public int getNanumInfo(@RequestBody HashMap<String, Object> orderInfo) {
        //uId, pId로 현재 나의 배달비 반환
        return postService.getUserFee(orderInfo);
    }

    @GetMapping("/chat/list")
    public List<HashMap<String, Object>> getChatList(@RequestParam int uId) {
        return postService.getChatList(uId);
    }

    @GetMapping("/chat/list/detail")
    public HashMap<String, Object> chatListDetailInfo (@RequestParam int pId) {
        //title, 전체 배달비
        return postService.getChatListDetailInfo(pId);
    }
}
