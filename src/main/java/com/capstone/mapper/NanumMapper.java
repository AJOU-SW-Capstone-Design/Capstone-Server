package com.capstone.mapper;

import com.capstone.dto.NanumMemberDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NanumMapper {
    public List<NanumMemberDto> getNanumMembers(int pId);

}
