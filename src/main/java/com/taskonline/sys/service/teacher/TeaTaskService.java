package com.taskonline.sys.service.teacher;

import com.taskonline.sys.entity.*;
import com.taskonline.sys.entity.expand.ExAnswer;
import com.taskonline.sys.entity.expand.ExQuestion;
import com.taskonline.sys.entity.expand.ExSubmitTask;
import com.taskonline.sys.entity.expand.ExTask;
import com.taskonline.sys.mapper.*;
import com.taskonline.sys.mapper.expand.ExSubmitAnswerMapper;
import com.taskonline.sys.mapper.expand.ExSubmitTaskMapper;
import com.taskonline.sys.mapper.expand.ExTaskMapper;
import com.taskonline.sys.mapper.expand.ExTaskQuestionMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @program: taskonline
 * @description: service；服务于教师，关于作业信息
 * @author: qwer
 * @create: 2020-03-29 18:26
 */
@Service
public class TeaTaskService {
    @Resource
    ExTaskMapper exTaskMapper;

    @Resource
    TaskMapper taskMapper;

    @Resource
    QuestionMapper questionMapper;

    @Resource
    ChoiceMapper choiceMapper;
    @Resource
    MultipleChoiceMapper multipleChoiceMapper;

    @Resource
    InfoString infoString;

    @Resource
    ExTaskQuestionMapper exTaskQuestionMapper;

    @Resource
    SubmitTasksMapper submitTasksMapper;

    @Resource
    ExSubmitAnswerMapper exSubmitAnswerMapper;

    @Resource
    ExSubmitTaskMapper exSubmitTaskMapper;

    @Resource
    SubmitAnswersMapper submitAnswersMapper;

    /*
     *@Description: 插入一个作业，插入作业表，插入问题表n条数据，插入choice、multipleChoice 若干条数据
     *@param cid：班级id，task：作业数据，questions：问题列表数据
     *@return: map，成功key:object value：task,失败key：msg
     *@Author: qwer
     *@date: 2020/4/9
     */
    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> createTaskAndQuestions(Long taskId,Long cid, Task task, List<ExQuestion> questions) throws SQLException{
        MessageMap map = new MessageMap(getClass());
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        task.setCid(cid);
        //在mapping文件中的insert中加入<selectKey keyProperty="id" order="AFTER" resultType="java.lang.Integer"> SELECT LAST_INSERT_ID()</selectKey>
        //这样就能返回自增的主键，以免难以得到插入数据的主键
        try{
            if(taskId!=null){ //如果是修改作业
                exTaskMapper.deleteTaskQuestions(taskId); //先删除作业下的问题
                task.setTaskId(taskId);
                taskMapper.updateByPrimaryKeySelective(task); //选择性更新作业总分
            }else{
                taskMapper.insert(task); //选择性插入一个作业
                taskId = task.getTaskId();   //获取插入的作业的id
            }
            int scoreCount = 0;
            for (ExQuestion q:questions) {
                q.setTaskId(taskId);
                questionMapper.insert((Question)q);
                if(q.getType().equals(q.CHOICE)){
                    Choice choice=new Choice();
                    choice.setQid(q.getQid());
                    choice.setChoiceA(q.getChoiceA());
                    choice.setChoiceB(q.getChoiceB());
                    choice.setChoiceC(q.getChoiceC());
                    choice.setChoiceD(q.getChoiceD());
                    choiceMapper.insertSelective(choice);
                }else if(q.getType().equals(q.MULTIPLE_CHOICE)){
                    MultipleChoice mc = new MultipleChoice();
                    mc.setQid(q.getQid());
                    mc.setChoiceA(q.getMChoiceA());
                    mc.setChoiceB(q.getMChoiceB());
                    mc.setChoiceC(q.getMChoiceC());
                    mc.setChoiceD(q.getMChoiceD());
                    multipleChoiceMapper.insertSelective(mc);
                }
            }
        }catch (Exception se){
            se.printStackTrace();
            throw se;
        }
        map.put(MessageMap.OBJECT, task);
        return map;
    }

    /*
     *@Description: 查询一个作业的详细数据，包含问题答案（教师查询的含有答案，学生查询的没有答案）
     *@param taskId作业id
     *@return: map:成功key:object,失败key:msg
     *@Author: qwer
     *@date: 2020/4/9
     */
    public Map<String,Object> selectTaskDetail(Long taskId){
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
        ExTask task = new ExTask();
        BeanUtils.copyProperties(t,task);
        List<ExQuestion> questions=exTaskQuestionMapper.selectTaskQuestionByTaskID(taskId);
        task.setQuestions(questions);
        map.put(MessageMap.OBJECT, task);
        return map;
    }

    /*
     *@Description: 查询教师下所有课程的提交的作业
     *@param tid，教师id
     *@return: map，失败key：msg value:错误信息。成功key：object，value：List<ExSubmitTask>对象
     *@Author: qwer
     *@date: 2020/4/11
     */
    public Map<String,Object> selectSubmitTaskOfCoursesOfTeacher(Long tid){
        MessageMap map = new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExSubmitTask> submitTasks = exSubmitTaskMapper.selectSubmitTasksOfCoursesOfTeacherByTeacherPrimaryKey(tid);
        map.put(MessageMap.OBJECT, submitTasks);
        return map;
    }

