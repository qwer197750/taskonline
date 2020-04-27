//关于作业的js，由于函数过多，所以在这里

function getQuestionHtml(i, data) {
    let questionType = $("#questionType").val();
    console.log(questionType);
    if (null != data) {
        questionType = data.type;
    }
    if (questionType === 'choice') {
        return getChoiceQuestion(i, data);
    } else if (questionType === 'multipleChoice') {
        return getMultipleQuestion(i, data);
    } else if (questionType === 'short') {
        return getShortQuestion(i, data);
    } else if (questionType === 'long') {
        return getLongQuestionHtml(i, data);
    }
    return '';
}

//添加一个问题
function addQuestion(data) {
    questionCount++;
    $("#questions").append(getQuestionHtml(quesionId, data));
    quesionId++;
    statisticsScore();//统计总分值
}


//教师删除一个问题，更新问题的序号，使序号连续
function deleteQuestion(i) {
    $('#question_' + i).remove()
    //修改被删除问题所有的问题的序号（由于删除时不更新id只更新序号，所以存在序号<id的情况，所以按id遍历时可能会得到空对象）
    let j = 1;
    i = 0;
    while (i++ < quesionId) {
        let questionSeq = $('#questionSeq_' + i);
        if (questionSeq.length <= 0) {
            console.log(i);
            continue;
        }
        questionSeq.html(j++);
    }
    questionCount--;
    statisticsScore();//重新统计总分值
}



var scoreCount;
//学生获取作业
function getQuestions(questions) {
    scoreCount = 0;
    let html = ``;
    for (let i = 0; i < questions.length; i++) {
        scoreCount+=questions[i].score;
        html += getSubmitQuestion(questions[i]);
    }
    $('#scoreCount').html(scoreCount);
    return html;
}

//获取学生获取作业页面（如果提交过则显示提交的作业）
function getSubmitQuestion(question) {

    switch (question.type) {
        case "choice":
            return getSubmitChoiceQuestion(question);
        case "multipleChoice":
            return getSubmitMultipleChoiceQuestion(question);
        case "short":
            return getSubmitShortQuestion(question);
        case "long":
            return getSubmitLongQuestion(question);
    }
}


//获取各个问题的分值，返回一个数组
function getQuestionScore() {
    let choiceScore = $('#choiceScore').val();
    let multipleChoiceScore = $('#multipleChoiceScore').val();
    let shortScore = $('#shortScore').val();
    let longScore = $('#longScore').val();
    if(choiceScore.length < 1 || multipleChoiceScore.length<1 || shortScore.length<1 || longScore.length<1){
        xtalert.alertError("请输入题目分值");
        return null;
    }
    try {
        choiceScore = parseInt(choiceScore);
        multipleChoiceScore = parseInt(multipleChoiceScore);
        shortScore = parseInt(shortScore);
        longScore = parseInt(longScore);
    }catch (e) {
        xtalert.alertError("输入的分值不是整数");
        return null;
    }
    return [choiceScore,multipleChoiceScore,shortScore,longScore];
}

//统计所有问题的分值，每次删除、增加时调用此方法，从头到尾进行统计
function statisticsScore() {
    let j = 1;
    let i = 0;
    let scoreCount=0;
    let scores = getQuestionScore();
    if(scores == null || scores.length<4)
        return ;
    while (i++ < quesionId) {
        let questionSeq = $('#questionSeq_' + i);
        if (questionSeq.length <= 0) { //如果该id对应的问题被删除了
            continue;
        }
        switch (questionSeq.attr('type')) {
            case 'choice':
                scoreCount+= scores[0];
                break;
            case 'multipleChoice':
                scoreCount+= scores[1];
                break;
            case 'short':
                scoreCount+= scores[2];
                break;
            case 'long':
                scoreCount+= scores[3];
        }
    }
    $('#scoreCount').val(scoreCount);
}

//获取教师批改作业的问题
function getCheckSubmitTaskQuestionHtml(questions, isShortAutoCheck) {
    let html = ``;
    for(let i=0;i<questions.length;i++){
        let question = questions[i];
        switch (question.type) {
            case 'choice':
                $('#choiceScore').html(question.score);
                html+=getCheckSubmitTaskChoiceHtml(i+1,question);
                break;
            case 'multipleChoice':
                $('#multipleChoiceScore').html(question.score);
                html+=getCheckSubmitTaskMultipleChoiceHtml(i+1, question);
                break;
            case 'short':
                $('#shortScore').html(question.score);
                html+=getCheckSubmitTaskShortHtml(isShortAutoCheck, question);
                break;
            case 'long':
                $('#longScore').html(question.score);
                html+=getCheckSubmitTaskLongHtml(i+1, question);
                break;
        }
    }
    return html;
}



function setScore(seq, score) {
    if(seq != null){
        $('#answerScore_'+seq).val(score);
    }
    let scoreCount = 0;
    for(let i=1;i<=questionCount;i++){
        let questionSeq = $('#answerScore_' + i);
        if (questionSeq.length <= 0) {
            continue;
        }
        let score;
        try{
            score = questionSeq.html();
            score = parseInt(score);
            if(score>0 && score<1000){
                scoreCount+=score;
                console.log(score);
            }
            score = questionSeq.val();
            score = parseInt(score);
            if(score>0 && score<1000){
                scoreCount+=score;
                console.log(score);
            }

        }catch (e) {
            xtalert.alertError("请输入整数类型的分数");
            return ;
        }
    }
    $('#scoreNowCount').html(scoreCount);
    $('#scoreNowCountInput').val(scoreCount);
}