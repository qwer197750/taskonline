package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.Student;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface StudentMapper {
    int deleteByPrimaryKey(Long sid);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Long sid);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);
}