package com.taskonline.sys.controller.rest.tea;

import com.taskonline.sys.entity.Task;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.entity.expand.ExQuestion;
import com.taskonline.sys.service.student.StuTaskService;
import com.taskonline.sys.service.teacher.TeaTaskService;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import com.taskonline.util.RequestParamUtil;
import com.taskonline.util.UserUtil;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.crypto.Data;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: 教师作业控制器
 * @author: qwer
 * @create: 2020-04-07 19:39
 */
@RequestMapping("/tea/task")
@RestController("teaTaskController")
public class TaskController {
    @Resource
    RequestParamUtil requestParamUtil;
    @Resource
    TeaTaskService teaTaskService;

    @Resource
    InfoString infoString;


    @PostMapping({"creatTask/{cid}/{taskId}","creatTask/{cid}/" })
    public Map<String,Object> createTask(@PathVariable("cid")String cid,@PathVariable(value = "taskId", required = false) String taskId
                                        , HttpServletRequest request){
        Map<String,String> param = requestParamUtil.getParaFromRequest(request);
        Map<String,Object> rs = requestParamUtil.arrangeParamForCreateTask(param);
        if(rs.containsKey(MessageMap.MSG)){
            return rs;
        }
        Long courseId, task_id = null;
        try{
            courseId=Long.parseLong(cid);
        }catch (NumberFormatException nfe){
            rs.put(MessageMap.MSG,"错误格式的班级号");
            return rs;
        }
        if(taskId!=null){
            try{
                task_id = Long.parseLong(taskId);
            }catch (NumberFormatException nfe){
                rs.put(MessageMap.MSG,"错误格式的作业号");
                return rs;
            }
        }
        try {
            rs = teaTaskService.createTaskAndQuestions(task_id, courseId, (Task)(rs.get("task")), (List<ExQuestion>)(rs.get("questions")));
        } catch (SQLException e) {
            e.printStackTrace();
            rs.put(MessageMap.MSG, "写入数据库失败");
            return rs;
        }
        return rs;
    }

    @GetMapping("{taskId}")
    public Map<String,Object> getTaskDetail(@PathVariable(value = "taskId",required = true) String taskId){
        long tid ;
        try{
            tid = Long.parseLong(taskId);
        }catch (NumberFormatException nfe){
            Map<String,Object> m = new HashMap<>();
            m.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return m;
        }
        return teaTaskService.selectTaskDetail(tid);
    }


    @GetMapping("submitTask/{subTaskId}")
    public Map<String,Object> getSubmitTaskDetail(@PathVariable(value = "subTaskId") String subTaskId){
        long submitTaskId ;
        try{
            submitTaskId = Long.parseLong(subTaskId);
        }catch (NumberFormatException nfe){
            Map<String,Object> m = new HashMap<>();
            m.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return m;
        }
        return teaTaskService.selectSubmitTaskDetailForTeaBySubTaskId(submitTaskId);
    }

    @PostMapping("CheckSubmitTask/{submitTaskId}")
    public Map<String,Object> checkSubmitTask(@PathVariable(value = "submitTaskId") String subTaskId, HttpServletRequest request){
        long submitTaskId ;
        try{
            submitTaskId = Long.parseLong(subTaskId);
        }catch (NumberFormatException nfe){
            Map<String,Object> m = new HashMap<>();
            m.put(MessageMap.MSG, infoString.TYPE_CHANGE_EXCEPTION);
            return m;
        }
        Map<String,String> rs = requestParamUtil.getParaFromRequest(request);
        System.out.println(rs);
        Map<String,Object> map = new MessageMap(getClass());
        try {
            map = teaTaskService.checkSubmitTask(submitTaskId,rs);
        } catch (SQLException e) {
            map.put(MessageMap.EXCEPTION, e);
            map.put(MessageMap.MSG, "写入数据库失败，数据已回滚");
        }
        return map;
    }


    @GetMapping("submitTasks")
    public Map<String,Object> getTasks(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        Map<String,Object> map = teaTaskService.selectSubmitTaskOfCoursesOfTeacher(currentTea.getTid());
        return map;
    }

    @GetMapping("waitCheckSubmitTasks")
    public Map<String,Object> getWaitCheckSubmitTasks(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        Map<String,Object> map = teaTaskService.selectSubmitTaskOfCoursesOfTeacherNotPass(currentTea.getTid());
        return map;
    }

    @GetMapping("checkedSubmitTasks")
    public Map<String,Object> getCheckedSubmitTasks(){
        Teacher currentTea = (Teacher) UserUtil.getCurrentUserFromSecurity();
        assert currentTea != null;
        Map<String,Object> map = teaTaskService.selectSubmitTaskOfCoursesOfTeacherOfPass(currentTea.getTid());
        return map;
    }
}