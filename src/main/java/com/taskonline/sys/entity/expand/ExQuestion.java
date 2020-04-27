package com.taskonline.sys.entity.expand;

import com.taskonline.sys.entity.Question;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;

/**
 * @program: taskonline
 * @description: 跨表查询结构。适用于查询一个作业下所有的问题（扩展单选、多选选项字段）
 * @author: qwer
 * @create: 2020-03-28 09:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ExQuestion extends Question {
    public final String CHOICE = "choice";
    public final String MULTIPLE_CHOICE = "multipleChoice";
    public final String SHORT = "short";
    public final String LONG = "long";

    private String choiceA;

    private String choiceB;

    private String choiceC;

    private String choiceD;

    private String mChoiceA;

    private String mChoiceB;

    private String mChoiceC;

    private String mChoiceD;

    private String mChoiceE;

    private String mChoiceF;

    private String mChoiceG;

    private String mChoiceH;

    private String mChoiceI;

    private String mChoiceJ;

    /*
     *@Description: 验证问题是否完整
     *@param
     *@return: null or "" is ok, not null is that something is null,that is error
     *@Author: qwer
     *@date: 2020/4/8
     */
    public String validator(){
        switch (getType()){
            case CHOICE:
                return validatorChoice();
            case MULTIPLE_CHOICE:
                return validatorMultipleChoice();
            case SHORT:
            case LONG:
                return validatorBaseQuestion();
            default:
                System.out.println("出乎意外的Question.type");
                return null;
        }
    }

    private String validatorBaseQuestion(){
        int seq = getSeqNum();
        if(StringUtils.isEmpty(getContent())){
            return "请输入第"+seq+"题的问题";
        }
        if (StringUtils.isEmpty(getAnswer())){
            return "请输入第"+seq+"题的答案";
        }
        return null;
    }
    private String validatorChoice(){
        String m = validatorBaseQuestion();
        if(m!=null) return m;
        int seq = getSeqNum();
        if(StringUtils.isEmpty(choiceA) || StringUtils.isEmpty(choiceB) || StringUtils.isEmpty(choiceC) ||StringUtils.isEmpty(choiceD)){
            return "请输入第"+seq+"题的选项";
        }
        return null;
    }
    private String validatorMultipleChoice(){
        String m = validatorBaseQuestion();
        if(m!=null) return m;
        int seq = getSeqNum();
        if(StringUtils.isEmpty(mChoiceA) || StringUtils.isEmpty(mChoiceB) || StringUtils.isEmpty(mChoiceC) ||StringUtils.isEmpty(mChoiceD)){
            return "请输入第"+seq+"题的选项";
        }
        return null;
    }

    public Boolean checkAnswer(String submitAnswer, Boolean isAutoCheckShort,List<Character> ignoreChars){
        if(getType().equals(CHOICE) || getType().equals(MULTIPLE_CHOICE)){
            return checkChoice(submitAnswer);
        }else if(getType().equals(SHORT)){
            if(isAutoCheckShort)
                return checkShortAnswer(submitAnswer, ignoreChars);
            else
                return null;
        }
        return null;
    }


    private boolean checkChoice(String submitAnswer){
        String answer = getAnswer();
        HashMap<Character, Integer> map = new HashMap<>();
        for(Character c:answer.trim().toLowerCase().toCharArray()){ //去收尾空格，小写化
            //使用hashMap进行校验，将答案的value设为true
            map.put(c,1);
        }
        for(Character c:submitAnswer.trim().toLowerCase().toCharArray()){ //去收尾空格，小写化
            //使用hashMap进行校验，与原来的值进行异或，如果答案对了那么为同-0，不对为1
            if(map.get(c) == null)return false;
            Integer v = map.get(c);
            v = v ^ 1;
            map.put(c,v);
        }
        for(Character key: map.keySet()){
            //如果存在1，说明存在异
            if(map.get(key) == 1){
                return false;
            }
        }
        return true;
    }

    /*
     *@Description: 检查简答题答案，忽略空格逗号等
     *@param subAnswer:提交的答案，answer：正常答案
     *@return: true:通过，false不通过
     *@Author: qwer
     *@date: 2020/4/10
     */
    private boolean checkShortAnswer(String subAnswer, List<Character> ignoreCharList){
        String answer = getAnswer();
        if(StringUtils.isEmpty(subAnswer) || StringUtils.isEmpty(answer))return false;
        int i=0,j=0;

        while(i<subAnswer.length()&&j<answer.length()){
            if(ignoreCharList.contains(subAnswer.charAt(i))){
                i++;
                continue;
            }
            if(ignoreCharList.contains(answer.charAt(j))){
                j++;
                continue;
            }
            if(subAnswer.charAt(i) != answer.charAt(j)){
                return false;
            }
            i++;
            j++;
        }
        for( ;i<subAnswer.length();i++){
            if(!ignoreCharList.contains(subAnswer.charAt(i)))
                return false;
        }
        for( ;j<answer.length();j++){
            if(!ignoreCharList.contains(answer.charAt(j)))
                return false;
        }
        return true;
    }

}