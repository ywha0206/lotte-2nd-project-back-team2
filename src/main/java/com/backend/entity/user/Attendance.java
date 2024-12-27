package com.backend.entity.user;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
public class Attendance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long attendanceId; // 인덱스번호

    @Column(name = "status")
    private int status;  // 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;  

    @Column(name = "`year_month`")
    private String yearMonth; // 몇년 몇월달

    @Column(name = "work_days")
    private int workDays; // 출석일

    @Column(name = "absence_days")
    private int absenceDays; // 결석일

    @Column(name = "vacation_days")
    private int vacationDays; // 휴가일

    @Column(name = "overtime_days")
    private int overtimeHours; // 야근일

    public void setAbsenceDays(int absenceDays) {
        this.absenceDays = absenceDays;
    }

    public void setWorkDays(int workDays) {
        this.workDays = workDays;
    }
}
