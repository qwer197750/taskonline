package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.Task;
import com.taskonline.sys.entity.expand.ExTask;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface ExTaskMapper {

    //查询学生所有的作业，扩展了班级字段，方便跳转到班级，如果需要查询过期作业，不提供sql，在程序中对于所有作业进行筛选
    @Select("SELECT\n" +
            "\ttask_id,\n" +
            "\ttask_name,\n" +
            "\ttask.create_time create_time,\n" +
            "\ttask.end_time end_time,\n" +
            "\tcourse.cid cid,\n" +
            "\tcourse.cname cname \n" +
            "FROM\n" +
            "\ttask\n" +
            "\tINNER JOIN stu_courses ON task.cid = stu_courses.cid\n" +
            "\tINNER JOIN course ON course.cid = task.cid \n" +
            "WHERE\n" +
            "\tsid =#{sid}")
    List<ExTask> selectStudentTasksByStudentPrimaryKey(@Param("sid") long sid);

    //查询学生所有未提交的作业，扩展了课程名称
    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcourse.cname cname,\n" +
            "\ttask.task_id task_id,\n" +
            "\ttask_name,\n" +
            "\ttask.create_time create_time,\n" +
            "\ttask.end_time end_time\n" +
            "\n" +
            "FROM\n" +
            "\tstu_courses\n" +
            "\tINNER JOIN task ON stu_courses.cid = task.cid \n" +
            "\tinner join course on stu_courses.cid=course.cid\n" +
            "WHERE\n" +
            "\tsid = #{sid} \n" +
            "\tAND task_id NOT IN ( SELECT task_id FROM submit_tasks WHERE sid = #{sid} );")
    List<ExTask> selectTaskOfStudentNotSubmitByPrimaryKey(@Param("sid") long sid);

    //查询教师发布的所有的作业，扩展了班级字段，方便跳转到班级，不提供查询过期课程，在程序中筛选过期课程
    @Select("SELECT\n" +
            "\ttask.task_id task_id,\n" +
            "\ttask_name,\n" +
            "\ttask.create_time create_time,\n" +
            "\ttask.end_time end_time,\n" +
            "\tcourse.cid cid,\n" +
            "\tcname,\n" +
            "\n" +
            "\t\n" +
            "\tCOUNT( sid )\n" +
            "FROM\n" +
            "\ttask\n" +
            "\tINNER JOIN course ON task.cid = course.cid\n" +
            "\tleft JOIN submit_tasks ON task.task_id = submit_tasks.task_id \n" +
            "WHERE\n" +
            "\ttid = #{tid} \n" +
            "GROUP BY\n" +
            "\ttask.task_id;")
    List<ExTask> selectTeacherTaskByTeacherPrimaryKey(@Param("tid") long tid);

    //查询一个课程下的作业
    @Select("SELECT\n" +
            "\t* \n" +
            "FROM\n" +
            "\ttask \n" +
            "WHERE\n" +
            "\tcid = #{cid}")
    List<Task> selectTaskOfCourse(@Param("cid") Long cid);

    @Delete("delete from question where task_id = #{taskId}")
    int deleteTaskQuestions(Long taskId);
}
