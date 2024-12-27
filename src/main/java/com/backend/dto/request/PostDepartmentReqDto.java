package com.backend.dto.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class PostDepartmentReqDto {
    private String name;
    private String discription;
    private Long leader;
    private List<Long> members;
    private Boolean link;
}
