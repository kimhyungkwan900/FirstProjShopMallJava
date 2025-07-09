package com.example.shop_mall_back.admin.faq.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@Getter
public class PageResponseDto <E>{

    private List<E> dtoList; //현재 데이터 페이지

    private List<Integer> pageNumList; //페이지 번호들

    private PageRequestDto pageRequestDto; //요청 정보

    private boolean prev, next; //이전페이지, 다음페이지 그룹 여부

    private int totalCount, prevPage, nextPage, totalPage, current; //전체 데이터 수, 클릭시 이동할 페이지, 전체페이지수, 현재 페이지 번호

    @Builder(builderMethodName = "withAll")
    public PageResponseDto(List<E> dtoList, PageRequestDto pageRequestDto, long totalCount) {
        this.dtoList = dtoList; //현재 페이지에 들어갈 데이터 리스트
        this.pageRequestDto = pageRequestDto; //페이지 요청 정보(몇 페이지, 한 페이지에 몇개)
        this.totalCount = (int)totalCount; //전체 데이터 개수

        //1. 현재 페이지 그룹의 마지막 페이지
        int end =   (int)(Math.ceil( pageRequestDto.getPage() / 10.0 )) *  10;

        //2. 시작페이지
        int start = end - 9;

        //3. 전체 마지막 페이지
        int last =  (int)(Math.ceil((totalCount/(double)pageRequestDto.getSize())));
        end = Math.min(end, last);

        //4. prev, next 버튼 여부
        this.prev = start > 1;

        this.next =  totalCount > end * pageRequestDto.getSize();

        //5. 페이지 번호 리스트
        this.pageNumList = IntStream.rangeClosed(start, end)
                .boxed()
                .collect(Collectors.toList());

        // 6. 이동할 prevPage, nextPage 계산
        if(prev) {
            this.prevPage = start -1;
        }

        if(next) {
            this.nextPage = end + 1;
        }

        //7. 현재 위치
        this.current = pageRequestDto.getPage(); //현재 페이지 번호
        this.totalPage = this.pageNumList.size();  //페이지 개수
    }


}