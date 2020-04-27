//这是负责管理html的js
//--------------------------------------------工具---------------------------------------------*/
function str(data) {
    let s = '';
    if (null != data) {
        s = data;
    }
    return s;
}

//----------------------------------------教师html------------------------------------------*/

//获取关于创建班级的表单*/
function getCreateCourseHtml() {
    // language=HTML
    return `
        <form action="/tea/course/createCourse" id="createCourseForm" method="post" class="form-horizontal"
              style="margin-left: 40%;margin-right: 40%; margin-top: 5%">
            <div class="form-inline" style="margin-bottom: 5%;">
                <div style="margin-left: 20%; margin-right: 20%"><label style="font-size: large"> 创建一门课程 </label></div>
            </div>
            <div class="form-group"><label>课程名称</label> <input type="text" class="form-control" name="cname"
                                                               placeholder="课程名称"></div>
            <div class="form-group"><label>结束时间</label> <input type="text" class="form-control" id="timeChoose" readonly
                                                               name="endTime" placeholder="结束时间"></div>
            <div class="form-group"><label>描述</label> <textarea class="form-control" name="describe"
                                                                placeholder="描述"></textarea></div>
            <div class="form-group">
                <div style="margin-left: 40%;margin-right: 40%">
                    <button type="submit" class="btn btn-success">创建</button>
                </div>
            </div>
        </form>`;
}

//获取教师课程列表html*/
function getTeacherCourseHtml(title, data) {
    let table_head = `     <table class="table">
            <caption>` + title + `</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>课程名称</th>
                <th>描述</th>
                <th>创建时间</th>
                <th>结束时间</th>
                <th>选课人数</th>
                <th>进入课程</th>
            </tr>
            </thead>
            <tbody>`
    let table_end = `
            </tbody>
        </table>`;

    let table_body = ``;
    for (let i = 0; i < data.length; i++) {
        let d = data[i];
        table_body += `<tr>`;
        table_body += `<th class="row">` + (i + 1) + `</th>`
            + `<td>` + d.cname + `</td>`
            + `<td>` + d.describe + `</td>`
            + `<td>` + d.createTime + `</td>`
            + `<td>` + d.endTime + `</td>`
            + `<td>` + d.count + `</td>`
            + `<td><a href="javascript:void(0);" onclick="showCourseForTeacher(` + d.cid + `)">进入课程</a></td>`;
        table_body += `</tr>`;
    }
    table_body += `</tr>`;
    return table_head + table_body + table_end;
}

//获取教师查看所有学生提交的作业*/
function getSubmitTaskListHtml(list) {
    let html = `        <table class="table">
            <caption>学生作业</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>学生</th>
                <th>作业名称</th>
                <th>班级</th>
                <th>提交时间</th>
                <th>是否批改</th>
                <th>进入课程</th>
                <th>查看提交作业</th>
            </tr>
            </thead>
            <tbody>`;
    let tbody = ``;
    for (let i = 0; i < list.length; i++) {
        let d = list[i];
        tbody += `<tr><th class="row">` + (i + 1) + `</th>
                    <td>` + d.sname + `</td>
                    <td>` + d.taskName + `</td>
                    <td>` + d.cname + `</td>
                    <td>` + d.submitTime + `</td>
                    <td>` + (d.isTeaMark === true ? '是' : '否') + `</td>
                    <td><a href="javascript:void(0);" onclick="showCourseForTeacher(` + d.cid + `)">进入课程</a></td>
                    <td><a href="javascript:void(0);" onclick="showSubmitTaskDetail(` + d.subTaskId + `)">查看</a></td>
                </tr>`;
    }
    let htmlEnd = `</tbody>
                        </table>`;
    return html + tbody + htmlEnd;
}

//获取教师查看待批改的作业*/
function getSubmitTaskOfNotPassListHtml(list) {
    let html = `<table class="table">
            <caption>待批改作业</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>学生</th>
                <th>作业名称</th>
                <th>班级</th>
                <th>提交时间</th>
                <th>进入课程</th>
                <th>批改</th>
            </tr>
            </thead>
            <tbody>`;

    let tbody = ``;
    for (let i = 0; i < list.length; i++) {
        let d = list[i];
        tbody += `<tr><th class="row">` + (i + 1) + `</th>
                    <td>` + d.sname + `</td>
                    <td>` + d.taskName + `</td>
                    <td>` + d.cname + `</td>
                    <td>` + d.submitTime + `</td>
                    <td><a href="javascript:void(0);" onclick="showCourseForTeacher(` + d.cid + `)">进入课程</a></td>
                    <td><a href="javascript:void(0);" onclick="showSubmitTaskDetail(` + d.subTaskId + `)">批改</a></td>
                </tr>`;
    }

    let htmlEnd = `</tbody>
        </table>`;
    return html + tbody + htmlEnd;
}

