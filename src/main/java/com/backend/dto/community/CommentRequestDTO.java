package com.backend.dto.community;

import com.backend.entity.community.Comment;
import com.backend.entity.community.Post;
import com.backend.entity.user.User;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDTO {

    private Long postId;

    private Long parentId;

    private String content;

    private String writer;

    private Long userId;

    private Long mentionUserId; //(옵션임)


}
