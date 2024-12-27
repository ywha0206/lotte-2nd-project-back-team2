package com.backend.document.drive;


/*
    날짜 : 2024.12.05
    이름 : 하진희
    내용 : drive허가권 저장을 위한 MogoDB collection
 */

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Document(collection = "drivePermission")
public class DrivePermission {
    @Id
    private String id;
    private String folderId;
    private String fileId;
    private String userId;
    private int permission;

    public void updatePermission(int updatePagePermission){
        this.permission = updatePagePermission;
    }
}
