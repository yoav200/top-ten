package com.alhalel.topten.social.comments.model;

import com.alhalel.topten.user.model.UserModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class CommentModel {
    private UserModel user;

    private final Long id;

    private Long parentId;

    // the level of the replays. root comments are of level 1
    // current application support 2 levels
    private int level;

    private LocalDateTime updateDateTime;

    private String content;

    private List<CommentModel> replays;


    // TODO: 04/08/2022 calculate the  time passed from updateDateTime
    public String postTime() {
        return null;
    }
}
