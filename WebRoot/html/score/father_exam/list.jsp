<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<html>

<head>
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
      <!--<button class="layui-btn layui-bg-black layui-btn-sm" onclick="goBack()">-->
        <!--<i class="layui-icon">&#xe65c;</i>返回-->
      <!--</button>-->
      <shiro:hasPermission name="father:exam:save">
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
          <th>考试开始时间</th>
          <th>考试结束时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody id="father_examList">
      </tbody>
    </table>
    <div id="page"></div>
  </div>
  <script id="tmpl-father_examList" type="text/x-jsrender">
    <tr>
      <td>{{:no}}</td>
      <td>{{:name}}</td>
      <td>{{:begin_time}}</td>
      <td>{{:end_time}}</td>
      <td>
        <div class="layui-btn-group">
          <shiro:hasPermission name="father:exam:update">
          <button class="layui-btn layui-bg-orange layui-btn-sm" onclick="goModify('{{:id}}')">修改</button>
          </shiro:hasPermission>
          <shiro:hasPermission name="father:exam:delete">
          <button class="layui-btn layui-bg-red layui-btn-sm" onclick="remove('{{:id}}')">删除</button>
          </shiro:hasPermission>
            <button class="layui-btn layui-btn-sm layui-bg-blue" onclick="enter('{{:id}}','{{:name}}', '{{:no}}')">查看各科目考试</button>
            <button class="layui-btn layui-btn-sm layui-bg-blue" onclick="gradeEnter('{{:id}}','{{:name}}', '{{:no}}')">成绩统计</button>
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
  
    $(document).ready(function() {
      list();
//      upload.render({
//        elem: '#importExcel',
//        url: '/father_exam/import',
//        accept: 'file',
//        acceptMime: '.xls,.xlsx',
//        exts: 'xls|xlsx',
//        done: function(result) {
//          layer.msg('导入完毕！');
//          list();
//        }
//      });
    })

    function enter(id, name, no) {
        sessionStorage.setItem('currentFatherExamId', id);
        sessionStorage.setItem('currentFatherExamName', name);
        sessionStorage.setItem('currentFatherExamNo', no);
        location.href=('../exam/list.jsp');
    }

    function gradeEnter(id, name) {
        sessionStorage.setItem('currentFatherExamId', id);
        sessionStorage.setItem('currentFatherExamName', name);
        location.href='../gradeStatistics/list.jsp'
    }

    function goCreate() {
      layer.open({
        type: 2,
        id: 'create',
        title: '新增考试信息',
        content: '/html/score/father_exam/create.html',
        area: [ '500px', '400px' ],
        shade: 0.5,
        shadeClose: true,
        closeBtn: false,
        end: function() {
          list();
        }
      });
    }
  
    function goModify(id) {
      sessionStorage.setItem('currentFatherExamId', id);
      layer.open({
        type: 2,
        id: 'modify',
        title: '修改考试信息',
        content: '/html/score/father_exam/modify.html',
        area: [ '500px', '400px' ],
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
            url: '/father_exam/delete/' + id,
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
  
    function list() {
      $.ajax({
        async: false,
        type: 'GET',
        url: '/father_exam/list',
        dataType: 'json',
        success: function(father_examList) {
          loadData(father_examList);
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
            data[i].begin_time=formatTime(data[i].begin_time, "yyyy-MM-dd")
            data[i].end_time=formatTime(data[i].end_time, "yyyy-MM-dd")
            arr.push(data[i]);
          }
          $('#father_examList').html($.templates('#tmpl-father_examList').render(arr));
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
