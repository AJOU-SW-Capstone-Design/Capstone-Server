package com.capstone.service;

import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;
import com.capstone.dto.PostDto;
import com.capstone.dto.PlaceDto;
import com.capstone.mapper.NanumMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;

@Service
public class NanumServiceImpl {
    @Autowired
    private final NanumMapper nanumMapper;

    public NanumServiceImpl(NanumMapper nanumMapper) {
        this.nanumMapper = nanumMapper;
    }

    public List<NanumMemberDto> getAllNanumMembers(int p_id){ return nanumMapper.getAllNanumMembers(p_id);}

    public List<NanumMemberPosDto> getNanumMembersPos(int p_id){ return nanumMapper.getNanumMembersPos(p_id);}

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
        center.add(0,Math.round(sum_x/memberNum * 10) / 10.0);
        center.add(1,Math.round(sum_y/memberNum * 10) / 10.0);

        return center;
    };

    public int getWalkingTime(double startX, double startY, double endX, double endY){
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";
        String key = "l7xx26b87d65e609452ead9149299e4a2e46";

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

        String totalTimeStr = response.getBody().toString().split("\"totalTime\": ")[1].split(",")[0];
        int totalTime = Integer.parseInt(totalTimeStr);

        return totalTime;
    }

    public PlaceDto setPlace(List<NanumMemberPosDto> nanumMemberPosDtoList, List<PlaceDto> placeDtoList) {
        double min = Double.MAX_VALUE;
        int min_index = 0;

        int memberNum = nanumMemberPosDtoList.size();
        int [] walkingTimeList = new int[memberNum];
        int placeNum = placeDtoList.size();

        for(int i=0; i<placeNum; i++) {
            double x = placeDtoList.get(i).getX();
            double y = placeDtoList.get(i).getY();
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
        return placeDtoList.get(min_index);
    };
}
