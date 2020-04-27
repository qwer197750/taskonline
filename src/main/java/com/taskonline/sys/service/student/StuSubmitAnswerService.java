package com.taskonline.sys.service.student;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.SubmitTasks;
import com.taskonline.sys.entity.expand.ExAnswer;
import com.taskonline.sys.mapper.SubmitAnswersMapper;
import com.taskonline.sys.mapper.SubmitTasksMapper;
import com.taskonline.sys.mapper.expand.ExSubmitAnswerMapper;
import com.taskonline.sys.mapper.expand.ExSubmitTaskMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: service：服务于学生。用于学生提交的答案等
 * @author: qwer
 * @create: 2020-03-29 15:58
 */
@Service
public class StuSubmitAnswerService {
    @Resource
    ExSubmitAnswerMapper exSubmitAnswerMapper;
    @Resource
    SubmitTasksMapper submitTasksMapper;
    @Resource
    SubmitAnswersMapper submitAnswersMapper;
    @Resource
    ExSubmitTaskMapper exSubmitTaskMapper;
    @Resource
    InfoString infoString;

    /*
     *@Description:查询学生提交的答案
     *@param subTaskId
     *@return: java.util.Map<java.lang.String,java.lang.Object> 失败：<msg,错误信息>成功：<object,List<ExAnswer>>
     *@Author: qwer
     *@date: 2020/3/29
     */
    public Map<String, Object> selectSubmitAnswer(Long subTaskId){
        Map<String, Object> map = new MessageMap(getClass());
        if(subTaskId == null){
            map.put(MessageMap.MSG, infoString.NULL_SUBMIT_TASK_ID);
            return map;
        }
        List<ExAnswer> listAnswer=exSubmitAnswerMapper.selectSubmitAnswerBySubmitTaskID(subTaskId);
        map.put(MessageMap.OBJECT, listAnswer);
        return map;
    }

    /*
     *@Description: 提交作业，使用事务，防止提交作业失败回滚
     *@param sid：学生id,taskId:作业id,answers：答案列表
     *@return: map，成功key:object value:submitTask。失败key:msg value:错误信息
     *@Author: qwer
     *@date: 2020/4/9
     */
    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> submitTask(Long sid, Long taskId, List<SubmitAnswers> answers)throws SQLException{
        MessageMap map = new MessageMap(getClass());
        SubmitTasks submitTask = exSubmitTaskMapper.selectSubmitTaskForCheck(taskId, sid);
        boolean tag=true;
        for(SubmitAnswers answer: answers){
            if(answer.getScore() == null){
                tag=false;
                break;
            }
        }
        try{
            if(submitTask == null){ //如果查询已经提交的作业为空，那么新建一个提交作业
                submitTask = new SubmitTasks();
                submitTask.setTaskId(taskId);
                submitTask.setSid(sid);
                submitTask.setSubmitTime(new Date());
                submitTasksMapper.insert(submitTask);
            }else  //如果存在，那么删除之前提交的答案
                exSubmitAnswerMapper.deleteSubmitAnswersOfSubmitTaskBySubmitTask(submitTask.getSubTaskId());
            //如果所有的题目都被自动批改了（等价于所有答案的分数都不为空），那么设为已被老师批改
            submitTask.setIsTeaMark(tag);
            if(tag){ //如果都是被自动批改的题目，那么可以计算出总分
                int count = 0;
                for(SubmitAnswers answer:answers){
                    count+=answer.getScore();
                }
                submitTask.setScore(count);
            }
            Long submitTaskId = submitTask.getSubTaskId();
            submitTasksMapper.updateByPrimaryKeySelective(submitTask);
            for(SubmitAnswers answer:answers){
                answer.setSubTaskId(submitTaskId);
                submitAnswersMapper.insertSelective(answer);
            }
        }catch (Exception se){
            se.printStackTrace();
            throw se;
        }
        map.put(MessageMap.OBJECT, submitTask);
        return map;
    }
}