package com.backend.document.drive;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Setter
@Document(collection = "ShareLink")
public class ShareLink {
    private String id;
    private String sharedId; //폴더 아이디값
    private String shared_By;
    private String token;
    @Builder.Default
    private String permission="읽기";
    private LocalDateTime createAt;
    private LocalDateTime expiry_date;
    private boolean is_active;

    public boolean isExpired() {
        System.out.println("현재시각 : "+LocalDateTime.now());
        return expiry_date.isBefore(LocalDateTime.now());
    }


}
