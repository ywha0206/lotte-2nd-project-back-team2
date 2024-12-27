package com.backend.repository.user;

import com.backend.entity.user.Attendance;
import com.backend.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    Optional<Attendance> findByUserAndYearMonth(User user, String yearMonth);

    Optional<Attendance> findByUser_IdAndYearMonth(Long userId, String yearMonth);
}
