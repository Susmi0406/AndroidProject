package com.example.efishshop.Model;

public class UserFeedback {
    private String Feedbacks, Username;

    public UserFeedback() {
    }

    public UserFeedback(String feedbacks, String username) {
        this.Feedbacks = feedbacks;
        this.Username = username;
    }

    public String getFeedbacks() {
        return Feedbacks;
    }

    public void setFeedbacks(String feedbacks) {
        Feedbacks = feedbacks;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
