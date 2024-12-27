package com.backend.dto.response.calendar;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetCalendarNameDto {
    private Long id;
    private String name;
    private Integer status;
    private String color;
    private List<Long> userIds;
    private Long myid;
}
