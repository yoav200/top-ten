package com.alhalel.topten.social.voting;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface VotesRepository extends CrudRepository<Votes, Long> {

    Optional<Votes> findByUserIdAndObjectAndObjectId(Long userId, Votes.VoteObject object, Long objectId);

    List<Votes> findAllByObjectAndObjectId(Votes.VoteObject object, Long objectId);

//    @Query(value = "SELECT count(*) FROM Votes WHERE object = ?1 and objectId = ?2 and vote = ?3")
//    long countVotes(Votes.VoteObject object, Long objectId, Votes.VoteOption voteOption);
//
//    @Query(value = "select count(e) = 1 from :entity e where e.id = :id")
//    boolean isKeyExistsForObject(@Param("entity") String entityName, @Param("id") long id);

//    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN TRUE ELSE FALSE END FROM #{#entityName} l WHERE l.state=?2")
//    boolean isObjectExists(@Entity(name = "entityName") String entityName, long id);
}
