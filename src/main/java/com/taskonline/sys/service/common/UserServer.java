package com.taskonline.sys.service.common;

import com.taskonline.sys.entity.Administrator;
import com.taskonline.sys.entity.Student;
import com.taskonline.sys.entity.Teacher;
import com.taskonline.sys.mapper.expand.ExAdministratorMapper;
import com.taskonline.sys.mapper.expand.ExStudentMapper;
import com.taskonline.sys.mapper.expand.ExTeacherMapper;
import com.taskonline.util.MessageMap;
import com.taskonline.util.InfoString;
import com.taskonline.util.PasswordChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class UserServer {
    @Autowired
    private ExAdministratorMapper administratorMapper;

    @Autowired
    private ExStudentMapper studentMapper;

    @Autowired
    private ExTeacherMapper teacherMapper;

    @Autowired
    InfoString infoString;

    @Autowired
    PasswordChange passwordChange;


    public Map<String, Object> administratorLogin(String aname, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(aname)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        Administrator administrator = administratorMapper.selectByNamePassword(aname, passwordChange.enPassword(password));
        if(administrator == null){
            map.put(MessageMap.MSG, infoString.ERROR_NAME_OR_PASSWORD);
            return map;
        }
        map.put(MessageMap.USER, administrator);
        return map;
    }

    public Map<String, Object> teacherLogin(String teacherID, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(teacherID)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        Teacher teacher = teacherMapper.selectByTeacherIDPassword(teacherID, passwordChange.enPassword(password));
        if(teacher == null){
            map.put(MessageMap.MSG, infoString.ERROR_NAME_OR_PASSWORD);
            return map;
        }
        map.put(MessageMap.USER, teacher);
        teacher.setLastLoginTime(new Date());
        teacherMapper.updateByPrimaryKeySelective(teacher);
        return map;
    }

    public Map<String, Object> studentLogin(String studentID, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(studentID)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        Student student = studentMapper.selectByStudentIDPassword(studentID, passwordChange.enPassword(password));
        if(student == null){
            map.put(MessageMap.MSG, infoString.ERROR_NAME_OR_PASSWORD);
            return map;
        }
        student.setLastLoginTime(new Date());
        studentMapper.updateByPrimaryKeySelective(student);
        map.put(MessageMap.USER, student);
        return map;
    }
    /****************************************以上是登录******************************************/
    /*--------------------------------------------------------------------------------------------*/
    /****************************************以下是注册*********************************************/
    public Map<String, Object> administratorRegister(String adminID, String aname, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(aname)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(adminID)) {
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_ID);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        long id;
        try{
            id=Long.parseLong(adminID);
        }catch (NumberFormatException ne){
            map.put(MessageMap.MSG, infoString.ILLEGAL_STRING_FOR_NUMBER);
            return map;
        }
        Administrator administrator = administratorMapper.selectByPrimaryKey(id);
        if(administrator != null){
            map.put(MessageMap.MSG, infoString.ADMINISTRATOR_ID_USED);
            return map;
        }
        administrator = new Administrator();
        administrator.setAname(aname);
        administrator.setPassword(passwordChange.enPassword(password));
        administrator.setRegisterTime(new Date());
        int i = administratorMapper.insert(administrator);
        if(i<=0){
            map.put(MessageMap.MSG, infoString.ADD_TEACHER_FAIL);
            return map;
        }
        map.put(MessageMap.USER, administrator);
        return map;
    }

    public Map<String, Object> teacherRegister(String teacherID, String tname, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(tname)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(teacherID)) {
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_ID);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        Teacher teacher = teacherMapper.selectByTeacherID(teacherID);
        if(teacher != null){
            map.put(MessageMap.MSG, infoString.TEACHER_ID_USED);
            return map;
        }
        teacher = new Teacher();
        teacher.setTname(tname);
        teacher.setPassword(passwordChange.enPassword(password));
        teacher.setRegisterTime(new Date());
        teacher.setStatus("active");
        teacher.setTeacherId(teacherID);
        teacher.setIcon("tea.jpg");
        int i = teacherMapper.insert(teacher);
        if(i<=0){
            map.put(MessageMap.MSG, infoString.ADD_TEACHER_FAIL);
            return map;
        }
        map.put(MessageMap.USER, teacher);
        return map;
    }

    public Map<String, Object> studentRegister(String studentID, String sname, String password){
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isEmpty(sname)) {
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        if (StringUtils.isEmpty(studentID)) {
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_ID);
            return map;
        }
        if (StringUtils.isEmpty(password)) {
            map.put(MessageMap.MSG, infoString.NULL_PASSWORD);
            return map;
        }
        Student student = studentMapper.selectByStudentID(studentID);
        if(student != null){
            map.put(MessageMap.MSG, infoString.STUDENT_ID_USED);
            return map;
        }
        student = new Student();
        student.setSname(sname);
        student.setPassword(passwordChange.enPassword(password));
        student.setRegisterTime(new Date());
        student.setStatus("active");
        student.setStudentId(studentID);
        student.setIcon("stu.jpg");
        int i = studentMapper.insert(student);
        if(i<=0){
            map.put(MessageMap.MSG, infoString.ADD_STUDENT_FAIL);
            return map;
        }
        map.put(MessageMap.USER, student);
        return map;
    }
    /****************************************以上是注册******************************************/
    /*--------------------------------------------------------------------------------------------*/
    /****************************************以下是信息修改*********************************************/
    public Map<String,Object> stuInfoChange(Student student){
        Map<String,Object> map= new MessageMap(getClass());
        if(StringUtils.isEmpty(student.getSname())){
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        int rows=studentMapper.updateByPrimaryKeySelective(student);
        if(rows<1){
            map.put(MessageMap.MSG, infoString.UPDATE_USER_FAIL);
            return map;
        }
        Student newStu = studentMapper.selectByPrimaryKey(student.getSid());
        map.put(MessageMap.USER, newStu);
        return map;
    }

    public Map<String,Object> teaInfoChange(Teacher teacher){
        Map<String,Object> map= new MessageMap(getClass());
        if(StringUtils.isEmpty(teacher.getTid())){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_PRIMARY_KEY);
            return map;
        }
        if (StringUtils.isEmpty(teacher.getTeacherId())){
            map.put(MessageMap.MSG, infoString.NULL_TEACHER_ID);
            return map;
        }
        int rows=teacherMapper.updateByPrimaryKeySelective(teacher);
        if(rows<1){
            map.put(MessageMap.MSG, infoString.UPDATE_USER_FAIL);
            return map;
        }
        Teacher newTea = teacherMapper.selectByPrimaryKey(Long.valueOf(teacher.getTid()));
        map.put(MessageMap.USER, newTea);
        return map;
    }

    public Map<String,Object> adminInfoChange(Administrator administrator){
        Map<String,Object> map= new MessageMap(getClass());
        if(StringUtils.isEmpty(administrator.getAid())){
            map.put(MessageMap.MSG, infoString.NULL_USER_NAME);
            return map;
        }
        int rows=administratorMapper.updateByPrimaryKeySelective(administrator);
        if(rows<1){
            map.put(MessageMap.MSG, infoString.UPDATE_USER_FAIL);
            return map;
        }
        Administrator newAdmin = administratorMapper.selectByPrimaryKey(administrator.getAid());
        map.put(MessageMap.USER, newAdmin);
        return map;
    }
}
