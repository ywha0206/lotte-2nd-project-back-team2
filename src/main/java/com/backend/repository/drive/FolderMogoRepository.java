package com.backend.repository.drive;

import com.backend.document.drive.Folder;
import com.backend.dto.request.drive.SharedUser;
import com.backend.entity.page.Page;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;


@Repository
public interface FolderMogoRepository extends MongoRepository<Folder, String>   {

    List<Folder> findByParentIdAndStatusIsNotOrderByOrderDesc( String parentId , int status);
    Optional<Folder> findByParentId(String parentId);
    Folder findByName(String name);
    Optional<Folder> findByPath(String path);

    Optional<Folder> findByTypeAndOwnerId(String type, String ownerId);

    List<Folder> findByOwnerIdAndParentIdAndStatusIsNot(String ownerId, String parentId, int status);
    List<Folder> findByOwnerIdAndIsPinnedAndStatus(String uid, int isPinned, int status);
    List<Folder> findByOwnerIdAndParentIdIsNotNullAndStatusIsNotOrderByUpdatedAtDesc(String uid, int status);

    List<Folder> findByOwnerIdAndTargetAndStatus(String uid, int target,int status);

    List<Folder> findAllByParentId(String parentId);

    Folder findFolderByNameAndParentIdAndRestore(String name , String parentId , int restore);

    Folder findFolderByNameAndParentIdAndStatusIsNot(String name, String parentId,int status);

    List<Folder> findBySharedUsersUidAndTarget(String userId,int target);

    List<Folder> findBySharedUsersUidAndStatusIsNot(String uid,int status);

    Optional<Folder> findByIdAndSharedUsersUid(String folderId,String uid);

    List<Folder> findAllByPathStartingWith(String path);
    Optional<Folder> findByNameAndParentIdAndRestore(String name , String parentId, int restore);


    List<Double> findOrderByParentIdOrderByOrderDesc(String parentId);


    @Query(value = "{ 'parentId': ?0 }", count = true)
    long countByParentId(String parentId);
}
