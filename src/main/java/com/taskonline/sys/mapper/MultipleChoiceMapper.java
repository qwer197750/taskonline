package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.MultipleChoice;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface MultipleChoiceMapper {
    int insert(MultipleChoice record);

    int insertSelective(MultipleChoice record);
}