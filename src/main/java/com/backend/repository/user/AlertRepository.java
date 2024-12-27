package com.backend.repository.user;

import com.backend.entity.user.Alert;
import com.backend.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    Page<Alert> findAllByUser(User user, Pageable pageable);

    Long countByUserAndStatus(User user, int i);

    Page<Alert> findAllByUserAndStatusOrderByCreateAtDesc(User user, int i, Pageable pageable);

    Boolean  existsByUserIdAndTypeAndCreateAtAfter(Long userId, int type, String createAt);
}
