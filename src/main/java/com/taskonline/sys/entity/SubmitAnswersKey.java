package com.taskonline.sys.entity;

import lombok.ToString;

@ToString
public class SubmitAnswersKey {
    private Long subTaskId;

    private Long qid;

    public Long getSubTaskId() {
        return subTaskId;
    }

    public void setSubTaskId(Long subTaskId) {
        this.subTaskId = subTaskId;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }
}