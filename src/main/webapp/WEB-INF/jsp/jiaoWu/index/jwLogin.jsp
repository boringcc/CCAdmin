<%--
  Created by IntelliJ IDEA.
  User: CC
  Date: 2017-11-11
  Time: 14:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    pageContext.setAttribute("APP_PATH", request.getContextPath());
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires", 0);
%>

<html>
<head>
    <title>Title</title>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />

    <link rel="stylesheet" href="${APP_PATH}/static/login/bootstrap.min.css" />
    <link rel="stylesheet" href="${APP_PATH}/static/login/css/camera.css" />
    <link rel="stylesheet" href="${APP_PATH}/static/login/bootstrap-responsive.min.css" />
    <link rel="stylesheet" href="${APP_PATH}/static/login/matrix-login.css" />
    <link href="${APP_PATH}/static/login/font-awesome.css" rel="stylesheet" />
    <script type="text/javascript" src="${APP_PATH}/static/login/js/jquery-1.5.1.min.js"></script>

<style type="text/css">
    .cavs{
        z-index: 1;
        position: fixed;
        width: 95%;
        margin-left: 20px;
        margin-right: 20px;
    }
</style>

</head>
<body>
  <div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
      <input type="text" name="loginname" id="loginname" value="" placeholder="请输入用户名" />
      <input type="password" name="password" id="password" placeholder="请输入密码" class="keypad" keypadMode="full" allowKeyboard="true" value=""/>
      <a onclick="severCheck();" class="flip-link btn btn-info" id="to-recover">登录</a>
  </div>
</body>
<script type="text/javascript">
    //服务器校验
    function severCheck(){
        if(check()){
            var loginname = $("#loginname").val();
            var password = $("#password").val();
            var code = loginname+","+password;
            $.ajax({
                type: "POST",
                url: "/jiaowu/login",
                data: {KEYDATA:code,tm:new Date().getTime()},
                dataType: 'json',
                cache: false,
                success:function(data){
                    if("success" == data.result){
                        saveCookie();
                        window.location.href="/jiaowu/index";
                    }else if("usererror" == data.result){
                        $("#loginname").tips({
                            side : 1,
                            msg : "用户名或密码有误",
                            bg : '#FF5080',
                            time : 15
                        });
                        $("#loginname").focus();
                    }else if("codeerror" == data.result){
                        $("#code").tips({
                            side : 1,
                            msg : "验证码输入有误",
                            bg : '#FF5080',
                            time : 15
                        });
                        $("#code").focus();
                    }else{
                        $("#loginname").tips({
                            side : 1,
                            msg : "缺少参数",
                            bg : '#FF5080',
                            time : 15
                        });
                        $("#loginname").focus();
                    }
                }
            });
        }
    }

    //客户端校验
    function check(){

        if ($("#loginname").val() == "") {
            $("#loginname").tips({
                side : 2,
                msg : '用户名不得为空',
                bg : '#AE81FF',
                time : 3
            });
            $("#loginname").focus();
            return false;
        } else {
            $("#loginname").val(jQuery.trim($('#loginname').val()));
        }
        if ($("#password").val() == "") {
            $("#password").tips({
                side : 2,
                msg : '密码不得为空',
                bg : '#AE81FF',
                time : 3
            });
            $("#password").focus();
            return false;
        }
        $("#loginbox").tips({
            side : 1,
            msg : '正在登录 , 请稍后 ...',
            bg : '#68B500',
            time : 10
        });
        return true;
    }



    jQuery(function() {
        var loginname = $.cookie('loginname');
        var password = $.cookie('password');
        if (typeof(loginname) != "undefined"
            && typeof(password) != "undefined") {
            $("#loginname").val(loginname);
            $("#password").val(password);
            $("#saveid").attr("checked", true);
        }
    });

    function saveCookie() {
        if ($("#saveid").attr("checked")) {
            $.cookie('loginname', $("#loginname").val(), {
                expires : 7
            });
            $.cookie('password', $("#password").val(), {
                expires : 7
            });
        }
    }
</script>
        <script src="${APP_PATH}/static/login/js/bootstrap.min.js"></script>
        <script src="${APP_PATH}/static/js/jquery-1.7.2.js"></script>
        <script src="${APP_PATH}/static/login/js/jquery.easing.1.3.js"></script>
        <script src="${APP_PATH}/static/login/js/jquery.mobile.customized.min.js"></script>
        <script src="${APP_PATH}/static/login/js/camera.min.js"></script>
        <script src="${APP_PATH}/static/login/js/templatemo_script.js"></script>
        <script type="text/javascript" src="${APP_PATH}/static/js/jQuery.md5.js"></script>
        <script type="text/javascript" src="${APP_PATH}/static/js/jquery.tips.js"></script>
        <script type="text/javascript" src="${APP_PATH}/static/js/jquery.cookie.js"></script>

</body>
</html>
