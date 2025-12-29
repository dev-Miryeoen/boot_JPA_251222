package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(exclude = "user") // 양방향 참조시 순환 참조 방지
@Entity
@Table(name = "auth_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthUser {
    // M:1 (다대일 맵핑)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // String email 대신 User 객체를 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private User user; // DB의 FK 칼럼명

    @Enumerated(EnumType.STRING)
    private AuthRole auth; // Enum의 이름을 문자열로 저장
}
