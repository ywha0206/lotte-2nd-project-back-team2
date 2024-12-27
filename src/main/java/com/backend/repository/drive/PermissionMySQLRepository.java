package com.backend.repository.drive;


import com.backend.entity.folder.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//날짜: 2024.12.12
@Repository
public interface PermissionMySQLRepository extends JpaRepository<Permission, Integer> {

    Optional<Permission> findByTypeAndTypeIdAndUser_Id(String type,String folderId, Long userId);
    Optional<Permission> findByTypeAndTypeId(String type,String folderId);

}
