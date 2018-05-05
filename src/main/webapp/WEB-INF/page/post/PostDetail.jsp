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
<title>WCTF - ${post.title}</title>
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

.warp .content>.post>.title {
	font-size: 24px;
	line-height: 36px;
}

.warp .content>.post .count-info {
	position: relative;
	margin: 10px 0 15px;
}

.warp .content>.post>.count-info .fly-list-nums {
	position: absolute;
	right: 0;
}

.warp .content>.post .count-info .layui-badge {
	border-radius: 2px;
}

.warp .content>.post .poster-info {
	position: relative;
	line-height: 20px;
	padding: 15px 15px 15px 75px;
	font-size: 13px;
	background-color: #f8f8f8;
	color: #999;
}

.warp .content>.post .poster-info .avatar {
	position: absolute;
	left: 15px;
	top: 15px;
}

.warp .content>.post .poster-info .avatar img {
	display: block;
	width: 45px;
	height: 45px;
	margin: 0;
	border-radius: 2px;
}

.warp .content>.post .post-content {
	margin: 20px 0 0;
	min-height: 306px;
	line-height: 26px;
	font-size: 16px;
	color: #333;
	word-wrap: break-word;
}

.warp .content>.post .post-content blockquote {
	padding: 10px 15px;
	margin: 10px 0;
	font-size: 12px;
	border-left: 6px solid #009688;
	font-family: Courier New;
	overflow: auto;
	border-color: #e6e6e6;
	border-style: solid;
	border-width: 1px 1px 1px 5px;
	background: 0 0;
}

.warp .content>.post .post-content ol, .warp .content>.post .post-content ul
	{
	padding: 20px;
}

.warp .content>.post .post-content ol, .warp .content>.post .post-content ol li
	{
	list-style: decimal;
}

.warp .content>.post .post-content ul, .warp .content>.post .post-content ul li
	{
	list-style: disc;
}

.warp .content>.replys .reply {
	position: relative;
	padding: 20px 0 10px;
	border-bottom: 1px dotted #DFDFDF;
}

.warp .content>.replys .reply .replyer {
	position: relative;
	line-height: 20px;
	font-size: 13px;
	color: #999;
	padding: 0 0 0 55px;
	background: none;
}

.warp .content>.replys .reply .replyer .avatar {
	position: absolute;
	top: 0;
	left: 0;
}

.warp .content>.replys .reply .replyer .avatar img {
	display: block;
	width: 45px;
	height: 45px;
	margin: 0;
	border-radius: 2px;
}

.warp .content>.replys .reply .replyer .name {
	padding-right: 10px;
	font-size: 14px;
}

.warp .content>.replys .reply .reply-content {
	margin: 25px 0 20px;
	min-height: 0;
	line-height: 24px;
	font-size: 14px;
	color: #333;
	word-wrap: break-word;
}

.warp .content>.replys .reply .reply-info {
	position: relative;
	margin-bottom: 20px;
}

.warp .content>.replys .reply .reply-info>span {
	padding-right: 20px;
	color: #999;
	cursor: pointer;
}

.warp .content>.replys .reply .reply-info>span>i {
	font-size: 22px;
	position: relative;
	top: 4px;
}

.warp .content>.replys .reply .reply-info>span>em {
	font-style: normal;
}

.warp .content>.replys .reply .reply-info>span {
	cursor: pointer;
}

.warp .content>.replys .reply .reply-info>span:hover {
	color: #FF4444;
}
.warp .content>.replys .reply .reply-info .fold {
	position: absolute;
    top: -3px;
    left: 620px;
    width: 40px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    color: #999;
    cursor: pointer;
}

