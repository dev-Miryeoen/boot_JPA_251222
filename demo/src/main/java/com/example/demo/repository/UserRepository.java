package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String > {

    // email을 주고 User 객체와 Auth_user List를 같이 리턴
    @Query("select u from User u left join fetch u.authList where u.email = :email")
    Optional<User> findByEmailWithAuth(@Param("email") String email);

    // userList => 권한까지 같이
    // join fetch를 사용하여 Usser 와 authUser 를 같이 가져오기
    // user 1: N Auth  중복데이터 발생 => distinct로 제거

    @Query("select distinct u from User u left join fetch u.authList")
    List<User> findAllWithAuthList();
}