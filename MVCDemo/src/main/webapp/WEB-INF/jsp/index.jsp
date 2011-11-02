<%@page language="java"
	contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c"
	  uri="http://java.sun.com/jsp/jstl/core"%>


<html>
  <head>
    <title>Hello World</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script src="../script/jquery-1.6.4.min.js"></script>
    <script src="../script/script.js"></script>
  </head>
  <body>
    <h2>Message的内容为:${message}</h2>
    <div>
      <a href="./path_var/9/hello.do">PathVariable: {id=9, word=hello}</a>
    </div>
    <div>
      <a href="./param_var.do?msg=world">ParamVar: msg=world</a>
    </div>
    <div>
      ParamVar: POST
      <form action="./param_var.do" method="post">
	<input type="textfield" name="msg"/>
	<input type="submit"/>
      </form>
    </div>
    <div>
      ObjectVar: POST
      <form action="./obj_var.do" method="post">
	name:
	<input type="textfield" name="name"/>
	<input type="submit"/>
      </form>
    </div>
    <div>
      File upload
      <form method="post" action="./upload.do" enctype="multipart/form-data">
	<input type="file" name="file"/>
	<input type="submit"/>
      </form>
    </div>
    <div>
      AJAX Demo
      <div>
	<input id="txt" type="textfield" />
	<input id="btn" type="button" value="GET"/>
      </div>
    </div>
    <div>
      数据校验
      <form action="./err.do" method="post">
	年龄(不得小于10):
	<input type="textfield" name="age"/>
	<input type="submit"/>
      </form>
    </div>
    <div>
      二进制视图
      <c:forEach items="${fileList}" var="i">
	<div>
	  <a href="download.do?id=${i.id}">${i.name}</a>
	</div>
      </c:forEach>
    </div>
  </body> 
</html>
