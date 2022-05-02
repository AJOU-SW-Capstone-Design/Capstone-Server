package com.capstone.service;

import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;
import com.capstone.mapper.NanumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NanumServiceImpl {
    @Autowired
    private final NanumMapper nanumMapper;

    public NanumServiceImpl(NanumMapper nanumMapper) {
        this.nanumMapper = nanumMapper;
    }

    public List<NanumMemberDto> getAllNanumMembers(int pId){ return nanumMapper.getAllNanumMembers(pId);}

    public List<NanumMemberPosDto> getNanumMembersPos(int pId){ return nanumMapper.getNanumMembersPos(pId);}

    public ArrayList<Double> setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList){
        int memberNum=0;
        double sum_x=0;
        double sum_y=0;
        ArrayList<Double> center = new ArrayList<>();
        memberNum=nanumMemberPosDtoList.size();
        for (int i=0; i<memberNum; i++){
            sum_x += nanumMemberPosDtoList.get(i).getUX();
            sum_y += nanumMemberPosDtoList.get(i).getUY();
        }
        center.add(0,Math.round(sum_x/memberNum * 10) / 10.0);
        center.add(1,Math.round(sum_y/memberNum * 10) / 10.0);

        return center;
    };

}