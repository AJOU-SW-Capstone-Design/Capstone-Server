package com.capstone.service;

import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;

import java.util.List;

public interface NanumService {
    List<NanumMemberDto> getAllNanumMembers(int pId);
    List<NanumMemberPosDto> getNanumMembersPos(int pId);
    int setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList);

}
