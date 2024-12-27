package com.backend.dto.response.calendar;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetCalendarsDto {
    private Long id;
    private String title;
    private String start;
    private String color;
    private String end;
    private Long sheave;
    private String location;
    private Integer importance;
    private Integer alert;
    private String memo;

}
