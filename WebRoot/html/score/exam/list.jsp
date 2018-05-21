<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<title>考试信息列表</title>
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
      <shiro:hasPermission name="exam:info:save">
      <button class="layui-btn layui-bg-green layui-btn-sm" onclick="goCreate()">
        <i class="layui-icon">&#xe654;</i>新增
      </button>
      </shiro:hasPermission>
      <!--<button class="layui-btn layui-bg-blue layui-btn-sm" id="importExcel">-->
        <!--<i class="layui-icon">&#xe67c;</i>导入-->
      <!--</button>-->
    </div>
    <table class="layui-table" id="test">
      <thead>
        <tr>
          <th>考试场次</th>
          <th>考试名称</th>
          <th>考试时间</th>
          <th>考试科目</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody id="examList">
      </tbody>
    </table>
    <div id="page"></div>
  </div>
  <script id="tmpl-examList" type="text/x-jsrender">
    <tr>
      <td>{{:exam_no}}</td>
      <td>{{:exam_name}}</td>
      <td>{{:exam_time}}</td>
      <td>{{:course_name}}</td>
      <td>
        <div class="layui-btn-group">
          <shiro:hasPermission name="exam:info:update">
            <button class="layui-btn layui-bg-orange layui-btn-sm" onclick="goModify('{{:id}}')">修改</button>
          </shiro:hasPermission>
          <shiro:hasPermission name="exam:info:delete">
            <button class="layui-btn layui-bg-red layui-btn-sm" onclick="remove('{{:id}}')">删除</button>
          </shiro:hasPermission>
          <button class="layui-btn layui-bg-blue layui-btn-sm" onclick="enter('{{:id}}', '{{:course_id}}')">查看成绩</button>
        </div>
      </td>
    </tr>
  </script>
  <script>
    var layer = layui.layer;
    var laypage = layui.laypage;
    var table = layui.table;
    var upload = layui.upload;
    var form = layui.form;
    var examId = sessionStorage.getItem('currentFatherExamId');
    var examName = sessionStorage.getItem('currentFatherExamName');
    var examNo = sessionStorage.getItem('currentFatherExamNo');

    $(document).ready(function() {
        list();
    })
  
    function goCreate() {
      sessionStorage.setItem('currentFatherExamId', examId);
      layer.open({
        type: 2,
        id: 'create',
        title: '新增考试信息',
        content: '/html/score/exam/create.html',
        area: [ '500px', '380px' ],
        shade: 0.5,
        shadeClose: true,
        closeBtn: false,
        end: function() {
          list();
        }
      });
    }
  
    function goModify(id) {
      sessionStorage.setItem('currentExamId', id);
      layer.open({
        type: 2,
        id: 'modify',
        title: '修改考试信息',
        content: '/html/score/exam/modify.html',
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

    //查看成绩
    function enter(id, course_id) {
        sessionStorage.setItem('currentExamId', id);
        sessionStorage.setItem('currentCourseId', course_id);
        location.href="../score/list.jsp"
    }

    function goBack() {
        location.href="../father_exam/list.jsp"
    }
    function list() {
      $.ajax({
        async: false,
        type: 'GET',
        url: '/exam/list/'+examId,
        dataType: 'json',
        success: function(examList) {
          loadData(examList);
        }
      });
    }
  
    function loadData(data) {
      laypage.render({
        elem: 'page',
        count: Math.ceil(data.length),
        limit: 10,
        layout: ['count', 'prev', 'page', 'next', 'limit', 'skip'],
        jump: function(obj, first) {
          var start = (obj.curr - 1) * obj.limit; //获得数据 的数组起始下标
          var end = obj.curr * obj.limit - 1; //获得数据 的数组终点下标
          var arr = []; //存放该页展示的数据 
          for (var i = start; i <= end && i < data.length; i++) {
            data[i].exam_time = formatTime(data[i].exam_time, "yyyy-MM-dd hh:mm:ss");
            data[i].exam_name = examName;
            data[i].exam_no = examNo;
            arr.push(data[i]);
          }
          $('#examList').html($.templates('#tmpl-examList').render(arr));
        }
      });
    }
    //格式化时间
    function formatTime(datetime,fmt){
        if (parseInt(datetime)==datetime) {
            if (datetime.length==10) {
                datetime=parseInt(datetime)*1000;
            } else if(datetime.length==13) {
                datetime=parseInt(datetime);
            }
        }
        datetime=new Date(datetime);
        var o = {
            "M+" : datetime.getMonth()+1,                 //月份
            "d+" : datetime.getDate(),                    //日
            "h+" : datetime.getHours(),                   //小时
            "m+" : datetime.getMinutes(),                 //分
            "s+" : datetime.getSeconds(),                 //秒
            "q+" : Math.floor((datetime.getMonth()+3)/3), //季度
            "S"  : datetime.getMilliseconds()             //毫秒
        };
        if(/(y+)/.test(fmt))
            fmt=fmt.replace(RegExp.$1, (datetime.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)
            if(new RegExp("("+ k +")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        return fmt;
    }

  </script>
</body>

</html>
