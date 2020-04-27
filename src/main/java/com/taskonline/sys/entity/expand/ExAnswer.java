package com.taskonline.sys.entity.expand;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @program: taskonline
 * @description: 提交的答案扩展。扩展了关于答案的问题部分。
 * @description: 原本应该继承SubmitAnswers类，但是由于该类的成员不多，而另外需要的关于问题的成员较多所以继承ExQuestion。这里的继承只是简单的为了继承父类成员。
 * @description: 相关的Getter和Setter有@Data注解生成
 * @author: qwer
 * @create: 2020-03-29 09:46
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class ExAnswer extends ExQuestion {

    //表示学生提交的答案的分数，针对单个答案
    private Integer submitScore;

    //表示学生提交的答案
    private String submitAnswer;
}