package com.backend.document.drive;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Setter
@Document(collection = "RestoreLogs")
public class Restore {

    @Id
    private String id;
    private String originalFolderId;
    private String restoreFolderId;
    private String restorePath;
    private String originalPath;
    private LocalDateTime restoreDate;
    private String restoreBy;
    private int status;  // 1: 새폴더 생성됨 0: 복구 완료 2: 충돌
    private String conflictHandled;

}
