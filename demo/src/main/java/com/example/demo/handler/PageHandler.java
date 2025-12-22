package com.example.demo.handler;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.domain.Page;

import java.util.List;

@ToString
@Getter
@Setter
public class PageHandler<T> {
    private int startPage;
    private int endPage;
    private int totalPage;
    private long totalElement; // 전체 게시글 수
    private int pageNo;
    private boolean prev, next;

    private List<T> list;

    // 생성자
    public PageHandler(Page<T> list, int pageNo){
        this.list = list.getContent();
        this.pageNo = pageNo;
        this.totalPage = list.getTotalPages();
        this.totalElement = list.getTotalElements();

        this.endPage = (int)Math.ceil(this.pageNo / 10.0)*10;
        this.startPage = endPage - 9;

        endPage = (endPage > totalPage) ? totalPage : endPage;

        this.prev = this.startPage > 1;
        this.next = this.endPage < this.totalPage;
    }
}
