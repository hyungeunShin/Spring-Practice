<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h3>7장 JSP</h3>
	<p>JSTL 태그들의 Example : c:forEach</p>
	<c:forEach items="${member.hobbyArray}" var="hobby" varStatus="stat">
		${stat.count} : hobbyArray[${stat.index}] => ${hobby}<br>
	</c:forEach>
</body>
</html>