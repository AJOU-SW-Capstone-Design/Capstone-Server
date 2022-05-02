package com.capstone.mapper;

import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NanumMapper {
    public List<NanumMemberDto> getAllNanumMembers(int pId);
    public List<NanumMemberPosDto> getNanumMembersPos(int pId);
}
