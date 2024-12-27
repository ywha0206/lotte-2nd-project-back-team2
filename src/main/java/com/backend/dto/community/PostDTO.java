package com.backend.dto.community;

/*
    날짜 : 2024/12/03
    이름 : 박서홍
    내용 : Post Entity 작성
 */

import com.backend.document.BoardFile;
import com.backend.entity.community.BaseTimeEntity;
import com.backend.entity.community.Board;
import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {


    private Long postId;

    private String writer;

    private String title;

    private String content;

    private int fileCount;

    private String regip;

    private boolean favoritePost;

    private List<MultipartFile> files;

    private List<BoardFile> savedFiles; // 조회용


    private boolean isMandatory;

    private int hit;

    private Long boardId;

    private String uid;

    private String boardName;

    private LocalDateTime createdAt;


    private int comment;

    public void setBoardName(String boardName) {
    }
}
