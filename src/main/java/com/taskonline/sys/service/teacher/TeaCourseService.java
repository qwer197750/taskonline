package com.taskonline.sys.service.teacher;

import com.taskonline.sys.entity.Course;
import com.taskonline.sys.entity.expand.ExCourse;
import com.taskonline.sys.mapper.CourseMapper;
import com.taskonline.sys.mapper.expand.ExCourseMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: service：为教师提供，查询教师所有的课程
 * @author: qwer
 * @create: 2020-03-29 17:52
 */
@Service
public class TeaCourseService {
    @Autowired
    ExCourseMapper exCourseMapper;

    @Autowired
    InfoString infoString;

    @Autowired
    CourseMapper courseMapper;

    /*
     *@Description:教师调用查询教师拥有的课程
     *@param tid 教师数据库表主键，而不是teacherId
     *@return: java.util.Map<java.lang.String,java.lang.Object>，成功：<object, List<ExStudent>> 失败：<msg,错误信息>
     *@Author: qwer
     *@date: 2020/3/29
     */
    public Map<String,Object> selectTeacherCourse(Long tid){
        Map<String,Object> map= new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExCourse> listCourse=exCourseMapper.selectCoursesOfTeacherByTeacherPrimaryKey(tid);
        map.put(MessageMap.OBJECT, listCourse);
        return map;
    }

    /*
     *@Description: 查询教师未过期的课程，没有相应的数据库接口，通过程序判断
     *@param tid 教师表的主键
     *@return:
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> selectTeacherCourseOfNotEnd(Long tid){
        Map<String,Object> map= new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExCourse> listCourse=exCourseMapper.selectCoursesOfTeacherByTeacherPrimaryKey(tid);

        //遍历，清除过期的课程，只留下未过期的课程
        for(int i=listCourse.size()-1; i>=0; i--){
            ExCourse ec = listCourse.get(i);
            Date endTime = ec.getEndTime();
            if(endTime.before(new Date())){
                listCourse.remove(i);
            }
        }
        map.put(MessageMap.OBJECT, listCourse);
        return map;
    }

    /*
     *@Description: 查询教师过期的课程
     *@param tid 教师表的主键
     *@return:
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> selectTeacherEndCourse(Long tid){
        Map<String,Object> map= new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExCourse> listCourse=exCourseMapper.selectCoursesOfTeacherByTeacherPrimaryKey(tid);

        //遍历，清除未过期的课程，只留下过期的课程
        for(int i=listCourse.size()-1; i>=0; i--){
            ExCourse ec = listCourse.get(i);
            Date endTime = ec.getEndTime();
            if(endTime.after(new Date())){
                listCourse.remove(i);
            }
        }
        map.put(MessageMap.OBJECT, listCourse);
        return map;
    }

    /*
     *@Description: 创建课程，课程名不为空，描述可空，创建时间不能晚于当前时间，结束时间不能早于当前时间和创建时间
     *@param
     *@return: map，成功key:object，失败key：msg
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> createCourse(Long tid, String cname, String describe, Date createTime, Date endTime){
        Map<String,Object> map=new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        if (StringUtils.isEmpty(cname)){
            map.put(MessageMap.MSG, "班级名称不能为空");
            return map;
        }
        if(endTime == null){
            map.put(MessageMap.MSG, "请输入结束时间");
            return map;
        }
        if(createTime == null){
            map.put(MessageMap.MSG, "请输入创建时间");
            return map;
        }
        if(createTime.after(new Date())){
            map.put(MessageMap.MSG, "创建时间不应该晚于当前时间");
            return map;
        }
        if (endTime.before(createTime) || endTime.before(new Date())){
            map.put(MessageMap.MSG, "结束时间不应该早于创建时间或当前时间");
            return map;
        }
        Course course=new Course();
        course.setTid(tid);
        course.setCname(cname);
        course.setDescribe(describe);
        course.setCreateTime(createTime);
        course.setEndTime(endTime);
        int row=courseMapper.insert(course);
        if(row<1){
            map.put(MessageMap.MSG, "创建课程失败");
            return map;
        }
        map.put(MessageMap.OBJECT, course);
        return map;
    }
}