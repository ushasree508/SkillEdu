<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*"%>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<%
  
    if (session == null || session.getAttribute("username") == null) {
        response.sendRedirect("login.jsp?msg=Please login first!");
        return;
    }
%>
    <h2>Welcome, <%= session.getAttribute("username") %>!</h2>
    <a href="LogoutServlet">Logout</a>
</body>
</html>
