package com.backend.dto.response.project;

import com.backend.entity.project.Project;
import com.backend.entity.project.ProjectCoworker;
import com.backend.entity.user.User;
import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProjectCoworkerDTO {
    private Long id;

    private Long userId;
    private String uid;
    private String name;
    private String email;
    private String group;
    private String level;
    private String profile; // 프로필 이미지의 sName (파일명)

    private boolean isOwner;

    private boolean canRead; // 읽기 권한
    private boolean canAddTask; // 추가 권한
    private boolean canUpdateTask; // 수정 권한
    private boolean canDeleteTask; // 삭제 권한
    private boolean canEditProject; // 프로젝트 전체 권한

    private Long taskId;
    private Long columnId;
    private Long projectId;

    public ProjectCoworker toProjectCoworker() {
        return ProjectCoworker.builder()
                .id(id)
                .user(User.builder().id(userId).uid(uid).name(name).email(email).profileImgPath(profile).build())
                .isOwner(isOwner)
                .canRead(canRead)
                .canAddTask(canAddTask)
                .canUpdateTask(canUpdateTask)
                .canDeleteTask(canDeleteTask)
                .canEditProject(canEditProject)
                .build();
    }

}
