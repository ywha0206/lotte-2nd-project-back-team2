package com.backend.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetAdminSidebarGroupsRespDto {
    private Long id;
    private String name;
}
