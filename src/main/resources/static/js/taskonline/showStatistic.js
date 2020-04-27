//这个js是专门负责统计数据的


//工具，从data中获取x轴的值数组，y周的值数组（排序由后台负责，属性seq为序号）
function getXYDataArray(data) {
    let xDataArray = [data.length];
    let yDataArray = [data.length];
    for(let i=0;i<data.length;i++){
        xDataArray[i] = data[i].xname;
        yDataArray[i] = data[i].ynumber;
    }
    return [xDataArray, yDataArray];
}


//展示课程下每次作业学生平均得分（一个课程下所有作业平均得分的起伏）
function showCourseAvgTasksScore(courseId) {
    let data = getData("/statistic/courseAvgScores/"+courseId, 'get', null);
    console.log(data);
    let xyArray = getXYDataArray(data);
    let option = {
        title:{
            text:'作业学生平均成绩数据',
            right:'100'
        },
        xAxis: {
            type: 'category',
            name:'作业名称',
            data: xyArray[0]
        },
        yAxis: {
            boundaryGap: ['40%', '20%'],
            type: 'value',
            name:'百分化分数(100*平均得分/总分)',
            min: 0
        },
        grid: {
            left: 200
        },
        series: [{
            data: xyArray[1],
            type: 'line'
        }]
    };
    let myChart = echarts.init(document.getElementById('statisticView'));
    myChart.setOption(option);
}

//展示作业下每个问题学生平均得分的起伏
function showTaskQuestionAvgScore(taskId) {
    let tdHtml = `<td colspan="6" id="taskStatistic_`+taskId+`" style="height: 400px; width: 800px"></td>`;
    let gly=$('#taskStatisticGLY_'+taskId)
    let className = gly.attr("class");
    if (className === 'glyphicon glyphicon-chevron-down'){ //展开
        className = 'glyphicon glyphicon-chevron-up';
        $('#statisticViewTask_'+taskId).html(tdHtml);
        let myChart = echarts.init(document.getElementById('taskStatistic_'+taskId));
        let data = getData("/statistic/taskQuestionsData/"+taskId, 'get', null);
        console.log(data);
        let xyArray = getXYDataArray(data);
        let option = {
            title:{
                text:'作业学生各题得分情况',
                right:'100'
            },
            xAxis: {
                type: 'category',
                name:'题目序号',
                data: xyArray[0]
            },
            yAxis: {
                type: 'value',
                name:'百分化分数(100*平均得分/总分)',
                min: 0
            },
            series: [{
                data: xyArray[1],
                type: 'bar'
            }]
        };
        myChart.setOption(option);
    }else if(className === 'glyphicon glyphicon-chevron-up'){ //回缩
        className = 'glyphicon glyphicon-chevron-down';
        $('#statisticViewTask_'+taskId).html('');
    }
    gly.attr("class", className);

}

//展示学生在一个课程下每次作业学生成绩的数据（一个学生在课程内每次作业成绩的起伏）
function showTaskStudentScores(sid, courseId) {
    let tdHtml = `<td colspan="3" id="stuStatistic_`+sid+`" style="height: 400px; width: 800px"></td>`;
    let gly=$('#stuStatisticGLY_'+sid)
    let className = gly.attr("class");
    if (className === 'glyphicon glyphicon-chevron-down'){ //展开
        className = 'glyphicon glyphicon-chevron-up';
        $('#statisticViewStu_'+sid).html(tdHtml);
        let data = getData("/statistic/stuScoresOfCourse/"+courseId+"/"+sid, 'get', null);
        console.log(data);
        let myChart = echarts.init(document.getElementById('stuStatistic_'+sid));
        let xyArray = getXYDataArray(data);
        let option = {
            title:{
                text:'学生各个作业得分情况',
                right:'100'
            },
            xAxis: {
                type: 'category',
                name:'作业名称',
                data: xyArray[0]
            },
            yAxis: {
                type: 'value',
                name:'百分化分数(100*平均得分/总分)',
                min: 0
            },
            series: [{
                data: xyArray[1],
                type: 'line'
            }]
        };
        myChart.setOption(option);
    }else if(className === 'glyphicon glyphicon-chevron-up'){ //回缩
        className = 'glyphicon glyphicon-chevron-down';
        $('#statisticViewStu_'+sid).html('');
    }
    gly.attr("class", className);
}

//展示学生在所有课程内作业的成绩的起伏
function showStudentScores() {
    let data = getData("/statistic/stuScoresData", 'get', null);
    console.log(data);
    $('#container').html(`<div id="stuScoresStatisticView" style="width: 800px;height: 500px"></div>`)
    let myChart = echarts.init(document.getElementById('stuScoresStatisticView'));
    let xyArray = getXYDataArray(data['所有课程']);
    let option = {
        title:{
            text:'作业得分折线图',
            right:'100'
        },
        xAxis: {
            type: 'category',
            name:'作业名称',
            data: xyArray[0]
        },
        yAxis: {
            type: 'value',
            name:'百分化分数(100*平均得分/总分)',
            min: 0
        },
        grid: {
            left: 200
        },
        series: [{
            data: xyArray[1],
            type: 'line'
        }]
    };
    myChart.setOption(option);

}