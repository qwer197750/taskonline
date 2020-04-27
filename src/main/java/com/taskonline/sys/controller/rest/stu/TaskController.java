package com.taskonline.sys.controller.rest.stu;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.expand.ExTask;
import com.taskonline.sys.service.student.StuSubmitAnswerService;
import com.taskonline.sys.service.student.StuTaskService;
import com.taskonline.sys.service.teacher.TeaTaskService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.RequestParamUtil;
import com.taskonline.util.UserUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 作业控制器。适用于学生
 * @author: qwer
 * @create: 2020-04-09 11:25
 */
@RequestMapping("/stu/task")
@RestController("stuTaskController")
public class TaskController {
    @Resource
    InfoString infoString;

    @Resource
    RequestParamUtil requestParamUtil;

    @Resource
    StuTaskService stuTaskService;
    @Resource
    TeaTaskService teaTaskService;
    @Resource
    StuSubmitAnswerService submitAnswerService;


    /*
     *@Description: 学生查询作业，如果未提交过这个作业，那么返回单纯的作业。如果已经提交过了，那么返回作业和提交的答案。如果老师已经批改了了，那么再返回正确答案
     *@param taskId：作业id。隐藏参数：如果存在提交的作业，如果提交的作业已被老师批改
     *@return:
     *@Author: qwer
     *@date: 2020/4/11
     */
    @GetMapping("{taskId}")
    public Map<String,Object> getTaskDetail(@PathVariable(value = "taskId",required = true) String taskId){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        long tid ;
        try{
            tid = Long.parseLong(taskId);
        }catch (NumberFormatException nfe){
            Map<String,Object> m = new HashMap<>();
            m.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return m;
        }
        return stuTaskService.selectTaskDetail(currentStu.getSid(),tid);
    }

    @PostMapping("submitTask/{taskId}")
    public Map<String,Object> submitTask(@PathVariable("taskId") String taskId,Boolean ignoreLockAnswer, HttpServletRequest request){
        if(ignoreLockAnswer == null)ignoreLockAnswer=false;
        Map<String,String> param = requestParamUtil.getParaFromRequest(request);
        Map<String,Object> map;
        long tid;
        try{
            tid=Long.parseLong(taskId);
        }catch (NumberFormatException nfe){
            Map<String,Object> m = new HashMap<>();
            m.put(MessageMap.MSG, infoString.NULL_TASK_ID);
            return m;
        }
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        List<SubmitAnswers> answers=requestParamUtil.arrangeParamForSubmitTask(param);
        Map<String,Object> m = teaTaskService.selectTaskDetail(tid);
        if (m.containsKey(MessageMap.MSG)){
            return m;
        }
        ExTask task = (ExTask) m.get(MessageMap.OBJECT);
        String msg = task.AnswerValidator(ignoreLockAnswer,answers);
        if(msg!=null){
            m.put(MessageMap.MSG,msg);
            return m;
        }
        try {
            assert currentStu != null;
            map = submitAnswerService.submitTask(currentStu.getSid(), tid, answers);
        } catch (SQLException e) {
            map = new HashMap<>();
            map.put(MessageMap.MSG, "插入数据库失败");
        }
        return map;
    }

    @GetMapping("myTasks")
    public Map<String,Object> getMyTasks(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        Map<String,Object> map = stuTaskService.selectStuAllTasks(currentStu.getSid());
        return map;
    }

    @GetMapping("myNotSubmitTasks")
    public Map<String,Object> getMyNotSubmitTasks(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        Map<String,Object> map = stuTaskService.selectStudentNotSubmitTasks(currentStu.getSid());
        return map;
    }

    @GetMapping("mySubmitTasks")
    public Map<String,Object> getMySubmitTasks(){
        Student currentStu = (Student) UserUtil.getCurrentUserFromSecurity();
        assert currentStu != null;
        Map<String,Object> map = stuTaskService.selectStudentSubmitTask(currentStu.getSid());
        return map;
    }

}