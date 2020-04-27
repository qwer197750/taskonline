//这是负责页面逻辑的控制器js
//------------------------------------------教师控制部分---------------------------------------*/
//显示创建课程html
function showCreateCourseHtml() {
    $('#container').html(getCreateCourseHtml());
    setTimeChoose();
    bindCreateCourseForm();
}

//显示教师的所有课程
function showTeacherCourses() {
    let data = getTeacherCourse();
    console.log(data);
    $('#container').html(getTeacherCourseHtml('创建的课程',data));
}

//显示教师所有的有效课程
function showTeacherCourseOfNotEnd() {
    let data = getTeacherCourseOfNotEnd();
    console.log(data);
    $('#container').html(getTeacherCourseHtml('有效课程',data));

}

//显示教师所有结束的课程
function showTeacherEndCourse() {
    let data = getTeacherEndCourse();
    console.log(data);
    $('#container').html(getTeacherCourseHtml('已结束课程',data));
}

var courseData ;
//为教师显示一个班级的课程
function showCourseForTeacher(cid) {
    let data=getCourseDetail(cid);
    console.log(data);
    courseData = data;
    $('#container').html(getCourseHtml(cid,true, data));
    showTaskTableThreadAndTbody(true, cid, 'task');
    showCourseAvgTasksScore(cid);
}

//添加一个问题
var quesionId; //每次创建作业时重置
var questionCount;  //每次创建作业时重置
var scoreCount;  //每次创建作业时重置
//显示创建作业的html
function showCreateTask(cid) {
    quesionId=1;
    questionCount=0;
    scoreCount=0;
    $('#container').html(getCreateTaskFormHtml(cid, null));
    setTimeChoose();
    bindCreateTaskForm();
    addQuestion();
}



//显示教师下所有的提交的作业
function showSubmitTasksOfTeacher() {
    let data = getData("/tea/task/submitTasks", 'get', null);
    console.log(data);
    $('#container').html(getSubmitTaskListHtml(data));
}

//显示教师下所有已经批改的作业
function showSubmitTaskOfTeacherPass() {
    let data = getData("/tea/task/checkedSubmitTasks", 'get', null);
    console.log(data);
    $('#container').html(getSubmitTaskPassListHtml(data));
}

//显示教师下所有待批改的作业列表
function showSubmitTaskOfTeacherNotPass() {
    let data = getData('/tea/task/waitCheckSubmitTasks', 'get', null);
    console.log(data);
    $('#container').html(getSubmitTaskOfNotPassListHtml(data));
}

var scoreCountNow;
//教师 显示学生提交的作业，并进行批改
function showSubmitTaskDetail(submitTaskId) {
    scoreCountNow = 0;
    questionCount = 0;
    let data = getData("/tea/task/submitTask/"+submitTaskId, 'get', null);
    console.log(data);
    $('#container').html(getCheckSubmitTaskFormHtml(submitTaskId,data));
    $('#questions').html(getCheckSubmitTaskQuestionHtml(data.answers, data.isShortAutoCheckAnswer));
    bindCheckSubmitTaskForm();
    questionCount+=data.answers.length;
    setScore(null,null);
}


/*-----------------------------------------------以上是教师控制部分---------------------------------------------------*/

//显示课程查询html
function showSelectCourse() {
    $("#container").html(getSelectCourseHtml());
    bindSelectCourseForm();
}

//加入一个课程
function joinCourse(cid) {
    data = getData("/stu/course/joinCourse", 'post', {'cid':cid});
    console.log(data);
    if(data != null){
        xtalert.alertInfo("选课成功");
    }
}


/*显示学生的所有课程*/
function showStudentCourses() {
    let data = getStudentCourse();
    console.log(data);
    $('#container').html(getStudentCourseHtml('我加入的课程',data));
}

/*显示学生所有的有效课程*/
function showStudentCourseOfNotEnd() {
    let data = getStudentCourseOfNotEnd();
    console.log(data);
    $('#container').html(getStudentCourseHtml('有效课程',data));

}

/*显示学生所有结束的课程*/
function showStudentEndCourse() {
    let data = getStudentEndCourse();
    console.log(data);
    $('#container').html(getStudentCourseHtml('已结束课程',data));
}
/*为学生显示一个课程*/
function showCourseForStudent(cid) {
    let data=getCourseDetail(cid);
    courseData = data;
    console.log(data);
    $('#container').html(getCourseHtml(cid,false, data));
    showTaskTableThreadAndTbody(false,cid,"task");
}

//展示学生所有作业
function showStudentTasks() {
    let data = getData('/stu/task/myTasks','get', null);
    console.log(data);
    $('#container').html(getStudentTasksHtml("所有作业",data));
}

//展示学生待完成作业
function showStudentNotSubmitTasks() {
    let data = getData('/stu/task/myNotSubmitTasks','get', null);
    console.log(data);
    $('#container').html(getStudentTasksHtml("待完成作业",data));
}

//展示学生待完成作业
function showStudentSubmitTasks() {
    let data = getData('/stu/task/mySubmitTasks','get', null);
    console.log(data);
    $('#container').html(getStudentSubmitTaskHtml(data));
}


/*---------------------------------------------------共用------------------------------------------------------*/

//展示作业
function showTask(isTeacher,taskId, cid) {
    let data = getTaskDetail(isTeacher,taskId);
    console.log(data);
    //如果是教师，返回不可提交，显示答案的表单，
    //如果是学生，返回可提交，不显示答案的表单
    if(isTeacher){
        $('#container').html(getCreateTaskFormHtml(cid, data, taskId));
        setTimeChoose();
        bindCreateTaskForm();
        quesionId=1;
        questionCount=0;
        for(let i=0;i<data.questions.length;i++){
            addQuestion(data.questions[i]);
        }
    }else {
        let questions = data.questions;
        if(null === questions || questions === undefined){
            questions = data.answers;
        }
        $('#container').html(getSubmitTaskFormHtml(taskId,data));
        $('#questions').html(getQuestions(questions));
        bindSubmitTaskForm();
    }
}

//展示课程页面的表单内容
function showTaskTableThreadAndTbody(isTeacher,cid ,tableType) {
    let hs = getCourseTableThreadAndTbody(tableType,cid,isTeacher, courseData);
    $('#courseDetailThead').html(hs[0]);
    $('#courseDetailTbody').html(hs[1]);
    $('#courseShowTaskListLi').attr("class","");
    $('#courseShowStuListLi').attr("class","");
    if(tableType ==="task"){
        $('#courseShowTaskListLi').attr("class","active");
    }else if(tableType === 'stu'){
        $('#courseShowStuListLi').attr("class","active");
    }
}
