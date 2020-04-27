package com.taskonline.sys.mapper.expand;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/*
 *@Description: 统计数据查询mapper
 *@Author: qwer
 *@date: 2020/4/16
 */
@Component
@Mapper
public interface ExStatisticMapper {

    //查询课程内每次作业学生平均成绩的数据
    @Select("SELECT\n" +
            "\tAVG(task.score) score,\n" +
            "\tAVG(submit_tasks.score) submit_score,\n" +
            "\tsubmit_tasks.task_id task_id,\n" +
            "\ttask_name\n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN task ON submit_tasks.task_id = task.task_id\n" +
            "WHERE\n" +
            "\tcid = #{cid} \n" +
            "\tand is_tea_mark=true\n" +
            "\tGROUP BY task_id\n" +
            "\tORDER BY task.create_time")
    List<Map<String, Object>> selectCourseTaskAvgScore(@Param("cid") Long cid);

    //查询学生在一个课程下每次作业学生成绩的数据
    @Select("SELECT\n" +
            "\tsubmit_tasks.score submit_score,\n" +
            "\ttask.score score,\n" +
            "\ttask_name \n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN task ON submit_tasks.task_id = task.task_id \n" +
            "WHERE\n" +
            "\tcid = #{cid} \n" +
            "\tAND sid = #{sid} \n" +
            "\tAND is_tea_mark = TRUE \n" +
            "ORDER BY\n" +
            "\tsubmit_time")
    List<Map<String, Object>> selectStuScoresOfCourse(@Param("cid") Long cid, @Param("sid") Long sid);

    //查询学生所有被批改作业成绩数据
    @Select("SELECT\n" +
            "\ttask.score score,\n" +
            "\tsubmit_tasks.score submit_score,\n" +
            "\tcourse.cid cid,\n" +
            "\tcourse.cname cname,\n" +
            "\ttask_name\n" +
            "FROM\n" +
            "\tsubmit_tasks\n" +
            "\tINNER JOIN task ON submit_tasks.task_id = task.task_id\n" +
            "\tINNER JOIN course ON task.cid = course.cid \n" +
            "WHERE\n" +
            "\tis_tea_mark = TRUE \n" +
            "\tAND sid = #{sid} \n" +
            "ORDER BY\n" +
            "\ttask.create_time")
    List<Map<String, Object>> selectStuScores(@Param("sid") Long sid);

    //查询一个作业下各个问题 学生提交答案的得分数据
    @Select("SELECT\n" +
            "\tAVG(submit_answers.score) submit_score,\n" +
            "\tAVG(question.score)\tscore,\n" +
            "\tseq_num\n" +
            "FROM\n" +
            "\tsubmit_answers\n" +
            "\tINNER JOIN question ON submit_answers.qid = question.qid\n" +
            "\tinner join submit_tasks on submit_answers.sub_task_id=submit_tasks.sub_task_id\n" +
            "WHERE\n" +
            "\tsubmit_tasks.task_id = #{taskId} and is_tea_mark=true\n" +
            "\tgroup by seq_num\n" +
            "ORDER BY question.seq_num")
    List<Map<String, Object>> selectTaskQuestionsAVGScore(@Param("taskId") Long taskId);


}
