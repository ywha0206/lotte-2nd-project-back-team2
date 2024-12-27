package com.backend.dto.response.alarm;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetAlarmDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String createAt;
    private int status;
    private int type;
}
