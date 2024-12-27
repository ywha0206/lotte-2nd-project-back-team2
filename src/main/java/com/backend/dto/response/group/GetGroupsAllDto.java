package com.backend.dto.response.group;

import com.backend.dto.response.user.GetUsersAllDto;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetGroupsAllDto {
    private Long id;
    private String name;
    private Long cnt;
//    private List<GetUsersAllDto> users;
}
