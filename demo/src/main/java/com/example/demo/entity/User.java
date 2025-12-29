package com.example.demo.entity;

import com.example.demo.dto.AuthUserDTO;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString(exclude = "authList")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User extends TimeBase{

    @Id
    private String email;
    @Column(nullable = false)
    private String pwd;
    @Column(name = "nick_name")
    private String nickName;
    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    /* mappedBy : 연관관계의 주  Auth_user의 user 필드와 연결
    *  JPA는 주인이 아닌쪽에서는 조회만 가능하도록 관리
    *  - cascade : User를 지정할 때 Auth_user도 같이 저장되도록 설계
    *  - orphanRemoval = true : 삭제시 같이 제거
    * */
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AuthUser> authList = new ArrayList<>();
    
    // 편의 메서드
    public void addAuth(AuthRole role){
        if(this.authList == null){
            this.authList = new ArrayList<>();
        }
        this.authList.add(AuthUser.builder()
                .user(this)
                .auth(role)
                .build());
    }
}
