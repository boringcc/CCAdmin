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
<script>
    var timer;
    var fhi = 1;
    var current = 0;
    var current2 = 1;
    function showfh() {
        fhi = 1;
        //setInterval 按照指定的时间周期性执行函数
        timer = setInterval(xzfh,10);
    };
    //transform 可以让元素进行旋转等操作，rotate + 数值 + deg(度数) 进行多少度的旋转，实现抖动效果
    function xzfh() {
        if(fhi>50){
            document.body.style.transform = 'rotate(0deg)';
            clearInterval(timer);
            return;
        }
        current = (current2)%360;
        document.body.style.transform = 'rotate(' + current + 'deg)';
        current ++;
        if(current2 == 1){current2 = -1;}
        else{current2 = 1 ;}
        fhi++;
    };
</script>


</head>
<body>

  <canvas class="cavs"></canvas>
  <div style="width:100%;text-align: center;margin: 0 auto;position: absolute;">
    <!--登录窗口 -->
     <div id="window-login">
        <div id="loginbox">
            <form action="" method="post" name="loginForm" id="loginForm">
                <div class="control-group normal_text">
                    <h3>
                        <img src="static/login/logo.png" alt="Logo" />
                    </h3>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="main_input_box">
							<span class="add-on bg_lg">
							<i><img height="37" src="static/login/user.png" /></i>
							</span><input type="text" name="loginname" id="loginname" value="" placeholder="请输入用户名" />
                        </div>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <div class="main_input_box">
							<span class="add-on bg_ly">
							<i><img height="37" src="static/login/suo.png" /></i>
							</span><input type="password" name="password" id="password" placeholder="请输入密码" class="keypad" keypadMode="full" allowKeyboard="true" value=""/>
                        </div>
                    </div>
                </div>
                <div style="float:right;padding-right:10%;">
                    <div style="float: left;margin-top:3px;margin-right:2px;">
                        <font color="white">记住密码</font>
                    </div>
                    <div style="float: left;">
                        <input name="form-field-checkbox" id="saveid" type="checkbox"
                               onclick="savePaw();" style="padding-top:0px;" />
                    </div>
                </div>
                <div class="form-actions">
                    <div style="width:86%;padding-left:8%;" class="">
                        <div style="float: left;padding-top: 2px;" class="pull-right">
                            <a href="/jiaowu/test" class="btn btn-success">教务管理登录</a>
                        </div>
                        <!--
                        <div style="float: left;padding-top:2px;">
                            <i><img src="static/login/yan.png" /></i>
                        </div>
                        <div style="float: left;" class="codediv">
                            <input type="text" name="code" id="code" class="login_code"
                                   style="height:16px; padding-top:4px;" />
                        </div>
                        <div style="float: left;">
                            <i><img style="height:22px;" id="codeImg" alt="点击更换" title="点击更换" src="" /></i>
                        </div>
                        -->

                        <span class="pull-right" style="padding-right:3%;"><a href="javascript:changepage(1);" class="btn btn-success">注册</a></span>

                        <span class="pull-right"><a onclick="severCheck();" class="flip-link btn btn-info" id="to-recover">登录</a></span>
                    </div>
                </div>
            </form>
        </div>
     </div>
     <!--注册窗口 -->
     <div id="window-register" style="display: none;">
         <div id="loginbox">
             <form action="" method="post" name="loginForm" id="loginForm">
                 <div class="control-group normal_text">
                     <h3>
                         <img src="${APP_PATH}/static/login/logo.png" alt="Logo" />
                     </h3>
                 </div>
                 <div class="control-group">
                     <div class="controls">
                         <div class="main_input_box">
							<span class="add-on bg_lg">
							<i>用户</i>
							</span><input type="text" name="USERNAME" id="USERNAME" value="" placeholder="请输入用户名" />
                         </div>
                     </div>
                 </div>
                 <div class="control-group">
                     <div class="controls">
                         <div class="main_input_box">
							<span class="add-on bg_ly">
							<i>密码</i>
							</span><input type="password" name="PASSWORD" id="PASSWORD" placeholder="请输入密码" class="keypad" keypadMode="full" allowKeyboard="true" value=""/>
                         </div>
                     </div>
                 </div>
                 <div class="control-group">
                     <div class="controls">
                         <div class="main_input_box">
							<span class="add-on bg_ly">
							<i>重输</i>
							</span><input type="password" name="chkpwd" id="chkpwd" placeholder="请重新输入密码" class="keypad" keypadMode="full" allowKeyboard="true" value=""/>
                         </div>
                     </div>
                 </div>
                 <div class="control-group">
                     <div class="controls">
                         <div class="main_input_box">
							<span class="add-on bg_lg">
							<i>姓名</i>
							</span><input type="text" name="NAME" id="name" value="" placeholder="请输入姓名" />
                         </div>
                     </div>
                 </div>
                 <div class="control-group">
                     <div class="controls">
                         <div class="main_input_box">
							<span class="add-on bg_lg">
							<i>邮箱</i>
							</span><input type="text" name="EMAIL" id="EMAIL" value="" placeholder="请输入邮箱" />
                         </div>
                     </div>
                 </div>
                 <div class="form-actions">
                     <div style="width:86%;padding-left:8%;">
                         <!--
                         <div style="float: left;padding-top:2px;">
                             <i><img src="${APP_PATH}/static/login/yan.png" /></i>
                         </div>
                         <div style="float: left;" class="codediv">
                             <input type="text" name="rcode" id="rcode" class="login_code"
                                    style="height:16px; padding-top:4px;" />
                         </div>
                         <div style="float: left;">
                             <i><img style="height:22px;" id="zcodeImg" alt="点击更换" title="点击更换" src="" /></i>
                         </div>
                         -->
                         <span class="pull-right" style="padding-right:3%;"><a href="javascript:changepage(2);" class="btn btn-success">取消</a></span>
                         <span class="pull-right"><a onclick="register()" class="flip-link btn btn-info" id="to-recover">提交</a></span>
                     </div>
                 </div>
             </form>

         </div>
     </div>
  </div>
  <!--背景图片 -->
  <div id="templatemo_banner_slide" class="container_wapper">
      <div class="camera_wrap camera_emboss" id="camera_slide">
          <!-- 背景图片 -->
          <c:choose>
              <c:when test="${not empty pd.listImg}">
                  <c:forEach items="${pd.listImg}" var="var" varStatus="vs">
                      <div data-src="static/login/images/${var.FILEPATH }"></div>
                  </c:forEach>
              </c:when>
              <c:otherwise>
                  <div data-src="static/login/images/banner_slide_01.jpg"></div>
                  <div data-src="static/login/images/banner_slide_02.jpg"></div>
                  <div data-src="static/login/images/banner_slide_03.jpg"></div>
                  <div data-src="static/login/images/banner_slide_04.jpg"></div>
                  <div data-src="static/login/images/banner_slide_05.jpg"></div>
              </c:otherwise>
          </c:choose>
      </div>
  </div>







