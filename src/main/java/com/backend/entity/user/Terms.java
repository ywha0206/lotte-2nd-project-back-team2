package com.backend.entity.user;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Entity
@Table(name = "terms")
public class Terms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "terms_id")
    private Long id;
    @Column(name = "terms_title")
    private String title;
    @Column(name = "terms_content", columnDefinition = "TEXT")
    private String content;
    @Builder.Default
    @Column(name = "terms_necessary")
    private int necessary = 1;
}
