package com.alhalel.topten.social.comments.model;

import com.alhalel.topten.user.model.UserModel;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    public String getTimeAgo() {
        return calculateTimeAgo(updateDateTime);
    }

    public static String calculateTimeAgo(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        long seconds = ChronoUnit.SECONDS.between(dateTime, now);
        long minutes = ChronoUnit.MINUTES.between(dateTime, now);
        long hours = ChronoUnit.HOURS.between(dateTime, now);
        long days = ChronoUnit.DAYS.between(dateTime, now);
        long month = ChronoUnit.MONTHS.between(dateTime, now);

        if (month > 1) {
            return month + " months ago";
        } else if (days > 1) {
            return days + " days ago";
        } else if (hours > 1) {
            return hours + " hours ago";
        } else if (minutes > 1) {
            return minutes + " minutes ago";
        } else {
            return seconds + " seconds ago";
        }
    }
}
