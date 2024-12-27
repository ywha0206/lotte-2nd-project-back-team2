package com.backend.dto.response.page;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class MessagePageDto {
    private String type;
    private String pageId;
    private Object content;
    private String uid;
}
