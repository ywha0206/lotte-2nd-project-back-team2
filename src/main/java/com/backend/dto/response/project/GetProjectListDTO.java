package com.backend.dto.response.project;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProjectListDTO {
        private Long id;
        private String title;
        private String status; // 대기중, 진행중, 완료, 삭제
        private Boolean isOwner;
}
