package com.example.demo.repository;

import com.example.demo.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository<Entity, Id Type>
public interface CommentRepository extends JpaRepository<Comment, Long> {
    /* findBy** => ** 테이블 안에 있는 모든 칼럼
     *  select * from comment where ** = ?  */

    // 기본키 (ID)가 아닌 일반 칼럼은 등록을 해야 사용할 수 있음.
//    List<Comment> findByBno(Long bno); // page 없을 경우
    Page<Comment> findByBno(long bno, Pageable pageable);
}
