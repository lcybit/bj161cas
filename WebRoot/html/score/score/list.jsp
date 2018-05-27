<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>

<head>
<title>成绩信息列表</title>
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
      <shiro:hasPermission name="grade:save">
      <button class="layui-btn layui-bg-blue layui-btn-sm" id="importExcel">
        <i class="layui-icon">&#xe67c;</i>导入成绩
      </button>
      </shiro:hasPermission>
    </div>
    <table class="layui-table" id="test">
      <thead id="courseList">
      </thead>
      <tbody id="scoreList">
      </tbody>
    </table>
    <div id="page"></div>
  </div>
  <script>
    var layer = layui.layer;
    var laypage = layui.laypage;
    var table = layui.table;
    var upload = layui.upload;
    var form = layui.form;
    var courseId = sessionStorage.getItem('currentCourseId');
    var examId = sessionStorage.getItem('currentExamId');
    var html='<tr><th lay-data="{sort: true}">学号</th><th>姓名</th>';
    var courseArr = [];
    var scoreMap = new Map();
    var studentMap = new Map();
    var scoreHtml = '';
        $(document).ready(function() {
        $.ajax({
            async: false,
            type: 'GET',
            url: '/sr_course/listByCourseId/'+courseId,
            dataType: 'json',
            success: function(courseList) {
                for (var i = 0; i < courseList.length; i++){
                    html+="<th>"+courseList[i].sr_course_name+'</th>'
                    courseArr.push(courseList[i].id);
                }
                html+='<th>总分</th><shiro:hasPermission name="grade:update"><th>操作</th></shiro:hasPermission></tr>';
            }
        });
        $('#courseList').html(html)
        list();
        upload.render({
            elem: '#importExcel',
            url: '/score/import/'+examId+','+courseId,
            accept: 'file',
            acceptMime: '.xls,.xlsx',
            exts: 'xls|xlsx',
            done: function(result) {
                if (result.code === 200){
                    layer.msg(result.msg);
                    list();
                }
                else {
                    layer.alert(result.msg, {icon:2});
                }
            }
        });
    })
  
    function goModify(d) {
      sessionStorage.setItem('currentExamId', examId);
      sessionStorage.setItem('currentCourseId', courseId);
      sessionStorage.setItem('currentStudentId', d);
      layer.open({
        type: 2,
        id: 'modify',
        title: '修改成绩',
        content: '/html/score/score/modify.html',
        area: [ '500px', '380px' ],
        shade: 0.5,
        shadeClose: true,
        closeBtn: false,
        end: function() {
          list();
        }
      });
    }
  
    function remove(id) {
      layer.confirm('确认要删除吗？',
        {
          title: '删除考试信息'
        },
        function(index) {
          $.ajax({
            async: false,
            type: 'DELETE',
            url: '/exam/delete/' + id,
            success: function(result) {
                if (result.code === 200){
                    top.layer.msg(result.msg);
                    layer.close(index);
                    list();
                }
                else {
                    layer.alert(result.msg, {icon:2});
                }
            }
          });
        });
    }

    function goBack() {
        location.href="../exam/list.jsp"
    }
    function list() {
      $.ajax({
        async: false,
        type: 'GET',
        url: '/score/listByExamId/'+examId,
        success: function(scoreList) {
            for(var i = 0; i < scoreList.length; i++){
                studentMap.put(scoreList[i].student_id, scoreList);
            }
            loadData(studentMap);
        }
      });
    }

    function getStudentInfo(id) {
        $.ajax({
            async:false,
            type:'GET',
            url: '/student/find/'+id,
            success:function (result) {
                if(result.code === 200){
                    scoreHtml = '<tr>';
                    scoreHtml += '<td>'+result.data.student_no+'</td>';
                    scoreHtml += '<td>'+result.data.name+'</td>';
                }else {
                    scoreHtml = '<tr>';
                    scoreHtml += '<td>null</td>';
                    scoreHtml += '<td>null</td>';
                    layer.msg(result.msg);
                }

            },

        })
    }

    function loadData(data) {
      laypage.render({
        elem: 'page',
        count: Math.ceil(data.size()),
        limit: 10,
        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
        jump: function(obj, first) {
          var start = (obj.curr - 1) * obj.limit; //获得数据 的数组起始下标
          var end = obj.curr * obj.limit - 1; //获得数据 的数组终点下标
          var key = data.keySet();
          var html='';
          for (var i = start; i < end && i < key.length; i++) {
              getStudentInfo(key[i]);
              var length = data.get(key[i]).length;
              var list = data.get(key[i]);
              for (var j = start; j <= end*list.length && j < length; j++) {
                  scoreMap.put(list[j].sr_course_id+"-"+list[j].student_id, list[j].score);
              }
              var totalScore = 0;
              for(var j = 0; j < courseArr.length; j++){
                  var score = scoreMap.get(courseArr[j]+"-"+key[i]);
                  if (typeof (score) === "undefined")
                      score = 0;
                  scoreHtml+='<td lay-data="sort:true">'+score+'</td>';
                  totalScore += score;
              }
              scoreHtml+='<td>'+totalScore+'</td>';
              scoreHtml+='<shiro:hasPermission name="grade:update"><td><button class="layui-btn layui-bg-orange layui-btn-sm" onclick="goModify('+key[i]+')">修改</button></td></shiro:hasPermission>';
              scoreHtml+='</tr>';
              html+=scoreHtml;
          }
            $('#scoreList').html(html);
        }
      });
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
