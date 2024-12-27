package com.backend.dto.request.drive;


import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenameRequest {

    private String id;
    private String newName;
    private String type;
    private String currentPath;
    private String newPath;
}
