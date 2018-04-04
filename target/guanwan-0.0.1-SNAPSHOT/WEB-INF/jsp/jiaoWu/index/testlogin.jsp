<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018-04-04
  Time: 23:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form   name= "myform" method = 'post'  action="/jiaowu/test"  >
    <table width="100%" border="0">
        <tr>
            <td><input type="text" value="" class="text2" name = "username" id = "username"/></td>
        </tr>
        <tr>
            <td><input type="password" value="" class="text2" name = "password" id = "password"/></td>
        </tr>
        <tr>
            <td>
                <input type="submit" value="提交" class="btn2"  />
            </td>
        </tr>
    </table>
</form>

</body>
</html>
