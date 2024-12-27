package com.backend.dto.request.calendar;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class PutCalendarContentsDto {
    private String title;
    private Long contentId;
    private String startDate;
    private String endDate;
}
