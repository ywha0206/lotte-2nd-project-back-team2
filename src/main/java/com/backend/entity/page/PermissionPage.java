package com.backend.entity.page;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Table(name = "permission_page")
public class PermissionPage {

    @Id
    @GeneratedValue
    private UUID id; // 권한 고유 ID

    @ManyToOne
    @JoinColumn(name = "page_id", nullable = false)
    private Page page; // 페이지 ID

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 권한 유형 (READ, WRITE, ADMIN)

    // 권한 Enum
    public enum Role {
        READ, WRITE, ADMIN
    }
}
