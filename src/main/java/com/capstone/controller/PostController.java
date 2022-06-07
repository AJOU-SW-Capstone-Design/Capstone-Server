package com.capstone.controller;

import com.capstone.configuration.properties.KakaoProperties;
import com.capstone.dto.*;
import com.capstone.service.NanumService;
import com.capstone.service.NanumServiceImpl;
import com.capstone.service.PostService;
import com.capstone.service.PostServiceImpl;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.maven.model.Model;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    @GetMapping("/post/expectTime")
    public String getExpectedTime(@RequestBody HashMap<String, String> param) {
        String UrlData = "http://3.39.125.17:5000/getExpectedTime";
        String rName = param.get("r_name");
        String orderTime = param.get("order_time");
        String ParamData = "{ \"rName\" : \""+rName+"\", \"orderTime\" : \""+orderTime+"\"}";

        //http 요청 시 필요한 url 주소를 변수 선언
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()){
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"POST BODY JSON");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+UrlData);
            System.out.println("http 요청 데이터 : "+ParamData);
            System.out.println("");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnData;
    }

    @PutMapping("/post")
    public HashMap<String, Object> createOrders(@RequestBody OrdersDto ordersDto){
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

        OrdersDto ordersDto1 = postService.getOrdersDto(ordersDto);
        HashMap<String, Object> ordersDtoAndFee = new HashMap<String, Object>();
        ordersDtoAndFee.put("p_id", ordersDto1.getP_id());
        ordersDtoAndFee.put("u_id", ordersDto1.getU_id());
        ordersDtoAndFee.put("menu", ordersDto1.getMenu());
        ordersDtoAndFee.put("price", ordersDto1.getPrice());
        ordersDtoAndFee.put("request", ordersDto1.getRequest());
        ordersDtoAndFee.put("fee", ordersDto1.getFee());
        ordersDtoAndFee.put("total_fee", totalFee);

        return ordersDtoAndFee;
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

    public boolean autoOrder(String plRoadAddress, String plName, String ordererPhone, String orderUrl, String orderList) {
        String UrlData = "http://3.39.125.17:5000/autoOrder";

        String ParamData = "{ \"plRoadAddress\" : \""+plRoadAddress+"\", \"plName\" : \""+plName+"\", \"ordererPhone\" : \""+ordererPhone+"\", \"orderUrl\" : \""+orderUrl+"\", \"orderList\" : \""+orderList+"\"}";

        //http 요청 시 필요한 url 주소를 변수 선언
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()){
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"POST BODY JSON");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+UrlData);
            System.out.println("http 요청 데이터 : "+ParamData);
            System.out.println("");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //주문 들어갔는지 확인
        boolean success;
        if(returnData == "" || returnData == "https://nid.naver.com/nidlogin.login")
            success = false;
        else
            success = true;
        return success;
    }

    @PostMapping("/chat")
    public HashMap<String, Object>  setNanumPlace(@RequestBody Map<String, String> param) throws JSONException, InterruptedException {
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
            String plRoadAddress = categoryPlaceDto.getRoad_address_name();
            String plName = categoryPlaceDto.getPlace_name() + " 앞";
            String ordererPhone = postService.getOrdererPhone(pId);
            String orderUrl = postService.getOrderUrl(pId);
            List<HashMap> orderListHashMap = postService.getOrderList(pId);
            String orderList = "";
            for(int i=0; i< orderListHashMap.size(); i++) {
                orderList += orderListHashMap.get(i).toString();
                orderList += " AND ";
            }
            boolean success = autoOrder(plRoadAddress, plName, ordererPhone, orderUrl, orderList);
            HashMap<String, Object> placeAndSuccessInfo = new HashMap<String, Object>();
            placeAndSuccessInfo.put("nanumPlace", categoryPlaceDto);
            placeAndSuccessInfo.put("success", success);

            return placeAndSuccessInfo;
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
            String plRoadAddress = categoryPlaceDto.getRoad_address_name();
            String plName = categoryPlaceDto.getPlace_name() + " 앞";
            String ordererPhone = postService.getOrdererPhone(pId);
            String orderUrl = postService.getOrderUrl(pId);
            List<HashMap> orderListHashMap = postService.getOrderList(pId);
            String orderList = "";
            for(int i=0; i< orderListHashMap.size(); i++) {
                orderList += orderListHashMap.get(i).toString();
                orderList += " AND ";
            }
            boolean success = autoOrder(plRoadAddress, plName, ordererPhone, orderUrl, orderList);
            HashMap<String, Object> placeAndSuccessInfo = new HashMap<String, Object>();
            placeAndSuccessInfo.put("nanumPlace", categoryPlaceDto);
            placeAndSuccessInfo.put("success", success);

            return placeAndSuccessInfo;
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
        
        // flask 코드 부르기
//        테스트 데이터
//        String plRoadAddress = "경기 수원시 팔달구 권광로340번길 79";
//        String plName = "GS25 우만파크점 앞";
//        String ordererPhone = "01041236951";
//        String orderUrl = "https://www.yogiyo.co.kr/mobile/#/452810/";
//        String orderList = "{request=, price=6100, menu=1리터보틀봉내.아메리카노} AND {request=사과잼 빼고 주세요, price=3500, menu=아메리칸애플생크림와플} AND {request=4등분 해주세요, price=5500, menu=카야버터 토스트} AND ";

        String plRoadAddress = nanumPlace.getAddress_name();
        String plName = nanumPlace.getPlace_name() + " 앞";
        String ordererPhone = postService.getOrdererPhone(pId);
        String orderUrl = postService.getOrderUrl(pId);
        List<HashMap> orderListHashMap = postService.getOrderList(pId);
        String orderList = "";
        for(int i=0; i< orderListHashMap.size(); i++) {
            orderList += orderListHashMap.get(i).toString();
            orderList += " AND ";
        }

        boolean success = autoOrder(plRoadAddress, plName, ordererPhone, orderUrl, orderList);

        HashMap<String, Object> placeAndSuccessInfo = new HashMap<String, Object>();
        placeAndSuccessInfo.put("nanumPlace", nanumPlace);
        placeAndSuccessInfo.put("success", success);

        return placeAndSuccessInfo;
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
    public List<OrdersDto> getOrders(@RequestParam int pId){
        return postService.getOrders(pId);
    }

    @GetMapping("/chat/members")
    public List<UserDto> getAllNanumMembers(@RequestParam int pId){
        return nanumService.getAllNanumMembers(pId);
    }

    @PutMapping("/chat/orders")
    public OrdersDto updateOrders(@RequestBody OrdersDto ordersDto){
        // 주문서 수정
        HashMap<String, Object> userOrderInfo = new HashMap<String, Object>();
        userOrderInfo.put("pId", ordersDto.getP_id());
        userOrderInfo.put("uId", ordersDto.getU_id());
        int prevPrice = postService.getUserMenuPrice(userOrderInfo);
        int prevTotalFee = postService.getTotalFee(ordersDto.getP_id());


        if (ordersDto.getMenu() == "" && ordersDto.getPrice() == 0)
            return ordersDto;
        else if(ordersDto.getMenu() == "")
            postService.updateOrdersPrice(ordersDto);   //menu는 안 바뀐 경우 -> price만 업데이트
        else if (ordersDto.getPrice() == 0)
            postService.updateOrdersMenu(ordersDto);    //price는 안 바뀐 경우 -> menu만 업데이트
        else
            postService.updateOrders(ordersDto);  //price, menu 업데이트

        int tempPrice = postService.getUserMenuPrice(userOrderInfo);

        int totalPoint = postService.getTotalPoint(ordersDto.getP_id());
        int totalFee = 0;
        int userOrderFee = 0;   //현재 주문서 작성한 참여자가 내야할 배달비

        if (prevPrice != tempPrice) {
            ordersDto.setPrice(-prevPrice);
            postService.updateTotalPoint(ordersDto);
            ordersDto.setPrice(tempPrice);
            postService.updateTotalPoint(ordersDto);    //total_point 업데이트

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

    @GetMapping("/chat/list/detail")
    public HashMap<String, Object> chatListDetailInfo (@RequestParam int pId) {
        //title, 전체 배달비
        return postService.getChatListDetailInfo(pId);
    }

    @GetMapping("/chat/nanumPlaceInfo")
    public HashMap<String, Object> getNanumPlaceInfo (@RequestParam int pId) {
        HashMap<String, Object> nanumPlaceInfo = new HashMap<String, Object>();
        nanumPlaceInfo.put("x", postService.getPLocationX(pId));
        nanumPlaceInfo.put("y", postService.getPLocationY(pId));
        return nanumPlaceInfo;
    }

    @GetMapping("/test/flask")
    public void flaskTest2() {
        String UrlData = "http://3.39.125.17:5000/autoOrder";
//        String plRoadAddress = "경기 수원시 팔달구 권광로340번길 79";
//        String plName = "GS25 우만파크점 앞";
//        String ordererPhone = "01041236951";
//        String orderUrl = "https://www.yogiyo.co.kr/mobile/#/452810/";
//        String orderList = "{request=, price=6100, menu=1리터보틀봉내.아메리카노} AND {request=사과잼 빼고 주세요, price=3500, menu=아메리칸애플생크림와플} AND {request=4등분 해주세요, price=5500, menu=카야버터 토스트} AND ";

        int pId = 202;
        String plRoadAddress ="경기 수원시 팔달구 권광로340번길 79";
        String plName = "GS25 우만파크점 앞";
        String ordererPhone = postService.getOrdererPhone(pId);
        String orderUrl = postService.getOrderUrl(pId);
        List<HashMap> orderListHashMap = postService.getOrderList(pId);
        String orderList = "";
        for(int i=0; i< orderListHashMap.size(); i++) {
            orderList += orderListHashMap.get(i).toString();
            orderList += " AND ";
        }

        //        String ParamData = "{ \"plRoadAddress\" : \""+plRoadAddress+"\", \"plName\" : \""+plName+"\", \"ordererPhone\" : \""+ordererPhone+"\", \"orderUrl\" : \""+orderUrl+"\", \"orderList\" : \""+orderList+"\"}";
        boolean success = autoOrder(plRoadAddress, plName, ordererPhone, orderUrl, orderList);
        System.out.println("success = " + success);

        /*
        //http 요청 시 필요한 url 주소를 변수 선언
        String totalUrl = "";
        totalUrl = UrlData.trim().toString();

        //http 통신을 하기위한 객체 선언 실시
        URL url = null;
        HttpURLConnection conn = null;

        //http 통신 요청 후 응답 받은 데이터를 담기 위한 변수
        String responseData = "";
        BufferedReader br = null;
        StringBuffer sb = null;

        //메소드 호출 결과값을 반환하기 위한 변수
        String returnData = "";

        try {
            //파라미터로 들어온 url을 사용해 connection 실시
            url = new URL(totalUrl);
            conn = (HttpURLConnection) url.openConnection();

            //http 요청에 필요한 타입 정의 실시
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8"); //post body json으로 던지기 위함
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true); //OutputStream을 사용해서 post body 데이터 전송
            try (OutputStream os = conn.getOutputStream()){
                byte request_data[] = ParamData.getBytes("utf-8");
                os.write(request_data);
                os.close();
            }
            catch(Exception e) {
                e.printStackTrace();
            }

            //http 요청 실시
            conn.connect();
            System.out.println("http 요청 방식 : "+"POST BODY JSON");
            System.out.println("http 요청 타입 : "+"application/json");
            System.out.println("http 요청 주소 : "+UrlData);
            System.out.println("http 요청 데이터 : "+ParamData);
            System.out.println("");

            //http 요청 후 응답 받은 데이터를 버퍼에 쌓는다
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            sb = new StringBuffer();
            while ((responseData = br.readLine()) != null) {
                sb.append(responseData); //StringBuffer에 응답받은 데이터 순차적으로 저장 실시
            }

            //메소드 호출 완료 시 반환하는 변수에 버퍼 데이터 삽입 실시
            returnData = sb.toString();

            //http 요청 응답 코드 확인 실시
            String responseCode = String.valueOf(conn.getResponseCode());
            System.out.println("http 응답 코드 : "+responseCode);
            System.out.println("http 응답 데이터 : "+returnData);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //http 요청 및 응답 완료 후 BufferedReader를 닫아줍니다
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */
    }
}