//获取教师查看所有已批改的作业*/
function getSubmitTaskPassListHtml(list) {
    let html = `        <table class="table">
            <caption>学生作业</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>学生</th>
                <th>作业名称</th>
                <th>班级</th>
                <th>提交时间</th>
                <th>进入课程</th>
                <th>查看提交作业</th>
            </tr>
            </thead>
            <tbody>`;
    let tbody = ``;
    for (let i = 0; i < list.length; i++) {
        let d = list[i];
        tbody += `<tr><th class="row">` + (i + 1) + `</th>
                    <td>` + d.sname + `</td>
                    <td>` + d.taskName + `</td>
                    <td>` + d.cname + `</td>
                    <td>` + d.submitTime + `</td>
                    <td><a href="javascript:void(0);" onclick="showCourseForTeacher(` + d.cid + `)">进入课程</a></td>
                    <td><a href="javascript:void(0);" onclick="showSubmitTaskDetail(` + d.subTaskId + `)">查看</a></td>
                </tr>`;
    }
    let htmlEnd = `</tbody>
                        </table>`;
    return html + tbody + htmlEnd;
}


//-------------------------------------------------以上是教师控制html---------------------------------------------------------------------*/
//---------------------------------------------------学生控制html------------------------------------------------------------------------*/

//获取学习搜索课程html
function getSelectCourseHtml() {
    let selectCourseFormHtml = `  <div>
            <form class="form-inline" id="selectCourseForm" action="/stu/course/selectCourse" method="post" style="margin-left: 30%; margin-right: 30%">
                <div class="form-group">
                    <div class="input-group">
                        <input type="text" class="form-control" name="key" placeholder="关键字">
                    </div>
                </div>
                <select class="form-control" name="column">
                    <option value="cname">按课程</option>
                    <option value="tname">按教师</option>
                </select>
                <button type="submit" class="btn btn-primary">查询课程</button>
            </form>
        </div>`;
    let selectCourseTableHtml = `<div>
            <table class="table">
                <caption>查询结果</caption>
                <thead>
                <tr>
                    <th>序号</th>
                    <th>课程名称</th>
                    <th>描述</th>
                    <th>教师</th>
                    <th>创建时间</th>
                    <th>结束时间</th>
                    <th>选课人数</th>
                    <th>选取课程</th>
                </tr>
                </thead>
                <tbody id="selectCourseTbody">
                    
                </tbody>
            </table>
        </div>`;

    return selectCourseFormHtml + selectCourseTableHtml;
}

//获取展示查询的课程列表body的html
function getSelectResultTable(data) {
    let tableBodyHtml = ``;
    for (let i = 0; i < data.length; i++) {
        let d = data[i];
        tableBodyHtml += `<tr>`;
        tableBodyHtml +=
            `<th class="row">` + (i + 1) + `</th>`
            + `<td>` + d.cname + `</td>`
            + `<td>` + d.describe + `</td>`
            + `<td>` + d.tname + `</td>`
            + `<td>` + d.createTime + `</td>`
            + `<td>` + d.endTime + `</td>`
            + `<td>` + d.count + `</td>`
            + `<td><a href="javascript:void(0);" onclick="joinCourse(` + d.cid + `)">选课</a></td>`;
        tableBodyHtml += `</tr>`;
    }

    return tableBodyHtml;
}

//获取显示学生课程列表的html
function getStudentCourseHtml(title, data) {
    let table_head = `<table class="table">
            <caption>` + title + `</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>课程名称</th>
                <th>描述</th>
                <th>创建时间</th>
                <th>结束时间</th>
                <th>进入课程</th>
            </tr>
            </thead>
            <tbody>`
    let table_end = `
            </tbody>
        </table>`;

    let table_body = ``;
    for (let i = 0; i < data.length; i++) {
        let d = data[i];
        table_body += `<tr>`;
        table_body += `<th class="row">` + (i + 1) + `</th>`
            + `<td>` + d.cname + `</td>`
            + `<td>` + d.describe + `</td>`
            + `<td>` + d.createTime + `</td>`
            + `<td>` + d.endTime + `</td>`
            + `<td><a href="javascript:void(0);" onclick="showCourseForStudent(` + d.cid + `)">进入课程</a></td>`;
        table_body += `</tr>`;
    }
    table_body += `</tr>`;
    return table_head + table_body + table_end;
}


//获取课程页面
function getCourseHtml(cid, isTeacher, data) {
    let tasks = data.tasks;
    let html = `
    <div class="panel panel-default">
            <div class="panel-heading">` + data.cname + `</div>
            <div class="panel-body">
            <div style="width:250px; height:auto; float:left; display:inline">
                <p>创建者：` + data.tname + `</p>
                <p>创建时间：` + data.createTime + `</p>
                <p>结束时间：` + data.endTime + `</p>
                <p>人数：` + data.count + `</p>
            </div>
            <div style="width:auto; height:auto; float:left; display:inline" hidden="">
                        <div id="statisticView" style="width: 650px;height:400px;"></div>
            
            </div>
            </div>
            <table class="table">
                <caption style="background-color: #f5f5f5; color: #333; width: 100%;">
                    <div class="navbar-header">
                        <ul class="nav nav-pills">
                            <li role="presentation" id="courseShowTaskListLi" >
                                <a href="javascript:void(0);" onclick="showTaskTableThreadAndTbody(`+isTeacher+`, `+cid+`, 'task')">作业列表</a>
                            </li>
                            <li role="presentation" id="courseShowStuListLi">
                                <a href="javascript:void(0);" onclick="showTaskTableThreadAndTbody(`+isTeacher+`, `+cid+`, 'stu')">学生学习信息</a>
                             </li>
                        </ul>
                    </div>`;
    if (isTeacher) {
        html += `<div class="collapse navbar-collapse" id="createTaskTag">
                        <p class="navbar-text navbar-right">
                            <a href="javascript:void(0);" onclick="showCreateTask(` + cid + `)" class="navbar-link">创建新的作业</a>
                        </p>
                    </div>`;
    }
    html += `</caption>
                <thead id="courseDetailThead">
                
                </thead>
                <tbody id="courseDetailTbody">
                </tbody> 
        </div>`;


    return html;
}

