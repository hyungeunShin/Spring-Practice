<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.0/jquery.min.js"></script>
</head>
<body>
	<h2>Read</h2>
	<table border="1">
		<tr>
			<td>제목</td>
			<td>${board.title}</td>
		</tr>
		<tr>
			<td>작성자</td>
			<td>${board.writer}</td>
		</tr>
		<tr>
			<td>내용</td>
			<td>${board.content}</td>
		</tr>
		<tr>
			<td>작성일</td>
			<td><fmt:formatDate value="${board.regDate}" pattern="yyyy-MM-dd hh:mm"/></td>
		</tr>
	</table>
	<form method="post" action="/crud/board/remove" id="delForm">
		<input type="hidden" name="boardNo" value="${board.boardNo}">
	</form>
	<button type="button" id="btnModify">수정</button>
	<button type="button" id="btnDelete">삭제</button>
	<button type="button" id="btnList">목록</button>
</body>
<script type="text/javascript">
let btnModify = $("#btnModify");
let btnDelete = $("#btnDelete");
let btnList = $("#btnList");
let form = $("#delForm");

btnModify.on("click", function() {
	form.attr("action", "/crud/board/modify");
	form.attr("method", "get");
	form.submit();
});

btnDelete.on("click", function() {
	if(confirm("정말 삭제하시겠습니까?")) {
		form.submit();
	}
});

btnList.on("click", function() {
	location.href = "/crud/board/list";	
});
</script>
</html>