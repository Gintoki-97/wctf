<%@page language="java" import="java.util.*" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8" isELIgnored="false"%>
<jsp:include page="/WEB-INF/page/templet/Support.jsp"></jsp:include>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="func" uri="http://wctf.ink/jstl/func"%>
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
<link type="text/css" rel="stylesheet" href="${server}/static/css/edge.css">
<title>WCTF - ${res.name}</title>
<style type="text/css">

#main {
	position: relative;
	top: 90px;
	width: 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
}

#main .tab {
	margin-bottom: 15px;
	border-radius: 2px;
	background-color: #fff;
	box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);
}

#main .tab .title {
	position: relative;
	height: 50px;
	line-height: 50px;
	padding: 0 15px;
	border-bottom: 1px dotted #E9E9E9;
	color: #333;
	border-radius: 2px 2px 0 0;
	font-size: 14px;
}

#main .tab .title>a {
	padding: 0 8px;
	border: none;
}

#main .tab .title>.layui-this {
	color: #5FB878;
}

.warp .content .panel {
	margin-bottom: 20px;
	padding: 20px;
	background-color: #fff;
}

.warp .content>.res>.title {
	font-size: 24px;
	line-height: 36px;
}

.warp .content>.res .count-info {
	position: relative;
	margin: 10px 0 15px;
}

.warp .content>.res .count-info .info {
	display: inline-block;
	margin-right: 20px;
    color: #67769a;
}

.warp .content>.res>.count-info .fly-list-nums {
	position: absolute;
	right: 0;
}

.warp .content>.res .count-info .layui-badge {
	border-radius: 2px;
}

.warp .content>.res .poster-info {
	position: relative;
	line-height: 20px;
	padding: 15px 15px 15px 75px;
	font-size: 13px;
	background-color: #f8f8f8;
	color: #999;
}

.warp .content>.res .poster-info .avatar {
	position: absolute;
	left: 15px;
	top: 15px;
}

.warp .content>.res .poster-info .avatar img {
	display: block;
	width: 45px;
	height: 45px;
	margin: 0;
	border-radius: 2px;
}

.warp .content>.res .res-content {
	margin: 20px 0 0;
	min-height: 306px;
	line-height: 26px;
	font-size: 16px;
	color: #333;
	word-wrap: break-word;
}

.warp .content>.res .res-content .img {
    margin-top: 80px;
    height: 90px;
    line-height: 180px;
}

.warp .content>.res .res-content .img>img {
    width: 56px;
    height: 64px;
    display: block;
    margin: 0 auto;
}

.warp .content>.res .res-content .downloads {
	position: relative;
    margin: 10px 0;
}

.warp .content>.res .res-content .info {
	float: left;
	min-width: 300px;
}

.warp .content>.res .res-content .info>label {
    display: inline-block;
}

.warp .content>.res .res-content .downloads .placeholder {
	position: relative;
    display: block;
    height: 40px;	
	z-index: -9999;
}

.warp .content>.res .res-content .down-btn {
    position: absolute;
    top: 0px;
    left: 0px;
	width: 100px;
    height: 38px;
    display: block;
	font-size: 13px;
    text-align: center;
    line-height: 38px;
    color: #3b8cff;
    border: 1px solid #c0d9fe;
    border-radius: 4px;
    cursor: pointer;
    font-weight: 500;
}

.fly-mid {
	margin: 0 8px;
	display: inline-block;
	height: 10px;
	width: 1px;
	margin: 0 10px;
	vertical-align: middle;
	background-color: #e2e2e2;
}

