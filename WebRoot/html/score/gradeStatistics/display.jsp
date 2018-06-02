<%--
  Created by IntelliJ IDEA.
  User: TGL
  Date: 2018/5/31
  Time: 14:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>

<head>
    <title>成绩分析列表</title>
    <link rel="stylesheet" href="../../../plugin/layui/css/layui.css" />
    <script src="../../../plugin/jquery/jquery.js"></script>
    <script src="../../../plugin/jquery/jquery.serializejson.js"></script>
    <script src="../../../plugin/jsrender/jsrender.js"></script>
    <script src="../../../plugin/layui/layui.all.js"></script>
</head>

<body>
<div class="layui-container" style="width:100%;">
    <div class="layui-btn-group">
        <button class="layui-btn layui-bg-black layui-btn-sm" onclick="goBack()">
            <i class="layui-icon">&#xe65c;</i>返回
        </button>
        <%--<shiro:hasPermission name="father:exam:save">--%>
        <%--<button class="layui-btn layui-bg-green layui-btn-sm" onclick="goCreate()">--%>
        <%--<i class="layui-icon">&#xe654;</i>新增--%>
        <%--</button>--%>
        <%--</shiro:hasPermission>--%>
        <!--<button class="layui-btn layui-bg-blue layui-btn-sm" id="importExcel">-->
        <!--<i class="layui-icon">&#xe67c;</i>导入-->
        <!--</button>-->
    </div>
    <h1 class="layui-main" style="text-align: center" id="examName">成绩</h1>
    <hr class="layui-bg-orange">
    <div>
            <h2 class="layui-text" style="float: left;margin-left: 50px" id="tclass">班级：成绩</h2>
            <h2 class="layui-text" style="float: left;margin-left: 350px;" id="name">姓名：成绩</h2>
            <h2 class="layui-text" style="float: left;margin-left: 350px;" id="student_no">学号：成绩</h2>
    </div>
    <table id="grade" class="layui-table" lay-filter="test">
        <thead id="courseList">
        </thead>
        <tbody id="scoreList">
        </tbody>
    </table>
    <%--<div id="page"></div>--%>
</div>

<script>
    var laypage = layui.laypage;
    var studentId = sessionStorage.getItem("currentStudentId");
    var examname = sessionStorage.getItem("currentFatherExamName");
    var examId = sessionStorage.getItem("currentFatherExamId");
    var html='<tr><th lay-data="{sort: true}">姓名</th><th>学号</th><th>班级</th>';
    var scoreHtml = '';
    var examArr = [];
    var studentMap = new Map();
    var rankMap = new Map();
    var studentCount = new Map();

    $(document).ready(function() {
        document.getElementById('examName').innerHTML=examname+"成绩报告单";
            $.ajax({
                async:false,
                type:'GET',
                url: '/student/find/'+studentId,
                success:function (result) {
                    if(result.code === 200){
                        document.getElementById('tclass').innerHTML="班级："+result.data.tclassName;
                        document.getElementById('name').innerHTML="姓名："+result.data.name;
                        document.getElementById('student_no').innerHTML="学号："+result.data.student_no;
                    }
                }
            })

        $.ajax({
            async: false,
            type: 'GET',
            url: '/exam/list/'+examId,
            dataType: 'json',
            success: function(examList) {
                for (var i = 0; i < examList.length; i++){
                    html+="<th>"+examList[i].course_name+"总分"+'</th>'
                    html+="<th>"+examList[i].course_name+"年级排名"+'</th>'
                    examArr.push(examList[i].id);
                }
                html+='<th>总分</th><th>总分年级排名</th></tr>';
            }
        });
        $('#courseList').html(html)
        list();

    })

    function list() {
        $.ajax({
            async: false,
            type: 'GET',
            url:  '/score/listByFatherExamId/'+examId+','+studentId,
            success: function(data) {
                for(var i = 0; i < data.data.length; i++){
                    studentMap.put(data.data[i].student_id+"-"+data.data[i].exam_id, data.data[i].totalScore);
                    studentCount.put(data.data[i].student_id, 0);
                    rankMap.put(data.data[i].student_id+"-"+data.data[i].exam_id, data.data[i].rank);
                    rankMap.put(data.data[i].student_id, data.data[i].allRank);
                }
                loadData(data.data);
            }
        });
    }

    function loadData(data) {
        laypage.render({
            elem: 'page',
            count: Math.ceil(studentCount.size()),
            limit: 10,
            layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
            jump: function(obj, first) {
                var start = (obj.curr - 1) * obj.limit; //获得数据 的数组起始下标
                var end = obj.curr * obj.limit - 1; //获得数据 的数组终点下标
                scoreHtml += '<tr>';
                for (var i = start; i < end && i < studentCount.size(); i++) {
                    scoreHtml += '<td>'+data[i].studentName+'</td>';
                    scoreHtml += '<td>'+data[i].studentNo+'</td>';
                    scoreHtml += '<td>'+data[i].className+'</td>';
                    var allScore = 0;
                    for (var j = 0; j < 2*examArr.length; j++){

                        if (j % 2 === 0){
                            var score = studentMap.get(data[i].student_id+"-"+examArr[j/2]);
                            if (typeof (score) === "undefined"){
                                score = 0;
                            }
                            scoreHtml += '<td>'+score+'</td>';
                            allScore += score;
                        } else {
                            var rank = rankMap.get(data[i].student_id+"-"+examArr[Math.floor(j/2)]);
                            scoreHtml += '<td>'+rank+'</td>';
                        }

                    }
                    scoreHtml+='<td>'+allScore+'</td>';
                    var allRank = rankMap.get(data[i].student_id);
                    scoreHtml+='<td>'+allRank+'</td>';
                    scoreHtml+='</tr>';
                }
                $('#scoreList').html(scoreHtml);
            }
        });
    }

    function goBack() {
        location.href="/html/score/gradeStatistics/list.jsp";
    }

    //创建map
    function Map(){
        this.container = new Object();
    }


    Map.prototype.put = function(key, value){
        this.container[key] = value;
    }


    Map.prototype.get = function(key){
        return this.container[key];
    }


    Map.prototype.keySet = function() {
        var keyset = new Array();
        var count = 0;
        for (var key in this.container) {
            // 跳过object的extend函数
            if (key == 'extend') {
                continue;
            }
            keyset[count] = key;
            count++;
        }
        return keyset;
    }


    Map.prototype.size = function() {
        var count = 0;
        for (var key in this.container) {
            // 跳过object的extend函数
            if (key == 'extend'){
                continue;
            }
            count++;
        }
        return count;
    }


    Map.prototype.remove = function(key) {
        delete this.container[key];
    }


    Map.prototype.toString = function(){
        var str = "";
        for (var i = 0, keys = this.keySet(), len = keys.length; i < len; i++) {
            str = str + keys[i] + "=" + this.container[keys[i]] + ";\n";
        }
        return str;
    }

</script>
</body>

</html>

