package com.backend.repository.drive;

import com.backend.entity.folder.DriveSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriveSettingRepository extends JpaRepository<DriveSetting, Long> {
    Optional<DriveSetting> findByUserId(Long userId);
    Optional<DriveSetting> findByUserUid(String userUid);
}
