package com.backend.entity.folder;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name = "drag_history")
public class DragHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 변경 기록의 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 드래그 앤 드랍을 수행한 사용자

    @Column(nullable = false)
    private Long itemId; // 드래그된 항목의 ID (파일 또는 폴더)

    @Column(nullable = false)
    private String itemType; // 항목 유형 ("FILE" 또는 "FOLDER")


    private String oldParent; // 드래그 이전의 부모 폴더

    private String newParent; // 드래그 이후의 부모 폴더

    @CreationTimestamp
    private LocalDateTime dragAt; // 드래그 작업이 수행된 시간
}

