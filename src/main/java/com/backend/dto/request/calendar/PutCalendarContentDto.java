package com.backend.dto.request.calendar;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PutCalendarContentDto {
    private String title;
    private String sdate;
    private String edate;
    private Long calendarId;
    private String location;
    private Integer importance;
    private Integer alert;
    private String memo;
    private Long sheave;
}
