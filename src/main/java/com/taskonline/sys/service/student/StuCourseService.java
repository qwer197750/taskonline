package com.taskonline.sys.service.student;

import com.taskonline.sys.entity.Course;
import com.taskonline.sys.entity.StuCourses;
import com.taskonline.sys.entity.StuCoursesKey;
import com.taskonline.sys.entity.expand.ExCourse;
import com.taskonline.sys.mapper.StuCoursesMapper;
import com.taskonline.sys.mapper.expand.ExCourseMapper;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: taskonline
 * @description: service：服务于学生，适用于课程数据查询
 * @author: qwer
 * @create: 2020-03-29 15:43
 */
@Service
public class StuCourseService {
    @Resource
    ExCourseMapper exCourseMapper;

    @Resource
    StuCoursesMapper stuCoursesMapper;

    @Resource
    InfoString infoString;

    /*
     *@Description:查询学生所有的选的课程
     *@param sid Long
     *@return: java.util.Map<java.lang.String,java.lang.Object>，失败：<msg,错误信息>成功：<object,List<ExCourse>>
     *@Author: qwer
     *@date: 2020/3/29
     */
    public Map<String, Object> selectStudentCourse(Long sid){
        Map<String, Object> map= new MessageMap(getClass());
        if(sid==null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_ID);
            return map;
        }
        List<ExCourse> listCourses = exCourseMapper.selectStudentCoursesByStudentPrimaryKey(sid);
        map.put(MessageMap.OBJECT, listCourses);
        return map;
    }

    /*
     *@Description: 查询学生所有未结束（有效）的课程
     *@param sid 学生表主键
     *@return: map，成功key:object,失败key；msg
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> selectStudentCourseOfNotEnd(Long sid){
        Map<String,Object> map= new MessageMap(getClass());
        if(sid == null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_PRIMARY_KEY);
            return map;
        }
        List<ExCourse> listCourse=exCourseMapper.selectStudentCoursesByStudentPrimaryKey(sid);

        //遍历，清除过期的课程，只留下未过期的课程
        for(int i=listCourse.size()-1; i>=0; i--){
            ExCourse ec = listCourse.get(i);
            Date endTime = ec.getEndTime();
            if(endTime.before(new Date())){
                listCourse.remove(i);
            }
        }
        map.put(MessageMap.OBJECT, listCourse);
        return map;
    }


    /*
     *@Description: 查询学生所有结束的课程
     *@param sid 学生表主键
     *@return: map，成功key:object,失败key；msg
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> selectStudentEndCourse(Long sid){
        Map<String,Object> map= new MessageMap(getClass());
        if(sid == null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_PRIMARY_KEY);
            return map;
        }
        List<ExCourse> listCourse=exCourseMapper.selectStudentCoursesByStudentPrimaryKey(sid);

        //遍历，清除未过期的课程，只留下过期的课程
        for(int i=listCourse.size()-1; i>=0; i--){
            ExCourse ec = listCourse.get(i);
            Date endTime = ec.getEndTime();
            if(endTime.after(new Date())){
                listCourse.remove(i);
            }
        }
        map.put(MessageMap.OBJECT, listCourse);
        return map;
    }

    /*
     *@Description: 按关键字查询课程
     *@param column字段名称：限定为cname和tname，key关键字
     *@return: map。成功key：object，失败key：msg
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> selectCourseByKey(String column, String key){
        Map<String,Object> map= new MessageMap(getClass());
        if(StringUtils.isEmpty(column)){
            map.put(MessageMap.MSG, "请选择按课程名称或教师查询");
            return map;
        }
        if(StringUtils.isEmpty(key)){
            map.put(MessageMap.MSG, "请输入查询的关键字");
            return map;
        }
        List<ExCourse> exCourseList;
        if(column.equals("cname")){
            exCourseList=exCourseMapper.selectCourseByKeyCname(key);
        }else if(column.equals("tname")){
            exCourseList=exCourseMapper.selectCourseByKeyTname(key);
        }else{
            map.put(MessageMap.MSG,"错误的查询类型");
            return map;
        }
        map.put(MessageMap.OBJECT, exCourseList);
        return map;
    }

    /*
     *@Description: 学生加入课程，默认选课字段isPass(是否通过)false
     *@param sid学生表主键、cid课程表主键
     *@return: map。成功key：object，失败key：msg
     *@Author: qwer
     *@date: 2020/4/6
     */
    public Map<String,Object> joinCourse(Long sid, Long cid){
        Map<String,Object> map= new MessageMap(getClass());
        if(sid == null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_PRIMARY_KEY);
            return map;
        }
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        StuCourses stuCourses= new StuCourses();
        stuCourses.setCid(cid);
        stuCourses.setSid(sid);
        stuCourses.setSelectTime(new Date());
        stuCourses.setIsPass(false);
        StuCoursesKey key = new StuCoursesKey();
        key.setCid(stuCourses.getCid());
        key.setSid(stuCourses.getSid());
        StuCourses existStuCourse = stuCoursesMapper.selectByPrimaryKey(key);
        if(existStuCourse!=null && existStuCourse.getSid()!=null){
            map.put(MessageMap.MSG, "请勿重复选课");
            return map;
        }
        int row = stuCoursesMapper.insertSelective(stuCourses);
        if(row<1){
            map.put(MessageMap.MSG, "选课失败");
        }
        map.put(MessageMap.OBJECT, stuCourses);
        return map;
    }
}