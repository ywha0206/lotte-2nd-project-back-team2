package com.backend.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetAdminUsersDtailRespDto {
    private Long id;
    private String name;
    private String email;
    private Integer status;
    private String attendance;
    private String level;
    private String createAt;
}
