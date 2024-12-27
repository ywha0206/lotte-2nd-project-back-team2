package com.backend.entity.message;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "chat_file_type", discriminatorType = DiscriminatorType.STRING)
@ToString
@Getter
@Builder
@Entity
@Table(name = "chat_file")
public class ChatFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_file_id")
    private Long id;

    @Column(name = "chat_file_status")
    private int status;

}
