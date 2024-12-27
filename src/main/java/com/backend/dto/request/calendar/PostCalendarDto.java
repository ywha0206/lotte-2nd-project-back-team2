package com.backend.dto.request.calendar;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PostCalendarDto {
    private String name;
    private String color;
    private List<Long> userIds;
    private int status;
}
