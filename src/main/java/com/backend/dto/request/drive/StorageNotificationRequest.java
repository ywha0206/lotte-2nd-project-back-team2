package com.backend.dto.request.drive;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StorageNotificationRequest {
    private String message;
    private String percent;
}
