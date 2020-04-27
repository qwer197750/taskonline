package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.mapper.StudentMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ExStudentMapper extends StudentMapper {
    /*
    * 继承自动生成的Mapper，添加注册、登录接口等方法
    * */

    //学生登录，密码已在service层被哈希（此处studentId不是主键，其他时候表示主键，仅限登录使用）
    @Select("select * from stu where student_id=#{studentID} and password=#{password}")
    Student selectByStudentIDPassword(@Param("studentID") String studentID, @Param("password") String password);

    //通过学号查询一个学生(此处学号并不是主键sid,仅限注册验证使用)
    @Select("select * from stu where student_id=#{studentID}")
    Student selectByStudentID(@Param("studentID") String studentID);

    //查询一个课程下的所有学生
    @Select("select * from stu where sid in (select sid from stu_courses where cid=#{cid})")
    List<Student> selectStudentsOfCourseByCourseID(@Param("cid") long cid);

    //查询一个作业下所有未提交作业的学生，需要作业id和班级id（限于数据库表的设计，所以需要班级id简化sql）
    @Select("SELECT\n" +
            "\tstu.sid sid, sname, icon\t\n" +
            "FROM\n" +
            "\t( stu INNER JOIN stu_courses ON stu.sid = stu_courses.sid ) \n" +
            "WHERE\n" +
            "\tcid = 1 \n" +
            "\tAND stu.sid NOT IN ( SELECT sid FROM submit_tasks WHERE task_id = 1 );")
    List<Student> selectStudentOfNotSubmitTaskByCidAndTaskID(@Param("cid") long cid, @Param("taskId") long taskId);

}
