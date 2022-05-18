package com.capstone.mapper;

import com.capstone.dto.NanumMemberPosDto;
import com.capstone.dto.OrdersDto;
import com.capstone.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface NanumMapper {
    public List<UserDto> getAllNanumMembers(int pId);
    public List<NanumMemberPosDto> getNanumMembersPos(int pId);
    public List<OrdersDto> getNanumOrders(int pId);
}
