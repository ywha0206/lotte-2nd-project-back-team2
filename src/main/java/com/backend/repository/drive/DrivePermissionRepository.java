package com.backend.repository.drive;

import com.backend.document.drive.DrivePermission;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DrivePermissionRepository extends MongoRepository<DrivePermission,String> {

    Optional<DrivePermission> findByFolderIdAndUserId(String folderId, String userId);
    Optional<DrivePermission> findByFileIdAndUserId(String file, String userId);
    Optional<DrivePermission> findByFolderId(String folderId);
    Optional<DrivePermission> findByFileId(String file);

}
