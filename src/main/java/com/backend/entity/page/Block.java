package com.backend.entity.page;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)  // UUID의 자동 생성 방식 명시
    private UUID id; // 블록 고유 ID

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private Page page; // 소속 페이지

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Block parent; // 부모 블록 (NULL이면 최상위 블록)

    @Column(nullable = false, name = "block_order")
    private int order; // 같은 부모 내 블록의 순서

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlockType type; // 블록 유형 (TEXT, IMAGE, TABLE 등)

    @Lob
    private String content; // 블록 내용 (JSON 또는 텍스트 형태)

    private String style; // 블록의 적용된 스타일 ( Heading1 ,heading2 ,heading3,,,)

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt; // 블록 생성일

    @Column(nullable = false)
    private LocalDateTime updatedAt; // 블록 마지막 수정일

    // 기본 생성자
    public Block() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters

    // 블록 타입 Enum
    public enum BlockType {
        TEXT, IMAGE, TABLE, VIDEO, ROW
    }
}