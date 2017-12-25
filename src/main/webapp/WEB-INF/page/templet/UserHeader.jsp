<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML>
<html>
<body>
	<div id="header">
		<div class="main">
			<div class="header-logo">
				<a class="logo" href="${root}/index" title="WCTF"
					style="background: url(${server}/static/image/logo5.png) no-repeat 10px;background-size: 20%;text-indent: 70px;color: #ccc;font-weight: 100;font-size: 17px;">WCTF 社区</a>
			</div>
			<div class="header-link">
				<a class="link" href="" title="首页"> <i class="layui-icon"
					style="font-size: 27px;">&#xe68e;</i> <span class="text">首页</span>
				</a> <a class="link" href="" title="反馈"> <i class="layui-icon"
					style="font-size: 27px;">&#xe63a;</i> <span class="text">反馈</span>
				</a> <a class="link" href="" title="关于"> <i class="layui-icon"
					style="font-size: 27px;">&#xe60b;</i> <span class="text">关于</span>
				</a>
			</div>
			<div class="header-user">
				<input id="userId" type="hidden" value="${sessionScope.user == null ? '' : sessionScope.user.id}">
				<!-- 用户未登录 -->
				<c:if test="${sessionScope.user == null }">
					<a class="link" href="${root}/user/login" title="用户"> <i
						class="layui-icon" style="font-size: 27px;">&#xe612;</i> <span
						class="text">（未登录）</span>
					</a>
					<a class="link login" href="${root}/user/login" title="登录"> <span
						class="text">登录</span></a>
					<a class="link reg" href="${root}/user/reg" title="注册"> <span
						class="text">注册</span></a>
				</c:if>
				<!-- 用户已登录 -->
				<c:if test="${sessionScope.user != null }">
					<a class="link user" href="${root}/user/setting/${sessionScope.user.id}"
						title="${sessionScope.user.nickname}" style="text-indent: 0px;">
						<img alt="" src="${sessionScope.user.header}" width="42px" height="42px"
						style="border-radius: 100%;" id="viewAvatar"> <cite class="un">${sessionScope.user.nickname}</cite>
					</a>
					<a class="link set" href="${root}/user/setting/${sessionScope.user.id}" title="设置"> <span
						class="text">设置</span></a>
					<a class="link logout" title="退了"> <span
						class="text">退了</span></a>
				</c:if>
			</div>
		</div>
	</div>
</body>
</html>