package com.backend.repository.chat;

import com.backend.document.chat.ChatRoomDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoomDocument, String> {

    List<ChatRoomDocument> findAllByLeaderOrMembersAndStatusIsNot(String leaderUid, String memberUid, int status);
}
