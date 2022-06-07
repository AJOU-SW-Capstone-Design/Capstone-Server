package com.capstone;

import com.capstone.dto.CategoryPlaceDto;
import com.capstone.dto.NanumMemberPosDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static java.lang.Math.abs;

public class MyThread extends Thread{
    private String key;
    private int start;
    private int end;
    private List<NanumMemberPosDto> nanumMemberPosDtoList;
    private List<CategoryPlaceDto> categoryPlaceDtos;

    private double resultMin;
    private int resultMinIndex;

    public MyThread(int start, int end, List<NanumMemberPosDto> nanumMemberPosDtoList, List<CategoryPlaceDto> categoryPlaceDtos, String key) {
        this.start = start;
        this.end = end;
        this.nanumMemberPosDtoList = nanumMemberPosDtoList;
        this.categoryPlaceDtos = categoryPlaceDtos;
        this.key = key;
    }

    public int getWalkingTime(double startX, double startY, double endX, double endY){
        String url = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&format=json&callback=result";

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

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

    @Override
    public void run() {
        double min = Double.MAX_VALUE;
        double min_average = Double.MAX_VALUE;
        int min_index = 0;

        int memberNum = nanumMemberPosDtoList.size();
        int [] walkingTimeList = new int[memberNum];

        for(int i = start; i <= end; i++) {
            System.out.println("몇 번째: " + i);
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
            if (min_average > average)
                min_average = average;
            if (abs(min_average - average) > 100)
                continue;
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
        resultMin = min;
        resultMinIndex = min_index;
    }
    public double getResultMin(){
        return this.resultMin;
    }
    public int getResultMinIndex(){
        return this.resultMinIndex;
    }
}
