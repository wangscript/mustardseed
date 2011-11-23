<%@page language="java"
	contentType="text/html;charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c"
	  uri="http://java.sun.com/jsp/jstl/core"%>


<html>
  <head>
    <title>Hello World</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <script src="http://code.jquery.com/jquery-1.7.min.js"></script>
    <script src="../script/validation.js"></script>
    <script src="../script/validator.js"></script>
    <script src="../script/script.js"></script>
  </head>
  <body>
    <a href="index.do">返回</a>
    <div>
      <div id="validResult" style="color:red">
	<c:forEach var="item" items="${error}">
	  <c:forEach var="i" items="${item.value}">
	    ${i}<br/>
	  </c:forEach>
	</c:forEach>
      </div>
      <form id="formValid" action="validation.do" method="post">
	<div>
	  用户名:<input name="name" type="textfield" value="${name}"/>
	</div>
	<div>
	  密码:<input name="password" type="password" value=""/>
	</div>
	<div>
	  密码:<input name="password" type="password" value=""/>
	</div>
	<input type="submit"/>
      </form>
    </div>
  </body> 
</html>