<script type="text/javascript">
    //服务器校验
    function severCheck(){
        if(check()){
            var loginname = $("#loginname").val();
            var password = $("#password").val();
            var code = loginname+","+password;
            $.ajax({
                type: "POST",
                url: "/login_login",
                data: {KEYDATA:code},
                dataType: 'json',
                cache: false,
                success:function(data){
                    if("success" == data.extend.result){
                        saveCookie();
                        window.location.href = "main/index";
                    }else if("usererror" == data.extend.result){
                        $("#loginname").tips({
                            side : 1,
                            msg : "用户名或密码不正确",
                            bg : 'FF5080',
                            time : 15
                        });
                        showfh();
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
            showfh();
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
            showfh();
            $("#password").focus();
            return false;
        }
        //以后加入验证码
//        if ($("#code").val() == "") {
//            $("#code").tips({
//                side : 1,
//                msg : '验证码不得为空',
//                bg : '#AE81FF',
//                time : 3
//            });
//            showfh();
//            $("#code").focus();
//            return false;
//        }
        $("#loginbox").tips({
            side : 1,
            msg : '正在登录 , 请稍后 ...',
            bg : '#68B500',
            time : 10
        });
        return true;
    }
    //登录注册窗口切换
    function changepage(value) {
        if(value == 1){
            $("#window-login").hide();
            $("#window-register").show();
        }else{
            $("#window-register").hide();
            $("#window-login").show();
        }
    }

    <!--记住用户名和密码-->
    function savePaw() {
        if(!$("#saveid").attr("checked")){
            $.cookie('loginname', $("#loginname").val(),{
               expires : 7
            });
            $.cookie('password', $("#password").val(),{
                expires : 7
            });
        }
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


    //提交注册
    function register(){
        if(rcheck()){
            var nowtime = date2str(new Date(),"yyyyMMdd");
            $.ajax({
                type: "POST",
                url: "appSysUser/registerSysUser",
                data: {USERNAME:$("#USERNAME").val(),PASSWORD:$("#PASSWORD").val(),NAME:$("#name").val(),EMAIL:$("#EMAIL").val(),tm:new Date().getTime()},
                dataType:'json',
                cache: false,
                success: function(data){
                    if("00" == data.extend.result){
                        alert(1);
                        $("#loginbox").tips({
                            side : 1,
                            msg : '注册成功,请登录',
                            bg : '#68B500',
                            time : 3
                        });

                    }else if("04" == data.extend.result){
                        $("#USERNAME").tips({
                            side : 1,
                            msg : "用户名已存在",
                            bg : '#FF5080',
                            time : 15
                        });
                        showfh();
                        $("#USERNAME").focus();
                    }
                }

            });
        }
    }


    //注册
    function rcheck(){
        if($("#USERNAME").val()==""){
            $("#USERNAME").tips({
                side:3,
                msg:'输入用户名',
                bg:'#AE81FF',
                time:2
            });
            $("#USERNAME").focus();
            $("#USERNAME").val('');
            return false;
        }else{
            $("#USERNAME").val(jQuery.trim($('#USERNAME').val()));
        }
        if($("#PASSWORD").val()==""){
            $("#PASSWORD").tips({
                side:3,
                msg:'输入密码',
                bg:'#AE81FF',
                time:2
            });
            $("#PASSWORD").focus();
            return false;
        }
        if($("#PASSWORD").val()!=$("#chkpwd").val()){
            $("#chkpwd").tips({
                side:3,
                msg:'两次密码不相同',
                bg:'#AE81FF',
                time:3
            });
            $("#chkpwd").focus();
            return false;
        }
        if($("#name").val()==""){
            $("#name").tips({
                side:3,
                msg:'输入姓名',
                bg:'#AE81FF',
                time:3
            });
            $("#name").focus();
            return false;
        }
        if($("#EMAIL").val()==""){
            $("#EMAIL").tips({
                side:3,
                msg:'输入邮箱',
                bg:'#AE81FF',
                time:3
            });
            $("#EMAIL").focus();
            return false;
        }else if(!ismail($("#EMAIL").val())){
            $("#EMAIL").tips({
                side:3,
                msg:'邮箱格式不正确',
                bg:'#AE81FF',
                time:3
            });
            $("#EMAIL").focus();
            return false;
        }
        return true;
    }

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

    //邮箱格式校验
    function ismail(mail){
        return(new RegExp(/^(?:[a-zA-Z0-9]+[_\-\+\.]?)*[a-zA-Z0-9]+@(?:([a-zA-Z0-9]+[_\-]?)*[a-zA-Z0-9]+\.)+([a-zA-Z]{2,})+$/).test(mail));
    }
    //js  日期格式
    function date2str(x,y) {
        var z ={y:x.getFullYear(),M:x.getMonth()+1,d:x.getDate(),h:x.getHours(),m:x.getMinutes(),s:x.getSeconds()};
        return y.replace(/(y+|M+|d+|h+|m+|s+)/g,function(v) {return ((v.length>1?"0":"")+eval('z.'+v.slice(-1))).slice(-(v.length>2?v.length:2))});
    };


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
