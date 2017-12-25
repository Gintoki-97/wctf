<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<jsp:include page="/WEB-INF/page/templet/Support.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<title>WCTF - 用户登录</title>
<style type="text/css">
#main {
	position: relative;
	top: 90px; 
	width : 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
}

.panel {
	height: 500px;
	background-color: #fff;
}

.panel .layui-tab {
	margin: 10px auto;
	width: 90%;
	padding-top: 10px;
}
.panel .layui-tab-item {
    margin: 0 auto;
    width: 90%;
}

#captcha {
	display: none;
}
</style>
</head>
<body>
	
	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="main" class="layui-clear">
		<div class="panel">
			<div class="layui-tab layui-tab-brief">
				<ul class="layui-tab-title">
					<li class="layui-this">登入</li>
					<li><a href="user/reg">注册</a>
					</li>
				</ul>
			</div>
			<div class="layui-form layui-tab-content" id="LAY_ucm"
				style="padding: 20px 0;">
				<div class="layui-tab-item layui-show">
					<div class="layui-form layui-form-pane">
						<form id="userLoginForm" action="${root}/user/login" method="post">
							<div class="layui-form-item">
								<label for="L_email" class="layui-form-label">邮箱</label>
								<div class="layui-input-inline">
									<input type="text" id="email" name="account" lay-verify="log_ac" autocomplete="off" class="layui-input" value='${user.account}'>
								</div>
							</div>
							<div class="layui-form-item">
								<label for="L_pass" class="layui-form-label">密码</label>
								<div class="layui-input-inline">
									<input type="password" id="pass" name="password"  lay-verify="log_pw" autocomplete="off" class="layui-input">
								</div>
							</div>
							<div class="layui-form-item" id="captcha">
								<label for="L_pass" class="layui-form-label">验证码</label>
								<div class="layui-input-inline">
									<input type="text" id="capt" name="imageCaptcha" autocomplete="off" class="layui-input" disabled="disabled">
								</div>
								<div class="layui-input-inline">
									<img id="image-validate" alt="点击刷新" style="border: 1px solid #e6e6e6;" src="" width="120px" height="38px" onclick="javascript:document.getElementById('image-validate').src='${root}/common/captcha/image?stamp='+Math.random();">
								</div>
							</div>
							<div class="layui-form-item">
								<button class="layui-btn" lay-filter="UserLogin" lay-submit>立即登录</button>
								<span style="padding-left:20px;"><a href="/user/forget">忘记密码？</a></span>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<form id="userLoginFormPostProcessor" action="${root}/index?stamp=U2FsdGVkX1+OBmjsb+WDw/dT7V+e9sgdYV31jN/mzOAfCOsPMNxD0wt2g3e5EcSR" style="display: none;" method="POST"></form>
<script type="text/javascript" src="${server}/static/layui/layui.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${server}/static/js/codec.js"></script>
<script type="text/javascript" src="${server}/static/js/static.js"></script>
<c:if test="${error ne null}">
	<input id="error" type="hidden" value="${error}">
	<script type="text/javascript">
		var error = $("#error").val();
		layer.open({
		  title: '消息'
		  ,content: error
		});  
	</script>
	<%
		session.removeAttribute("error");
	 %>
</c:if>
</body>
</html>