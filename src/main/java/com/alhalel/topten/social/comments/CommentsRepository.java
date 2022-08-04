package com.alhalel.topten.social.comments;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentsRepository extends CrudRepository<Comments, Long> {

    List<Comments> findAllByObjectAndObjectId(Comments.CommentsObject object, Long objectId);

    @Query(value = "SELECT count(c) FROM Comments c WHERE c.object = ?1 and c.objectId = ?2 and c.user.id = ?3")
    long countCommentsForUser(Comments.CommentsObject object, Long objectId, Long userId);
}
