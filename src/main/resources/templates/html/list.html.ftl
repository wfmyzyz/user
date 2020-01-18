<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${table.entityPath}列表</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <link rel="stylesheet" href="../layuiadmin/layui/css/layui.css" media="all">
    <script src="../layuiadmin/layui/layui.js"></script>
    <script src="../layuiadmin/jquery-3.4.1.js"></script>
    <script src="config.js"></script>
</head>
<body>
    <div class="layui-fluid">
        <div id="head" style="margin-top: 10px" class="layui-form">
            <button class="layui-btn add-btn">添加</button>
            <button class="layui-btn layui-btn-danger remove-btn" style="margin-right: 20px">删除</button>
            <div class="layui-inline">
        <#list table.fields as field>
            <#if field.type != "timestamp">
                <#if field.propertyName != "tbStatus">
                    <div class="layui-inline">
                        <label class="layui-form-label">${field.comment}</label>
                        <div class="layui-input-inline">
                            <input type="text" id="search-${field.propertyName}"  placeholder='请输入${field.comment}' class="layui-input">
                        </div>
                    </div>
                    <#else>
                    <div class="layui-inline">
                        <label class="layui-form-label">状态：正常，删除</label>
                        <div class="layui-input-inline">
                            <select id="search-tbStatus" name="search-tbStatus" lay-verify="">
                                <option value="">请选择状态</option>
                                <option value="">全部</option>
                                <option value="正常">正常</option>
                                <option value="删除">删除</option>
                            </select>
                        </div>
                    </div>
                </#if>
            </#if>
        </#list>
                <div class="layui-inline">
                    <label class="layui-form-label">开始时间</label>
                    <div class="layui-input-inline">
                        <input type="text" id="search-start-time"   class="layui-input">
                    </div>
                </div>
                <div class="layui-inline">
                    <label class="layui-form-label">结束时间</label>
                    <div class="layui-input-inline">
                        <input type="text" id="search-end-time"   class="layui-input">
                    </div>
                </div>
                <button class="layui-btn search-result layui-btn-warm">搜索</button>
                <button class="layui-btn search-back layui-btn-normal">返回</button>
            </div>
        </div>
        <table id="table" lay-filter="table"></table>
    </div><!--layui-form-item-->
    <div id="box" style="padding:20px;display: none">
        <div class="layui-form">
    <#list table.fields as field>
        <#if field.keyFlag == true && field.keyIdentityFlag == true >
            <input type="hidden" id="result-${field.propertyName}" />
        </#if>
        <#if field.keyIdentityFlag == false && (field.propertyName != "createTime" && field.propertyName != "updateTime")>
            <#if field.propertyName != "tbStatus">
            <div class="layui-form-item">
                <label class="layui-form-label">${field.comment}</label>
                <div class="layui-input-block">
                    <input type="text" id="result-${field.propertyName}" name="${field.propertyName}"  placeholder='请输入${field.comment}' class="layui-input">
                </div>
            </div>
            <#else>
            <div class="layui-form-item">
                <label class="layui-form-label">状态</label>
                <div class="layui-input-block" id="result-tbStatus">
                    <input type="radio" name="tbStatus" value="正常" title="正常" checked>
                    <input type="radio" name="tbStatus" value="删除" title="删除">
                </div>
            </div>
            </#if>
        </#if>
    </#list>
            <div class="layui-form-item">
                <button class="layui-btn add-submit" style="float: right" data-opt="add">添加</button>
            </div>
        </div>
    </div>
    <script type="text/html" id="tools">
        <a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
        <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
    </script>
    <script>
        var articleBox;
        var finishPage;
        var indexPage;
        layui.use(['table','laydate','form'], function(){
            var table = layui.table;
            var laydate = layui.laydate;
            var form = layui.form;
            laydate.render({
                elem: '#search-start-time' //指定元素
                ,type: 'datetime'
            });
            laydate.render({
                elem: '#search-end-time' //指定元素
                ,type: 'datetime'
            });
    <#list table.fields as field>
        <#if field.type == "timestamp" && (field.propertyName != "createTime" && field.propertyName != "updateTime")>
            laydate.render({
                elem: '#result-${field.propertyName}' //指定元素
            });
        </#if>
    </#list>
            var tableIns =table.render({
                elem: '#table'
                ,url: backRoot+'/base/${table.entityPath}/get${table.entityName}List' //数据接口
                ,page: true //开启分页
                ,cols: [[ //表头
                    {checkbox: true}
                    <#list table.fields as field>
                    ,{field: '${field.propertyName}', title: '${field.comment}',<#if field.columnType == "INTEGER" || field.columnType == "LONG"
                     || field.columnType == "SHORT" || field.columnType == "FLOAT" || field.columnType == "DOUBLE" || field.columnType == "BIGDECIMAL">sort: true,</#if>align: "center"}
                    </#list>
                    , {title: '操作', align: "center", toolbar: '#tools'}
                ]],
                done: function (res, curr, count) {
                    if (count%10==0){
                        count++;
                    }
                    finishPage = Math.ceil(count/parseInt($(".layui-laypage-limits").find("option:selected").val()));
                    indexPage = curr;
                }
            });
            $(document).on("click",".search-result",function () {
                tableIns.reload({
                    where: { //设定异步数据接口的额外参数，任意设
                    <#list table.fields as field>
                        <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
                        ${field.propertyName}: $("#search-${field.propertyName}").val(),
                        </#if>
                    </#list>
                        startTime: $("#search-start-time").val(),
                        endTime: $("#search-end-time").val()
                    }
                })
            })
            $(document).on("click",".search-back",function () {
                tableIns.reload({
                    where: { //设定异步数据接口的额外参数，任意设
                    <#list table.fields as field>
                        <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
                        ${field.propertyName}: "",
                        </#if>
                    </#list>
                        startTime: "",
                        endTime: ""
                    }
                })
            })
            $(document).on("click",".add-btn",function () {
                clearForm();
                form.render();
                $(".add-submit").attr("data-opt","add").text("添加");
                articleBox = layer.open({
                    type: 1,
                    content: $('#box'),
                    area: ['60%', 'auto'],
                    title: "添加"
                });
            })
            table.on('tool(table)', function(obj) {
                var data = obj.data;
                if (obj.event === 'edit') {
                    $(".add-submit").attr("data-opt","edit").text("修改");
                    layer.load();
                    $.ajax({
                        type: "get",
                    <#list table.fields as field>
                        <#if field.keyFlag == true && field.keyIdentityFlag == true>
                        url: backRoot + "/base/${table.entityPath}/get/"+data.${field.propertyName},
                        </#if>
                    </#list>
                        success: function (result) {
                            layer.closeAll("loading");
                            if(result.code == 200){
                        <#list table.fields as field>
                            <#if field.propertyName != "createTime" && field.propertyName != "updateTime">
                                <#if field.propertyName != "tbStatus">
                                $("#result-${field.propertyName}").val(result.map.data.${field.propertyName});
                                </#if>
                            </#if>
                        </#list>
                                if (result.map.data.tbStatus == "正常"){
                                    $(':radio[name="tbStatus"][value="正常"]').prop("checked",true);
                                }else {
                                    $(':radio[name="tbStatus"][value="删除"]').prop("checked",true);
                                }
                                form.render();
                            }
                        }
                    })
                    articleBox = layer.open({
                        type: 1,
                        content: $('#box'),
                        area: ['60%', 'auto'],
                        title: "修改"
                    });
                }else if(obj.event === 'del'){
                    layer.confirm('是否删除?', function(index){
                        layer.load();
                        $.ajax({
                            type: "get",
                        <#list table.fields as field>
                            <#if field.keyFlag == true && field.keyIdentityFlag == true>
                            url: backRoot + "/base/${table.entityPath}/remove/"+data.${field.propertyName},
                            </#if>
                        </#list>
                            success: function (result) {
                                layer.closeAll("loading");
                                if(result.code == 200){
                                    layer.alert(result.map.data);
                                    tableIns.reload({
                                        page:{
                                            curr:1
                                        }
                                    });
                                    return false;
                                }
                                layer.alert(result.map.error);
                            }
                        })
                        layer.close(index);
                    });
                }
            });
            $(".add-submit").click(function () {
                if ($(this).attr("data-opt") == "add") {
                    layer.load();
                    $.ajax({
                        type: "post",
                        url: backRoot + "/base/${table.entityPath}/add",
                        data: {
                    <#list table.fields as field>
                        <#if field.keyIdentityFlag != true && field.propertyName != "createTime" && field.propertyName != "updateTime" && field.propertyName != "tbStatus">
                            ${field.propertyName}:$("#result-${field.propertyName}").val(),
                        </#if>
                    </#list>
                            tbStatus:$(':radio[name="tbStatus"]:checked').val()
                        },
                        success: function (result) {
                            layer.closeAll("loading");
                            if(result.code == 200){
                                layer.alert(result.map.data);
                                layer.close(articleBox);
                                tableIns.reload({
                                    page: {
                                        curr: finishPage
                                    }
                                });
                                return false;
                            }
                            if(result.map.error){
                                layer.alert(result.map.error);
                            }
                        }
                    })
                }
                if ($(this).attr("data-opt") == "edit") {
                    layer.load();
                    $.ajax({
                        type: "post",
                        url: backRoot + "/base/${table.entityPath}/update",
                        data: {
                    <#list table.fields as field>
                        <#if field.keyFlag == true && field.keyIdentityFlag == true>
                            ${field.propertyName}: $("#result-${field.propertyName}").val(),
                        </#if>
                        <#if field.keyIdentityFlag != true && field.propertyName != "createTime" && field.propertyName != "updateTime" && field.propertyName != "tbStatus">
                            ${field.propertyName}:$("#result-${field.propertyName}").val(),
                        </#if>
                    </#list>
                            tbStatus:$(':radio[name="tbStatus"]:checked').val()
                        },
                        success: function (result) {
                            layer.closeAll("loading");
                            if(result.code == 200){
                                layer.alert(result.map.data);
                                layer.close(articleBox);
                                tableIns.reload({
                                    page: {
                                        curr: indexPage
                                    }
                                });
                                return false;
                            }
                            if(result.map.error){
                                layer.alert(result.map.error);
                            }
                        }
                    })
                }
            })
            $(document).on("click",".remove-btn",function () {
                var checkStatus = table.checkStatus('table');
                if (checkStatus.data.length > 0) {
                    layer.confirm('是否删除已选中的?', function(index){
                        layer.load();
                        var arr = [];
                        for(var i = 0;i<checkStatus.data.length;i++){
                            arr.push(checkStatus.data[i]);
                        }
                        $.ajax({
                            type: "post",
                            url: backRoot + "/base/${table.entityPath}/remove",
                            contentType: "application/json; charset=utf-8",
                            data: JSON.stringify(arr),
                            success: function (result) {
                                layer.closeAll("loading");
                                if (result.code == 200) {
                                    layer.alert(result.map.data);
                                    tableIns.reload({
                                        page:{
                                            curr:1
                                        }
                                    });
                                    return false;
                                }
                                layer.alert(result.map.error);
                            }
                        })
                        layer.close(index);
                    })
                }
            })
        });
    </script>
    <script>
        function clearForm() {
        <#list table.fields as field>
            <#if field.propertyName != "createTime" && field.propertyName != "updateTime" && field.propertyName != "tbStatus">
            $("#result-${field.propertyName}").val("");
            </#if>
        </#list>
            $(':radio[name="tbStatus"][value="正常"]').prop("checked",true);
        }
    </script>
</body>
</html>