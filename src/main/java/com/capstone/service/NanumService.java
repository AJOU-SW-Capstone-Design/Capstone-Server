package com.capstone.service;

import com.capstone.dto.NanumMemberDto;

import java.util.List;

public interface NanumService {
    List<NanumMemberDto> getNanumMembers(int pId);
    int setMembersCenter(List<NanumMemberDto> nanumMemberDtoList);

}
