package com.backend.entity.page;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@Builder
@ToString
@Getter
public class Page {

    @Id
    @GeneratedValue
    private UUID id; // 페이지 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 페이지 소유 사용자

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Page parent; // 부모 페이지 (NULL이면 최상위 페이지)

    @Column(nullable = false)
    private String title; // 페이지 제목

    @Column(nullable = false)
    private boolean isShared; // 페이지 공유 여부

    @Column(nullable = false)
    private LocalDateTime createdAt; // 페이지 생성일

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 페이지 마지막 수정일

    // 기본 생성자
    public Page() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

}
