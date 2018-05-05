<%
	response.setStatus(500);

	// 获取异常类
	Throwable ex = Exceptions.getThrowable(request);
	if (ex != null) {
		LoggerFactory.getLogger("500.jsp").error(ex.getMessage(), ex);
	}

	// 编译错误信息
	StringBuilder sb = new StringBuilder("StackTrace：\n");
	if (ex != null) {
		sb.append(Exceptions.getStackTraceAsString(ex));
	} else {
		sb.append("未知错误.\n\n");
	}

	// 如果是异步请求或是手机端，则直接返回信息
	if (Servlets.isAjaxRequest(request)) {
		out.print(sb);
	}

	// 输出异常信息页面
	else {
%>
<%@page import="org.slf4j.Logger,org.slf4j.LoggerFactory"%>
<%@page import="cn.gin.wctf.common.web.Servlets"%>
<%@page import="cn.gin.wctf.common.util.Exceptions"%>
<%@page import="cn.gin.wctf.common.util.StringUtils"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<jsp:include page="/WEB-INF/page/templet/Support.jsp"></jsp:include>
<!DOCTYPE html>
<html>
<head>
<title>500 - 系统内部错误</title>
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
	margin: 0 0 150px 0;
}
#main .container {
    display: inline-block;
    position: relative;
    top: 80px;
    left: 200px;
    width: 1000px;
    min-height: 300px;
}
.ctrl .val {
	color: #666;
}
.ctrl>div {
    height: 25px;
    line-height: 25px;
}
.ctrl .tips {
	height: initial;
}
.ctrl .tips .val:hover {
	color: #333;
}
.error-msg {
    height: 500px;
    overflow: scroll;
    white-space: nowrap;
    border: 1px solid #ddd;
    margin-top: 20px;
    padding: 10px;
}

div::-webkit-scrollbar {
	width: 5px;
	height: 5px;
}

div::-webkit-scrollbar-track {
	border-radius: 10px;
	background-color: #999;
}

div::-webkit-scrollbar-thumb {
	height: 1px;
	background-color: #666;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="main" class="container-fluid">
		<div class="container">
			<div class="page-header">
				<h1>500.系统内部错误</h1>
			</div>
			<div class="ctrl">
				<div class="desc">
					<label>错误信息：</label>
					<span class="val"><%=ex == null ? "未知错误." : StringUtils.toHtml(ex.getMessage())%></span>
				</div>
				<div class="tips">
					<p>请点击“查看详细信息”按钮，将详细错误信息发送给系统管理员，谢谢！</p>
					<div style="display: inline-block;position: relative;left: 260px;color: #666;">
						<a href="javascript:;" onclick="history.go(-1);" class="btn val">返回上一页</a>
						OR
						<a href="javascript:;" onclick="$('.error-msg').toggle();" class="btn val">查看详细信息</a>
					</div>
				</div>
			</div>
			<div class="error-msg none">
				<a class="feedback layui-btn" style="float: right;background-color: #07111b;" title="提交反馈">
					<i class="layui-icon">&#xe608;</i> BUG
				</a>
				<code style="font-size: 11px;">
				<%=StringUtils.toHtml(sb.toString())%>
				</code><br>
				<a href="javascript:;" onclick="history.go(-1);" class="btn">返回上一页</a>
				&nbsp; 
				<a href="javascript:;" onclick="$('.error-msg').toggle();" class="btn">隐藏详细信息</a><br><br>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
</body>
</html>
<%
	}
	out = pageContext.pushBody();
%>