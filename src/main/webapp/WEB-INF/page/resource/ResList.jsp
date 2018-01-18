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
<title>WCTF - 资源索引</title>
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
}

#main .tab .title {
    position: relative;
    height: 50px;
    line-height: 50px;
    padding: 0 15px;
	border-top: 1px dotted #E9E9E9;
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

.warp .content {
	margin-bottom: 15px;
    border-radius: 2px;
}

.warp .content .query-items {

}

.warp .content .query-items .items .layui-this {
	color: #5FB878;	
	padding: 0 8px;
    border: none;
}

.warp .content .query-item {
    position: relative;
    line-height: 50px;
	background-color: #fff;
}

.warp .content .query-item .label {
    display: inline-block;
    width: 100px;
    height: 50px;
	text-align: center;
	color: #999;
}

.warp .content .query-item .content {
	margin-bottom: initial;
    width: 614px;
    float: right;
}

.warp .content .query-item .items li {
    display: block;
    width: 60px;
    text-align: center;
    float: left;
}

.warp .content .query-item .items li>a {
    cursor: pointer;
}

.warp .content .query-item-console .items li {
    display: list-item;
	margin-right: 8px;
    width: initial;
	text-indent: initial;
    float: left;
}

.warp .content .query-item-console .items li i {
    font-size: 12px;
    cursor: pointer;
}

.warp .content .panel {
	background-color: #fff;
}

.warp .content .panel .res {
    padding: 20px 20px;
    border-bottom: dashed 1px #e6e6e6;
}

.warp .content .panel .res .image {
    width: 45px;
    outline: none;
    text-decoration: none;
    color: #333;
    float: left;
}

.warp .content .panel .res .title {
	display: inline-block;
    padding-bottom: 20px;
    font-size: 16px;
    color: #333;
    font-weight: bold;
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}

.warp .content .panel .res .desc {
    font-size: 12px;
    color: #666;
    margin-top: -10px;
    padding-bottom: 10px;
    line-height: 24px;
    word-break: break-all;
}

.warp .content .panel .res .info {
    margin-right: 20px;
	font-size: 12px;
	color: #444;
}

.warp .content .panel .res .info .label {
    color: #888;
}

.warp .content .panel .paging {
    padding: 15px 15px 15px 15px;
}

.option {
	cursor: pointer;
}

</style>
</head>
<body data-page="index">

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="main" class="layui-clear">
		<div class="layui-row layui-col-space15 warp">
			<div class="layui-col-md8 content">
				<div class="query-items layui-clear" style="margin-bottom: 0;" data-qi="${requestScope.items}">
					<form id="ResSearchForm" action="${root}/res/search" class="none" method="get">
						<input type="hidden" value="" name="items">
					</form>
					<div class="query-item layui-clear tags">
						<span class="label">资源标签：</span>
						<div class="content">
							<ul class="items layui-clear">
								<li><a class="option">全部</a></li>
								<li><a class="option">规范</a></li>
							</ul>
						</div>
					</div>
					<div class="query-item layui-clear cls">
						<span class="label">资源类型：</span>
						<div class="content">
							<ul class="items layui-clear">
								<li><a class="option">全部</a></li>
								<li><a class="option">文档</a></li>
								<li><a class="option">代码</a></li>
								<li><a class="option">工具</a></li>
								<li><a class="option">其它</a></li>
							</ul>
						</div>
					</div>
					<div class="query-item query-item-console" data-qi="${requestScope.items}">
						<span class="label">已选条件：</span>
						<div class="content">
							<ul class="items layui-clear" style="padding-left: 18px;">
								<li style="width: 60px;margin-left: -18px;"><a>全部</a></li>
								<li style="display:none;"><a></a><i class="layui-icon close hidden">&nbsp;&#x1006;</i></li>
								<li style="display:none;"><a></a><i class="layui-icon close hidden">&nbsp;&#x1006;</i></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="tab order-item" style="margin-bottom: 0;">
					<div class="title">
						<a class="option" data-in="0">最新上传</a>
						<span class="fly-mid"></span>
						<a class="option" data-in="1">最多下载</a>
					</div>
				</div>
				<ul class="panel res-box">
					<c:forEach items="${requestScope.paging.list}" var="res" varStatus="status">
					<li class="res">
						<a class="image" href="${root}/res/${res.id}" target="_blank">
							<img alt="" src="${server}/${res.img}" style="width: 42px;height: 48px;">
						</a>
						<div style="margin-left: 60px;">
							<a class="title" href="${root}/res/${res.id}" target="_blank">${res.name}</a>
							<p class="desc">${res.name}</p>
							<div>
								<span class="info">
									<span class="label">下载量：</span>
									<span>${res.downloads}</span>
								</span>
								<span class="info">
									<span class="label">资源大小：</span>
									<span class="res-size" data-si="${res.size}"></span>
								</span>
								<span class="info">
									<span class="label">上传时间：</span>
									<span class="res-time" data-ti="${res.uploadTime.time}"></span>
								</span>
								<span class="info">
									<span class="label">上传者：</span>
									<span><a title="${res.user.nickname}" href="${root}/user/${res.user.id}" target="_blank">${res.user.nickname}</a></span>
								</span>
							</div>
						</div>
					</li>
					</c:forEach>
					<li id="paging" class="paging" data-in="${paging.index}" data-co="${paging.count}" data-tp="${paging.totalPage}" data-li="${paging.limit}" data-qi="${paging.queryItems}"></li>
				</ul>
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
						<c:forEach items="${activityNews}" var="activity" varStatus="status">
						<li class="activity layui-timeline-item">
							<i class="layui-icon layui-timeline-axis">&#xe63f;</i>
							<div class="layui-timeline-content layui-text">
						      <h3 class="layui-timeline-title">8月16日</h3>
						      <p>
						      	<a href="${root}/post/${activity.postId}">${activity.desc}</a>
						      </p>
						    </div>
						</li>	
						</c:forEach>
					</ul>
				</div>
				<div class="panel bbs-news">
					<h3 class="title">社区消息</h3>
					<ul>
						<c:forEach items="${bbsNews}" var="bbs" varStatus="status">
						<li>
							<a href="${root}/post/${bbs.postId}" target="_blank">${bbs.desc}</a>
						</li>
						</c:forEach>
					</ul>
				</div>
				<div class="panel fast-enter">
					<h3 class="title">
						快速入口
						<span><a style="color:#999;" href="${root}/res" target="_blank">更多</a></span>
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
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
</body>
</html>