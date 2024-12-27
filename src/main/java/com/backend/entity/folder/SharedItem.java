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
@Table(name = "shared_item")
public class SharedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 공유 항목의 고유 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType itemType; // 공유 항목 유형 (FILE 또는 FOLDER)

    @Column(nullable = false)
    private Long itemId; // 공유된 파일 또는 폴더의 ID

    @ManyToOne
    @JoinColumn(name = "shared_by", nullable = false)
    private User sharedBy; // 공유한 사용자

    @ManyToOne
    @JoinColumn(name = "shared_with", nullable = false)
    private User sharedWith; // 공유받은 사용자

    @Column(nullable = false)
    private int permissions; // 권한 (r: 읽기, rw: 읽기/쓰기)

    @CreationTimestamp
    private LocalDateTime sharedAt; // 공유된 시간

    public enum ItemType {
        FILE,   // 파일
        FOLDER  // 폴더
    }
}
