package com.backend.dto.request.calendar;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PostCalendarContentDto {
    private String title;
    private String sdate;
    private String edate;
    private String stime;
    private String etime;
    private Long calendarId;
    private String location;
    private Integer importance;
    private Integer alert;
    private String memo;
}
