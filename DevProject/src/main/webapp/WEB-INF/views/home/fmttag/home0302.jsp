<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h4>1) type 속성을 지정하거나 pattern 속성을 지정하여 숫자를 형식화한다</h4>
	<p>now : ${now}</p>
	<p>date default : <fmt:formatDate value="${now}" type="time" timeStyle="default" /></p>
	<p>date short : <fmt:formatDate value="${now}" type="time" timeStyle="short" /></p>
	<p>date medium : <fmt:formatDate value="${now}" type="time" timeStyle="medium" /></p>
	<p>date long : <fmt:formatDate value="${now}" type="time" timeStyle="long" /></p>
	<p>date full : <fmt:formatDate value="${now}" type="time" timeStyle="full" /></p>
</body>
</html>