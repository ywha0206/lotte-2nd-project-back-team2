package com.backend.document.page;


/*
    날짜 : 2024.12.05
    이름 : 하진희
    내용 : Page 허가권저장을 위한 MogoDB collection
 */

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Document(collection = "pagePermission")
public class PagePermission {
    @Id
    private String id;
    private String pageId;
    private String userId;
    private int permission;

    public void updatePagePermission(int updatePagePermission){
        this.permission = updatePagePermission;
    }
}
