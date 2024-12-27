package com.backend.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetAdminUsersApprovalRespDto {
    private Long id;
    private String name;
    private String email;
    private String uid;
    private String createAt;
}
