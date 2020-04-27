package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.expand.ExAnswer;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/*
* 这个接口是SubmitAnswerMapper的扩展，用于查询提交的答案时，顺带查询出对应的问题、选项
* */
@Component
@Mapper
public interface ExSubmitAnswerMapper {
    //查询学生提交的答案，扩展答案对应的问题、选项
    @Select("SELECT\n" +
            "\tsub_task_id,\n" +
            "\tquestion.qid qid,\n" +
            "\tseq_num,\n" +
            "\tcontent,\n" +
            "\tquestion.answer answer, \n" +
            "\tquestion.score score,\n" +
            "\ttype,\n" +
            "\tchoice.choice_a choice_a,\n" +
            "\tchoice.choice_b choice_b,\n" +
            "\tchoice.choice_c choice_c,\n" +
            "\tchoice.choice_d\tchoice_d,\n" +
            "\tmultiple_choice.choice_a m_choice_a,\n" +
            "\tmultiple_choice.choice_b m_choice_b,\n" +
            "\tmultiple_choice.choice_c m_choice_c,\n" +
            "\tmultiple_choice.choice_d m_choice_d,\n" +
            "\tmultiple_choice.choice_e m_choice_e,\n" +
            "\tmultiple_choice.choice_f m_choice_f,\n" +
            "\tmultiple_choice.choice_g m_choice_g,\n" +
            "\tmultiple_choice.choice_h m_choice_h,\n" +
            "\tmultiple_choice.choice_i m_choice_i,\n" +
            "\tmultiple_choice.choice_j m_choice_j,\n" +
            "\tsubmit_answers.answer submit_answer,\n" +
            "\tsubmit_answers.score submit_score\n" +
            "FROM\n" +
            "\t(select \n" +
            "\tqid,\n" +
            "\tseq_num,\n" +
            "\tcontent, \n" +
            "\tanswer,\n" +
            "\tquestion.score score,\n" +
            "\ttype\n" +
            "from question inner join submit_tasks on question.task_id=submit_tasks.task_id where sub_task_id= #{subTaskId}) as question\n" +
            "\tinner JOIN submit_answers on submit_answers.qid=question.qid\n" +
            "\tLEFT JOIN choice ON question.qid = choice.qid\n" +
            "\tLEFT JOIN multiple_choice ON question.qid = multiple_choice.qid \n" +
            "\twhere sub_task_id= #{subTaskId} or sub_task_id is null\n" +
            "ORDER BY\n" +
            "\tseq_num;")
    List<ExAnswer> selectSubmitAnswerBySubmitTaskID(@Param("subTaskId") Long subTaskId);

    @Delete("delete from submit_answers where sub_task_id = #{subTaskId}")
    void deleteSubmitAnswersOfSubmitTaskBySubmitTask(@Param("subTaskId") Long subTaskId);

    @Select("select * from submit_answers where sub_task_id = #{submitTaskId}")
    List<SubmitAnswers> selectSubmitAnswersOfSubmitTaskBySubmitTaskId(@Param("submitTaskId") Long submitTaskId);
}
