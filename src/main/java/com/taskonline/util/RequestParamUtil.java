package com.taskonline.util;

import com.taskonline.sys.entity.SubmitAnswers;
import com.taskonline.sys.entity.Task;
import com.taskonline.sys.entity.expand.ExAnswer;
import com.taskonline.sys.entity.expand.ExQuestion;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: taskonline
 * @description: 工具类，用于从request中获取传递的参数。返回map
 * @author: qwer
 * @create: 2020-04-07 20:39
 */
@Component
public class RequestParamUtil {

    /*
     *@Description: 从request获取参数列表放到map中
     *@param request
     *@return:
     *@Author: qwer
     *@date: 2020/4/8
     */
    public Map<String,String> getParaFromRequest(HttpServletRequest request){
        Map<String,String> param=new HashMap<>();
        for (Object o : request.getParameterMap().keySet()) {
            String key = (String) o;
            String[] values = request.getParameterValues(key);
            String value = "";
            for (String v : values) {
                value+=v;
            }
            param.put(key, value);
        }
        return param;
    }

    /*
     *@Description: 为创建作业整理map
     *@param para map
     *@return: 适合创建作业的map。成功含有key:task value:Task对不含cid）,key：questions value:List<ExQuestion> 失败：含有key:msg value：失败信息
     *@Author: qwer
     *@date: 2020/4/8
     */
    public Map<String,Object> arrangeParamForCreateTask(Map<String,String> param){
        MessageMap map = new MessageMap(getClass());
        map.ignoreKeyWarn=true; //忽略意外的key，MessageMap对于不同的key进行不同的日志处理，如果发现不在范围内的key会生成警告日志
        if(StringUtils.isEmpty(param.get("tname"))){
            map.put(MessageMap.MSG, "请输入作业名称");
            return map;
        }
        if(StringUtils.isEmpty(param.get("endTime"))){
            map.put(MessageMap.MSG, "请输入作业结束时间");
            return map;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date endTime = new Date();
        try{
            endTime = sdf.parse(param.get("endTime"));
        }catch (ParseException pe){
            map.put(MessageMap.MSG, "请输入正确格式的结束时间");
            return map;
        }
        if(endTime.before(new Date())){
            map.put(MessageMap.MSG, "结束时间不应该早于当前时间");
            return map;
        }
        boolean isShortAutoCheckAnswer =true;
        if (StringUtils.isEmpty(param.get("isShortAutoCheckAnswer"))){
            isShortAutoCheckAnswer=false;
        }
        boolean isShowAnswer=true;
        if(StringUtils.isEmpty(param.get("isShowAnswer"))){
            isShowAnswer=false;
        }
        int choiceScore,multipleChoiceScore,shortScore, longScore;
        if(StringUtils.isEmpty(param.get("choiceScore"))
                || StringUtils.isEmpty(param.get("multipleChoiceScore"))
                || StringUtils.isEmpty(param.get("shortScore"))
                || StringUtils.isEmpty(param.get("longScore"))){
            map.put(MessageMap.MSG,"请输入题目分值");
            return map;
        }
        try{
            choiceScore = Integer.parseInt(param.get("choiceScore"));
            multipleChoiceScore = Integer.parseInt(param.get("multipleChoiceScore"));
            shortScore = Integer.parseInt(param.get("shortScore"));
            longScore = Integer.parseInt(param.get("longScore"));
        }catch (NumberFormatException nfe){
            map.put(MessageMap.MSG, "请输入正整数类型的分值");
            return map;
        }
        if(choiceScore<=0 || multipleChoiceScore <=0 || shortScore <=0 || longScore <=0){
            map.put(MessageMap.MSG, "请输入大于0的分值");
            return map;
        }
        Task task = new Task();
        task.setTaskName(param.get("tname"));
        task.setCreateTime(new Date());
//        task.setCid(cid); 此处不是控制器，交由控制器setCid
        task.setEndTime(endTime);
        task.setIsShortAutoCheckAnswer(isShortAutoCheckAnswer);
        task.setIsShowAnswer(isShowAnswer);
        map.put("task", task);
        ArrayList<ExQuestion> questions = new ArrayList<>();
        Set<String> keySet = param.keySet();
        //遍历key,对于单选题、多选题、简答题、大题的key进行生成相应的问题，然后加入arrayList
        int scoreCount = 0;
        for (String key : keySet) {
            String[] s = key.split("_");
            if(s.length<2)continue;
            int seq;
            try{
                seq = Integer.parseInt(s[s.length-1]);
            }catch (NumberFormatException nfe){
                continue;
            }
            String questionType = s[0];
            //以上获取name中的前缀和后缀序号
            ExQuestion exQuestion = new ExQuestion();
            exQuestion.setSeqNum(seq);
            String content;
            String answer;
            boolean isQuestionContent=true;//是否匹配到问题的内容，如果是那么将exQuestion加入list
            switch (questionType){
                case "choiceContent":
                    content = param.get(key); //获取问题主体
                    answer = param.get("choiceAnswer_"+seq);
                    String choiceA = param.get("choiceAnswerAContent_"+seq);
                    String choiceB = param.get("choiceAnswerBContent_"+seq);
                    String choiceC = param.get("choiceAnswerCContent_"+seq);
                    String choiceD = param.get("choiceAnswerDContent_"+seq);
                    //将获取的数据填入相应的对象；数据校验由ExQuestion自带的validator验证
                    exQuestion.setContent(content);
                    exQuestion.setAnswer(answer);
                    exQuestion.setChoiceA(choiceA);
                    exQuestion.setChoiceB(choiceB);
                    exQuestion.setChoiceC(choiceC);
                    exQuestion.setChoiceD(choiceD);
                    exQuestion.setType(exQuestion.CHOICE);
                    exQuestion.setScore(choiceScore);
                    scoreCount+=choiceScore;
                    break;
                case "multipleChoiceContent":
                    content = param.get(key); //获取问题主体
                    answer = param.get("multipleChoiceAnswer_"+seq);
                    String mChoiceA = param.get("multipleChoiceAnswerAContent_"+seq);
                    String mChoiceB = param.get("multipleChoiceAnswerBContent_"+seq);
                    String mChoiceC = param.get("multipleChoiceAnswerCContent_"+seq);
                    String mChoiceD = param.get("multipleChoiceAnswerDContent_"+seq);
                    //将获取的数据填入相应的对象；数据校验由ExQuestion自带的validator验证
                    exQuestion.setContent(content);
                    exQuestion.setAnswer(answer);
                    exQuestion.setMChoiceA(mChoiceA);
                    exQuestion.setMChoiceB(mChoiceB);
                    exQuestion.setMChoiceC(mChoiceC);
                    exQuestion.setMChoiceD(mChoiceD);
                    exQuestion.setType(exQuestion.MULTIPLE_CHOICE);
                    exQuestion.setScore(multipleChoiceScore);
                    scoreCount+=multipleChoiceScore;
                    break;
                case "shortQuestionContent":
                    content = param.get(key); //获取问题主体
                    answer = param.get("shortQuestionAnswer_"+seq);
                    //将获取的数据填入相应的对象；数据校验由ExQuestion自带的validator验证
                    exQuestion.setContent(content);
                    exQuestion.setAnswer(answer);
                    exQuestion.setType(exQuestion.SHORT);
                    exQuestion.setScore(shortScore);
                    scoreCount+=shortScore;
                    break;
                case "longQuestionContent":
                    content = param.get(key); //获取问题主体
                    answer = param.get("longQuestionAnswer_"+seq);
                    //将获取的数据填入相应的对象；数据校验由ExQuestion自带的validator验证
                    exQuestion.setContent(content);
                    exQuestion.setAnswer(answer);
                    exQuestion.setType(exQuestion.LONG);
                    exQuestion.setScore(longScore);
                    scoreCount+=longScore;
                    break;
                default:
                    isQuestionContent=false;
            }
            if(isQuestionContent){
                questions.add(exQuestion);
            }
        }
        task.setScore(scoreCount);
        questions.sort(new Comparator<ExQuestion>() {
            @Override
            public int compare(ExQuestion q1, ExQuestion q2) {
                return Integer.compare(q1.getSeqNum(), q2.getSeqNum());
            }
        });
        for (ExQuestion eq:questions) {
            System.out.println(eq);
        }
        String msg ;
        for(int i=0;i<questions.size();i++){
            ExQuestion q=questions.get(i);
            q.setSeqNum(i+1);
            msg=q.validator();
            if(!StringUtils.isEmpty(msg)){
                map.put(MessageMap.MSG,msg);
                return map;
            }
        }
        map.put("questions", questions);
        return map;
    }

    /*
     *@Description: 整理提交作业的参数，整理为List<SubmitAnswer>
     *@param null
     *@return:
     *@Author: qwer
     *@date: 2020/4/9
     */
    public List<SubmitAnswers> arrangeParamForSubmitTask(Map<String,String> param){
        List<SubmitAnswers> answers = new ArrayList<>();
        for(String key: param.keySet()){
            String[] s=key.split("_");
            if(s.length<2)continue;;
            long qid ;
            try{
                qid = Long.parseLong(s[1]);
            }catch (NumberFormatException nfe){
                continue;
            }
            SubmitAnswers answer = new SubmitAnswers();
            answer.setQid(qid);
            answer.setAnswer(param.get(key));
            answers.add(answer);
        }
        return answers;
    }

}