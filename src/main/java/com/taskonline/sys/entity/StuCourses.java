package com.taskonline.sys.entity;

import lombok.ToString;

import java.util.Date;

@ToString
public class StuCourses extends StuCoursesKey {
    private Date selectTime;

    private String courseAppraise;

    private Integer courseScore;

    private Boolean isPass;

    public Date getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(Date selectTime) {
        this.selectTime = selectTime;
    }

    public String getCourseAppraise() {
        return courseAppraise;
    }

    public void setCourseAppraise(String courseAppraise) {
        this.courseAppraise = courseAppraise == null ? null : courseAppraise.trim();
    }

    public Integer getCourseScore() {
        return courseScore;
    }

    public void setCourseScore(Integer courseScore) {
        this.courseScore = courseScore;
    }

    public Boolean getIsPass() {
        return isPass;
    }

    public void setIsPass(Boolean isPass) {
        this.isPass = isPass;
    }
}