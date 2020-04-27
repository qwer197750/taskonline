package com.taskonline.sys.entity;

import lombok.ToString;

import java.util.Date;

@ToString
public class SubmitTasks {
    private Long subTaskId;

    private Long sid;

    private Long taskId;

    private Date submitTime;

    private Boolean isTeaMark;

    private Integer score;

    public Long getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(Long subTaskId) {
        this.subTaskId = subTaskId;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Boolean getIsTeaMark() {
        return isTeaMark;
    }

    public void setIsTeaMark(Boolean isTeaMark) {
        this.isTeaMark = isTeaMark;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}