<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<html>
<head>
<link rel="stylesheet" href="${pageContext.request.contextPath }/resources/css/bootstrap.min.css" />
<title>자유게시판 목록</title>
<style type="text/css">
.mytr:hover {
	background: yellow;
}
</style>
</head>
<body>
	<c:set value="${pagingVO.dataList}" var="list"></c:set>
	<div class="jumbotron">
		<div class="container">
			<h1 class="display-3">자유 게시판 목록</h1>
		</div>
	</div>
	<div class="container">
			<div>
				<div class="text-right">
					<span class="badge badge-success">전체 ${totalRecord}건</span>
				</div>
			</div>
			<div style="padding-top: 50px">
				<div class="row" style="padding-bottom: 20px">
					<div class="col-md-7"></div>
					<div class="col-md-5">
						<div class="row">
							<div class="col-md-2"></div>
							<div class="col-md-10">
								<form id="searchForm" method="post">
									<input type="hidden" name="page" id="page">
									<select name="searchType">
										<option value="title" <c:if test="${searchType == 'title'}"><c:out value="selected"/></c:if>>제목</option>
										<option value="writer" <c:if test="${searchType == 'writer'}"><c:out value="selected"/></c:if>>작성자</option>
										<option value="titlewriter" <c:if test="${searchType == 'titlewriter'}"><c:out value="selected"/></c:if>>제목+작성자</option>
									</select>
									<input type="text" name="searchWord" value="${searchWord}"/>
									<input type="submit" value="검색" />
								</form>
							</div>
						</div>
					</div>
				</div>
				<table class="table">
					<thead class="table-dark">
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성일</th>
							<th>작성자</th>
							<th>조회수</th>
						</tr>
					</thead>
					<tbody>
						<c:choose>
							<c:when test="${empty list}">
								<tr>
									<td colspan="5">조회하신 게시글이 존재하지 않습니다.</td>
								</tr>	
							</c:when>
							<c:otherwise>
								<c:forEach items="${list}" var="free">
								<tr class="mytr" data-no="${free.boNo}">
									<td>${free.boNo}</td>
									<td>${free.boTitle}</td>
									<td>${free.boDate}</td>
									<td>${free.boWriter}</td>
									<td>${free.boHit}</td>
								</tr>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</tbody>
				</table>
			</div>
			<div align="left">
				<a href="/free/form.do" class="btn btn-primary">&laquo;글쓰기</a>
			</div>
			<div class="card-footer clearfix" id="pagingArea">
				${pagingVO.pagingHTML}
			</div>
		<hr>
	</div>
	<script src="${pageContext.request.contextPath}/resources/plugins/jquery/jquery.min.js"></script>
</body>
<script type="text/javascript">
let searchForm = $('#searchForm');
let pagingArea = $('#pagingArea');

pagingArea.on('click', 'a', function(event) {
	event.preventDefault();
	var pageNo = $(this).data('page');
	searchForm.find('#page').val(pageNo);
	searchForm.submit();
})

$('.mytr').on('click', function() {
	var boNo = $(this).data('no');
	location.href = "/free/detail.do?boNo=" + boNo;
})
</script>
</html>
