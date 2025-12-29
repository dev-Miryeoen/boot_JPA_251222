package com.example.demo.repository;

import com.example.demo.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {
    /* type, keyword, pageable 주고, Page<Board>를 리턴 */
    Page<Board> searchBoard(String type, String keyword, Pageable pageable);
}
