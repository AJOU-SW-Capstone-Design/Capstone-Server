package com.capstone.service;

import com.capstone.dto.NanumMemberDto;
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

    public List<NanumMemberDto> getNanumMembers(int pId){ return nanumMapper.getNanumMembers(pId);}

}
