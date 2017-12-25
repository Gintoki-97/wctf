<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
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
<title>WCTF - 用户注册</title>
<style type="text/css">
#main {
	position: relative;
	top: 90px; width : 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
	width: 1079px;
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
</style>
</head>
<body>

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>

	<div id="main" class="layui-clear">
		<div class="panel">
			<div class="layui-tab layui-tab-brief">
				<ul class="layui-tab-title">
					<li><a href="${root}/user/login">登入</a></li>
					<li class="layui-this">注册</li>
				</ul>
			</div>
			<div class="layui-form layui-tab-content" id="LAY_ucm"
				style="padding: 20px 0;">
				<div class="layui-tab-item layui-show">
					<div class="layui-form layui-form-pane">
						<form id="userRegForm" action="${root}/user/reg" method="post">
							<div class="layui-form-item">
								<label class="layui-form-label">邮箱</label>
								<div class="layui-input-inline" style="margin-right: 0px;">
									<input type="text" id="email" name="account" lay-verify="reg_ac" autocomplete="off" class="layui-input" value="">
								</div>
								<div class="layui-input-inline" style="width: 38px;margin-right: 0px;border-top: 1px solid #eee;border-bottom: 1px solid #eee;">
									<span style="width: 38px;height: 36px;display: inline-block;text-align: center;line-height: 38px;background: rgba(200,200,200,.1);">@</span>
								</div>
								<div class="layui-input-inline" style="width: 120px;">
									<select name="emailDomain" id="emailDomain" lay-verify="reg_ed">
										<option value="0">选择邮箱</option>
										<option value="qq.com">qq.com</option>
										<option value="126.com">126.com</option>
										<option value="163.com">163.com</option>
										<option value="sohu.com">sohu.com</option>
										<option value="sina.com">sina.com</option>
										<option value="yahoo.com">yahoo.com</option>
										<option value="gmail.com">gmail.com</option>
									</select>
								</div>
								<div class="layui-form-mid layui-word-aux">将成为您登录的合法标识</div>
							</div>
							<div class="layui-form-item">
								<label for="L_pass" class="layui-form-label">验证码</label>
								<div class="layui-input-inline">
									<input type="text" id="captcha" name="emailCaptcha" lay-verify="reg_ca" autocomplete="off" class="layui-input" placeholder="请输入邮箱验证码" value="">
								</div>
								<div class="layui-input-inline">
									<span id="send-email-cover" class="layui-btn" style="width: 106px;position: absolute;z-index: 99;left: 10px;opacity: 0;display: none;cursor: not-allowed;"></span>
									<span id="send-email" class="layui-btn">发送验证码</span>
								</div>
							</div>
							<div class="layui-form-item">
								<label for="L_pass" class="layui-form-label">密码</label>
								<div class="layui-input-inline">
									<input type="password" id="pass" name="password" lay-verify="reg_pw" autocomplete="off" class="layui-input" value="">
								</div>
								<div class="layui-form-mid layui-word-aux">6到16个字母，数字，下划线或横线</div>
							</div>
							<div class="layui-form-item">
								<label for="L_pass" class="layui-form-label">昵称</label>
								<div class="layui-input-inline">
									<input type="text" id="nick" name="nickname" lay-verify="reg_ni" autocomplete="off" class="layui-input">
								</div>
								<div class="layui-form-mid layui-word-aux">不超过16个字符</div>
							</div>
							<div class="layui-form-item">
								<button class="layui-btn" lay-filter="UserReg" lay-submit>立即注册</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="userRegFormPostProcessor" action="${root}/index?stamp=U2FsdGVkX1+OBmjsb+WDw/dT7V+e9sgdYV31jN/mzOAfCOsPMNxD0wt2g3e5EcSR" style="display: none;" method="POST"></form>
<script type="text/javascript" src="${server}/static/layui/layui.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${server}/static/js/codec.js"></script>
<script type="text/javascript" src="${server}/static/js/static.js"></script>
<c:if test="${error ne null }">
	<input id="error" type="hidden" value="${error }">
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
