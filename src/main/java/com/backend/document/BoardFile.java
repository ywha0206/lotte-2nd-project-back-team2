package com.backend.document;


import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Document(collection = "boardFile")
public class BoardFile {
    private String id;
    private Long postId;
    private String originalName;
    private String savedName;
    private String path;
    private String ownerUid;
    private long size;
    private LocalDateTime createdAt;
}


