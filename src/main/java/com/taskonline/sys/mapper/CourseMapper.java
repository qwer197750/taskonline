package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.Course;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface CourseMapper {
    int deleteByPrimaryKey(Long cid);

    int insert(Course record);

    int insertSelective(Course record);

    Course selectByPrimaryKey(Long cid);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKeyWithBLOBs(Course record);

    int updateByPrimaryKey(Course record);
}