package com.taskonline.sys.entity;

import lombok.ToString;

@ToString
public class SubmitAnswers extends SubmitAnswersKey {
    private Integer score;

    private String answer;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer == null ? null : answer.trim();
    }
}