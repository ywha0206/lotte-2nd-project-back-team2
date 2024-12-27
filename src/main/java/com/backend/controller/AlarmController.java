package com.backend.controller;

import com.backend.dto.request.user.PostUserAlarmDto;
import com.backend.dto.response.alarm.GetAlarmDto;
import com.backend.service.AlarmService;
import com.backend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AlarmController {

    private final UserService userService;
    private final AlarmService alarmService;

    @GetMapping("/alarm")
    public ResponseEntity<?> getAlarm(
            HttpServletRequest req,
            @RequestParam(value = "page",defaultValue = "0") int page
    ) {
        Object idObj = req.getAttribute("id");
        Long id;
        if (idObj != null) {
            id = Long.valueOf(idObj.toString());  // 문자열을 Long으로 변환
        } else {
            id= 0L;
        }
        Page<GetAlarmDto> alarms = alarmService.getAlarm(page,id);
        Map<String,Object> map = new HashMap<>();
        map.put("alarms", alarms.getContent());
        map.put("totalPages", alarms.getTotalPages());
        map.put("totalElements", alarms.getTotalElements());
        map.put("currentPage", alarms.getNumber());
        map.put("hasNextPage", alarms.hasNext());
        return ResponseEntity.ok().body(map);
    }

    @PostMapping("/alarm")
    public ResponseEntity<?> postAlarm(
            @RequestBody PostUserAlarmDto dto,
            HttpServletRequest req
    ){
        Object idObj = req.getAttribute("id");
        Long id;
        if (idObj != null) {
            id = Long.valueOf(idObj.toString());  // 문자열을 Long으로 변환
        } else {
            id= 0L;
        }
        ResponseEntity<?> response = userService.postAlert(dto,id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/alarm/cnt")
    public ResponseEntity<?> getAlarmCnt(
            HttpServletRequest req
    ){
        Object idObj = req.getAttribute("id");
        Long id;
        if (idObj != null) {
            id = Long.valueOf(idObj.toString());  // 문자열을 Long으로 변환
        } else {
            id= 0L;
        }
        ResponseEntity<?> response = alarmService.getAlarmCnt(id);
        return response;
    }

    @PatchMapping("/alarm/status")
    public ResponseEntity<?> patchAlarmStatus(
            @RequestParam Long id
    ){
        ResponseEntity<?> response = alarmService.patchAlarmStatus(id);
        return response;
    }
}
