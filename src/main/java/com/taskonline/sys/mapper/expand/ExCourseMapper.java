package com.taskonline.sys.mapper.expand;

import com.taskonline.sys.entity.expand.ExCourse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: taskonline
 * @description: CourseMapper扩展，扩展班级人数查询、扩展学生选课信息、扩展课程教师信息
 * @author: qwer
 * @create: 2020-03-27 18:20
 */
@Component
@Mapper
public interface ExCourseMapper {

    //查询教师所有的课程（扩展班级人数字段）
    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcourse.cname cname,\n" +
            "\tcourse.tid tid,\n" +
            "\tcourse.`describe` `describe`,\n" +
            "\tcourse.create_time create_time,\n" +
            "\tcourse.end_time end_time,\n" +
            "\tCOUNT( stu_courses.sid ) count \n" +
            "FROM\n" +
            "\tcourse\n" +
            "\tLEFT JOIN stu_courses ON course.cid = stu_courses.cid \n" +
            "WHERE\n" +
            "\ttid = #{tid} \n" +
            "GROUP BY\n" +
            "\tcourse.cid \n" +
            "ORDER BY\n" +
            "\tcourse.create_time DESC;")
    public List<ExCourse> selectCoursesOfTeacherByTeacherPrimaryKey(@Param("tid") long tid);

    //查询一个学生选的所有课程（不提供过期课程查询，在程序中对于所有课程进行筛选）
    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcname,\n" +
            "\ttea.tid tid,\n" +
            "\ttname,\n" +
            "\t`describe`,\n" +
            "\tcreate_time,\n" +
            "\tend_time,\n" +
            "\tselect_time,\n" +
            "\tcourse_appraise,\n" +
            "\tcourse_score \n" +
            "FROM\n" +
            "\tcourse\n" +
            "\tINNER JOIN tea ON course.tid = tea.tid\n" +
            "\tINNER JOIN stu_courses ON course.cid = stu_courses.cid\n" +
            "\twhere sid=#{sid}\n" +
            "\tORDER BY course.create_time desc\n" +
            "\t")
    public List<ExCourse> selectStudentCoursesByStudentPrimaryKey(@Param("sid") long sid);

    //模糊查询课程-关键字-课程名称
    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcname,\n" +
            "\ttea.tid tid,\n" +
            "\ttname,\n" +
            "\t`describe`,\n" +
            "\tcreate_time,\n" +
            "\tend_time,\n" +
            "\tCOUNT(stu_courses.sid) count\n" +
            "FROM\n" +
            "\tcourse\n" +
            "\tINNER JOIN tea ON course.tid = tea.tid\n" +
            "\tLEFT JOIN stu_courses on course.cid = stu_courses.cid\n" +
            "\twhere cname  like CONCAT('%',#{cnameKey},'%')\n" +
            "\tGROUP BY course.cid \n" +
            "\tORDER BY course.create_time DESC;")
    public List<ExCourse> selectCourseByKeyCname(@Param("cnameKey") String cnameKey);

    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcname,\n" +
            "\ttea.tid tid,\n" +
            "\ttname,\n" +
            "\t`describe`,\n" +
            "\tcreate_time,\n" +
            "\tend_time,\n" +
            "\tCOUNT(stu_courses.sid) count\n" +
            "FROM\n" +
            "\tcourse\n" +
            "\tINNER JOIN tea ON course.tid = tea.tid\n" +
            "\tLEFT JOIN stu_courses on course.cid = stu_courses.cid\n" +
            "\twhere tname  like CONCAT('%',#{tnameKey},'%')\n" +
            "\tGROUP BY course.cid \n" +
            "\tORDER BY course.create_time DESC;")
    public List<ExCourse> selectCourseByKeyTname(@Param("tnameKey") String tnameKey);

    //查询一个课程的信息-扩展教师名称、选课人数
    @Select("SELECT\n" +
            "\tcourse.cid cid,\n" +
            "\tcname,\n" +
            "\t`describe`,\n" +
            "\ttname,\n" +
            "\tcreate_time,\n" +
            "\tend_time,\n" +
            "\tcount(stu_courses.sid) count\n" +
            "FROM\n" +
            "\tcourse\n" +
            "\tINNER JOIN tea ON course.tid = tea.tid\n" +
            "\tLEFT JOIN stu_courses ON course.cid = stu_courses.cid\n" +
            "\twhere course.cid= #{cid}\n" +
            "\tGROUP BY course.cid;")
    public List<ExCourse> selectCourseByCid(@Param("cid") Long cid);

}