package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.mapper.TeacherMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface ExTeacherMapper extends TeacherMapper {
    /*
    * 继承自动生成的TeacherMapper接口，添加教师登录、注册等方法
    * */

    //教师登录，密码在service已被哈希，此处teacherId未数据库中的teacher_id，而不是tid（仅在登录注册时有效）
    @Select("select * from tea where teacher_id=#{teacherID} and password=#{password}")
    Teacher selectByTeacherIDPassword(@Param("teacherID") String teacherID, @Param("password") String password);

    //查询教师信息(此处仅限注册时检查是否已被注册使用。teacherId为数据库中的teacher_id，而不是主键tid， 其他时候查询教师信息请使用selectByPrimaryKey(tid))
    @Select("select * from tea where teacher_id=#{teacherID}")
    Teacher selectByTeacherID(@Param("teacherID") String teacherID);
}
