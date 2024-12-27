package com.backend.entity.message;

import com.backend.entity.user.User;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Entity
@Table(name = "chat_members")
@Builder
public class ChatMembers {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_members_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom room;
}
