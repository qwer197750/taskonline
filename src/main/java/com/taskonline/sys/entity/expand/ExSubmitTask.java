package com.taskonline.sys.entity.expand;

import com.taskonline.sys.entity.SubmitTasks;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @program: taskonline
 * @description: 用于连表查询的数据对象。用于查询学生信息和提交作业信息连接的数据对象
 * @description: 扩展了学生名、学生头像、作业名称、作业开始时间、作业截至时间、班级名称等字段
 * @author: qwer
 * @create: 2020-03-27 15:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ExSubmitTask extends SubmitTasks {
    /*来自学生表扩展*/
    //扩展字段，来自外键sid对应的sname(学生姓名)
    private String sname;

    //作业总分
    private Integer scoreCount;

    private Boolean isShortAutoCheckAnswer;

    //扩展字段，来自外键sid对应的icon(学生头像)
    private String icon;

    /*来自作业表扩展*/
    //扩展字段，来自外键taskName对应的taskName(作业名称)
    private String taskName;

    /*来自作业扩展*/
    //扩展作业id字段
    private Long taskId;

    //扩展字段，提交作业对应作业的开始时间
    private Date createTime;

    //扩展字段，提交作业对应的截至时间
    private Date endTime;

    /*来自班级表扩展*/
    //扩展字段，来自taskID对应的course-cid，用于方便跳转到相应的班级
    private Long cid;

    //扩展字段，来自taskID对应的course-cname，用于显示提交的作业属于那个班级
    private String cname;

    private List<ExAnswer> answers;
}