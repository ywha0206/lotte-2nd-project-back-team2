package com.backend.entity.message;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@DiscriminatorValue("img")  // group_type이 "DEPARTMENT"인 경우
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
public class ChatFileImg extends ChatFile{
    @Column(name = "chat_file_rname")
    private String chatFileRname;  // 실제파일이름

    @Column(name = "chat_file_sname")
    private String chatFileSname;  //  uuid 저장파일이름
}
