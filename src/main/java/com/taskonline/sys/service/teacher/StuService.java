package com.taskonline.sys.service.teacher;

import com.taskonline.sys.entity.Student;
import com.taskonline.sys.mapper.expand.ExStudentMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: service：提供学生信息服务
 * @author: qwer
 * @create: 2020-03-29 17:40
 */
@Service
public class StuService {
    @Autowired
    ExStudentMapper exStudentMapper;

    @Autowired
    InfoString infoString;

    /*
     *@Description:教师调用查询一个课程下的所有学生信息
     *@param cid 课程id
     *@return: java.util.Map<java.lang.String,java.lang.Object>，成功：<object, List<ExStudent>> 失败：<msg,错误信息>
     *@Author: qwer
     *@date: 2020/3/29
     */
    public Map<String,Object> selectStudentsOfCourse(Long cid){
      Map<String,Object> map= new MessageMap(getClass());
      if(cid == null){
          map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
          return map;
      }
      List<Student> listStudent=exStudentMapper.selectStudentsOfCourseByCourseID(cid);
      map.put(MessageMap.OBJECT, listStudent);
      return map;
    }

    /*
     *@Description:教师查询一个作业下未提交作业的学生
     *@param taskId
     *@return: java.util.Map<java.lang.String,java.lang.Object>
     *@Author: qwer
     *@date: 2020/3/29
     */
    public Map<String,Object> selectStudentOfNotSubmitTask(Long cid,Long taskId){
        Map<String,Object> map= new MessageMap(getClass());
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID         );
            return map;
        }else if(taskId == null){
            map.put(MessageMap.MSG, infoString.NULL_TASK_ID);
            return map;
        }
        List<Student> listStudent=exStudentMapper.selectStudentOfNotSubmitTaskByCidAndTaskID(cid, taskId);
        map.put(MessageMap.OBJECT, listStudent);
        return map;
    }
}