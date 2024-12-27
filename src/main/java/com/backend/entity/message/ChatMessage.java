package com.backend.entity.message;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Builder
@Entity
@Table(name = "chat_message")
public class ChatMessage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Long id;

    @Column(name = "chat_message_status")
    private int status;  // 상태

    @Column(name = "chat_message_type")
    private int type; // 공지 ,, 일반채팅 , 파일, 

    @Column(name = "chat_message_content")
    private String content; // 내용

    @Column(name = "chat_message_uid")
    private String uid; // 아이디
}
