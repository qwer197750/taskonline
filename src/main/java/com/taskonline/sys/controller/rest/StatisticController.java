package com.taskonline.sys.controller.rest;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.service.common.StatisticService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.UserUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 统计数据控制器
 * @author: qwer
 * @create: 2020-04-17 09:26
 */
@RestController
@RequestMapping("/statistic")
public class StatisticController {
    @Resource
    StatisticService statisticService;

    @Resource
    InfoString infoString;

    //获取学生所有作业成绩起伏
    @GetMapping("stuScoresData")
    public Map<String,Object> getStuScoresData(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        MessageMap map = new MessageMap(getClass());
        assert currentStu != null;
        return statisticService.getStuScores(currentStu.getSid());
    }

    //查询作业下每个问题学生成绩的平均成绩
    @GetMapping("taskQuestionsData/{taskId}")
    public Map<String,Object> getTaskQuestionsAVGScore(@PathVariable("taskId") String taskId){
        MessageMap map = new MessageMap(getClass());
        if(StringUtils.isEmpty(taskId)){
            map.put(MessageMap.MSG, infoString.NULL_TASK_ID);
            return map;
        }
        long task_id;
        try{
            task_id = Long.parseLong(taskId);
        }catch (NumberFormatException nfe){
            map.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return map;
        }
        return statisticService.getTaskQuestionsAVGScore(task_id);
    }

    //查询一个课程下学生每次作业的成绩起伏
    @GetMapping("stuScoresOfCourse/{cid}/{sid}")
    public Map<String,Object> getStuScoresOfCourse(@PathVariable("cid") String cid,@PathVariable("sid") String sid){
        MessageMap map = new MessageMap(getClass());
        if(StringUtils.isEmpty(cid)){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        long stuId;
        long courseId;
        try{
            stuId = Long.parseLong(sid);
            courseId = Long.parseLong(cid);
        }catch (NumberFormatException nfe){
            map.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return map;
        }
        return statisticService.getStuScoresOfCourse(courseId,stuId);
    }

    //查询课程下每次作业的平均成绩起伏
    @GetMapping("courseAvgScores/{cid}")
    public Map<String,Object> getCourseScoresAvgData(@PathVariable("cid") String cid){
        MessageMap map = new MessageMap(getClass());
        if(StringUtils.isEmpty(cid)){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        long courseId;
        try{
            courseId = Long.parseLong(cid);
        }catch (NumberFormatException nfe){
            map.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return map;
        }
        return statisticService.getCourseScoresAvgData(courseId);
    }

}