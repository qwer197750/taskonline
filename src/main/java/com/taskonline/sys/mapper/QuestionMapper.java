package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface QuestionMapper {
    int deleteByPrimaryKey(Long qid);

    int insert(Question record);

    int insertSelective(Question record);

    Question selectByPrimaryKey(Long qid);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);
}