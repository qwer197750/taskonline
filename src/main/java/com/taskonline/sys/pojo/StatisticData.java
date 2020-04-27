package com.taskonline.sys.pojo;

import lombok.Data;
import lombok.ToString;

/**
 * @program: taskonline
 * @description: 用于成绩数据统计的对象，每个对象代表一个成绩数据集
 * @author: qwer
 * @create: 2020-04-17 15:18
 */
@Data
@ToString
public class StatisticData {
    Double xNumber;
    Double yNumber;

    String xName;
    String yName;

    int seq;
}