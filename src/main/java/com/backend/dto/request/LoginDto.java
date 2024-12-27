package com.backend.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class LoginDto {
    private String uid;
    private String pwd;
}
