package com.capstone.service;

import com.capstone.configuration.properties.KakaoProperties;
import com.capstone.dto.CategoryPlaceDto;
import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;
import com.capstone.dto.PostDto;
import com.capstone.dto.UserPlaceDto;
import com.capstone.mapper.NanumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

@Service
public class NanumServiceImpl {
    @Autowired
    private final NanumMapper nanumMapper;

    private final KakaoProperties kakaoProperties;

    @Value("${tmap.api-key}")
    private String key;

    public NanumServiceImpl(NanumMapper nanumMapper, KakaoProperties kakaoProperties) {
        this.nanumMapper = nanumMapper;
        this.kakaoProperties = kakaoProperties;
    }

    public List<NanumMemberDto> getAllNanumMembers(int pId){ return nanumMapper.getAllNanumMembers(pId);}

    public List<NanumMemberPosDto> getNanumMembersPos(int pId){ return nanumMapper.getNanumMembersPos(pId);}

    public ArrayList<Double> setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList){
        int memberNum=0;
        double sum_x=0;
        double sum_y=0;
        ArrayList<Double> center = new ArrayList<>();
        memberNum=nanumMemberPosDtoList.size();
        for (int i=0; i<memberNum; i++){
            sum_x += nanumMemberPosDtoList.get(i).getU_x();
            sum_y += nanumMemberPosDtoList.get(i).getU_y();
        }
        center.add(0,Math.round(sum_x/memberNum * 10000000) / 10000000.0);
        center.add(1,Math.round(sum_y/memberNum * 10000000) / 10000000.0);

        return center;
    };

    public ArrayList<CategoryPlaceDto> getCategoryPlace(@RequestParam Double x, Double y) throws JSONException {
        ArrayList<CategoryPlaceDto> categoryPlaceDtos = new ArrayList<>();
        String [] kakaoCategory={"CS2","FD6","CE7","SW8"};
        int totalPlaceNum=0;
        for(int i=0;i<kakaoCategory.length;i++) {
            int finalI = i;
            Mono<String> mono = WebClient.builder().baseUrl("https://dapi.kakao.com")
                    .build().get()
                    .uri(builder -> builder.path("/v2/local/search/category.json")
                            .queryParam("category_group_code", kakaoCategory[finalI])
                            .queryParam("x", x)
                            .queryParam("y", y)
                            .queryParam("radius", 300)
                            .build()
                    )
                    .header("Authorization", "KakaoAK " + kakaoProperties.getRestapi())
                    .exchangeToMono(response -> {
                        return response.bodyToMono(String.class);
                    });

            //JSON으로 형태 변경
            JSONObject jsonObject = new JSONObject(mono.block());

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("documents");

                //카테고리별 검색할 장소 개수 설정
                if (kakaoCategory[i] == "SW8")
                    totalPlaceNum = 2;
                else
                    totalPlaceNum = 10;
                //검색한 개수가 검색하려고 했던 개수보다 적은 경우
                if (jsonArray.length()<totalPlaceNum)
                    totalPlaceNum=jsonArray.length();

                for (int j = 0; j < totalPlaceNum; j++) {
                    JSONObject explrObject = jsonArray.getJSONObject(j);
                    //하나의 장소마다 CategoryPlaceDto로 저장
                    CategoryPlaceDto categoryPlaceDto = new CategoryPlaceDto(
                            explrObject.get("place_name").toString(),
                            explrObject.get("category_group_name").toString(),
                            explrObject.get("road_address_name").toString(),
                            explrObject.get("address_name").toString(),
                            Double.parseDouble(explrObject.get("x").toString()),
                            Double.parseDouble(explrObject.get("y").toString()),
                            Double.parseDouble(explrObject.get("distance").toString())
                    );
                    categoryPlaceDtos.add(categoryPlaceDto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryPlaceDtos;
    }

    public int getWalkingTime(double startX, double startY, double endX, double endY){
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("appKey", key);
        body.add("startX", Double.toString(startX));
        body.add("startY", Double.toString(startY));
        body.add("endX", Double.toString(endX));
        body.add("endY", Double.toString(endY));
        body.add("reqCoordType", "WGS84GEO");
        body.add("resCoordType", "EPSG3857");
        body.add("startName", "출발지");
        body.add("endName", "도착지");

        HttpHeaders headers = new HttpHeaders();
        headers.add("appKey", key);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);
        HttpEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        try{
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.format("IOException: %s%n", e);
        }

        String totalTimeStr = response.getBody().toString().split("\"totalTime\": ")[1].split(",")[0];
        int totalTime = Integer.parseInt(totalTimeStr);

        return totalTime;
    }

    public CategoryPlaceDto setPlace(List<NanumMemberPosDto> nanumMemberPosDtoList, List<CategoryPlaceDto> categoryPlaceDtos) {
        double min = Double.MAX_VALUE;
        int min_index = 0;

        int memberNum = nanumMemberPosDtoList.size();
        int [] walkingTimeList = new int[memberNum];
        int placeNum = categoryPlaceDtos.size();

        for(int i=0; i<placeNum; i++) {
            double x = categoryPlaceDtos.get(i).getX();
            double y = categoryPlaceDtos.get(i).getY();
            for (int j = 0; j < memberNum; j++) {
                double u_x = nanumMemberPosDtoList.get(j).getU_x();
                double u_y = nanumMemberPosDtoList.get(j).getU_y();
                int walkingTime = getWalkingTime(u_x, u_y, x, y);   // tmap api 사용하여, 사용자 - 대표장소 간 도보거리 계산
                walkingTimeList[j] = walkingTime;
            }
            int sum = 0;
            for(int k = 0; k < memberNum; k++)
                sum += walkingTimeList[k];

            double average = (double) (sum / memberNum);    // 사용자 - 대표장소 간 도보거리의 평균
            double sumOfDeviation = 0;
            for(int k = 0; k < memberNum; k++) {
                double deviation = abs(average - walkingTimeList[k]);   // 편차
                sumOfDeviation += deviation;    //편차의 합
            }
            // 편차의 합이 제일 작은 곳을 대표장소로
            if (sumOfDeviation < min){
                min = sumOfDeviation;
                min_index = i;
            }
        }
        return categoryPlaceDtos.get(min_index);
    };
}
