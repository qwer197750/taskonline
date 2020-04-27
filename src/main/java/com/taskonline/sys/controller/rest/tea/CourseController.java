package com.taskonline.sys.controller.rest.tea;

import com.taskonline.sys.entity.Course;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.mapper.CourseMapper;
import com.taskonline.sys.pojo.User;
import com.taskonline.sys.service.teacher.TeaCourseService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 前后端分离控制器。关于教师的课程信息
 * @author: qwer
 * @create: 2020-04-05 10:53
 */
@RequestMapping("/tea/course")
@RestController("teaCourseController")
public class CourseController {
    @Resource
    TeaCourseService teaCourseService;
    @Resource
    InfoString infoString;

    @GetMapping("myCourses")
    public Map<String,Object> getMyCourses(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea!= null;
        return teaCourseService.selectTeacherCourse(currentTea.getTid());
    }


    @GetMapping("notEndCourses")
    public Map<String,Object> getCourseOfNotEnd(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        return teaCourseService.selectTeacherCourseOfNotEnd(currentTea.getTid());

    }

    @GetMapping("endCourses")
    public Map<String,Object> getCourseOfEnd(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        return teaCourseService.selectTeacherEndCourse(currentTea.getTid());

    }


    @PostMapping("createCourse")
    public Map<String,Object> createCourse(String cname, String describe, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime ){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        return teaCourseService.createCourse(currentTea.getTid(),cname,describe,new Date(), endTime);
    }
}