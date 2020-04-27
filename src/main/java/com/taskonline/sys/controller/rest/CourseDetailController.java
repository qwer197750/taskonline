package com.taskonline.sys.controller.rest;

import com.taskonline.sys.pojo.User;
import com.taskonline.sys.service.common.CourseService;
import com.taskonline.util.MessageMap;
import com.taskonline.util.UserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 课程信息控制器。查询课程信息。课程下的作业列表
 * @author: qwer
 * @create: 2020-04-07 17:19
 */
@RequestMapping("/course")
@RestController
public class CourseDetailController {
    @Resource
    CourseService courseService;

    @GetMapping("{cid}")
    public Map<String,Object> map(@PathVariable String cid){
         User user= UserUtil.getCurrentUserFromSecurity();
         Map<String,Object> map = new HashMap<>();
         if (user == null){
             map.put(MessageMap.MSG, "请登录");
             return map;
         }
         return courseService.selectCourseInfo(cid);
    }
}