.warp .content>.replys .reply .slreplys {
    display: block;
	position: relative;
	margin-bottom: 20px;
    padding-top: 20px;
	border: 1px solid rgb(238, 238, 238);
}
.warp .content>.replys .reply .slreplys .slreplyer {
    position: relative;
	min-height: 50px;
    line-height: 20px;
    font-size: 13px;
    color: #999;
    padding: 0 0 0 55px;
    background: none;
}
.warp .content>.replys .reply .slreplys .slreplyer .avatar {
    position: absolute;
    top: 0;
    left: 20px;
}
.warp .content>.replys .reply .slreplyer .avatar img {
    display: block;
    width: 24px;
    height: 24px;
    margin: 0;
    border-radius: 2px;
}
.warp .content>.replys .reply .slreplyer .name {
    padding-right: 10px;
    font-size: 14px;
    position: absolute;
}
.warp .content>.replys .reply .slreplyer .detail-hits {
    position: absolute;
    top: 1px;
    left: 570px;
}
.warp .content>.replys .reply .slreplyer .content {
	position: relative;
    width: 540px;
    line-height: 22px;
	font-size: 9px;
}
.warp .content>.replys .reply .slreplyer .content img {
    width: 4%;
}
.warp .content>.replys .reply .slreplyer .slreplyer-detail .slreplyer-info {
	height: 20px;
    margin-bottom: 8px;
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

	<div id="classify" class="fly-panel fly-column"
		style="margin-bottom: 15px; position: relative; top: 65px; line-height: 50px; border-radius: 2px; background-color: #fff; box-shadow: 0 1px 2px 0 rgba(0, 0, 0, .05);">
		<div class="layui-container">
			<ul class="layui-clear">
				<li class="layui-hide-xs"><a href="${root}/index">首页</a></li>
				<li class="dis"><a href="${root}/post/discuss">讨论</a></li>
				<li class="que"><a href="${root}/post/question">提问 </a></li>
				<li class="sug"><a href="${root}/post/suggest">建议</a></li>
				<li class="sha"><a href="${root}/post/sharing">分享</a></li>
				<li><a href="${root}/post/notice">公告</a></li>
				<li><a href="${root}/news/activity">动态</a></li>
				<li><a href="${root}/posts/1">我的贴</a></li>
			</ul>
		</div>
	</div>

	<div id="main" class="layui-clear">
		<div class="layui-row layui-col-space15 warp">
			<div class="layui-col-md8 content">
				<div class="post panel">
					<h2 class="title">${post.title}</h2>
					<div class="count-info">
						<span class="layui-badge layui-bg-green fly-detail-column">
							${post.classifyStr} </span>
						<c:if test="${post.close == 0}">
							<span class="layui-badge" style="background-color: #999;">未结</span>
						</c:if>
						<c:if test="${post.close != 0}">
							<span class="layui-badge" style="background-color: #999;">已结结</span>
						</c:if>
						<c:if test="${post.top > 0}">
							<span class="layui-badge layui-bg-black">置顶</span>
						</c:if>
						<span class="fly-list-nums" style="color: #666;"> <a
							href="${root}/post/${post.id}#PostReply" style="margin: 0 10px; color: #666;"> <i
								class="layui-icon" title="回答">&#xe63a;</i> ${post.countReply}
						</a> <i class="layui-icon" title="人气">&#xe756;</i> ${post.viewed}
						</span>
					</div>
					<div class="poster-info">
						<a class="avatar"> <img src="${server}/${post.user.header}"
							alt="${post.user.nickname}">
						</a>
						<div class="fly-detail-user">
							<a href="" class="fly-link"> <cite>${post.user.nickname}</cite>
								<c:if
									test="${(post.user.auth ne null) && (post.user.auth ne '')}">
									<i class="layui-icon" title="认证信息：${post.user.auth}"
										style="color: #FFB800; position: relative; top: 1px;">&#xe735;</i>
								</c:if>
							</a> <span class="post-time" data-ti="${post.postTime.time}"></span>
						</div>
						<div class="detail-hits" style="height: 25px;">
							<span
								style="padding-right: 10px; color: #FF7200; position: relative; top: 5px;">悬赏：20 斤</span>
							<c:if test="${post.collect}">
							<span class="layui-btn layui-btn-mini jie-admin"
								style="position: relative; top: 2px; left: 20px; color: #fff; padding: 0 5px; height: 20px; line-height: 20px;">已收藏</span>
							</c:if>
							<c:if test="${!post.collect}">
							<span class="layui-btn layui-btn-mini collect"
								style="position: relative; top: 2px; left: 20px; color: #fff; padding: 0 5px; height: 20px; line-height: 20px;" data-co="${post.id}">收藏</span>
							</c:if>
						</div>
					</div>
					<div class="post-content">${post.content}</div>
				</div>
				<div id="PostReply" class="replys panel">
					<fieldset class="layui-elem-field layui-field-title"
						style="text-align: center;">
						<legend>回贴</legend>
					</fieldset>
					<ul class="replys-list" style="margin-bottom: 50px;">
						<c:if
							test="${requestScope.post.replys == null || requestScope.post.replys.size() eq 0}">
							<p
								style="height: 50px; line-height: 50px; text-align: center; font-size: 18px; color: #999; margin: 20px 0px;">
								暂无回贴</p>
						</c:if>
						<c:if test="${requestScope.post.replys.size() gt 0}">
							<c:forEach items="${requestScope.post.replys}" var="reply"
								varStatus="status">
								<li class="reply" data-li="${reply.id}">
									<div class="replyer">
										<a class="avatar" href="${root}/user/${reply.userId}"> <img
											src="${server}/${reply.user.header}" alt="${reply.user.nickname}">
										</a>
										<div class="name">
											<a href="${root}/user/${reply.userId}" class="fly-link">
												<cite>${reply.user.nickname}</cite>
											</a>
										</div>
										<div class="detail-hits" style="position: relative; top: 8px;">
											<span class="reply-time" data-ti="${reply.replyTime.time}"></span>
										</div>
									</div>
									<div class="reply-content">${reply.content}</div>
									<div class="reply-info">
										<span class="thumb" title="赞同" data-th="${reply.thumbed eq null ? 0 : reply.thumbed}"> <i
											class="layui-icon">&#xe6c6;</i> <em>${reply.thumb}</em>
										</span> <span class="slreply"> <i class="layui-icon">&#xe63a;</i>
											回复<c:if test="${reply.slReplys.size() gt 0}">(${reply.slReplys.size()})</c:if>
										</span>
										<c:if test="${reply.slReplys.size() gt 0}">
										<div class="fold"> 
											 <i class="layui-icon up" style="display:block;" title="收起">&#xe619;</i>
											 <i class="layui-icon down" style="display:none;" title="下拉">&#xe61a;</i>
										</div>
										</c:if>
									</div>
									<c:if test="${reply.slReplys ne null && reply.slReplys.size() gt 0}">
									<div class="slreplys">
										<ul style="margin:0 15px;">
										<c:forEach items="${reply.slReplys}" var="slReply" varStatus="status">
										<c:if test="${status.count ne reply.slReplys.size()}">
										<li style="margin-bottom: 10px;border-bottom: 1px solid #eee;padding-bottom: 10px;">
											<div class="slreplyer">
												<a class="avatar" href="${root}/user/${slReply.user.id}"> <img src="${server}/${slReply.user.header}" alt="${slReply.user.nickname}"></a>
												<div class="slreplyer-detail">
													<div class="slreplyer-info">
														<div class="name">
															<a href="${root}/user/${slReply.user.id}" class="fly-link">
																<cite>${slReply.user.nickname}</cite>
															</a>
														</div>
														<div class="detail-hits">
															<span class="slreply-time" data-ti="${slReply.replyTime.time}"></span>
														</div>
													</div>
													<div class="content">
														${slReply.content}
													</div>
												</div>
											</div>	
										</li>	
										</c:if>
										<c:if test="${status.count eq reply.slReplys.size()}">
										<li style="margin-bottom: 10px;">
											<div class="slreplyer">
												<a class="avatar" href="${root}/user/${slReply.user.id}"> <img src="${server}/${slReply.user.header}" alt="${slReply.user.nickname}"></a>
												<div class="slreplyer-detail">
													<div class="slreplyer-info">
														<div class="name">
															<a href="${root}/user/${slReply.user.id}" class="fly-link">
																<cite>${slReply.user.nickname}</cite>
															</a>
														</div>
														<div class="detail-hits">
															<span class="slreply-time" data-ti="${slReply.replyTime.time}"></span>
														</div>
													</div>
													<div class="content">
														${slReply.content}
													</div>
												</div>
											</div>	
										</li>	
										</c:if>
										</c:forEach>
										</ul>
									</div>
									</c:if>
								</li>
							</c:forEach>
						</c:if>
					</ul>
					<div class="layui-form layui-form-pane">
						<fieldset class="layui-elem-field layui-field-title" style="text-align: center;">
							<legend>回复</legend>
						</fieldset>
						<form method="post">
							<input type="hidden" name="postId" value="${post.id}"
								readonly="readonly">
							<div class="layui-form-item">
								<div class="layui-input-block" style="margin-left: 0px;">
									<textarea class="post-send-editor" id="postReplyContent"
										name="content" placeholder="详细描述" class="layui-textarea none"
										style="height: 260px;"></textarea>
								</div>
							</div>
							<div class="layui-form-item">
								<button class="layui-btn" lay-filter="PostReply" lay-submit>提交回复</button>
							</div>
						</form>
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


	<form id="postSendFormPostProcessor" action="${root}/index"
		method="post" style="display: none;"></form>
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
	
</body>
</html>