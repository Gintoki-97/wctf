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
<title>WCTF - 发贴分类</title>
<style type="text/css">
#classify li {
    display: inline-block;
    width: 80px;
    height: 50px;
    float: left;
	list-style: none;
    text-align: center;	
}
#classify .fly-column-right {
    position: absolute;
    right: 0;
    top: 0;
}

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
    box-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
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
	background-color: #fff;
	margin-bottom: 15px;
    border-radius: 2px;
}
.warp .content .panel li {
	position: relative;
    height: 45px;
    line-height: 22px;
    padding: 15px 15px 15px 75px;
    border-bottom: 1px dotted #e2e2e2;
}
.warp .content .panel .more {
	text-align: center;
}
.warp .content .panel .more .more-box {
	display: inline-block;
    margin: 20px 0;
    border: 1px solid #009E94;
    border-right: none;
    border-bottom: none;
    font-size: 0;
}
.warp .content .panel .more .more-box a {
	display: inline-block;
	padding: 0 20px;
    line-height: 36px;
    border-right: 1px solid #009E94;
    border-bottom: 1px solid #009E94;
    font-size: 14px;
}
.warp .content .panel .paging {
    padding: 15px 15px 15px 15px;
}
.warp .content .posts .avatar {
    position: absolute;
    left: 15px;
    top: 15px;
} 
.warp .content .posts .avatar img {
	display: block;
    width: 45px;
    height: 45px;
    margin: 0;
    border-radius: 2px;
}
.warp .content .posts .post .title {
	height: 26px;
}
.warp .content .posts .post .title a {
	display: inline-block;
    max-width: 80%;
    padding-right: 10px;
    font-size: 16px;
}
.warp .content .posts .post .title span {
    position: relative;
    display: inline-block;
    margin-left: 5px;
    padding: 0 5px;
	height: 20px;
    line-height: 20px;
    font-size: 12px;
    color: #fff;
	background-color: #393D49;
    vertical-align: top;
}
.warp .content .posts .post .title .badge span {
    display: inline-block;
    max-width: 80%;
	top: -2px;
    height: 16px;
    line-height: 16px;
    padding: 0 5px;
    margin-right: 10px;
    font-size: 12px;
    border: 1px solid #5FB878;
    background: none;
    color: #5FB878;
}
.warp .content .post .info {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    position: relative;
    font-size: 13px;
    color: #999;
}
.warp .content .post .info>* {
    padding-right: 15px;
}
.warp .content .post .info .poster {
	color: #999;
}
.warp .content .post .info .poster i {
    position: relative;
    top: 1px;
    margin-right: 3px;
}
.warp .content .post .info .reply {
    position: absolute;
    right: 80px;
    top: 0;
    padding-right: 0!important;
}
.warp .content .post .info .viewed {
    position: absolute;
    right: 10px;
    top: 0;
    padding-right: 0!important;
}

.edge .search {
	background-color: #fff;
	margin-bottom: 15px;
    border-radius: 2px;
}
.edge .search .layui-icon {
    position: absolute;
    top: 8px;
    left: 318px;
    display: inline-block;
    width: 38px;
    height: 38px;
    line-height: 38px;
    cursor: pointer;
    text-align: center;
}
.edge .panel {
	background-color: #fff;
	margin-bottom: 15px;
    border-radius: 2px;
    padding: 0 15px;
}
.edge .panel .title {
    position: relative;
    height: 50px;
    line-height: 50px;
    margin-bottom: 5px;
    border-bottom: 1px dotted #E9E9E9;
    color: #333;
    border-radius: 2px 2px 0 0;
    font-size: 16px;
}
.edge .btns  {
	position: relative;
}
.edge .btns .btn {
	position: absolute;
	top: 0px;
	left: 0px;
	display: block;
	width: 80px;
	height: 80px;
	text-align: center;
	padding-top: 10px;
	cursor: pointer;
}
.edge .btns .btn:hover {
	color: rgb(51,102,255);
}
.edge .btns .btn i {
    display: block;
    width: 50px;
    height: 50px;
    margin: 0 auto;
    font-size: 28px;
    line-height: 50px;
}
.edge .btns .l1 {
	position: relative;
	top: 0px;
	left: 0px;
}

.edge .activities  {
	font-size: 12px;
	padding-bottom: 20px;
}
.edge .activities>ul  {
	padding-left: 15px;
	height: 210px;
	overflow: scroll;
}
.edge .activities>ul::-webkit-scrollbar {
    width: 5px;
    height: 0px;
}
.edge .activities>ul::-webkit-scrollbar-track {
    background-color: #f5f5f5;
}
.edge .activities>ul::-webkit-scrollbar-thumb {
    height: 1px;
    background-color: rgb(51,102,255);
}
.edge .activities .activity  {
    list-style-type: none;
    min-height: 25px;
    max-height: 50px;
    line-height: 25px;
	color: #999;
}
.edge .activities .activity>i  {
    font-size: 12px;
}
.edge .activities .activity h3  {
    font-size: 14px;
}
.edge .activities .activity a {
	color: #999;
	text-decoration: none;
}
.edge .activities .activity a:hover {
	color: #444;
}

