package com.capstone.service;

import com.capstone.configuration.properties.KakaoProperties;
import com.capstone.dto.CategoryPlaceDto;
import com.capstone.dto.NanumMemberDto;
import com.capstone.dto.NanumMemberPosDto;
import com.capstone.mapper.NanumMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class NanumServiceImpl {
    @Autowired
    private final NanumMapper nanumMapper;

    private final KakaoProperties kakaoProperties;

    public NanumServiceImpl(NanumMapper nanumMapper, KakaoProperties kakaoProperties) {
        this.nanumMapper = nanumMapper;
        this.kakaoProperties = kakaoProperties;
    }

    public List<NanumMemberDto> getAllNanumMembers(int p_id){ return nanumMapper.getAllNanumMembers(p_id);}

    public List<NanumMemberPosDto> getNanumMembersPos(int p_id){ return nanumMapper.getNanumMembersPos(p_id);}

    public ArrayList<Double> setMembersCenter(List<NanumMemberPosDto> nanumMemberPosDtoList){
        int memberNum=0;
        double sum_x=0;
        double sum_y=0;
        ArrayList<Double> center = new ArrayList<>();
        memberNum=nanumMemberPosDtoList.size();
        for (int i=0; i<memberNum; i++){
            sum_x += nanumMemberPosDtoList.get(i).getU_x();
            sum_y += nanumMemberPosDtoList.get(i).getU_y();
        }
        center.add(0,Math.round(sum_x/memberNum * 10000000) / 10000000.0);
        center.add(1,Math.round(sum_y/memberNum * 10000000) / 10000000.0);

        return center;
    };

    public ArrayList<CategoryPlaceDto> getCategoryPlace(@RequestParam Double x, Double y) throws JSONException {
        ArrayList<CategoryPlaceDto> categoryPlaceDtos = new ArrayList<>();
        String [] kakaoCategory={"CS2","FD6","CE7","SW8"};
        int totalPlaceNum=0;
        for(int i=0;i<kakaoCategory.length;i++) {
            int finalI = i;
            Mono<String> mono = WebClient.builder().baseUrl("https://dapi.kakao.com")
                    .build().get()
                    .uri(builder -> builder.path("/v2/local/search/category.json")
                            .queryParam("category_group_code", kakaoCategory[finalI])
                            .queryParam("x", x)
                            .queryParam("y", y)
                            .queryParam("radius", 300)
                            .build()
                    )
                    .header("Authorization", "KakaoAK " + kakaoProperties.getRestapi())
                    .exchangeToMono(response -> {
                        return response.bodyToMono(String.class);
                    });

            //JSON으로 형태 변경
            JSONObject jsonObject = new JSONObject(mono.block());

            try {
                JSONArray jsonArray = jsonObject.getJSONArray("documents");

                //카테고리별 검색할 장소 개수 설정
                if (kakaoCategory[i] == "SW8")
                    totalPlaceNum = 2;
                else
                    totalPlaceNum = 10;
                //검색한 개수가 검색하려고 했던 개수보다 적은 경우
                if (jsonArray.length()<totalPlaceNum)
                    totalPlaceNum=jsonArray.length();

                for (int j = 0; j < totalPlaceNum; j++) {
                    JSONObject explrObject = jsonArray.getJSONObject(j);
                    //하나의 장소마다 CategoryPlaceDto로 저장
                    CategoryPlaceDto categoryPlaceDto = new CategoryPlaceDto(
                            explrObject.get("place_name").toString(),
                            explrObject.get("category_group_name").toString(),
                            explrObject.get("road_address_name").toString(),
                            explrObject.get("address_name").toString(),
                            Double.parseDouble(explrObject.get("x").toString()),
                            Double.parseDouble(explrObject.get("y").toString()),
                            Double.parseDouble(explrObject.get("distance").toString())
                    );
                    categoryPlaceDtos.add(categoryPlaceDto);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return categoryPlaceDtos;
    }

}
