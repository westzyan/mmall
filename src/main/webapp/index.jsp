<%--
  Created by IntelliJ IDEA.
  User: ubuntu2
  Date: 19-3-4
  Time: 下午9:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<H1>tomcat22222222222222222222222</H1>
hello world！

springmvc上传文件
<form name="form1" action="/mmall_war/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件"/>
</form>

富文本图片上传
<form name="form1" action="/mmall_war/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name="upload_file"/>
    <input type="submit" value="上传文件"/>
</form>
 </body>
</html>
