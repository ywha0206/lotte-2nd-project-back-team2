package com.backend.dto.request.drive;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShareLinkRequest {
    private String id;
    private String ownerId;
}
