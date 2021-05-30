/**

 @Name：layuiAdmin 内容系统
 @Author：star1029
 
 @License：LPPL
    
 */

 layui.define(['table', 'form'], function(exports){
  var $ = layui.$
  ,table = layui.table
  ,admin = layui.admin
  ,upload = layui.upload
  ,setter = layui.setter
  ,form = layui.form;
  
  //管理端土地管理
  table.render({
    elem: '#LAY-app-land-list'
    ,url: layui.setter.reqUrl + '/land/listByPageForAdmin' //模拟接口
    ,headers: {
      'admin_access_token': layui.data(setter.tableName).admin_access_token
    }
    // ,where: {
    //   'isCollect': -1  //无要求
    // }
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', width: 50, title: 'ID', align: 'center'}//隐藏
      ,{field: 'address', minWidth: 260, title: '地址', align: 'center'}
      ,{field: 'classify', width: 70, title: '分类',templet: '#buttonTpl-classify' , align: 'center'}
      ,{field: 'area', title: '面积(亩)', width: 90, align: 'center'}
      ,{field: 'price', title: '价格(元/亩)', width: 100, align: 'center'}
      ,{field: 'contractPeriod', title: '租期(年)', width: 90, align: 'center'} //隐藏
      ,{field: 'introduction', title: '简介', minWidth: 260, align: 'center'}
      ,{field: 'adminName', title: '管理员', width: 90, align: 'center'}
      ,{field: 'bidPrice', title: '竞价(元/亩)',templet: '#buttonTpl-bidPrice', width: 100 , align: 'center'}
      ,{field: 'bidUserName', title: '最高竞价者',templet: '#buttonTpl-bidUserName', width: 100 , align: 'center'}
      ,{title: '操作', minWidth: 330, align: 'center', fixed: 'right', templet: '#table-content-list', toolbar: '#table-content-list'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });
  



  //管理端土地监听工具条
  table.on('tool(LAY-app-land-list)', function(obj){
    var data = obj.data;

    if(obj.event === 'end'){
      admin.req({
        url: layui.setter.reqUrl + '/land/isBided' //实际使用请改成服务端真实接口
        ,data: data
        ,type: 'post'
        ,done: function(res){
          layui.table.reload('LAY-app-land-list'); //重载表格
        }
      });
    }else if(obj.event === 'edit'){
      json = JSON.stringify(data)
      layer.open({
        type: 2
        ,title: '编辑班级'
        ,content: '../../../views/admin/land/listform.html?id='+ data.id
        ,maxmin: true
        ,area: ['550px', '550px']
        ,btn: ['确定', '取消']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submit = layero.find('iframe').contents().find("#layuiadmin-app-form-edit");
          //监听提交
          iframeWindow.layui.form.on('submit(layuiadmin-app-form-edit)', function(data){
            var field = data.field; //获取提交的字段
            

            admin.req({
              url: layui.setter.reqUrl + '/land/update' //实际使用请改成服务端真实接口
              ,data: field
              ,type: 'post'
              ,done: function(res){

                layui.table.reload('LAY-app-land-list'); //重载表格

                layer.msg('已更新');
              }
            });        
            form.render();
            layer.close(index); //关闭弹层
          });  
          
          submit.trigger('click');
        }
      });
    }

  });

  //管理端【举报】管理
  table.render({
    elem: '#LAY-app-report-list'
    ,url: layui.setter.reqUrl + '/report/listByPageForAdmin' //模拟接口
    ,headers: {
      'admin_access_token': layui.data(setter.tableName).admin_access_token
    }
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', minWidth: 50, title: 'ID', align: 'center'}//隐藏
      ,{field: 'landClassify', minWidth: 70, title: '分类',templet: '#buttonTpl-classify' , align: 'center'}
      ,{field: 'landAddress', title: '土地地址', minWidth: 290, align: 'center'}
      ,{field: 'userName', title: '投诉人', minWidth: 100, align: 'center'}
      ,{field: 'adminName', title: '土地所属管理员', minWidth: 90, align: 'center'} //隐藏
      ,{field: 'state', title: '投诉状态',templet: '#buttonTpl-bidUserName', minWidth: 100 , align: 'center'}
      ,{title: '操作', minWidth: 330, align: 'center', fixed: 'right', templet: '#table-content-list', toolbar: '#table-content-list'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });

  
  //管理端【举报】监听工具条
  table.on('tool(LAY-app-report-list)', function(obj){
    var data = obj.data;
    if(obj.event === 'sure'){
      admin.req({
        url: layui.setter.reqUrl + '/report/update' //实际使用请改成服务端真实接口
        ,data: data
        ,type: 'post'
        ,done: function(res){
          layui.table.reload('LAY-app-report-list'); //重载表格
        }
      });
    }

  });

  //用户端【预约】管理
  table.render({
    elem: '#LAY-app-appointment-list'
    ,url: layui.setter.reqUrl + '/appointment/listByPageForAdmin' //模拟接口
    ,headers: {
      'admin_access_token': layui.data(setter.tableName).admin_access_token
    }
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', minWidth: 50, title: 'ID', align: 'center'}
      ,{field: 'landClassify', minWidth: 70, title: '分类',templet: '#buttonTpl-classify' , align: 'center'}
      ,{field: 'landAddress', title: '土地地址', minWidth: 290, align: 'center'}
      ,{field: 'userName', title: '预约用户', minWidth: 100, align: 'center'}
      ,{field: 'userTel', title: '用户电话', minWidth: 100, align: 'center'}
      ,{field: 'adminName', title: '土地所属管理员', minWidth: 90, align: 'center'} 
      ,{field: 'appointedTime', title: '预约时间', minWidth: 190, align: 'center'} 
      ,{field: 'appointedAddress', title: '预约地点', minWidth: 90, align: 'center'} 
      ,{field: 'state', title: '预约状态',templet: '#buttonTpl-bidUserName', minWidth: 100 , align: 'center'}
      ,{title: '操作', minWidth: 330, align: 'center', fixed: 'right', templet: '#table-content-list', toolbar: '#table-content-list'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });
  //管理端【预约】监听工具条
  table.on('tool(LAY-app-appointment-list)', function(obj){
    var data = obj.data;
    if(obj.event === 'sure'){
      admin.req({
        url: layui.setter.reqUrl + '/appointment/update' //实际使用请改成服务端真实接口
        ,data: data
        ,type: 'post'
        ,done: function(res){
          layui.table.reload('LAY-app-appointment-list'); //重载表格
        }
      });
    }

  });

  exports('adminlist', {})
});