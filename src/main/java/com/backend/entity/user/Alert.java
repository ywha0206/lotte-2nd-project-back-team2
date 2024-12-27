package com.backend.entity.user;

import com.backend.dto.response.alarm.GetAlarmDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Alert {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "alarm_title")
    private String title;

    @Column(name = "alarm_content")
    private String content;

    @Column(name = "alarm_createAt")
    private String createAt;

    @Column(name = "alert_status")
    private Integer status;  //0 삭제 , 1 읽음 , 2 읽지않음

    @Column(name = "alert_type")
    private Integer type;  // 1 알람 , 2 공지사항 , 3. 페이지 , 4. 프로젝트 , 5. 결재 , 6. 결제


    public GetAlarmDto toGetAlarmDto() {
        return GetAlarmDto.builder()
                .id(id)
                .userId(user.getId())
                .title(title)
                .content(content)
                .createAt(createAt)
                .status(status)
                .type(type)
                .build();
    }

    public void patchStatus(int i) {
        this.status = i;
    }
}
