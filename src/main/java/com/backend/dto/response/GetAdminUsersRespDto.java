package com.backend.dto.response;

import lombok.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Service
public class GetAdminUsersRespDto {
    private Long id;
    private String uid;
    private String email;
    private String imgPath;
    private String name;
}
