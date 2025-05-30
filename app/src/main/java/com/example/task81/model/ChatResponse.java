package com.example.task81.model;

import java.util.List;

public class ChatResponse {
    private List<GeneratedText> generated_text;

    public String getReply() {
        if (generated_text != null && !generated_text.isEmpty()) {
            return generated_text.get(0).getGenerated_text();
        }
        return "No valid reply";
    }

    public static class GeneratedText {
        private String generated_text;

        public String getGenerated_text() {
            return generated_text;
        }
    }
}

