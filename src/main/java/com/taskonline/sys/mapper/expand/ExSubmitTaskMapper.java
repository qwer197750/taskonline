package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.SubmitTasks;
import com.taskonline.sys.entity.expand.ExSubmitTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: taskonline
 * @description: mybatis逆向生成的类不便于跨表查询，会破坏一个Mapper用于一种Pojo数据结构的原则。采用自定义的Mapper来更好的适应业务逻辑。适用于查询学生提交作业的数据展示
 * @author: qwer
 * @create: 2020-03-27 16:47
 */
@Component
@Mapper
public interface ExSubmitTaskMapper {
    //查询一个作业下所有提交了作业的学生
    @Select("select stu.sid sid, " +
            "stu.sname sname, " +
            "stu.icon icon, " +
            "submit_tasks.sub_task_id sub_task_id," +
            " submit_tasks.submit_time submit_time," +
            " submit_tasks.is_tea_mark is_tea_mark," +
            " submit_tasks.score score\n" +
            "from stu INNER join submit_tasks on stu.sid=submit_tasks.sid " +
            "where task_id=#{taskId};")
    List<ExSubmitTask> selectStudentsOfSubmitTaskByTaskID(@Param("taskId") long taskId);

    //查询一个学生提交的所有的作业
    @Select("SELECT\n" +
            "\tsub_task_id,\n" +
            "\tsid,\n" +
            "\tsubmit_time,\n" +
            "\tis_tea_mark,\n" +
            "\ttask.score score_count,\n" +
            "\tsubmit_tasks.score score,\n" +
            "\ttask.task_id task_id,\n" +
            "\ttask_name,\n" +
            "\ttask.end_time end_time,\n" +
            "\tcourse.cid cid,\n" +
            "\tcname\n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN task ON submit_tasks.task_id = task.task_id inner join course on task.cid=course.cid\n" +
            "\twhere sid= #{sid};")
    List<ExSubmitTask> selectSubmitTasksOfStudentByStudentPrimaryKey(@Param("sid") long sid);

    //查询一个教师下所有课程所有作业下学生的提交作业（如果需要查询已批改和未批改的作业，请通过isPass属性进行筛选）
    @Select("SELECT\n" +
            "\tsub_task_id,\n" +
            "\tstu.sid sid,\n" +
            "\tsubmit_time,\n" +
            "\ttask.score score,\n" +
            "\tsname,\n" +
            "\ttask_name,\n" +
            "\tcourse.cid,\n" +
            "\tcname,\n" +
            "\tis_tea_mark\n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN stu ON submit_tasks.sid = stu.sid\n" +
            "\tINNER JOIN task ON task.task_id = submit_tasks.task_id\n" +
            "\tINNER JOIN course ON task.cid = course.cid \n" +
            "WHERE\n" +
            "\ttid = #{tid} \n" +
            "\tORDER BY submit_time\n" +
            "\t")
    List<ExSubmitTask> selectSubmitTasksOfCoursesOfTeacherByTeacherPrimaryKey(@Param("tid") Long tid);

    //查询一个已经提交的作业，方便学生修改已经提交的作业
    @Select("SELECT  * FROM submit_tasks WHERE sid = #{sid} AND task_id = #{taskId};")
    SubmitTasks selectSubmitTaskForCheck(@Param("taskId") Long taskId, @Param("sid") Long sid);

    @Select("SELECT\n" +
            "\tsub_task_id,\n" +
            "\tsubmit_tasks.score score,\n" +
            "\tis_tea_mark,\n" +
            "\tsubmit_time,\n" +
            "\tstu.sid sid,\n" +
            "\tsname,\n" +
            "\ttask.task_id task_id,\n" +
            "\ttask_name,\n" +
            "\ttask.score score_count,\n" +
            "\tis_short_auto_check_answer\n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN task ON submit_tasks.task_id = task.task_id\n" +
            "\tinner join stu on submit_tasks.sid=stu.sid\n" +
            "\twhere sub_task_id=#{submitTaskId}")
    ExSubmitTask selectExSubmitTaskBySubmitTaskId(@Param("submitTaskId") Long submitTaskId);
}