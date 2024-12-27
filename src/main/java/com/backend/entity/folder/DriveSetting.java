package com.backend.entity.folder;

import com.backend.dto.request.drive.DriveSettingDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "DriveSetting")
public class DriveSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 자동 생성
    private Long id;
    private String userUid;
    private Long userId;
    private boolean drive_updates;
    private boolean  share_notifications;
    private boolean storage_alerts;
    @LastModifiedDate
    private LocalDateTime updateAt;

    public DriveSettingDto toDto() {
        DriveSettingDto dto = DriveSettingDto.builder()
                .drive_updates(drive_updates)
                .share_notifications(share_notifications)
                .storage_alerts(storage_alerts)
                .id(id)
                .userId(userId)
                .userUid(userUid)
                .build();
        return dto;
    }

}