//获取课程下表格的thread和tbody，tableType：什么类型的表格（作业列表或者学生列表），data：数据
function getCourseTableThreadAndTbody(tableType, cid, isTeacher ,data) {
    let thread = ``;
    let tbody = ``;
    if(tableType === "task"){
        thread+=`<tr style="background: #23ddd1">
                    <th>序号</th>
                    <th>作业名称</th>
                    <th>创建时间</th>
                    <th>结束时间</th>`;
        if(isTeacher){
            thread+=`<th>作业数据统计</th>`;
        }
        thread+=` <th>进入作业</th>
                </tr>`;
        let tasks = data.tasks;
        for (let i = 0; i < tasks.length; i++) {
            let task = tasks[i];
            tbody += `<tr style="background: #ddd"><th class="row">` + (i + 1) + `</th>
                    <td>` + task.taskName + `</td>
                    <td>` + task.createTime + `</td>
                    <td>` + task.endTime + `</td>`;
            if(isTeacher){
                tbody+=`<td><a href="javascript:void(0);" onclick="showTaskQuestionAvgScore(` + task.taskId + `)">查看
                            <span class="glyphicon glyphicon-chevron-down" id="taskStatisticGLY_`+task.taskId+`"></span></a></a>
                        </td>`;
            }
            tbody+=`<td><a href="javascript:void(0);" onclick="showTask(` + isTeacher + `,` + task.taskId + `, ` + cid + `)">进入作业</td></tr>`;
            //额外加一行空的tr,以便展开统计信息显示
            tbody+=`<tr id="statisticViewTask_`+task.taskId+`"></tr>`;
        }
    }else if(tableType === "stu"){
        thread+=`<tr style="background: #23ddd1">
                    <th>序号</th>
                    <th>学生</th>
                    <th>学生学习数据</th>
                </tr>`;
        let stus = data.students;
        for (let i = 0; i < stus.length; i++) {
            let stu = stus[i];
            tbody += `<tr style="background: #ddd"><th class="row">` + (i + 1) + `</th>
                    <td>` + stu.sname + `</td>
                    <td><a href="javascript:void(0);" onclick="showTaskStudentScores(` + stu.sid + `, ` + cid + `)">查看
                        <span class="glyphicon glyphicon-chevron-down" id="stuStatisticGLY_`+stu.sid+`"></span></a>
                    </td></tr>`;
            //额外加一行空的tr,为了显示统计信息
            tbody+=`<tr id="statisticViewStu_`+stu.sid+`"></tr>`;
        }
    }
    return [thread, tbody];
}


//获取学生显示作业的表单
function getSubmitTaskFormHtml(taskId, data) {
    let html = `<form role="form" action="/stu/task/submitTask/` + taskId + `" method="post" id="submitTaskForm" class="form-horizontal">
            <div id="taskHead" style="margin-left: 40%" width="100%">
                <div class="form-group" style="text-align: center">
                    <label class="col-sm-2 control-label">作业名称</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" value="` + data.taskName + `" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">结束时间</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" value="` + data.endTime + `" readonly id="timeChoose"
                               placeholder="结束时间">
                    </div>
                </div>
                <div class="form-group" >
                    <span>单选题分值：</span><span id="choiceScore"></span><span>&nbsp;&nbsp;</span>
                    <span>多选题分值：</span><span id="multipleChoiceScore"></span><span>&nbsp;&nbsp;</span>
                    <span>简答题分值：</span><span id="shortScore"></span><span>&nbsp;&nbsp;</span>
                    <span>大题分值：</span><span id="longScore"></span><span>&nbsp;&nbsp;</span>
                    <label>满分：</label><label id="scoreCount"></label>
                </div>
            </div>
            <div id="questions">
            </div>
            <div id="formFoot" width="100%">
                <div class="row" width="100%" style="margin-bottom: 4%">
                    <div style="text-align: center">
                        <input type="checkbox" name="ignoreLockAnswer" value="true">强行提交
                        <button type="submit" class="btn btn-success">提交</button>
                    </div>
                </div>
            </div>
        </form>`;
    return html;
}

//获取学生作业列表Html
function getStudentTasksHtml(title, data) {
    let html = `
        <table class="table">
            <caption>` + title + `</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>作业</th>
                <th>来自</th>
                <th>创建时间</th>
                <th>结束时间</th>
                <th>进入课程</th>
                <th>进入作业</th>
            </tr>
            </thead>
            <tbody>`;
    let tbodyHtml = ``;
    for (let i = 0; i < data.length; i++) {
        let d = data[i];
        tbodyHtml += `
            <tr>
                <th class="row">` + (i + 1) + `</th>
                <td>` + d.taskName + `</td>
                <td>` + d.cname + `</td>
                <td>` + d.createTime + `</td>
                <td>` + d.endTime + `</td>
                <td><a href="javascript:void(0);" onclick="showCourseForStudent(` + d.cid + `)">进入课程</a></td>
                <td><a href="javascript:void(0);" onclick="showTask(false,` + d.taskId + `)">进入作业</a></td>
            </tr>`;
    }
    let htmlEnd = `</tbody>
        </table>`;
    return html + tbodyHtml + htmlEnd;
}

