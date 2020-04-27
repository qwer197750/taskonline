package com.taskonline;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taskonline.sys.mapper.expand.*;

/**
 * @program: taskonline
 * @description: 测试Mapper
 * @author: qwer
 * @create: 2020-03-29 11:01
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TaskonlineApplication.class)
public class TestMapper {
    @Autowired
    ExTeacherMapper exTeacherMapper;
    @Autowired
    ExStudentMapper exStudentMapper;
    @Autowired
    ExAdministratorMapper exAdministratorMapper;
    @Autowired
    ExCourseMapper exCourseMapper;
    @Autowired
    ExTaskMapper exTaskMapper;
    @Autowired
    ExSubmitAnswerMapper exSubmitAnswerMapper;
    @Autowired
    ExSubmitTaskMapper exSubmitTaskMapper;
    @Autowired
    ExTaskQuestionMapper exTaskQuestionMapper;

    @Test
    public void testMapper(){
        System.out.println("开始");
        System.out.println(exAdministratorMapper.selectByNamePassword("zhangsan", "dasdas"));
        System.out.println();

        System.out.println(exCourseMapper.selectCoursesOfTeacherByTeacherPrimaryKey(1));
        System.out.println(exCourseMapper.selectStudentCoursesByStudentPrimaryKey(1));
        System.out.println();

        System.out.println(exStudentMapper.selectByStudentID("1"));
        System.out.println(exStudentMapper.selectByStudentIDPassword("155646", "dasdas"));
        System.out.println(exStudentMapper.selectStudentOfNotSubmitTaskByCidAndTaskID(1, 1));
        System.out.println(exStudentMapper.selectStudentsOfCourseByCourseID(1));
        System.out.println();

        System.out.println(exSubmitAnswerMapper.selectSubmitAnswerBySubmitTaskID(1L));
        System.out.println();

        System.out.println(exSubmitTaskMapper.selectStudentsOfSubmitTaskByTaskID(1));
        System.out.println(exSubmitTaskMapper.selectSubmitTasksOfStudentByStudentPrimaryKey(1));
        System.out.println();

        System.out.println(exTaskMapper.selectStudentTasksByStudentPrimaryKey(1));
        System.out.println(exTaskMapper.selectTaskOfStudentNotSubmitByPrimaryKey(1));
        System.out.println(exTaskMapper.selectTeacherTaskByTeacherPrimaryKey(1));
        System.out.println();

        System.out.println(exTaskQuestionMapper.selectTaskQuestionByTaskID(1));
        System.out.println();

        System.out.println(exTeacherMapper.selectByTeacherID("4546"));
        System.out.println(exTeacherMapper.selectByTeacherIDPassword("zhangsan", "ldas"));

        System.out.println("结束");
    }
}