package com.alhalel.topten.social.voting;

import com.alhalel.topten.ranking.RankListRepository;
import com.alhalel.topten.social.comments.CommentsRepository;
import com.alhalel.topten.social.voting.model.VoteModel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Log4j2
@Service
@AllArgsConstructor
public class VotesService {

    private final VotesRepository votesRepository;

    // to check exists
    private final RankListRepository rankListRepository;
    // to check exists
    private final CommentsRepository commentsRepository;


    private boolean isObjectExist(Votes.VoteObject object, long objectId) {
        switch (object) {
            case RANK_LIST:
                return rankListRepository.existsById(objectId);
            case COMMENT:
                return commentsRepository.existsById(objectId);
            default:
                return false;
        }
    }

    public VoteModel getVotesForObject(Votes.VoteObject object, long objectId) {
        List<Votes> votes = votesRepository.findAllByObjectAndObjectId(object, objectId);

        Map<Votes.VoteOption, Long> map = votes.stream()
                .collect(groupingBy(Votes::getVote, counting()));

        return VoteModel.builder()
                .id(objectId)
                .upvotes(Optional.ofNullable(map.get(Votes.VoteOption.UP)).orElse(0L))
                .downvotes(Optional.ofNullable(map.get(Votes.VoteOption.DOWN)).orElse(0L))
                .build();
    }

    public void updateVote(long userId, Votes.VoteObject object, long objectId, Votes.VoteOption voteOption) {

        if (!isObjectExist(object, objectId)) {
            throw new IllegalArgumentException("Object doesn't exists");
        }

        Votes votes = votesRepository.findByUserIdAndObjectAndObjectId(userId, object, objectId)
                .map(vote -> {
                    // cannot vote more than once
                    if (!vote.getVote().equals(voteOption)) {
                        vote.setVote(voteOption);
                    }
                    return vote;
                }).orElse(Votes.builder()
                        .userId(userId)
                        .object(object)
                        .objectId(objectId)
                        .vote(voteOption)
                        .build()
                );

        votesRepository.save(votes);
    }
}
