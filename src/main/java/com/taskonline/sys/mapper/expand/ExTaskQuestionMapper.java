package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.expand.ExQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/*
 * 扩展Mapper，原Mapper只适合查询的单个表数据，此Mapper配合ExTaskQuestion查询一个作业下的所有问题（包括问题扩展-选择题选项和多选题选项）
 * */
@Component
@Mapper
public interface ExTaskQuestionMapper {

    //查询一个作业下的所有的问题（扩展选择题、多选题字段）
    @Select("SELECT\n" +
            "\tquestion.qid qid,\n" +
            "\tseq_num,\n" +
            "\tcontent,\n" +
            "\tanswer, \n" +
            "\tscore,\n" +
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
            "\tmultiple_choice.choice_j m_choice_j\n" +
            "FROM\n" +
            "\tquestion\n" +
            "\tLEFT JOIN choice ON question.qid = choice.qid\n" +
            "\tLEFT JOIN multiple_choice ON question.qid = multiple_choice.qid \n" +
            "WHERE\n" +
            "\ttask_id = #{taskId} \n" +
            "ORDER BY\n" +
            "\tseq_num;")
    List<ExQuestion> selectTaskQuestionByTaskID(@Param("taskId") long taskId);
}
