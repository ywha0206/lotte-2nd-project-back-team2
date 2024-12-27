package com.backend.entity.group;

import com.backend.dto.response.GetAdminUsersRespDto;
import com.backend.entity.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity
@Table(name = "group_mapper")
public class GroupMapper {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_mapper_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;  // 그룹

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // 사용자

    public GetAdminUsersRespDto toGetAdminUsersRespDto() {
        return GetAdminUsersRespDto.builder()
                .email(user.getEmail())
                .id(user.getId())
                .uid(user.getUid())
                .name(user.getName())
                .build();
    }
}
