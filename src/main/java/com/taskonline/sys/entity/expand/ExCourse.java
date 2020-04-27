package com.taskonline.sys.entity.expand;

import com.taskonline.sys.entity.Course;
import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @program: taskonline
 * @description: 课程表和外键依赖课程表的选课表、课程表依赖的教师表扩展（字段由于过于强调兼容性，所以查询难免有空字段）
 * @author: qwer
 * @create: 2020-03-27 18:17
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ExCourse extends Course {

    //扩展列，班级总人数
    private Integer count;

    //扩展列，来自课程表依赖的教师外键-教师名
    private String tname;

    //扩展列，来自选课表外键依赖-选课时间
    private Date selectTime;

    //扩展列，来自选课表外键依赖-学生对于课程的评价
    private String courseAppraise;

    //扩展列，来自选课表外键依赖-学生课程分数
    private Integer courseScore;

    //扩展列，一般查询不涉及此属性，仅在单独查询出课程列表后通过程序写入这个对象。方便service和controller间传递数据(约定MessageMap一般只put指定的key)
    private List<Task> tasks;

    private List<Student> students;

}