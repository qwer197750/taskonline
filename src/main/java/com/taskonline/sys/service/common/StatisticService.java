package com.taskonline.sys.service.common;

import com.taskonline.sys.mapper.expand.ExStatisticMapper;
import com.taskonline.sys.pojo.StatisticData;
import com.taskonline.util.InfoString;
import com.taskonline.util.MessageMap;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @program: taskonline
 * @description: 统计数据service
 * @author: qwer
 * @create: 2020-04-17 07:57
 */
@Service
public class StatisticService {
    @Resource
    ExStatisticMapper exStatisticMapper;

    @Resource
    InfoString infoString;


    /*
     *@Description: 查询课程下每次作业学生平均成绩起伏
     *@param map，成功key：object,value:List<StatisticData>
     *@return:
     *@Author: qwer
     *@date: 2020/4/17
     */
    public Map<String,Object> getCourseScoresAvgData(Long cid){
        MessageMap map = new MessageMap(getClass());
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        List<Map<String, Object>> rs = exStatisticMapper.selectCourseTaskAvgScore(cid);
        List<StatisticData> statisticDataList = new ArrayList<>();
        int i=0;
        for(Map<String,Object> m:rs){
            StatisticData data = new StatisticData();
            data.setSeq(i++);
            double subScore = Double.parseDouble(m.get("submit_score").toString());
            double score = Double.parseDouble(m.get("score").toString());
            data.setYNumber(100*subScore/score); //百分化
            data.setXName((String) m.get("task_name"));
            statisticDataList.add(data);
        }
        map.put(MessageMap.OBJECT,statisticDataList);
        return map;
    }


    /*
     *@Description: 查询课程下学生每个作业的成绩起伏
     *@param map，成功key：object,value:List<StatisticData>
     *@return:
     *@Author: qwer
     *@date: 2020/4/17
     */
    public Map<String,Object> getStuScoresOfCourse(Long cid, Long sid){
        MessageMap map = new MessageMap(getClass());
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        if(sid == null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_PRIMARY_KEY);
            return map;
        }
        List<Map<String, Object>> rs = exStatisticMapper.selectStuScoresOfCourse(cid, sid);
        List<StatisticData> statisticDataList = new ArrayList<>();
        int i=0;
        for(Map<String,Object> m:rs){
            StatisticData data = new StatisticData();
            data.setSeq(i++);
            double subScore = Double.parseDouble(m.get("submit_score").toString());
            double score = Double.parseDouble(m.get("score").toString());
            data.setYNumber(100*subScore/score); //百分化
            data.setXName((String) m.get("task_name"));
            statisticDataList.add(data);
        }
        map.put(MessageMap.OBJECT,statisticDataList);
        return map;
    }


    /*
     *@Description: 查询学生个人所有作业成绩起伏
     *@param map，成功key：object,value:List<StatisticData>
     *@return:
     *@Author: qwer
     *@date: 2020/4/17
     */
    public Map<String,Object> getStuScores(Long sid){
        MessageMap map = new MessageMap(getClass());
        if(sid == null){
            map.put(MessageMap.MSG, infoString.NULL_STUDENT_PRIMARY_KEY);
            return map;
        }
        List<Map<String, Object>> rs = exStatisticMapper.selectStuScores(sid);
        rs.sort(new Comparator<Map<String, Object>>() { //按cid排序，以便接下来按cid进行分类
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Long long1 = Long.parseLong(o1.get("cid").toString());
                Long long2 = Long.parseLong(o2.get("cid").toString());
                return Long.compare(long1, long2);
            }
        });
        int i=0;
        //构建一个含有多个数据集的map,key是课程，value是学生在课程内每次作业的成绩
        Map<String,List<StatisticData>> rsMap = new HashMap<>();
        //new 数据集的第一个元素
        List<StatisticData> statisticDataList = new ArrayList<>();
        List<StatisticData> all = new ArrayList<>();
        //上一个类别的cid，按cid进行分类，如果是新获取的cid等于这个，那么说明是同一个课程，放到同一个元素。如果不是，那么新建一个元素
        Long beforeCid = Long.MIN_VALUE; //设一个不可能的初值
        String beforeCname = "";
        for(Map<String,Object> m:rs){ //按cid进行分类
            System.out.println(m);
            StatisticData data = new StatisticData();
            data.setSeq(i++);
            double subScore = Double.parseDouble(m.get("submit_score").toString());
            double score = Double.parseDouble(m.get("score").toString());
            data.setYNumber(100*subScore/score); //百分化
            data.setXName((String) m.get("task_name"));
            Long nowCid = Long.parseLong(m.get("cid").toString());
            //如果当前cid等于上一次的cid，说明是同一个课程，那么归为一个List<StatisticData>
            //如果不相等，那么将老元素加入集合，new一个新元素
            if(beforeCid.equals(nowCid)){
                statisticDataList.add(data);
            }else{
                if(statisticDataList.size()>0){ //预防第一次空元素
                    rsMap.put(beforeCname,statisticDataList);
                }
                statisticDataList = new ArrayList<StatisticData>();
                statisticDataList.add(data);
            }
            beforeCid = nowCid;
            beforeCname = (String) m.get("cname");
            all.add(data);
        }
        if(statisticDataList.size()>0){ //如果最后一个元素存了东西
            rsMap.put(beforeCname,statisticDataList);
        }
        if(all.size()>0)
            rsMap.put("所有课程", all);
        map.put(MessageMap.OBJECT,rsMap);
        return map;
    }


    /*
     *@Description: 查询作业每道题的学生的平均得分
     *@param map，成功key：object,value:List<StatisticData>
     *@return:
     *@Author: qwer
     *@date: 2020/4/17
     */
    public Map<String,Object> getTaskQuestionsAVGScore(Long cid){
        MessageMap map = new MessageMap(getClass());
        if(cid == null){
            map.put(MessageMap.MSG, infoString.NULL_COURSE_ID);
            return map;
        }
        List<Map<String, Object>> rs = exStatisticMapper.selectTaskQuestionsAVGScore(cid);
        List<StatisticData> statisticDataList = new ArrayList<>();
        int i=0;
        for(Map<String,Object> m:rs){
            StatisticData data = new StatisticData();
            data.setSeq(i++);
            double subScore = Double.parseDouble(m.get("submit_score").toString());
            double score = Double.parseDouble(m.get("score").toString());
            data.setYNumber(100*subScore/score); //百分化
            data.setXName((String) m.get("seq_num").toString());
            statisticDataList.add(data);
        }
        map.put(MessageMap.OBJECT,statisticDataList);
        return map;
    }
}