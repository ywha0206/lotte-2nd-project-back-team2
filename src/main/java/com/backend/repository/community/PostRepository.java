package com.backend.repository.community;

import com.backend.entity.community.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 게시판의 게시글 조회
    Page<Post> findByBoard_BoardIdOrderByCreatedAtDesc(Long boardId, Pageable pageable);

    // 특정 작성자의 게시글 조회
    Optional<Post> findByBoard_BoardIdAndPostId(Long boardId, Long postId);


    List<Post> findTop5ByBoard_BoardIdOrderByCreatedAtDesc(Long boardId);

    // 검색 쿼리 수정
    @Query("SELECT p FROM Post p " +
            "WHERE p.board.boardId = :boardId " +
            "AND (:keyword IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.content) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.writer) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "ORDER BY p.createdAt DESC")
    Page<Post> searchPosts(
            @Param("boardId") Long boardId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

}
