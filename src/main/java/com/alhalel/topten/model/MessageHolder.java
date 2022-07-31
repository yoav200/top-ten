package com.alhalel.topten.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MessageHolder {

    public enum Severity {
        INFO, WARNING, ERROR
    }


    private List<Message> messages = new ArrayList<>();

    @Data
    @Builder
    public static class Message {
        private final Severity severity;
        private final String title;
        private final String text;
    }
}
