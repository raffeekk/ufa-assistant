package org.example.model;

import java.time.LocalDateTime;

public class FaqItem {
    private Long id;
    private String question;
    private String answer;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public FaqItem() {}

    public FaqItem(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}