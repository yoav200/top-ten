package com.alhalel.topten.controllers;

import com.alhalel.topten.security.UserPrincipal;
import com.alhalel.topten.social.voting.Votes;
import com.alhalel.topten.social.voting.VotesService;
import com.alhalel.topten.social.voting.model.VoteModel;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.alhalel.topten.controllers.PlayersController.principalToUserPrincipal;

@RequestMapping("voting")
@AllArgsConstructor
@RestController
public class VotingController {

    private final VotesService votesService;


    @GetMapping("/{object}/{objetId}")
    public VoteModel getVotesForObject(
            @PathVariable("object") String object,
            @PathVariable("objetId") long objetId) {

        Votes.VoteObject voteObject = Votes.VoteObject.valueOf(object.toUpperCase());
        return votesService.getVotesForObject(voteObject, objetId);
    }

    @Secured("ROLE_USER")
    @PostMapping("/{object}/{objetId}")
    public VoteModel updateVotesForObject(
            @PathVariable("object") String object,
            @PathVariable("objetId") long objetId,
            @RequestParam("vote") Votes.VoteOption vote,
            Principal p) {

        UserPrincipal principal = principalToUserPrincipal(p);

        Votes.VoteObject voteObject = Votes.VoteObject.valueOf(object.toUpperCase());

        votesService.updateVote(principal.getId(), voteObject, objetId, vote);

        return votesService.getVotesForObject(voteObject, objetId);
    }

}
