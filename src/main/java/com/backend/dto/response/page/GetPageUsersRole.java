package com.backend.dto.response.page;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class GetPageUsersRole {
    private String uid;
    private int role;
}