//获取学生已完成作业列表
function getStudentSubmitTaskHtml(data) {
    let htmHead = `
        <table class="table">
            <caption>已完成作业</caption>
            <thead>
            <tr>
                <th>序号</th>
                <th>作业</th>
                <th>来自</th>
                <th>提交时间</th>
                <th>结束时间</th>
                <th>是否批改</th>
                <th>进入课程</th>
                <th>进入作业</th>
            </tr>
            </thead>
            <tbody>`;
    let tbodyHtml = ``;
    for (let i = 0; i < data.length; i++) {
        let d = data[i];
        tbodyHtml += `
            <tr>
                <th class="row">` + (i + 1) + `</th>
                <td>` + d.taskName + `</td>
                <td>` + d.cname + `</td>
                <td>` + d.submitTime + `</td>
                <td>` + d.endTime + `</td>
                <td>已批改</td>
                <td><a href="javascript:void(0);" onclick="showCourseForStudent(` + d.cid + `)">进入课程</a></td>
                <td><a href="javascript:void(0);" onclick="showTask(false,` + d.taskId + `)">进入作业</a></td>
            </tr>`
    }
    let htmlEnd = `</tbody>
        </table>`;
    return htmHead + tbodyHtml + htmlEnd;
}


//----------------------------------------------------作业部分html-------------------------------------------------------------------------*/

//获取创建作业的表单
function getCreateTaskFormHtml(cid, data, taskId) {
    let title, endTime, isShortAutoCheckAnswer, isShowAnswer;
    if (null != data) {
        title = data.taskName;
        endTime = data.endTime;
        isShortAutoCheckAnswer = data.isShortAutoCheckAnswer;
        isShowAnswer = data.isShowAnswer;
    }
    return `<form role="form" action="/tea/task/creatTask/` + cid + `/` + str(taskId) + `" method="post" id="createTaskForm" class="form-horizontal">
            <div id="taskHead" style="margin-left: 40%" width="100%">
                <div class="form-group" style="text-align: center">
                    <label class="col-sm-2 control-label">作业名称</label>
                    <div class="col-sm-3">
                        <input type="text" name="tname" class="form-control" value="` + str(title) + `" placeholder="作业名称">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">结束时间</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" name="endTime" value="` + str(endTime) + `" readonly id="timeChoose" placeholder="结束时间">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" style="font-size: 10px;">简答题自动批改</label>
                    <div class="col-sm-3">
                        <input name="isShortAutoCheckAnswer" type="checkbox" ` + (isShortAutoCheckAnswer ? "checked='checked'" : "") + `>
                    </div>
                </div>
                 <div class="form-group">
                    <label class="col-sm-2 control-label" style="font-size: 10px;">答题后显示答案</label>
                    <div class="col-sm-3">
                        <input name="isShowAnswer" type="checkbox" ` + (isShowAnswer ? "checked='checked'" : "") + `>
                    </div>
                </div>
                
                <div class="form-group" style="text-align: center">
                    <label class="col-sm-2 control-label" style="font-size: 10px;">单选题分值
                        <input type="number" name="choiceScore" value="5" id="choiceScore">
                    </label>


                    <label class="col-sm-2 control-label" style="font-size: 10px; margin-left: 5%">多选题分值
                        <input type="number" name="multipleChoiceScore" value="5" id="multipleChoiceScore">
                    </label>

                    <label class="col-sm-2 control-label" style="font-size: 10px; margin-left: 5%">简答题分值
                        <input type="number" name="shortScore" value="10" id="shortScore">
                    </label>

                    <label class="col-sm-2 control-label" style="font-size: 10px; margin-left: 5%">大题分值
                        <input name="longScore" type="number" value="20" id="longScore">
                    </label>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label" style="font-size: 10px;">当前总分值
                        <input type="number" id="scoreCount" readonly>
                    </label>

                </div>
                
            </div>
            <div id="questions">

            </div>

            <div id="formFoot" width="100%">
                <div class="row" style="margin-bottom: 4%">
                    <div class="col-lg-1" style="float: right">
                        <button type="button" class="btn btn-primary" onclick="addQuestion()">添加题目</button>
                    </div>
                    <div class="col-lg-1" style="float: right; width: 120px">
                        <select id="questionType" class="form-control form-group-lg">
                            <option value="choice">单选题</option>
                            <option value="multipleChoice">多选题</option>
                            <option value="short">简答题</option>
                            <option value="long">大题</option>
                        </select>
                    </div>
                </div>
                <div class="row" width="100%" style="margin-bottom: 4%">
                    <div style="text-align: center">
                        <button type="submit" class="btn btn-success">发布</button>
                    </div>
                </div>
            </div>
        </form>`;
}

