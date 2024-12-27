package com.backend.dto.request.drive;


import com.backend.util.Role;
import lombok.*;

import java.util.Objects;

@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SharedUser {
    private Long id;
    private String name;
    private String email;
    private String group;
    private String uid;
    private String authority;
    private String permission; // 읽기, 수정 등 권한
    private String profile;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SharedUser that = (SharedUser) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(email, that.email) &&
                Objects.equals(uid, that.uid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, uid);
    }
}
