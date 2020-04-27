//这是关于项目网络请求的js

/*工具类，同步请求数据*/
function getData(url, type, datas) {
    var data =null;
    $.ajax({
        url: url,
        type: type,
        data: datas,
        dataType: 'json',
        async: false, //设置同步，以便获取数据
        success: function(result){
            msg=result.msg;
            if (msg!=null){
                xtalert.alertError(msg);
                return null
            }
            data=result.object;
        },
        error:function (result) {
            xtalert.alertError('加载数据失败');
        },
        fail:function (result) {
            xtalert.alertError('请求失败');
        }
    }, 'json');
    return data;
}

/*-----------------------------------------------------------------------------------*/

/*为创建班级的表单绑定点击事件，用于处理发送数据。接受处理数据*/
function bindCreateCourseForm() {
    $(document).ready(function() {
        // bind form using ajaxForm
        $('#createCourseForm').ajaxForm({
            // target identifies the element(s) to update with the server response
            //target: '#htmlExampleTarget',
            // success identifies the function to invoke when the server response
            // has been received; here we apply a fade-in effect to the new content
            success: function (rs) {
                console.log(rs);
                let msg = rs.msg;
                if(msg!=null){
                    xtalert.alertError("创建课程失败："+msg);
                }else {
                    xtalert.alertInfo("创建课程成功");
                    $('#createCourseForm').clearForm();
                }
            },
            error: function (rs) {
                console.log(rs);
                xtalert.alertInfo("创建课程失败");
            }
        });
    });
}

/*为创建作业表单提供基于ajax的提交，并处理接收数据*/
function bindCreateTaskForm() {
    $(document).ready(function() {
        // bind form using ajaxForm
        $('#createTaskForm').ajaxForm({
            // target identifies the element(s) to update with the server response
            //target: '#htmlExampleTarget',
            // contentType: 'application/json',
            // success identifies the function to invoke when the server response
            // has been received; here we apply a fade-in effect to the new content
            success: function (rs) {
                console.log(rs);
                let msg = rs.msg;
                if(msg!=null){
                    xtalert.alertError("创建作业失败："+msg);
                }else {
                    console.log(rs);
                    xtalert.alertInfo("创建作业成功");
                }
            },
            error: function (rs) {
                console.log(rs);
                xtalert.alertInfo("创建课程失败");
            }
        });
    });
}

/*为查询课程的表单绑定点击事件、发送ajax请求，处理返回数据*/
function bindSelectCourseForm() {
    $(document).ready(function() {
        // bind form using ajaxForm
        $('#selectCourseForm').ajaxForm({
            success: function (rs) {
                console.log(rs);
                let msg = rs.msg;
                if(msg!=null){
                    xtalert.alertError("查询课程失败："+msg);
                }else {
                    let data = rs.object;
                    $('#selectCourseTbody').html(getSelectResultTable(data));
                }
            },
            error: function (data) {
                console.log(data);
                xtalert.alertInfo("查询课程失败");
            }
        });
    });
}


/*为提交作业表单绑定ajax、发送ajax请求，处理返回数据*/
function bindSubmitTaskForm() {
    $(document).ready(function() {
        // bind form using ajaxForm
        $('#submitTaskForm').ajaxForm({
            success: function (rs) {
                console.log(rs);
                let msg = rs.msg;
                let warm = rs.wram;
                if(msg!=null){
                    xtalert.alertError("提交作业失败："+msg);
                }else {
                    let data = rs.object;
                    console.log(data);
                    xtalert.alertInfo("提交成功");
                }
            },
            error: function (data) {
                console.log(data);
                xtalert.alertInfo("连接服务器失败");
            }
        });
    });
}


/*为批改作业表单绑定ajax、发送ajax请求，处理返回数据*/
function bindCheckSubmitTaskForm() {
    $(document).ready(function() {
        // bind form using ajaxForm
        $('#submitTaskForm').ajaxForm({
            success: function (rs) {
                console.log(rs);
                let msg = rs.msg;
                let warm = rs.wram;
                if(msg!=null){
                    xtalert.alertError("批改作业失败："+msg);
                }else {
                    let data = rs.object;
                    console.log(data);
                    xtalert.alertInfo("提交成功");
                }
            },
            error: function (data) {
                console.log(data);
                xtalert.alertInfo("连接服务器失败");
            }
        });
    });
}


/*--------------------------------------------------------------------------------------*/

/*获取教师所有课程*/
function getTeacherCourse() {
    return getData('/tea/course/myCourses', 'get', null);
}

/*获取教师所有未过期的课程*/
function getTeacherCourseOfNotEnd() {
    return getData('/tea/course/notEndCourses', 'get', null);
}

/*获取教师所有过期课程*/
function getTeacherEndCourse() {
    return getData('/tea/course/endCourses', 'get', null);
}


/*--------------------------------------------------------------------*/

/*获取学生所有课程*/
function getStudentCourse() {
    return getData('/stu/course/myCourses', 'get', null);
}

/*获取学生所有未过期的课程*/
function getStudentCourseOfNotEnd() {
    return getData('/stu/course/notEndCourses', 'get', null);
}

/*获取学生所有过期课程*/
function getStudentEndCourse() {
    return getData('/stu/course/endCourses', 'get', null);
}

//学习获取作业详细
function getTaskDetail(isTeacher,taskId) {
    if(isTeacher) return getData('/tea/task/'+taskId, 'get', null);
    else return getData('/stu/task/'+taskId, 'get', null);
}

/*--------------------------------------共用----------------------------------------------*/

//查询课程信息、课程下作业列表
function getCourseDetail(cid) {
    return getData('/course/'+cid, 'get', null);
}