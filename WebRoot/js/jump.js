/**
 * Created by TGL on 2018/5/14.
 */
var $, element;
layui.use(['form','element'], function() {
        $ = layui.jquery,
        element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
})

function addTabs(url){
    element.tabAdd("bodyTab", {
        title: "111",
        content: url,
        id: new Date().getTime()
    })
}