//获取单选题html
function getChoiceQuestion(i, data) {
    let content, answer, choiceA, choiceB, choiceC, choiceD;
    if (null != data) {
        content = data.content;
        answer = data.answer;
        choiceA = data.choiceA;
        choiceB = data.choiceB;
        choiceC = data.choiceC;
        choiceD = data.choiceD;
    }
    let questionHtml =
        `<div id="question_` + i + `" style="margin-bottom: 4%;">
                    <div class="form-group">
                        <div class="input-group">
                            <div id="questionSeq_` + i + `" class="input-group-addon" type="choice">` + questionCount + `.</div>
                            <textarea type="text" name="choiceContent_` + i + `"   class="form-control" placeholder="单项选择题">` + str(content) + `</textarea>
                            <div class="input-group-addon"><a href="javascript:void(0);" onclick="deleteQuestion(` + i + `)">删除</a></div>
                        </div>
                    </div>
                    <div class="row" style="margin-left: 2%">
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="radio" name="choiceAnswer_` + i + `" ` + (answer === 'A' ? "checked='checked'" : "") + ` value="A"> A
                                </div>
                                <input type="text" name="choiceAnswerAContent_` + i + `" value="` + str(choiceA) + `" class="form-control" placeholder="选项A">
                            </div>
                        </div>
                       <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="radio" name="choiceAnswer_` + i + `" ` + (answer === 'B' ? "checked='checked'" : "") + ` value="B"> B
                                </div>
                                <input type="text" name="choiceAnswerBContent_` + i + `" value="` + str(choiceB) + `" class="form-control" placeholder="选项B">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="radio" name="choiceAnswer_` + i + `" ` + (answer === 'C' ? "checked='checked'" : "") + ` value="C"> C
                                </div>
                                <input type="text" name="choiceAnswerCContent_` + i + `" value="` + str(choiceC) + `" class="form-control" placeholder="选项C">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="radio" name="choiceAnswer_` + i + `" ` + (answer === 'D' ? "checked='checked'" : "") + ` value="D"> D
                                </div>
                                <input type="text" name="choiceAnswerDContent_` + i + `" value="` + str(choiceD) + `" class="form-control" placeholder="选项D">
                            </div>
                        </div>
                    </div>
                </div>`;
    return questionHtml;
}

//获取的多选题html
function getMultipleQuestion(i, data) {
    let content, answer, choiceA, choiceB, choiceC, choiceD;
    let A = false, B = false, C = false, D = false;
    if (null != data) {
        content = data.content;
        answer = data.answer;
        choiceA = data.mchoiceA;
        choiceB = data.mchoiceB;
        choiceC = data.mchoiceC;
        choiceD = data.mchoiceD;
        for (let i = 0; i < answer.length; i++) {
            switch (answer.charAt(i)) {
                case 'A':
                    A = true;
                    break;
                case 'B':
                    B = true;
                    break;
                case 'C':
                    C = true;
                    break;
                case 'D':
                    D = true;
                    break;
            }
        }
    }


    let questionHtml =
        `<div id="question_` + i + `" style="margin-bottom: 4%;">
                    <div class="form-group">
                        <div class="input-group">
                            <div id="questionSeq_` + i + `" class="input-group-addon" type="multipleChoice">` + questionCount + `.</div>
                            <textarea type="text" name="multipleChoiceContent_` + i + `" class="form-control" placeholder="多项选择题">` + str(content) + `</textarea>
                            <div class="input-group-addon"><a href="javascript:void(0);" onclick="deleteQuestion(` + i + `)">删除</a></div>
                        </div>
                    </div>
                    <div class="row" style="margin-left: 2%">
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="checkbox" name="multipleChoiceAnswer_` + i + `" ` + (A ? "checked='checked'" : "") + `  value="A"> A
                                </div>
                                <input type="text" name="multipleChoiceAnswerAContent_` + i + `" value="` + str(choiceA) + `" class="form-control" placeholder="选项A">
                            </div>
                        </div>
                       <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="checkbox" name="multipleChoiceAnswer_` + i + `" ` + (B ? "checked='checked'" : "") + ` value="B"> B
                                </div>
                                <input type="text" name="multipleChoiceAnswerBContent_` + i + `" value="` + str(choiceB) + `" class="form-control" placeholder="选项B">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="checkbox" name="multipleChoiceAnswer_` + i + `" ` + (C ? "checked='checked'" : "") + `  value="C"> C
                                </div>
                                <input type="text" name="multipleChoiceAnswerCContent_` + i + `" value="` + str(choiceC) + `" class="form-control" placeholder="选项C">
                            </div>
                        </div>
                        <div class="col-lg-3">
                            <div class="input-group">
                                <div class="input-group-addon">
                                    <input type="checkbox" name="multipleChoiceAnswer_` + i + `" ` + (D ? "checked='checked'" : "") + ` value="D"> D
                                </div>
                                <input type="text" name="multipleChoiceAnswerDContent_` + i + `" value="` + str(choiceD) + `" class="form-control" placeholder="选项D">
                            </div>
                        </div>
                    </div>
                </div>`;
    return questionHtml;
}

//获取的简答题html
function getShortQuestion(i, data) {
    let content, answer;
    if (null != data) {
        content = data.content;
        answer = data.answer;
    }
    let questionHtml =
        `<div id="question_` + i + `" style="margin-bottom: 4%;">
                    <div class="form-group">
                        <div class="input-group">
                            <div id="questionSeq_` + i + `" class="input-group-addon" type="short">` + questionCount + `.</div>
                            <textarea type="text" name="shortQuestionContent_` + i + `" class="form-control" placeholder="简答题">` + str(content) + `</textarea>
                            <div class="input-group-addon"><a href="javascript:void(0);" onclick="deleteQuestion(` + i + `)">删除</a></div>
                        </div>
                    </div>
                    <div class="row" style="margin-left: 2%;margin-right: 0.2%">
                        <div class="input-group">
                            <div class="input-group-addon">答案</div>
                            <input type="text" name="shortQuestionAnswer_` + i + `" value="` + str(answer) + `" class="form-control" placeholder="正确答案">
                        </div>
                    </div>
                </div>`;
    return questionHtml;
}

