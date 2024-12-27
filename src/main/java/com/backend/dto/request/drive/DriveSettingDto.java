package com.backend.dto.request.drive;

import lombok.*;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DriveSettingDto {
    private Long id;
    private Long userId;
    private String userUid;
    private boolean drive_updates;
    private boolean  share_notifications;
    private boolean storage_alerts;
    @LastModifiedDate
    private boolean updateAt;

    private String setting;
    private Boolean value;

}
