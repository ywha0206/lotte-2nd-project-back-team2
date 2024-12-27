package com.backend.entity.page;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class PageProperties {

    @Id
    @GeneratedValue
    private UUID id; // 속성 고유 ID

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private Page page; // 소속 페이지

    @Column(nullable = false)
    private String name; // 속성 이름 (예: 태그, 마감일)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType type; // 속성 타입 (TEXT, NUMBER, DATE 등)

    @Lob
    private String value; // 속성 값 (JSON 형태로 저장 가능)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 속성 생성일

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 속성 마지막 수정일

    // 기본 생성자
    public PageProperties() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    // 속성 타입 Enum
    public enum PropertyType {
        TEXT, NUMBER, DATE, BOOLEAN, MULTI_SELECT
    }
}
