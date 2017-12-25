<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<jsp:include page="/WEB-INF/page/templet/Support.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE HTML>
<html>
<head>
<base href="<%=basePath%>">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="${server}/static/simditor/styles/simditor.css" />
<title>WCTF - 发贴</title>
<style type="text/css">
#main {
	position: relative;
	top: 90px;
	width: 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
	background-color: #fff;
}

#main>.panel {
	padding: 5px 20px 20px 20px;
	margin-bottom: 15px;
	border-radius: 2px;
	background-color: #fff;
	box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);
}

.post-send-editor ol, .post-send-editor ol li {
	list-style: decimal;
}

.post-send-editor ul, .post-send-editor ul li {
	list-style: disc;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>

	<div id="main">
		<div class="panel">
			<div class="layui-form layui-form-pane">
				<div class="layui-tab layui-tab-brief" lay-filter="user">
					<ul class="layui-tab-title">
						<li class="layui-this">发表新帖</li>
					</ul>
				</div>
				<div class="layui-form layui-tab-content" style="padding: 20px 0;">
					<div class="layui-tab-item layui-show">
						<form action="" method="post" id="postSendForm">
							<div class="layui-row layui-col-space15 layui-form-item">
								<div class="layui-col-md3">
									<label class="layui-form-label">所在专栏</label>
									<div class="layui-input-block">
										<select id="PostSendClassify" lay-verify="pos_cl" name="classify" lay-filter="column">
											<option value="1">讨论</option>
											<option value="2">提问</option>
											<option value="3">建议</option>
											<option value="4">分享</option>
										</select>
									</div>
								</div>
								<div class="layui-col-md9">
									<label class="layui-form-label">标题</label>
									<div class="layui-input-block">
										<input type="text" name="title" lay-verify="pos_ti" autocomplete="off" class="layui-input">
									</div>
								</div>
							</div>
							<div class="layui-form-item" id="editors">
								<div class="layui-tab" lay-filter="switchEditor">
								  <ul class="layui-tab-title">
								    <li class="layui-this" style="border-bottom: none;">文本编辑器</li>
								    <li style="border-bottom: none;">代码编辑器</li>
								  </ul>
								  <div class="layui-tab-content" style="padding: 0;">
								    <div class="layui-tab-item layui-show">
								    	<textarea class="post-send-editor" id="layuiPostSendContent" name="content" placeholder="详细描述"
										class="layui-textarea none" style="height: 260px;"></textarea>
								    </div>
								    <div class="layui-tab-item">
								    	<textarea class="post-send-editor" id="simditorPostContent" name="content" placeholder="详细描述"
										class="layui-textarea none" style="height: 260px;"></textarea>
								    </div>
								  </div>
								</div>	
							</div>
							<div class="layui-form-item">
								<div class="layui-inline">
									<label class="layui-form-label">悬赏金币</label>
									<div class="layui-input-inline" style="width: 190px;">
										<select name="coin">
											<option value="20">20</option>
											<option value="30">30</option>
											<option value="50">50</option>
											<option value="60">60</option>
											<option value="80">80</option>
										</select>
									</div>
									<div class="layui-form-mid layui-word-aux">发表后无法更改金币</div>
								</div>
							</div>
							<div class="layui-form-item" id="captcha">
								<label class="layui-form-label">人类验证</label>
								<div class="layui-input-inline">
									<input type="text" id="capt" name="imageCaptcha" autocomplete="off" class="layui-input" lay-verify="pos_ca">
								</div>
								<div class="layui-input-inline">
									<img id="image-validate" alt="点击刷新" style="border: 1px solid #e6e6e6;" src="${root}/common/captcha/image" width="120px" height="38px" onclick="javascript:document.getElementById('image-validate').src='common/captcha/image?stamp='+Math.random();">
								</div>
							</div>
							<div class="layui-form-item">
								<button class="layui-btn" lay-filter="PostSend" lay-submit="">立即发布</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<form id="postSendFormPostProcessor" action="${root}/index" method="post" style="display: none;"></form>
	
<script type="text/javascript" src="${server}/static/layui/layui.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${server}/static/js/codec.js"></script>

<script type="text/javascript" src="${server}/static/simditor/scripts/module.js"></script>
<script type="text/javascript" src="${server}/static/simditor/scripts/hotkeys.js"></script>
<script type="text/javascript" src="${server}/static/simditor/scripts/uploader.js"></script>
<script type="text/javascript" src="${server}/static/simditor/scripts/simditor.js"></script>

<script type="text/javascript" src="${server}/static/js/static.js"></script>
</body>
</html>