#ResDetailQRcode {
    position: absolute;
    top: -100px;
    left: 450px;
    width: 100px;
    height: 100px;
    margin-top: 15px;
    padding: 10px;
    border: 1px solid rgb(238, 238, 238);
    box-shadow: rgb(204, 204, 204) 2px 2px 5px;
    display: none;
}
</style>
</head>
<body data-page="detail">

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="main" class="layui-clear">
		<div class="layui-row layui-col-space15 warp">
			<div class="layui-col-md8 content">
				<div class="res panel">
					<h2 class="title">${res.name}</h2>
					<div class="count-info">
						<div class="info">
							<label>上传时间&nbsp;</label>
							<span class="res-time" data-ti="${res.uploadTime.time}"></span>
						</div>
						<div class="info">
							<label>大小&nbsp;</label>
							<span class="res-size" data-si="${res.size}"></span>
						</div>
						<span class="layui-badge">
							资源 </span>
						<c:forTokens items="${res.tagStr}" delims=" " var="tag" varStatus="status">
						<c:if test="${status.count % 3 == 0}">
						<span class="layui-badge layui-bg-orange">
							${tag} </span>	
						</c:if>
						<c:if test="${status.count % 3 == 1}">
						<span class="layui-badge layui-bg-cyan">
							${tag} </span>	
						</c:if>
						<c:if test="${status.count % 3 == 2}">
						<span class="layui-badge layui-bg-blue">
							${tag} </span>	
						</c:if>
						</c:forTokens>
					</div>
					<div class="poster-info">
						<a class="avatar"> <img src="${server}/${res.user.header}"
							alt="${res.user.nickname}">
						</a>
						<div class="fly-detail-user">
							<a href="${root}/user/${res.user.id}" class="fly-link"> <cite>${res.user.nickname}</cite>
								<c:if
									test="${(res.user.auth ne null) && (res.user.auth ne '')}">
									<i class="layui-icon" title="认证信息：${res.user.auth}"
										style="color: #FFB800; position: relative; top: 1px;">&#xe735;</i>
								</c:if>
							</a> <span class="res-time" data-ti="${res.uploadTime.time}"></span>
						</div>
						<div class="detail-hits" style="height: 25px;">
							<span
								style="padding-right: 10px; color: #FF7200; position: relative; top: 5px;">体重：${res.user.weight} 斤</span>
						</div>
					</div>
					<div class="res-content">
						<div class="img">
							<img alt="${res.name}" src="${server}/${res.img}">
						</div>
						<div class="downloads">
							<span class="placeholder hidden" style="display:block;height:38px;position:initial;"></span>
							<a href="javascript:;" id="ResDetailDownload" class="down-btn" style="left:264px;" data-id="${res.id}" data-na="${res.name}">
								<i class="layui-icon" style="position: relative;font-size: 18px;top: 2px;font-weight: 600;">&#xe601;</i>
								<span>立即下载</span>
							</a>
							<span id="ResDetailQR" class="down-btn" style="width:45px;left:380px;">
								<i class="layui-icon">&#xe64c;</i>
							</span>
							<div id="ResDetailQRcode" style="display:none;width:100px; height:100px; margin-top:15px;" data-qr="${root}/res/${res.id}"></div>
						</div>
						<div style="margin-top: 30px;font-size: 13px;">
							<fieldset class="layui-elem-field">
								<legend style="font-size: 15px;">资源详情</legend>
								<div class="layui-field-box layui-clear" style="font-size: 11px;color: #666;">
									<div class="info">
										<label>所需：</label>
										<span>${res.weight}斤</span>
									</div>
									<div class="info">
										<label>下载量：</label>
										<span>${res.downloads}次</span>
									</div>
									<div class="info">
										<label>上传时间：</label>
										<span class="res-time-det" data-ti="${res.uploadTime.time}"></span>
									</div>
									<div class="info" style="float: initial;">
										<label>文件大小：</label>
										<span class="res-size" data-si="${res.size}"></span>
									</div>
									<div class="desc" style="position: relative;">
										<label style="position: absolute;top: 0px;width: 60px;display: inline-block;">资源描述：</label>
										<span style="display: inline-block;width: 540px;position:relative;left:60px;">${res.desc}</span>
									</div>
						  		</div>
							</fieldset>
						</div>
					</div>
				</div>
			</div>
			<div class="layui-col-md4 edge">
				<div class="search">
					<input type="text" placeholder="社区搜索" value="" class="layui-input">
					<i class="layui-icon" style="top: 7px;left: 320px;">&#xe615;</i>
				</div>
				<div class="panel btns">
					<ul>
						<c:if test="${func:isPunchToday(sessionScope.user.punchToday)}">
						<li class="l1 btn">
						<a>
							<i class="layui-icon">&#xe605;</i>
							<span>已签到</span>
						</a>	
						</li>
						</c:if>
						<c:if test="${!func:isPunchToday(sessionScope.user.punchToday)}">
						<li class="l1 btn">
							<a id="punch">
								<i class="layui-icon">&#xe610;</i>
								<span>签到</span>
							</a>
						</li>
						</c:if>
						<li class="btn" style="left: 115px;">
							<a class="post-send-btn" data-val="1">
								<i class="layui-icon">&#xe654;</i>
								<span>发贴</span>
							</a>
						</li>
						<li class="btn" style="left: 210px;">
							<a class="post-send-btn" data-val="2">
								<i class="layui-icon" style="font-size: 27px;">&#xe6b2;</i>
								<span>提问</span>
							</a>
						</li>
					</ul>
				</div>
				<div class="panel activities">
					<h3 class="title">活动消息</h3>
					<ul class="layui-timeline">
						<c:forEach items="${activityNews}" var="activity"
							varStatus="status">
							<li class="activity layui-timeline-item"><i
								class="layui-icon layui-timeline-axis">&#xe63f;</i>
								<div class="layui-timeline-content layui-text">
									<h3 class="layui-timeline-title">8月16日</h3>
									<p>
										<a href="${root}/post/${activity.postId}">${activity.desc}</a>
									</p>
								</div></li>
						</c:forEach>
					</ul>
				</div>
				<div class="panel bbs-news">
					<h3 class="title">社区消息</h3>
					<ul>
						<c:forEach items="${bbsNews}" var="bbs" varStatus="status">
							<li><a href="${root}/post/${bbs.postId}" target="_blank">${bbs.desc}</a>
							</li>
						</c:forEach>
					</ul>
				</div>
				<div class="panel fast-enter">
					<h3 class="title">
						快速入口
						<span><a style="color:#999;" href="${root}/res/all" target="_blank">更多</a></span>
					</h3>
					<ul>
						<li>
							<a href="${root}/res/doc" target="_blank">文档</a>
						</li>
						<li>
							<a href="${root}/res/code" target="_blank">代码</a>
						</li>
						<li>
							<a href="${root}/res/tool" target="_blank">工具</a>
						</li>
						<li>
							<a href="${root}/res/other" target="_blank">其它</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript" src="${server}/static/js/qrcode.min.js"></script>
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
</body>
</html>