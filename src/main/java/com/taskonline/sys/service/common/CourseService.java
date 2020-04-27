package com.taskonline.sys.service.common;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Task;
import com.taskonline.sys.entity.expand.ExCourse;
import com.taskonline.sys.entity.expand.ExTask;
import com.taskonline.sys.mapper.expand.ExCourseMapper;
import com.taskonline.sys.mapper.expand.ExStudentMapper;
import com.taskonline.sys.mapper.expand.ExTaskMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 课程信息service；查询课程信息、及课程下的作业列表
 * @author: qwer
 * @create: 2020-04-07 17:29
 */
@Service
public class CourseService {
    @Resource
    ExCourseMapper exCourseMapper;
    @Resource
    ExTaskMapper exTaskMapper;
    @Resource
    ExStudentMapper exStudentMapper;
    @Resource
    InfoString infoString;

    /*
     *@Description: 查询一个课程的信息-扩展教师名称、选课人数
     *@param
     *@return:map。成功key：object、value：包含tasks的exCourse对象，失败key:msg
     *@Author: qwer
     *@date: 2020/4/7
     */
    public Map<String,Object> selectCourseInfo(String courseId){
        Map<String,Object> map = new MessageMap(getClass());
        if(StringUtils.isEmpty(courseId)){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        Long cid;
        try{
            cid=Long.valueOf(courseId);
        }catch (NumberFormatException nfe){
            map.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return map;
        }
        List<ExCourse> courseList = exCourseMapper.selectCourseByCid(cid);
        if(courseList.size()<1){
            map.put(MessageMap.MSG, "没有查询到对应的课程信息");
            return map;
        }
        List<Task> taskList=exTaskMapper.selectTaskOfCourse(cid);
        List<Student> students = exStudentMapper.selectStudentsOfCourseByCourseID(cid);
        ExCourse exCourse=courseList.get(0);
        exCourse.setTasks(taskList);
        exCourse.setStudents(students);
        for(Student student:students)student.setPassword(null);
        map.put(MessageMap.OBJECT, exCourse);
        return map;
    }
}