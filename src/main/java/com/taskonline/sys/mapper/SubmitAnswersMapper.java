package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.SubmitAnswersKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SubmitAnswersMapper {
    int deleteByPrimaryKey(SubmitAnswersKey key);

    int insert(SubmitAnswers record);

    int insertSelective(SubmitAnswers record);

    SubmitAnswers selectByPrimaryKey(SubmitAnswersKey key);

    int updateByPrimaryKeySelective(SubmitAnswers record);

    int updateByPrimaryKeyWithBLOBs(SubmitAnswers record);

    int updateByPrimaryKey(SubmitAnswers record);
}