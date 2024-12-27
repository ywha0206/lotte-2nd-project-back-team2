package com.backend.dto.request.admin.user;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Setter
public class PatchAdminUserApprovalDto {
    private Long userId;
    private Integer level;
    private String joinDate;
}
