package com.capstone.service;

import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;

import java.util.List;

public interface NanumService {
    List<NanumMemberDto> getAllNanumMembers(int p_id);
    List<NanumMemberPosDto> getNanumMembersPos(int p_id);
    int setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList);

}
