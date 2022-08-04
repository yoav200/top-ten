package com.alhalel.topten.social.voting.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VoteModel {

    private final long id;

    private final long upvotes;

    private final long downvotes;

}
