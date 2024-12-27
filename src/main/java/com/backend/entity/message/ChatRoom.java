package com.backend.entity.message;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Entity
@Table(name = "chat_room")
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(name = "chat_room_status")
    private int status; // 상태

    @Column(name = "chat_room_favorite")
    private int chatRoomFavorite; // 즐겨찾기

    @Column(name = "chat_room_notread_cnt")
    private int chatRoomReadCnt; // 안읽은메세지수

    @Column(name = "chat_room_name")
    private String chatRoomName; // 채팅방이름

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User leader;  // 방장

}
