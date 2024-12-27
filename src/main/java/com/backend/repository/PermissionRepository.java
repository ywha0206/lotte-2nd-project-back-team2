package com.backend.repository;

import com.backend.document.page.PagePermission;
import com.backend.entity.folder.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends MongoRepository<PagePermission, String> {

    Optional<PagePermission> findByPageId(String pageId);
    Optional<PagePermission> findByPageIdAndUserId(String pageId, String userId);
}
