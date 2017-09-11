<%@ page language="java"  contentType="text/html; charset=UTF-8" %>

<html>
<body>
<h2>Hello World!</h2>

<h2>springmvc上传文件</h2>

<form action="/manage/product/upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name = "upload_file" >
    <input type="submit" value="spingmvc上传文件">
</form>

<h2>springmvc上传文件</h2>

<form action="/manage/product/richtext_img_upload.do" method="post" enctype="multipart/form-data">
    <input type="file" name = "upload_file" >
    <input type="submit" value="spingmvc富文本上传文件">
</form>
</body>
</html>
