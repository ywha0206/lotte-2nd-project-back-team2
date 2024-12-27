package com.backend.entity.folder;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Table(name = "drive_permission")
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 권한의 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 권한을 가진 사용자

    @Column(nullable = true)
    private String typeId; // folderId  or FileId

    @Column(nullable = false)
    private String type; // type  file/folder

    // 권한을 비트마스크로 저장
    @Column(nullable = false)
    private String permissions;

    @CreationTimestamp
    private LocalDateTime createdAt; // 권한 생성 날짜 및 시간

    private LocalDateTime updatedAt;
}
