package com.alhalel.topten.social.voting;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface VotesRepository extends CrudRepository<Votes, Long> {

    Optional<Votes> findByUserIdAndObjectAndObjectId(Long userId, Votes.VoteObject object, Long objectId);

    List<Votes> findAllByObjectAndObjectId(Votes.VoteObject object, Long objectId);
}
