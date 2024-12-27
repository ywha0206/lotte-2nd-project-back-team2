package com.backend.entity.page;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class TableProperties {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "block_id", nullable = false)
    private Block table; // 표 블록

    @Column(nullable = false)
    private String name; // 컬럼 이름

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ColumnType type; // 컬럼 타입

    @Column(nullable = false,name = "table_properties_order")
    private int order; // 컬럼 순서

    public enum ColumnType {
        TEXT, NUMBER, DATE, BOOLEAN
    }

    // Getters and Setters
}
