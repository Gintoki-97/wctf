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
<title>WCTF - 世界 CTF 大赛交流社区</title>
<style type="text/css">
#indexCarousel {
    position: relative;
    top: 65px;
    width: 1079px;
    height: 520px!important;
    margin: 0 auto 15px;
}
#indexCarousel img {
    width: 100%;
    height: 100%;
}
#main {
	position: relative;
	top: 90px; 
	width : 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
}
#main .warp {
	float: left;
    width: 100%;
    min-height: 300px;
}
.warp .content {
    margin-right: 351px;
}
.warp .content .tab {
	background-color: #fff;
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
    overflow: hidden;
	text-overflow:ellipsis;
	white-space: nowrap;
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

#main .edge {
    position: relative;
    float: left;
    top: 10px;
    width: 336px;
    margin-left: -336px;
}
.edge .search {
	background-color: #fff;
	margin-bottom: 15px;
    border-radius: 2px;
}
.edge .search .layui-icon {
    position: absolute;
    top: 1px;
    left: 297px;
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
.edge .btns .btn a {
	height: 100%;
	display: block;
}
.edge .btns .btn a:hover {
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
	color: #999;
}
.edge .activities .activity>i  {
    font-size: 12px;
}
.edge .activities .activity>div  {
	max-height: 80px;
    overflow: hidden;
}
.edge .activities .activity h3  {
    font-size: 14px;
	color: #999;
}
.edge .activities .activity a {
    font-size: 12px;
	color: #333;
}
.edge .activities .activity a:hover {
	color: #000;
	text-decoration: none;
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

</style>
</head>
<body>

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>
	
	<div class="layui-carousel" id="indexCarousel">
		<div carousel-item>
    		<div><img src="${server}/static/image/carousel-01.jpg" alt=""></div>
    		<div><img src="${server}/static/image/carousel-02.jpg" alt=""></div>
    		<div><img src="${server}/static/image/carousel-01.jpg" alt=""></div>
    		<div><img src="${server}/static/image/carousel-02.jpg" alt=""></div>
    		<div><img src="${server}/static/image/carousel-01.jpg" alt=""></div>
  		</div>
	</div>
	
	<div id="main" class="layui-clear">
		<div class="warp">
			<div class="content">
				<div class="tab">
					<div class="layui-tab layui-tab-brief">
						<ul class="layui-tab-title">      
							<li class="layui-this">
        						<a href="${root}/index">全部帖</a>
              				</li>
                    		<li>
                            	<a href="${root}/post/discuss">讨论</a>
                            </li>      
                            <li>
                            	<a href="${root}/post/question">提问</a>   
                            </li>    
                            <li>      
                            	<a href="${root}/post/suggest">建议</a>  
                       		</li>   
                            <li>  
                            	<a href="${root}/post/sharing">分享</a>  
                     		</li>         
                    		<li>   
                      			<a href="">我的帖</a> 
                        	</li>       
                      	</ul> 
                	</div>
				</div>
				
				<ul class="posts panel top">
					<c:forEach items="${requestScope.tops}" var="top" varStatus="status">
					<li class="post">
						<a href="${root}/user/${top.userId}" class="avatar">
							<img src="${top.user.header}" alt="纸飞机">
						</a>
						<h2 class="title">
							<c:if test="${post.classifyStr ne null && post.classifyStr ne ''}">
							<a class="badge">
								<span class="classify" title="${post.classifyStr}">${post.classifyStr}</span>
							</a>
							</c:if>
					    	<a href="${root}/post/${top.id}">${top.title}</a>
					    	<span>置顶</span>
					    </h2>
					    <div class="info">    
					    	<a href="${root}/user/${top.userId}" class="poster">      
					        	<cite>${top.user.nickname}</cite>
					        	<c:if test="${(top.user.auth ne null) && (top.user.auth ne '')}">
					        	<i class="layui-icon" title="认证信息：${top.user.auth}" style="color: #FFB800;">&#xe735;</i>                
					        	</c:if>     
					        </a>
							<span class="post-time" data-ti="${top.postTime.time}"></span>
							<a href="${root}/post/${top.id}#PostReply" style="color:#999;">
					        <span class="reply" title="回答">
					        	<i class="layui-icon">&#xe63a;</i>
					        	 ${top.countReply}
							</span>
							</a>
							<span class="viewed" title="人气">       
					            <i class="layui-icon">&#xe756;</i>
					            ${top.viewed}
							</span>
						</div>
					</li>
					</c:forEach>
				</ul>
				
				<ul class="posts panel">
					<c:forEach items="${requestScope.posts}" var="post" varStatus="status">
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
					        	<c:if test="${(post.user.auth ne null) and (post.user.auth ne '')}">
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
							<span class="viewed" title="人气">       
					            <i class="layui-icon">&#xe756;</i>
					            ${post.viewed}
							</span>
						</div>
					</li>
					</c:forEach>
					<c:if test="${paging eq null}">
					<div class="more">
						<div class="more-box">
							<a href="${root}/index/page/1#main">查看更多</a>
						</div>	
					</div>
					</c:if>
					<c:if test="${paging ne null}">
					<li id="paging" class="paging" data-in="${paging.index}" data-co="${paging.count}" data-tp="${paging.totalPage}" data-li="${paging.limit}" data-qi="${paging.queryItems}"></li>
					</c:if>
				</ul>
				
			</div>
		</div>
		<div class="edge">
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
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>

</body>
</html>
