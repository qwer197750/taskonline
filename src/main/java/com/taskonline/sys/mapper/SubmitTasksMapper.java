package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.SubmitTasks;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface SubmitTasksMapper {
    int deleteByPrimaryKey(Long subTaskId);

    int insert(SubmitTasks record);

    int insertSelective(SubmitTasks record);

    SubmitTasks selectByPrimaryKey(Long subTaskId);

    int updateByPrimaryKeySelective(SubmitTasks record);

    int updateByPrimaryKey(SubmitTasks record);
}