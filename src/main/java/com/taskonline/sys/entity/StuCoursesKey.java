package com.taskonline.sys.entity;

import lombok.ToString;

@ToString
public class StuCoursesKey {
    private Long sid;

    private Long cid;

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }
}