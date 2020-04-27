package com.taskonline.sys.controller.rest.stu;

import com.taskonline.sys.entity.Course;
import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.mapper.CourseMapper;
import com.taskonline.sys.service.common.UserServer;
import com.taskonline.sys.service.student.StuCourseService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 前后端分离控制器。关于学生课程信息。返回json
 * @author: qwer
 * @create: 2020-04-05 10:38
 */
@RequestMapping("/stu/course")
@RestController("stuCourseController")
public class CourseController {
    @Resource
    UserServer userServer;
    @Resource
    StuCourseService stuCourseService;
    @Resource
    InfoString infoString;
    @Resource
    CourseMapper courseMapper;

    @GetMapping("myCourses")
    public Map<String, Object> getMyCourses(Model model){
        Student currentStu= (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        return stuCourseService.selectStudentCourse(currentStu.getSid());
    }

    @GetMapping("notEndCourses")
    public Map<String,Object> getCourseOfNotEnd(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        return stuCourseService.selectStudentCourseOfNotEnd(currentStu.getSid());

    }

    @GetMapping("endCourses")
    public Map<String,Object> getCourseOfEnd(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        return stuCourseService.selectStudentEndCourse(currentStu.getSid());

    }


    @PostMapping("joinCourse")
    public Map<String,Object> createCourse(String cid){
        long courseId;
        try{
            courseId= Long.parseLong(cid);
        }catch (NumberFormatException ignored){
            Map<String,Object> map = new HashMap<>();
            map.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return map;
        }
        Course course = courseMapper.selectByPrimaryKey(courseId);
        if(course == null || course.getCid()==null){
            Map<String,Object> map=new HashMap<>();
            map.put(MessageMap.MSG,"所选课程不存在");
            return map;
        }
        if(course.getEndTime().before(new Date())){
            Map<String,Object> map=new HashMap<>();
            map.put(MessageMap.MSG,"所选课程已结束");
            return map;
        }
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        return stuCourseService.joinCourse(currentStu.getSid(), courseId);
    }

    /*
     *@Description: 学生按关键字模糊查询课程列表
     *@param column:字段名（限制为教师名称、班级名称），key 查询关键字
     *@return:
     *@Author: qwer
     *@date: 2020/4/6
     */
    @PostMapping("selectCourse")
    public Map<String,Object> selectCourse(String column, String key){
        return stuCourseService.selectCourseByKey(column, key);
    }
}