//获取大题html
function getLongQuestionHtml(i, data) {
    let content, answer;
    if (null != data) {
        content = data.content;
        answer = data.answer;
    }
    let html = `<div id="question_` + i + `" style="margin-bottom: 4%;">
                    <div class="form-group">
                        <div class="input-group">
                            <div id="questionSeq_` + i + `" class="input-group-addon" type="long">` + questionCount + `.</div>
                            <textarea type="text" name="longQuestionContent_` + i + `" class="form-control" placeholder="大题">` + str(content) + `</textarea>
                            <div class="input-group-addon"><a href="javascript:void(0);" onclick="deleteQuestion(` + i + `)">删除</a></div>
                        </div>
                    </div>
                    <div class="row" style="margin-left: 2%; margin-right: 0.2%">
                        <div class="input-group">
                            <div class="input-group-addon">答案</div>
                            <textarea type="text" name="longQuestionAnswer_` + i + `" class="form-control" placeholder="参考答案">` + str(answer) + `</textarea>
                        </div>
                    </div>
                </div>`;
    return html;
}

//获取批改作业formHtml
function getCheckSubmitTaskFormHtml(submitTaskId,data) {

    let html = `<form role="form" action="/tea/task/CheckSubmitTask/`+submitTaskId+`" method="post" id="submitTaskForm" class="form-horizontal">
            <div id="taskHead" style="margin-left: 40%" width="100%">
                <div class="form-group" style="text-align: center">
                    <label class="col-sm-2 control-label">作业名称</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" value="` + str(data.taskName) + `" readonly="">
                    </div>
                </div>
                <div class="form-group" style="text-align: center">
                    <label class="col-sm-2 control-label">所属学生</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" value="` + str(data.sname) + `" readonly="">
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-2 control-label">提交时间</label>
                    <div class="col-sm-3">
                        <input type="text" class="form-control" value="` + str(data.submitTime) + `" readonly="" id="timeChoose" placeholder="提交时间">
                    </div>
                </div>
                <div class="form-group">
                    <span>单选题分值：</span><span id="choiceScore"></span><span>&nbsp;&nbsp;</span>
                    <span>多选题分值：</span><span id="multipleChoiceScore"></span><span>&nbsp;&nbsp;</span>
                    <span>简答题分值：</span><span id="shortScore"></span><span>&nbsp;&nbsp;</span>
                    <span>大题分值：</span><span id="longScore"></span><span>&nbsp;&nbsp;</span>
                    <label>满分：</label><label id="scoreCount">` + str(data.scoreCount) + `</label>
                </div>
            </div>
            <div id="questions">
            
            </div>
            <div id="formFoot" width="100%">
                <div class="row" width="100%" style="margin-bottom: 4%;text-align: center">
                        <label>当前总分：
                            <span id="scoreNowCount"></span>
                            <input type="text" name="scoreCount" id="scoreNowCountInput" hidden="hidden">
                        </label>
                </div>
                <div class="row" width="100%" style="margin-bottom: 4%">
                    <div style="text-align: center">
                        <button type="submit" class="btn btn-success">批改</button>
                    </div>
                </div>
            </div>
        </form>`;
    return html;
}

