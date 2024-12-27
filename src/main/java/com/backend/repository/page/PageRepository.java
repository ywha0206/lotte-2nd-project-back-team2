package com.backend.repository.page;

import com.backend.document.page.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageRepository extends MongoRepository<Page, String> {

    List<Page> findByOwnerUid(String ownerUid);

    List<Page> findByOwnerUidContaining(String uid);

    List<Page> findByOwnerUidContainingAndType(String uid, String number);

    Optional<Page> findByIdAndTypeIsNot(String uid, String number);

    List<Page> findAllByOwnerUidAndTypeIsNot(String uid, String number);

    List<Page> findAllByOwnerUidAndTypeIsNotAndTypeIsNot(String uid, String number, String number1);
}
