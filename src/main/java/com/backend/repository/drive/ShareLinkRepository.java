package com.backend.repository.drive;

import com.backend.document.drive.ShareLink;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ShareLinkRepository extends MongoRepository<ShareLink, String> {

    Optional<ShareLink> findByToken(String token);

}
