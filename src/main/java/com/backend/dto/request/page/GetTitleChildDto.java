package com.backend.dto.request.page;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
public class GetTitleChildDto {
    private String selectedTextContent;
    private String id;
}
