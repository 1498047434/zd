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
  
  //用户端土地管理
  table.render({
    elem: '#LAY-app-land-list'
    ,url: layui.setter.reqUrl + '/land/listByPageForUser' //模拟接口
    ,headers: {
      'user_access_token': layui.data(setter.tableName).user_access_token
    }
    ,where: {
      'isCollect': -1  //无要求
    }
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
  
  // //执行实例【文件上传】
  // uploadInst = upload.render({
  //   elem: '#homework-file' //绑定元素
  //   ,url: layui.setter.reqUrl + '/homework/upload' //上传接口
  //   ,accept: 'file' //允许上传的文件类型
  //   ,done: function(res){
  //     if(res.code == 0){
  //       //上传完毕回调
  //       admin.req({
  //         url: layui.setter.reqUrl + '/homework/submitHomework' //实际使用请改成服务端真实接口
  //         ,type: 'post'
  //         ,data: {
  //           id: submit_homework_data.id
  //           ,homeworkId: submit_homework_data.homeworkId
  //           ,clazzId: submit_homework_data.clazzId
  //           ,filePath: submit_homework_data.filePath //老师作业目录
  //           ,studentFileName: res.data.fileName
  //           ,studentFileRandomName: res.data.fileRandomName
  //           ,tmpPath: res.data.tmpPath
  //         }
  //         ,done: function(res){
  //           layer.msg('上传完成！');
  //         }
  //       });

  //     }else{
  //       //请求异常回调
  //       layer.msg('上传文件失败，请务必重新上传文件！');
  //     }
  //   }
  //   ,error: function(){
  //     //请求异常回调
  //     layer.msg('上传文件失败，请务必重新上传文件！');
  //   }
  // });


  //用户端【土地】监听工具条
  table.on('tool(LAY-app-land-list)', function(obj){
    var data = obj.data;
    if(obj.event === 'takeBid'){
      json = JSON.stringify(data)
      layer.open({
        type: 2
        ,title: '参与竞价'
        ,content: '../../../views/userview/land/takeBid.html?id='+ data.id
        ,maxmin: true
        ,area: ['550px', '220px']
        ,btn: ['确定']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submit = layero.find('iframe').contents().find("#layuiadmin-app-form-edit");
          //监听提交
          iframeWindow.layui.form.on('submit(layuiadmin-app-form-edit)', function(data){
            var field = data.field; //获取提交的字段
            
            if(field.bidPrice <= field.oldBidPrice){
              return layer.msg('请输入大于的价格大于目前竞价价格');
            }
            admin.req({
              url: layui.setter.reqUrl + '/land/bidding' //实际使用请改成服务端真实接口
              ,data: field
              ,type: 'post'
              ,done: function(res){
                layer.close(index);//再执行关闭
                layer.msg('竞价成功');
                layui.table.reload('LAY-app-land-list'); //重载表格
              }
            });   
            // layer.close(index); //关闭弹层
          });  
          
          submit.trigger('click');
        }
      });
    }else if(obj.event === 'uoload-agreement'){//上传合同

      $('#agreement-file').click();
      submit_agreement_data = data;
      console.log(submit_agreement_data)

    }else if(obj.event === 'appointment'){//提交预约
      json = JSON.stringify(data)
      layer.open({
        type: 2
        ,title: '参与竞价'
        ,content: '../../../views/userview/land/appointment.html?id='+ data.id
        ,maxmin: true
        ,area: ['550px', '520px']
        ,btn: ['确定']
        ,yes: function(index, layero){
          var iframeWindow = window['layui-layer-iframe'+ index]
          ,submit = layero.find('iframe').contents().find("#layuiadmin-app-form-edit");
          //监听提交
          iframeWindow.layui.form.on('submit(layuiadmin-app-form-edit)', function(data){
            var field = data.field; //获取提交的字段
            
            if(field.bidPrice <= field.oldBidPrice){
              return layer.msg('请输入大于的价格大于目前竞价价格');
            }
            admin.req({
              url: layui.setter.reqUrl + '/appointment/add' //实际使用请改成服务端真实接口
              ,data: field
              ,type: 'post'
              ,done: function(res){
                layer.close(index);//再执行关闭
                layer.msg('预约成功');
                // layui.table.reload('LAY-app-land-list'); //重载表格
              }
            });   
            // layer.close(index); //关闭弹层
          });  
          
          submit.trigger('click');
        }
      });

    }else if(obj.event === 'report'){//提交预约
      admin.req({
        url: layui.setter.reqUrl + '/report/add' //实际使用请改成服务端真实接口
        ,data: {
          landId: data.id
          ,landAddress: data.address
          ,landClassify: data.classify
          ,adminId: data.adminId
          ,adminName: data.adminName
        }
        ,type: 'post'
        ,done: function(res){
          layer.msg('举报成功');
          // layui.table.reload('LAY-app-land-list'); //重载表格
        }
      });   

    }else if(obj.event === 'addCollect'){//提交预约
      admin.req({
        url: layui.setter.reqUrl + '/collect/add' //实际使用请改成服务端真实接口
        ,data: {
          landId: data.id
        }
        ,type: 'post'
        ,done: function(res){
          layer.msg('收藏成功');
          layui.table.reload('LAY-app-land-list'); //重载表格
        }
      });   

    }else if(obj.event === 'cancelCollect'){//提交预约
      admin.req({
        url: layui.setter.reqUrl + '/collect/delByLandIdAndUserId' //实际使用请改成服务端真实接口
        ,data: {
          landId: data.id
        }
        ,type: 'post'
        ,done: function(res){
          layer.msg('取消收藏成功');
          layui.table.reload('LAY-app-land-list'); //重载表格
        }
      });   

    }

  });

  //用户端【举报】管理
  table.render({
    elem: '#LAY-app-report-list'
    ,url: layui.setter.reqUrl + '/report/listByPageForUser' //模拟接口
    ,headers: {
      'user_access_token': layui.data(setter.tableName).user_access_token
    }
    ,cols: [[
      {type: 'checkbox', fixed: 'left'}
      ,{field: 'id', minWidth: 50, title: 'ID', align: 'center'}//隐藏
      ,{field: 'landClassify', minWidth: 70, title: '分类',templet: '#buttonTpl-classify' , align: 'center'}
      ,{field: 'landAddress', title: '土地地址', minWidth: 290, align: 'center'}
      ,{field: 'userName', title: '投诉人', minWidth: 100, align: 'center'}
      ,{field: 'adminName', title: '土地所属管理员', minWidth: 90, align: 'center'} //隐藏
      ,{field: 'state', title: '投诉状态',templet: '#buttonTpl-bidUserName', minWidth: 100 , align: 'center'}
      // ,{title: '操作', minWidth: 330, align: 'center', fixed: 'right', templet: '#table-content-list', toolbar: '#table-content-list'}
    ]]
    ,page: true
    ,limit: 10
    ,limits: [10, 15, 20, 25, 30]
    ,text: '对不起，加载出现异常！'
  });

    //用户端【预约】管理
    table.render({
      elem: '#LAY-app-appointment-list'
      ,url: layui.setter.reqUrl + '/appointment/listByPageForUser' //模拟接口
      ,headers: {
        'user_access_token': layui.data(setter.tableName).user_access_token
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
        // ,{title: '操作', minWidth: 330, align: 'center', fixed: 'right', templet: '#table-content-list', toolbar: '#table-content-list'}
      ]]
      ,page: true
      ,limit: 10
      ,limits: [10, 15, 20, 25, 30]
      ,text: '对不起，加载出现异常！'
    });

  exports('userlist', {})
});