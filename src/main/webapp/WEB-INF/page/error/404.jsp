<%@ page language="java" contentType="text/html; charset=UTF8"
    pageEncoding="UTF-8" isErrorPage="true"%>
<%@ page import="cn.gin.wctf.common.web.Servlets" %>
<jsp:include page="/WEB-INF/page/templet/Support.jsp"></jsp:include>
<%	response.setStatus(404);%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>WCTF - 404 Error!</title>
<style type="text/css">
body {
	background: #fff;
}
#header {
    background-color: rgb(7,17,27);
}
#main {
	position: relative;
	top: 65px;
	margin: 0;
}
#main .container {
    display: inline-block;
    position: relative;
    top: 100px;
    left: 360px;
    width: 600px;
    height: 400px;
}
.info-box {
    position: relative;
    top: 32px;
    left: 10px;	
}
.info-box a:hover {
    color: #333;	
}
.btn {
	color: #666;
} 
</style>
</head>
<body>

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="main" class="layui-clear">
		<div class="container">
			<i class="layui-icon" style="float: left;font-size: 96px;">&#xe61c;</i>
			<div class="info-box" style="top: 24px;">
				<h1>请求的页面不存在</h1>
			</div>
			<div class="info-box" style="left: 80px;color: #666;">
				<a href="javascript:" onclick="history.go(-1);" class="btn">返回首页</a>
				OR
				<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a>
			</div>
		</div>
	</div>
	
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
</body>
</html>