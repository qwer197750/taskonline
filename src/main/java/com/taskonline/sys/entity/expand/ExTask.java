package com.taskonline.sys.entity.expand;

import java.lang.Character;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.Task;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @program: taskonline
 * @description: 用于表示学生查询的作业信息，扩展了班级字段,扩展了作业提交人数
 * @author: qwer
 * @create: 2020-03-27 19:02
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ExTask extends Task {
    private Long cid;
    private String cname;
    private Long submitCount;
    private List<ExQuestion> questions;


    /*
     *@Description: 验证提交的答案
     *@param ignoreLockAnswer:是否忽略缺少答案，answers: 答案列表,默认参数：此对象应该是查询数据库后的结果对象，根据set进来的questions和参数answers进行检验
     *@return: String："" or null show that answers is ok, not null show that something is wrong
     *@Author: qwer
     *@date: 2020/4/10
     */
    public String AnswerValidator(boolean ignoreLockAnswer, List<SubmitAnswers> answers){
        //先对两个列表进行排序，然后对比是否提交的答案多了还是少了
        answers.sort(new Comparator<SubmitAnswers>() {
            @Override
            public int compare(SubmitAnswers a1, SubmitAnswers a2) {
                return Long.compare(a1.getQid(), a2.getQid());
            }
        });
        questions.sort(new Comparator<ExQuestion>() {
            @Override
            public int compare(ExQuestion o1, ExQuestion o2) {
                return Long.compare(o1.getQid(),o2.getQid());
            }
        });
        if(answers.size()>questions.size())return "输入的答案数量超过了题目数量";
        //初始化检查答案的忽略字符集（忽略空格换行符等和逗号句号等再比较）
        String ignoreChars = ",.;:'\"`?，。？；：’‘”“\r\t\n\b "; //比较答案时忽略的字符
        char[] cs = ignoreChars.toCharArray();
        ArrayList<Character> ignoreCharList = new ArrayList<>();
        for(char c:cs)ignoreCharList.add(c);
        for(int i=0;i<questions.size();i++){
            ExQuestion question = questions.get(i);
            Long aQid = questions.get(i).getQid();
            if(i>=answers.size()){
                if(ignoreLockAnswer)
                    continue;
                else return "请输入第"+question.getSeqNum()+"题的答案";
            }
            Long sQid = answers.get(i).getQid();
            if((!aQid.equals(sQid)) && (!ignoreLockAnswer))return "请输入第"+question.getSeqNum()+"题的答案";
            Boolean b = question.checkAnswer(answers.get(i).getAnswer(), getIsShortAutoCheckAnswer() ,ignoreCharList);
            if(b == null){
                continue;
            }
            if(b)answers.get(i).setScore(question.getScore());
            else answers.get(i).setScore(0);
        }
        return null;
    }




}