//获取批改作业的单选题
function getCheckSubmitTaskChoiceHtml(questionSeq, question) {
    let A = false, B = false, C = false, D = false;
    let answer = question.submitAnswer;
    if (answer != null) {
        for (let i = 0; i < answer.length; i++) {
            switch (answer.charAt(i)) {
                case 'A':
                    A = true;
                    break;
                case 'B':
                    B = true;
                    break;
                case 'C':
                    C = true;
                    break;
                case 'D':
                    D = true;
                    break;
            }
        }
    }
    let html = `
        <div class="panel panel-default small">
                    <div style="">
                        <pre style="border:0px;margin: 0 0 0 "><span>`+questionSeq+`</span><span>. </span><span style="height: auto;margin-bottom: 0px">`+question.content+`</span></pre>
                    </div>

                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" ` + (A ? "checked='checked'" : "") + ` disabled  style="margin-top: 0.2%" value="A">
                                <span>A </span><span>`+question.choiceA+`</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (B ? "checked='checked'" : "") + ` disabled value="B">
                                <span>B </span><span>`+question.choiceB+`</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (C ? "checked='checked'" : "") + ` disabled value="C">
                                <span>C </span><span>`+question.choiceC+`</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (D ? "checked='checked'" : "") + ` disabled value="D">
                                <span>D </span><span>`+question.choiceD+`</span>
                            </label>
                        </div>
                    </div>`;
    let htmlFooter = ``;
    if(question.submitScore>0){
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;得分：</span><span id="answerScore_`+question.seqNum+`">`+question.submitScore+`</span><span>（自动批改）</span>
                    <input type="text"  name="answerScore_`+question.qid+`" value="`+question.submitScore+`" hidden="hidden">
                </div>`;
    }else {
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true">&nbsp;</span>
                    <span>正确答案：`+question.answer+`</span><span>&nbsp;&nbsp;得分：</span><span>`+question.submitScore+`</span><span>（自动批改）</span>
                </div>`;
    }
    htmlFooter+=`</div>`;
    return html+htmlFooter;
}


//获取批改作业的多选题
function getCheckSubmitTaskMultipleChoiceHtml(questionSeq, question) {
    let A = false, B = false, C = false, D = false;
    let answer = question.submitAnswer;
    if (answer != null) {
        for (let i = 0; i < answer.length; i++) {
            switch (answer.charAt(i)) {
                case 'A':
                    A = true;
                    break;
                case 'B':
                    B = true;
                    break;
                case 'C':
                    C = true;
                    break;
                case 'D':
                    D = true;
                    break;
            }
        }
    }
    let html = `
    <div class="panel panel-default small">
        <div>
            <pre><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre>
        </div>
        <div style="margin-left: 2%">
            <div class="checkbox">
            <label>
            <input type="checkbox" ` + (A ? "checked='checked'" : "") + ` disabled style="margin-top: 0.2%"  value="A">
            <span>A&ensp;</span><span>` + question.mchoiceA + `</span>
        </label>
        </div>
        </div>
        <div style="margin-left: 2%">
            <div class="checkbox">
            <label>
            <input type="checkbox" ` + (B ? "checked='checked'" : "") + ` disabled style="margin-top: 0.2%" value="B">
            <span>B&ensp;</span><span>` + question.mchoiceB + `</span>
        </label>
        </div>
        </div>
        <div style="margin-left: 2%">
            <div class="checkbox">
            <label>
            <input type="checkbox" ` + (C ? "checked='checked'" : "") + ` disabled style="margin-top: 0.2%" value="C">
            <span>C&ensp;</span><span>` + question.mchoiceC + `</span>
        </label>
        </div>
        </div>
        <div style="margin-left: 2%">
            <div class="checkbox">
                <label>
                    <input type="checkbox" ` + (D ? "checked='checked'" : "") + ` disabled style="margin-top: 0.2%" value="D">
                    <span>D&ensp;</span><span>` + question.mchoiceD + `</span>
                </label>
            </div>
        </div>`;
    let htmlFooter = ``;
    if(question.submitScore>0){
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;得分：</span><span id="answerScore_`+question.seqNum+`">`+question.submitScore+`</span><span>（自动批改）</span>
                    <input type="text"  name="answerScore_`+question.qid+`" value="`+question.submitScore+`" hidden="hidden">
                </div>`;
    }else {
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true">&nbsp;</span>
                    <span>正确答案：`+question.answer+`</span><span>&nbsp;&nbsp;得分：</span><span>`+question.submitScore+`</span><span>（自动批改）</span>
                </div>`;
    }
    htmlFooter+=`</div>`;

    return html+htmlFooter;
}

//获取批改作业的简答题
function getCheckSubmitTaskShortHtml(isShortAutoCheck, question) {
    let html = `
        <div class="panel panel-default small">
                    <div>
                        <pre style="border: 0;margin: 0 0 0"><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre></div>
                         <div class="input-group" style="width: 100%">
                                <input type="text" value="` + str(question.submitAnswer) + `" class="form-control" placeholder=无 readonly>
                         </div>
               `;
    let htmlFooter = ``;
    let checkScores = [0,0,0,0,0,0];
    for(let i=0;i<checkScores.length;i++){
        checkScores[i] = question.score*(i*2)/10;
    }

    if(question.submitScore>0){
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;得分：</span><span id="answerScore_`+question.seqNum+`">`+question.submitScore+`</span><span>（自动批改）</span>
                    <input type="text"  name="answerScore_`+question.qid+`" value="`+question.submitScore+`" hidden="hidden">
                </div>`;
    }else if(isShortAutoCheck){
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true">&nbsp;</span>
                    <span>正确答案：`+question.answer+`</span><span id="answerScore_`+question.seqNum+`">&nbsp;&nbsp;得分：</span><span>`+question.submitScore+`</span><span>（自动批改）</span>
                </div>`;
    }else {
        htmlFooter += `<div class="panel-footer">
                    <div class="input-group" style="margin-right: 50%; height: 100%">
                        <div class="input-group-addon">打分</div>
                        <input type="text" id="answerScore_`+question.seqNum+`"  name="answerScore_`+question.qid+`" class="form-control" placeholder="输入分数">
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[0]+`)">`+checkScores[0]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[1]+`)">`+checkScores[1]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[2]+`)">`+checkScores[2]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[3]+`)">`+checkScores[3]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[4]+`)">`+checkScores[4]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[5]+`)">`+checkScores[5]+`分</div>
                    </div>
                </div>`;
    }
    htmlFooter+=`</div>`;

    return html+htmlFooter;
}

