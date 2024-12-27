package com.backend.repository.community;

import com.backend.document.BoardFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardFileRepository extends MongoRepository<BoardFile, String> {
    List<BoardFile> findByPostId(Long postId);

    Optional<BoardFile> findByPostIdAndSavedName(Long postId, String savedName);

}
