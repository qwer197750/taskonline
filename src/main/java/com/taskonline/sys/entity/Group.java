package com.taskonline.sys.entity;

import lombok.ToString;

@ToString
public class Group {
    private Long gid;

    private String gname;

    private String describe;

    private String anthority;

    public Long getGid() {
        return gid;
    }

    public void setGid(Long gid) {
        this.gid = gid;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname == null ? null : gname.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getAnthority() {
        return anthority;
    }

    public void setAnthority(String anthority) {
        this.anthority = anthority == null ? null : anthority.trim();
    }
}