package com.taskonline.sys.service.student;

import com.taskonline.sys.entity.SubmitTasks;
import com.taskonline.sys.entity.Task;
import com.taskonline.sys.entity.expand.ExAnswer;
import com.taskonline.sys.entity.expand.ExQuestion;
import com.taskonline.sys.entity.expand.ExSubmitTask;
import com.taskonline.sys.entity.expand.ExTask;
import com.taskonline.sys.mapper.TaskMapper;
import com.taskonline.sys.mapper.expand.ExSubmitAnswerMapper;
import com.taskonline.sys.mapper.expand.ExSubmitTaskMapper;
import com.taskonline.sys.mapper.expand.ExTaskMapper;
import com.taskonline.sys.mapper.expand.ExTaskQuestionMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: service：服务于学生，关于学生的作业信息
 * @author: qwer
 * @create: 2020-03-29 18:32
 */
@Service
public class StuTaskService {
    @Resource
    ExTaskMapper exTaskMapper;

    @Resource
    InfoString infoString;

    @Resource
    TaskMapper taskMapper;

    @Resource
    ExTaskQuestionMapper exTaskQuestionMapper;

    @Resource
    ExSubmitTaskMapper exSubmitTaskMapper;

    @Resource
    ExSubmitAnswerMapper exSubmitAnswerMapper;

    /*
     *@Description: 查询作业详细，如果没有提交过，那么返回
     *@param taskId:作业id
     *@return: Map<String,Object>
     *@Author: qwer
     *@date: 2020/4/9
     */
    public Map<String,Object> selectTaskDetail(Long sid,Long taskId){
        MessageMap map = new MessageMap(getClass());
        if(taskId == null){
            map.put(MessageMap.MSG, infoString.NULL_TASK_ID);
            return map;
        }
        Task t = taskMapper.selectByPrimaryKey(taskId);
        if(t == null || t.getTaskId() == null){
            map.put(MessageMap.MSG,"该作业不存在");
            return map;
        }
        SubmitTasks submitTask = exSubmitTaskMapper.selectSubmitTaskForCheck(taskId,sid);
        if(submitTask == null){ //如果是没有提交过这个作业，只是单纯的获取作业问题
            ExTask task = new ExTask();
            BeanUtils.copyProperties(t,task);
            List<ExQuestion> questions=exTaskQuestionMapper.selectTaskQuestionByTaskID(taskId);
            for (ExQuestion question: questions) {
                question.setAnswer(null);//清空答案
            }
            task.setQuestions(questions);
            map.put(MessageMap.OBJECT, task);
        }else{ //如果提交过作业，那么把问题和提交的作业信息一起返回
            List<ExAnswer> answers = exSubmitAnswerMapper.selectSubmitAnswerBySubmitTaskID(submitTask.getSubTaskId());
            System.out.println(t.toString());
            ExSubmitTask exSubmitTask = new ExSubmitTask();
            System.out.println(submitTask.toString());
            BeanUtils.copyProperties(submitTask, exSubmitTask);
            exSubmitTask.setTaskName(t.getTaskName());
            exSubmitTask.setEndTime(t.getEndTime());
            exSubmitTask.setAnswers(answers);
            map.put(MessageMap.OBJECT, exSubmitTask);
        }
        return map;

    }

    /*
     *@Description: 查询学生的所有作业
     *@param sid学生id
     *@return: map
     *@Author: qwer
     *@date: 2020/4/9
     */
    public Map<String,Object> selectStuAllTasks(Long sid){
        MessageMap map = new MessageMap(getClass());
        List<ExTask> tasks=exTaskMapper.selectStudentTasksByStudentPrimaryKey(sid);
        map.put(MessageMap.OBJECT, tasks);
        return map;
    }

    /*
     *@Description: 查询学生未提交的作业
     *@param sid学生id
     *@return: map
     *@Author: qwer
     *@date: 2020/4/9
     */
    public Map<String,Object> selectStudentNotSubmitTasks(Long sid){
        MessageMap map = new MessageMap(getClass());
        List<ExTask> tasks =exTaskMapper.selectTaskOfStudentNotSubmitByPrimaryKey(sid);
        map.put(MessageMap.OBJECT,tasks);
        return map;
    }

    /*
     *@Description: 查询学生已提交的作业
     *@param sid学生id
     *@return: map
     *@Author: qwer
     *@date: 2020/4/9
     */
    public Map<String,Object> selectStudentSubmitTask(Long sid){
        MessageMap map = new MessageMap(getClass());
        List<ExSubmitTask> submitTasks =exSubmitTaskMapper.selectSubmitTasksOfStudentByStudentPrimaryKey(sid);
        map.put(MessageMap.OBJECT,submitTasks);
        return map;
    }
}