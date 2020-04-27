package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.StuCourses;
import com.taskonline.sys.entity.StuCoursesKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface StuCoursesMapper {
    int deleteByPrimaryKey(StuCoursesKey key);

    int insert(StuCourses record);

    int insertSelective(StuCourses record);

    StuCourses selectByPrimaryKey(StuCoursesKey key);

    int updateByPrimaryKeySelective(StuCourses record);

    int updateByPrimaryKey(StuCourses record);
}