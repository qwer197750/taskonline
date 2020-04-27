package com.taskonline.sys.mapper;

import com.taskonline.sys.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface GroupMapper {
    int deleteByPrimaryKey(Long gid);

    int insert(Group record);

    int insertSelective(Group record);

    Group selectByPrimaryKey(Long gid);

    int updateByPrimaryKeySelective(Group record);

    int updateByPrimaryKey(Group record);
}