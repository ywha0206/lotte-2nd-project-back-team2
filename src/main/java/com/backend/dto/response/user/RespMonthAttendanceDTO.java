package com.backend.dto.response.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespMonthAttendanceDTO {
    private Long attendanceId;
    private String yearMonth;
    private int workDays;
    private int absenceDays;
    private int vacationDays;
    private int overtimeHours;
    private int annualVacation;
}
