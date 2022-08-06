package com.alhalel.topten.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.alhalel.topten.social.comments.model.CommentModel.calculateTimeAgo;

@Data
public class MessageHolder {

    public enum Severity {
        INFO, WARNING, ERROR
    }

    private List<Message> messages = new ArrayList<>();

    @Data
    @Builder
    public static class Message {
        private final LocalDateTime updateDateTime = LocalDateTime.now();
        private final Severity severity;
        private final String title;
        private final String text;

        public String getTimeAgo() {
            return calculateTimeAgo(updateDateTime);
        }
    }
}
