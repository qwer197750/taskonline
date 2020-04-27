package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.Choice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ChoiceMapper {
    int deleteByPrimaryKey(Long qid);

    int insert(Choice record);

    int insertSelective(Choice record);

    Choice selectByPrimaryKey(Long qid);

    int updateByPrimaryKeySelective(Choice record);

    int updateByPrimaryKey(Choice record);
}