package com.capstone.service;

import com.capstone.dto.CategoryPlaceDto;
import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;

import java.util.ArrayList;
import java.util.List;

public interface NanumService {
    //나눔 멤버 리스트 조회
    List<NanumMemberDto> getAllNanumMembers(int p_id);

    //나눔 멤버들의 위치 좌표 조희
    List<NanumMemberPosDto> getNanumMembersPos(int p_id);

    //나눔 멤버들의 중심점 설정
    int setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList);

    //카테고리별 장소 조회(중심점 기준)
    ArrayList<CategoryPlaceDto> getCategoryPlace(String x, String y);

}
