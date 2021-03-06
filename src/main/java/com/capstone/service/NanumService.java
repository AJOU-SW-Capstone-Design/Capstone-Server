package com.capstone.service;

import com.capstone.dto.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


public interface NanumService {
    //나눔 멤버 리스트 조회
    List<UserDto> getAllNanumMembers(int pId);

    //나눔 멤버들의 위치 좌표 조희
    List<NanumMemberPosDto> getNanumMembersPos(int pId);

    //나눔 멤버들의 중심점 설정
    ArrayList<Double> setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList);

    //카테고리별 장소 조회(중심점 기준)
    ArrayList<CategoryPlaceDto> getCategoryPlace(double x, double y);

    //보행자 도보시간 계산
    int getWalkingTime(double startX, double startY, double endX, double endY);
    
    //나눔 위치 선정
    CategoryPlaceDto setPlace(List<NanumMemberPosDto> nanumMemberPosDtoList, List<CategoryPlaceDto> categoryPlaceDtos);

    void blameUsers(BlameDto blameDto);
}
