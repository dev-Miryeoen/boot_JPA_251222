package com.example.demo.repository;

import com.example.demo.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

/* JPA 기능을 처리하는 인터페이스 */
/* JpaRepository 상속 받아서 사용 */
/* JpaRepositoey<테이블명, id Type> id Type : 클래스 타입이어야만 함 */
public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {
    // 일반 DB 사용시 repository에서 할 일은 없음.

}
