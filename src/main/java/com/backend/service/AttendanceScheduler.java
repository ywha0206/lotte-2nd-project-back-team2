package com.backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Log4j2
@RequiredArgsConstructor
@Component
public class AttendanceScheduler {

    private final AttendanceService attendanceService;

    @Scheduled(cron = "0 27 11 * * ?")
    public void checkVacation(){
        attendanceService.markVacation();
        log.info("12시 연차 처리"+ LocalDateTime.now());
    }
//
//    @Scheduled(cron = "0 0 9 * * ?")
//    public void checkLateUsers(){
//        attendanceService.markAttendance("late");
//        log.info("9시 지각 체크 "+ LocalDateTime.now());
//    }

    @Scheduled(cron = "0 0 14 * * ?")
    public void checkAbsent(){
        try {
            attendanceService.markVacation();
            attendanceService.markAttendance("absent");
            log.info("이거 된 거냐?"+ LocalDateTime.now());
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void makeAttendance(){
        attendanceService.insertAllAttendance();
    }

}
