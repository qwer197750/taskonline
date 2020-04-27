package com.taskonline.sys.entity;

import lombok.ToString;

import java.util.Date;

@ToString
public class Task {
    private Long taskId;

    private String taskName;

    private Long cid;

    private Integer score;

    private Date createTime;

    private Date endTime;

    private Boolean isShortAutoCheckAnswer;

    private Boolean isShowAnswer;

    private String whenShowAnswer;

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Boolean getIsShortAutoCheckAnswer() {
        return isShortAutoCheckAnswer;
    }

    public void setIsShortAutoCheckAnswer(Boolean isShortAutoCheckAnswer) {
        this.isShortAutoCheckAnswer = isShortAutoCheckAnswer;
    }

    public Boolean getIsShowAnswer() {
        return isShowAnswer;
    }

    public void setIsShowAnswer(Boolean isShowAnswer) {
        this.isShowAnswer = isShowAnswer;
    }

    public String getWhenShowAnswer() {
        return whenShowAnswer;
    }

    public void setWhenShowAnswer(String whenShowAnswer) {
        this.whenShowAnswer = whenShowAnswer == null ? null : whenShowAnswer.trim();
    }
}