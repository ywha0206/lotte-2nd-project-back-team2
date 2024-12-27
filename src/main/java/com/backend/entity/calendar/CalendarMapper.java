package com.backend.entity.calendar;

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
@Table(name = "calendar_mapper")
public class CalendarMapper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_calendar_id")
    private Long id; // 권한의 고유 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 권한을 가진 사용자

    @ManyToOne
    @JoinColumn(name = "calendar_id")
    private Calendar calendar; // 권한이 적용된 파일 (NULL이면 폴더에 적용)

    @Column(nullable = false)
    private int canRead;

    @Column(nullable = false)
    private int canWrite;

    @Column(nullable = false)
    private int canDelete;

    @Column(nullable = false)
    private int canShare;

    @CreationTimestamp
    private LocalDateTime createdAt; // 권한 생성 날짜 및 시간

}
