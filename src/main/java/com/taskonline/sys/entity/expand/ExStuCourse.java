package com.taskonline.sys.entity.expand;

import com.taskonline.sys.entity.StuCourses;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @program: taskonline
 * @description: 扩展选课表，扩展学生姓名，班级名称字段
 * @author: qwer
 * @create: 2020-04-04 11:10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
public class ExStuCourse extends StuCourses {
    //扩展选课学生的姓名
    String sname;
    //扩展选课课程的名称
    String cname;
}