.edge .bbs-news>ul {
    overflow: hidden;
    padding: 10px 0 10px;
}
.edge .bbs-news>ul>li {
    width: 100%;
    color: #373d41;
    font-size: 12px;
    line-height: 20px;
    margin-bottom: 10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
}

.edge .fast-enter>ul {
	overflow: hidden;
    padding: 10px 0 10px;
}
.edge .fast-enter>ul>li {
    width: 70px;
    float: left;
    color: #373d41;
    font-size: 12px;
    line-height: 20px;
    margin-bottom: 10px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
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

</style>
</head>
<body>
	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div id="classify" class="fly-panel fly-column" style="margin-bottom: 15px;position:relative;top:65px;line-height:50px;border-radius:2px;background-color:#fff;box-shadow:0 1px 2px 0 rgba(0,0,0,.05);">
		<div class="layui-container">
			<ul class="layui-clear">
				<li class="layui-hide-xs">
					<a href="${root}/index">首页</a>
				</li>
				<li class="dis">
					<a href="${root}/post/discuss">讨论</a>
				</li>
				<li class="que">
					<a href="${root}/post/question">提问 </a>
				</li>
				<li class="sug">
					<a href="${root}/post/suggest">建议</a>
				</li>
				<li class="sha">
					<a href="${root}/post/sharing">分享</a>
				</li>
				<li>
					<a href="${root}/post/notice">公告</a>
				</li>
				<li>
					<a href="${root}/news/activity">动态</a>
				</li>
				<li>
					<a href="${root}/posts/1">我的贴</a>
				</li>
			</ul>
		</div>
	</div>

	<div id="main" class="layui-clear">
		<div class="layui-row layui-col-space15 warp">
			<div class="layui-col-md8 content">
				<div class="tab" style="margin-bottom: 0;">
					<div class="title">
						<a href="${root}/post/discuss" class="layui-this">全部</a>
						<span class="fly-mid"></span>
						<a href="${root}/post/discuss/unclosed">未结</a>
						<span class="fly-mid"></span>
						<a href="${root}/post/discuss/closed">已结</a>
						<span class="fly-mid"></span>
						<a href="${root}/post/discuss/sticky">精华</a>
					</div>
				</div>
				<ul class="panel posts">
					<c:forEach items="${requestScope.paging.list}" var="post" varStatus="status">
					<li class="post">
						<a href="${root}/user/${post.userId}" class="avatar">
							<img src="${post.user.header}" alt="纸飞机">
						</a>
						<h2 class="title">
							<c:if test="${post.classifyStr ne null && post.classifyStr ne ''}">
							<a class="badge">
								<span class="classify" title="${post.classifyStr}">${post.classifyStr}</span>
							</a>
							</c:if>
					    	<a href="${root}/post/${post.id}">${post.title}</a>
					    </h2>
					    <div class="info">    
					    	<a href="${root}/user/${post.userId}" class="poster">      
					        	<cite>${post.user.nickname}</cite>
					        	<c:if test="${(post.user.auth ne null) && (post.user.auth ne '')}">
					        	<i class="layui-icon" title="认证信息：${post.user.auth}" style="color: #FFB800;">&#xe735;</i>                
					        	</c:if>     
					        </a>
							<span class="post-time" data-ti="${post.postTime.time}"></span>          
					        <span class="close" title="${post.close == 0 ? '未结' : '已结'}">${post.close == 0 ? '未结' : '已结'}</span>        
					        <a href="${root}/post/${post.id}#PostReply" style="color:#999;">
					        <span class="reply" title="回答">
					        	<i class="layui-icon">&#xe63a;</i>
					        	${post.countReply}      
							</span>
							</a>        
					        <span class="reply" title="回答">       
					        	<i class="layui-icon">&#xe63a;</i>
					        	 ${post.countReply}     
							</span>
							<span class="viewed" title="人气">       
					            <i class="layui-icon">&#xe756;</i>
					            ${post.viewed}
							</span>
						</div>
					</li>
					</c:forEach>
					<li id="paging" class="paging" data-in="${paging.index}" data-co="${paging.count}" data-tp="${paging.totalPage}" data-li="${paging.limit}" data-qi="${paging.queryItems}"></li>
				</ul>
			</div>
			<div class="layui-col-md4 edge">
				<div class="search">
					<input type="text" placeholder="社区搜索" value="" class="layui-input">
					<i class="layui-icon">&#xe615;</i>
				</div>
				<div class="panel btns">
					<ul>
						<c:if test="${sessionScope.user.punchToday}">
						<li class="l1 btn">
						<a>
							<i class="layui-icon">&#xe605;</i>
							<span>已签到</span>
						</a>	
						</li>
						</c:if>
						<c:if test="${!sessionScope.user.punchToday}">
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
					<h3 class="title">快速入口</h3>
					<ul>
						<c:forEach begin="1" end="8">
						<li>
							<a href="" target="_blank" data-spm-anchor-id="5176.100238.spm-fast-go.1">下载</a>
						</li>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
	</div>


	<form id="postSendFormPostProcessor" action="${root}/index" method="post" style="display: none;"></form>
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
	
</body>
</html>