package com.backend.repository.drive;

import com.backend.document.drive.Restore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RestoreRepository extends MongoRepository<Restore, String> {

    List<Restore> findByOriginalFolderIdAndStatusOrderByRestoreDateDesc(String folderId,int status);
}
