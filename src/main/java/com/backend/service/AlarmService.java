package com.backend.service;

import com.backend.dto.response.alarm.GetAlarmDto;
import com.backend.entity.user.Alert;
import com.backend.entity.user.User;
import com.backend.repository.UserRepository;
import com.backend.repository.user.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.Get;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AlarmService {
    private final AlertRepository alertRepository;
    private final UserRepository userRepository;

    public Page<GetAlarmDto> getAlarm(int page, Long id) {
        Pageable pageable = PageRequest.of(page, 5);
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return null;
        }
        Page<Alert> alerts = alertRepository.findAllByUserAndStatusOrderByCreateAtDesc(user.get(),2,pageable);
        Page<GetAlarmDto> dtos = alerts.map(Alert::toGetAlarmDto);
        return dtos;
    }

    public ResponseEntity<?> getAlarmCnt(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            return ResponseEntity.badRequest().body("로그인 정보가 일치하지 않습니다...");
        }
        Long cnt = alertRepository.countByUserAndStatus(user.get(),2);
        return ResponseEntity.ok(cnt);
    }

    public ResponseEntity<?> patchAlarmStatus(Long id) {
        Optional<Alert> alert = alertRepository.findById(id);
        if(alert.isEmpty()){
            return ResponseEntity.badRequest().body("일치하는 알람이 없습니다...");
        }
        alert.get().patchStatus(1);
        return ResponseEntity.ok().body("읽음처리!");
    }
}
