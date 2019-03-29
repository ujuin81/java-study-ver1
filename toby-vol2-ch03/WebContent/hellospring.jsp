<%@page import="springbook.temp.HelloSpring"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>Insert title here</title>
</head>
<body>
<%
	ApplicationContext context = WebApplicationContextUtils
			.getWebApplicationContext(request.getSession().getServletContext());

	HelloSpring helloSpring = context.getBean(HelloSpring.class);
	
	out.println(helloSpring.sayHello("Root context"));
%>
</body>
</html>