//获取批改作业的大题
function getCheckSubmitTaskLongHtml(questionSeq, question) {
    let html = `
        <div class="panel panel-default small">
                   <div>
                        <pre style="border: 0;margin: 0 0 0"><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre>
                   </div>
                   <div class="input-group" style="width: 100%">
                                <textarea rows="5" class="form-control" placeholder='无' readonly>` + str(question.submitAnswer) + `</textarea>
                            </div>
               `;
    let htmlFooter = ``;
    let checkScores = [0,0,0,0,0,0];
    for(let i=0;i<checkScores.length;i++){
        checkScores[i] = question.score*(i*2)/10;
    }

    if(question.submitScore>0){
        htmlFooter+=`<div class="panel-footer">
                    <span class="glyphicon glyphicon-ok" aria-hidden="true">&nbsp;得分：</span><span id="answerScore_`+question.seqNum+`">`+question.submitScore+`</span>
                </div>`;
    }else {
        htmlFooter+=` <div class="panel-footer">
                    <div class="input-group" style="margin-right: 50%; height: 100%">
                        <div class="input-group-addon">打分</div>
                        <input type="text" id="answerScore_`+question.seqNum+`"  name="answerScore_`+question.qid+`" class="form-control" placeholder="输入分数">
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[0]+`)">`+checkScores[0]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[1]+`)">`+checkScores[1]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[2]+`)">`+checkScores[2]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[3]+`)">`+checkScores[3]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[4]+`)">`+checkScores[4]+`分</div>
                        <div class="input-group-addon" onclick="setScore(`+question.seqNum+`,`+checkScores[5]+`)">`+checkScores[5]+`分</div>
                    </div>
                </div>`;
    }
    htmlFooter+=`</div>`;

    return html+htmlFooter;
}

//-------------------------------------------------学生提交作业部分html-----------------------------------------------------------------------------*/

//获取提交选择题部分
function getSubmitChoiceQuestion(question) {
    $('#choiceScore').html(question.score);
    let A = false, B = false, C = false, D = false;
    let answer = question.submitAnswer;
    if (answer != null) {
        for (let i = 0; i < answer.length; i++) {
            switch (answer.charAt(i)) {
                case 'A':
                    A = true;
                    break;
                case 'B':
                    B = true;
                    break;
                case 'C':
                    C = true;
                    break;
                case 'D':
                    D = true;
                    break;
            }
        }
    }
    let questionHtml =
        `<div class="panel panel-default small">
                    <div style="">
                        <pre style="border:0px;margin: 0 0 0 "><span>` + question.seqNum + `</span><span>.&ensp;</span><span style="height: auto;margin-bottom: 0px">` + question.content + `</span></pre></div>

                    <div style="margin-left: 2%">
                        <div class="radio" >
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (A ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="A">
                                <span>A&ensp;</span><span>` + question.choiceA + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (B ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="B">
                                <span>B&ensp;</span><span>` + question.choiceB + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (C ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="C">
                                <span>C&ensp;</span><span>` + question.choiceC + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="radio">
                            <label>
                                <input type="radio" style="margin-top: 0.2%" ` + (D ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="D">
                                <span>D&ensp;</span><span>` + question.choiceD + `</span>
                            </label>
                        </div>
                    </div>
                </div>`;
    return questionHtml;
}

//获取提交多选题部分
function getSubmitMultipleChoiceQuestion(question) {
    $('#multipleChoiceScore').html(question.score);
    let A = false, B = false, C = false, D = false;
    let answer = question.submitAnswer;
    if (answer != null) {
        for (let i = 0; i < answer.length; i++) {
            switch (answer.charAt(i)) {
                case 'A':
                    A = true;
                    break;
                case 'B':
                    B = true;
                    break;
                case 'C':
                    C = true;
                    break;
                case 'D':
                    D = true;
                    break;
            }
        }
    }

    let questionHtml = `<div class="panel panel-default small">
                    <div>
                        <pre><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre></div>
                    <div style="margin-left: 2%">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" style="margin-top: 0.2%" ` + (A ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="A">
                                <span>A&ensp;</span><span>` + question.mchoiceA + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" style="margin-top: 0.2%" ` + (B ? "checked='checked'" : "") + `  name="questionAnswer_` + question.qid + `" value="B">
                                <span>B&ensp;</span><span>` + question.mchoiceB + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" style="margin-top: 0.2%" ` + (C ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="C">
                                <span>C&ensp;</span><span>` + question.mchoiceC + `</span>
                            </label>
                        </div>
                    </div>
                    <div style="margin-left: 2%">
                        <div class="checkbox">
                            <label>
                                <input type="checkbox" style="margin-top: 0.2%" ` + (D ? "checked='checked'" : "") + ` name="questionAnswer_` + question.qid + `" value="D">
                                <span>D&ensp;</span><span>` + question.mchoiceD + `</span>
                            </label>
                        </div>
                    </div>
                </div>`;
    return questionHtml;
}

//获取提交简答题部分
function getSubmitShortQuestion(question) {
    $('#shortScore').html(question.score);
    let answer = question.submitAnswer;
    let questionHtml = `
        <div class="panel panel-default small">
                    <div>
                        <pre style="border: 0;margin: 0 0 0"><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre></div>
                         <div class="input-group" style="width: 100%">
                                <input type="text" name="questionAnswer_` + question.qid + `" value="` + str(answer) + `" class="form-control" placeholder=请输入你的答案>
                         </div>
                </div>`;
    return questionHtml;
}

//获取提交大题部分
function getSubmitLongQuestion(question) {
    $('#longScore').html(question.score);
    let answer = question.submitAnswer;
    let questiinHtml = `
        <div class="panel panel-default small">
                   <div>
                        <pre style="border: 0;margin: 0 0 0"><span>` + question.seqNum + `</span><span>.&ensp;</span><span>` + question.content + `</span></pre>
                   </div>
                   <div class="input-group" style="width: 100%">
                                <textarea rows="5" name="choiceContent_` + question.qid + `"  value="` + str(answer) + `" class="form-control" placeholder=请输入你的答案></textarea>
                            </div>
                </div>`;
    return questiinHtml;
}

