package com.example.demo.repository;

import com.example.demo.entity.Board;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.example.demo.entity.QBoard.board;

@Slf4j
public class BoardCustomRepositoryImpl implements BoardCustomRepository{

    private final JPAQueryFactory queryFactory;
    public BoardCustomRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Board> searchBoard(String type, String keyword, Pageable pageable) {
        /* select * from board
        * where title like '%aaa%'
        * where title like '%aaa%' or writer like '%aaa%'
        * where title like '%aaa%' or writer like '%aaa%' or content like '%aaa%'
        * BooleanExpression : 동적쿼리를 사용할 때 실행 여부 확인 객체 (필수)
        * */
        BooleanExpression condition = null;

        // 동적검색 조건 추가
        if(type != null && keyword != null){
            // 타입이 여러개 들어올 경우 배열로 처리
            String[] typeArr = type.split("");
            for(String t : typeArr){
                switch (t){
                    case "t" :
                        condition = (condition == null) ?
                                board.title.containsIgnoreCase(keyword) :
                                condition.or(board.title.containsIgnoreCase(keyword));
                        break;
                    case "w" :
                        condition = (condition == null) ?
                                board.writer.containsIgnoreCase(keyword) :
                                condition.or(board.writer.containsIgnoreCase(keyword));
                        break;
                    case "c" :
                        condition = (condition == null) ?
                                board.content.containsIgnoreCase(keyword) :
                                condition.or(board.content.containsIgnoreCase(keyword));
                        break;
                    default:  break;
                }
            }
        }

        // 쿼리 작성, 페이징 적용
        List<Board> result = queryFactory
                .selectFrom(board)
                .where(condition)
                .orderBy(board.bno.desc())
                .offset(pageable.getOffset()) // 번지 limit 0, 10
                .limit(pageable.getPageSize()) // 갯수
                .fetch();
        log.info(">>> condition >> {}", condition);
        log.info(">>> offset >> {}", pageable.getOffset());
        log.info(">>> result >> {}", result);

        // 검색 데이터를 반영한 전체 개수 조회
        long total = queryFactory
                .selectFrom(board)
                .where(condition)
                .fetch().size();

        return new PageImpl<>(result, pageable, total);
    }
}
