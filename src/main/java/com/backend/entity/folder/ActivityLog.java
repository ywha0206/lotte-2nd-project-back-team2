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
@Table(name = "activity_log")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 로그의 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 작업을 수행한 사용자

    @ManyToOne
    @JoinColumn(name = "file_id")
    private File file; // 작업 대상 파일 (NULL이면 폴더 작업)


    private String folderId; // 작업 대상 폴더 (NULL이면 파일 작업)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Action action; // 수행된 작업 (CREATE, UPDATE, DELETE 등)

    @CreationTimestamp
    private LocalDateTime timestamp; // 작업 수행 시간

    public enum Action {
        CREATE,  // 생성 작업
        UPDATE,  // 수정 작업
        DELETE,  // 삭제 작업
        RESTORE, // 복구 작업
        SHARE    // 공유 작업
    }
}