    /*
     *@Description: 查询教师下提交的作业，并批改了
     *@param tid
     *@return: map
     *@Author: qwer
     *@date: 2020/4/11
     */
    public Map<String,Object> selectSubmitTaskOfCoursesOfTeacherOfPass(Long tid){
        MessageMap map = new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExSubmitTask> submitTasks = exSubmitTaskMapper.selectSubmitTasksOfCoursesOfTeacherByTeacherPrimaryKey(tid);
        submitTasks.removeIf(new Predicate<ExSubmitTask>() {
            @Override
            public boolean test(ExSubmitTask exSubmitTask) {
                //筛选教师批改了的提交的作业。返回true表示要被remove，false表示不被remove
                //如果isTeaMark == null 表示没有被批改，返回true被remove
                //如果isTeaMark==true,返回false不被筛选
                //如果isTeaMark==false,返回true被remove
                if(exSubmitTask.getIsTeaMark() == null)return true;
                return !exSubmitTask.getIsTeaMark();
            }
        });
        map.put(MessageMap.OBJECT, submitTasks);
        return map;
    }

    /*
     *@Description: 查询教师待批改的作业
     *@param tid
     *@return: map
     *@Author: qwer
     *@date: 2020/4/11
     */
    public Map<String,Object> selectSubmitTaskOfCoursesOfTeacherNotPass(Long tid){
        MessageMap map = new MessageMap(getClass());
        if(tid == null){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        List<ExSubmitTask> submitTasks = exSubmitTaskMapper.selectSubmitTasksOfCoursesOfTeacherByTeacherPrimaryKey(tid);
        //筛选出带批改的作业
        submitTasks.removeIf(new Predicate<ExSubmitTask>() {
            @Override
            public boolean test(ExSubmitTask exSubmitTask) {
                if(exSubmitTask.getIsTeaMark() == null)return false;
                return exSubmitTask.getIsTeaMark();
            }
        });
        map.put(MessageMap.OBJECT, submitTasks);
        return map;
    }

    /*
     *@Description: 为教师查询一个学生提交作业的信息，便于教师批改
     *@param submitTaskId 提交作业的id
     *@return: map
     *@Author: qwer
     *@date: 2020/4/15
     */
    public Map<String,Object> selectSubmitTaskDetailForTeaBySubTaskId(Long submitTaskId){
        MessageMap map = new MessageMap(getClass());
        ExSubmitTask submitTask = exSubmitTaskMapper.selectExSubmitTaskBySubmitTaskId(submitTaskId);
        List<ExAnswer> answers=exSubmitAnswerMapper.selectSubmitAnswerBySubmitTaskID(submitTaskId);
        submitTask.setAnswers(answers);
        map.put(MessageMap.OBJECT, submitTask);
        return map;
    }

    /*
     *@Description: 教师批改作业
     *@param submitTaskId：提交作业id，param：提交的参数map(包含题目的得分-仅部分未被自动批改的题目，总分)
     *@return:
     *@Author: qwer
     *@date: 2020/4/16
     */
    @Transactional(rollbackFor = SQLException.class)
    public Map<String,Object> checkSubmitTask(Long submitTaskId,Map<String,String> param) throws SQLException {
        MessageMap map = new MessageMap(getClass());
        SubmitTasks submitTask = submitTasksMapper.selectByPrimaryKey(submitTaskId);
        if(submitTask == null){
            map.put(MessageMap.MSG, "该学生作不存在");
            return map;
        }
        String sc = param.get("scoreCount");
        if(sc == null){
            map.put(MessageMap.MSG,"请确定学生作业得分");
            return map;
        }
        param.remove("scoreCount");
        for(String key:param.keySet()){
            SubmitAnswers answer = new SubmitAnswers();
            String s[] = key.split("_");
            if(s.length<2)continue;
            Long qid ;
            try{
                qid=Long.parseLong(s[s.length-1]);
            }catch (NumberFormatException nfe){
                continue;
            }
            int score;
            try{
                score=Integer.parseInt(param.get(key));
            }catch (NumberFormatException nfe){
                continue;
            }
            answer.setSubTaskId(submitTaskId);
            answer.setQid(qid);
            answer.setScore(score);
            submitAnswersMapper.updateByPrimaryKeySelective(answer);
        }
        List<SubmitAnswers> answers = exSubmitAnswerMapper.selectSubmitAnswersOfSubmitTaskBySubmitTaskId(submitTaskId);
        int scoreCount = 0;
        for(SubmitAnswers answer: answers){
            System.out.println(answer);
            if(answer.getScore() == null){
                throw new SQLException("存在未批改的题目");
            }
            scoreCount+=answer.getScore();
        }
        submitTask.setIsTeaMark(true);
        submitTask.setScore(scoreCount);
        submitTasksMapper.updateByPrimaryKeySelective(submitTask);
        map.put(MessageMap.OBJECT,submitTask);
        return map;
    }
}