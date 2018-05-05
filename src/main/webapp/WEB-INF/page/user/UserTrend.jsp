<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
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
<title>WCTF - ${sessionScope.user.nickname}的主页</title>
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
	min-height: 600px;
	margin: 0;
}

.panel {
    position: relative;
    padding: 30px 0 0;
    height: 170px;
    text-align: center;
    background-color: #fff;
}

.panel>img {
    position: absolute;
    top: 40px;
    left: 50px;
    width: 180px;
    height: 180px;
    border-radius: 100%;
}
.panel .info {
    position: relative;
    top: 40px;
    margin: 0 auto;
    width: 800px;
	color: #fff;
	text-align: left;
}
.panel .info>h1 {
    margin-top: 10px;
    line-height: 30px;
    font-size: 26px;
}
.panel .info>i {
    padding-right: 5px;
    padding-left: 10px;
    color: #666;
}
.panel .info .option {
	margin-top: 5px;
}
.panel .info .option>span {
	margin: 0 10px 0 0;
}
.panel .sign {
    margin-top: 20px;
	font-size: 14px;
    line-height: 24px;
    color: #fff;
    word-break: break-all;
    word-wrap: break-word;
    opacity: .5;
}

.wrap {
    margin: 0 auto;
	position: relative;
    width: 1200px;
}
.wrap .fixed {
	height: 50px;
}
.wrap .container {
    padding-left: 100px;
}
.wrap .container .title-wrap {
	position: relative;
    margin-top: 10px;
    height: 35px;
    line-height: 20px;
    font-size: 14px;	
    color: #666;
	border-bottom: 1px solid #eee;
}
.wrap .container .title-wrap .dynamic-setting {
    position: absolute;
    top: 0px;
	left: 1010px;
	cursor: pointer;
}
.activities .item {
	margin: 20px 0;
	padding-right: 100px;	
	position: relative;
}
.activities .item:after {
	content: "020"; 
	display: block; 
	height: 0; 
	clear: both; 
	visibility: hidden;
}
.activities .item .header {
	position: relative;
    left: 30px;
	width: 40px;
	height: 40%;
	float: left;
}
.activities .item .content {
	width: 900px;
	float: right;
}
.activities .item .content .head {
	margin-bottom: 16px;
}
.activities .item .content .head .name {
    line-height: 14px;
}
.activities .item .content .head .time {
    margin-top: 10px;
    line-height: 12px;
    font-size: 12px;
	color: #93999f;
}
.activities .item .content .head .title {
    margin-top: 20px;
    line-height: 16px;
    color: #07111b;
    font-size: 15px;
}
.activities .item .content .body {
	margin-bottom: 40px;
    padding: 25px;
    height: 40px;
 	background-color: rgba(240,240,240,.1);
}
.activities .item .content .body>a>img {
    position: relative;
    top: -2px;
}
.activities .item .content .body .detail {
    padding-top: 0;
    width: 798px;
	float: right;
    line-height: 30px;
    color: #4C545C;
}
.activities .item .content .body .detail .tag {
	line-height: 12px;
    font-size: 12px;
    color: #4C545C;
}

.dynamic-setting-panel {
	padding: 0 36px;
    background-color: #fff;
}
.dynamic-setting-panel *{
	word-break: break-all!important;
}
.dynamic-setting-panel .head {
    height: 24px;
    padding: 22px 0;	
}
.dynamic-setting-panel .title {
    float: left;
    font-size: 16px;
    font-weight: 600;
    color: #07111b;	
}
.dynamic-setting-panel .close {
    margin-top: 6px;
	float: right;
    color: #93999f;
    cursor: pointer;
    transition: all .3s;	
    -webkit-transition: all .3s;
    -moz-transition: all .3s;
}
.dynamic-setting-panel .container {
    height: 405px;
    overflow: hidden;	
}
.dynamic-setting-panel .container>dl {
    position: relative;
    width: 568px;
}
.dynamic-setting-panel .container .dt-line {
    position: absolute;
    width: 100%;
    height: 1px;
    background-color: #d9dde1;
    line-height: 1px;
    top: 10px;
}
.dynamic-setting-panel .container dd {
    padding-top: 20px;
}
.dynamic-setting-panel .container .dt-tit {
	margin-right: 10px;
}
.dynamic-setting-panel .container .dd-item {
    float: left;
    width: 45%;
    padding-right: 5%;
    margin-bottom: 20px;
}
.dynamic-setting-panel .container .dd-item .switch {
    width: 40px;
    height: 20px;
}
.dynamic-setting-panel .container .dd-item .switchname {
	width: 140px;
	padding-left: 0;
}
.dynamic-setting-panel .container .submit {
	position: relative;
	top: 140px;
}
.dynamic-setting-panel .container .submit p {
	height: 40px;
	line-height: 55px;
	text-align: center;
}
.dynamic-setting-panel .container .submit span {
    position: absolute;
	top: 0px;
    left: 480px;
    width: 80px;
    height: 38px;
    line-height: 40px;
    text-align: center;
    color: #fff;
    background-color: #f01414;
    cursor: pointer;
}

</style>
</head>
<body>

	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>

	<div id="main" class="layui-clear">
		<div class="panel" style="background: url(${server}/static/image/user-page.jpg) no-repeat;">
			<img src="${server}/${requestScope.found.header}" alt="${requestScope.found.nickname}">
			<span></span>
			<div class="info">
				<h1>
					${requestScope.found.nickname} <i class="iconfont"></i>
				</h1>
				<p class="option">
					<span>
						<i class="layui-icon" title="大腿" style="color: orange;">&#xe65e;</i>
						<span style="color: #FF7200;">200 斤</span>
					</span>
					<span>
						<i class="layui-icon">&#xe60e;</i>
						<span id="userPageReg" style="color: #eee;" data-re="${requestScope.found.reg.time}"></span>
					</span>
					<span>
						<i class="layui-icon">&#xe715;</i>
						<span id="userPageAddress" style="color: #eee;" data-ad="${requestScope.found.address}"></span>
					</span>
				</p>
				<p class="sign">${(requestScope.found.sign eq "" || requestScope.found.sign eq "一句话签名") ? "这位同学很懒，木有签名的说～" : requestScope.found.sign}</p>
			</div>
		</div>
		
		<div class="wrap">
			<div class="fixed"></div>
			<div class="container">
				<div class="title-wrap">
					<c:if test="${!requestScope.other}">
					<p class="dynamic-title">Wo 的动态</p>
					<p class="dynamic-setting"><i class="layui-icon">&#xe631; </i>动态设置</p>
					</c:if>
					<c:if test="${requestScope.other}">
					<p class="dynamic-title">Ta 的动态</p>
					</c:if>
				</div>
				<div class="activities">
					<ul id="UserPageActivities"></ul>
				</div>
			</div>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/page/templet/Footer.jsp"></jsp:include>
	
	<script type="text/javascript" src="${server}/static/js/linkage.js"></script>
</body>

<c:if test="${!requestScope.other}">
<div class="dynamic-setting-panel none">
    <div class="head layui-clear">
        <span class="title">动态设置</span>
        <i class="icon-close close"></i>
    </div>
    <div class="container">
    <form class="layui-form" action="" id="UserTrendForm" data-tr="${requestScope.trendSetting}">
        <dl class="layui-clear">
            <dt style="position: relative;">
                <span class="dt-tit">问题</span>
                <span class="dt-line"></span>
            </dt>
            <dd class="clearfix">
                <div class="dd-item layui-clear">
                	<label class="switchname layui-form-label">显示用户提问动态</label>
                	<div class="switch layui-input-inline">
                    	<input id="UserTrend1" type="checkbox" name="c1" lay-skin="switch" lay-text="ON|OFF" <c:if test="${requestScope.trendSettingChars[0] eq 49}">checked</c:if> >
				    </div>
                </div>
                <div class="dd-item">
                	<label class="switchname layui-form-label">显示用户讨论动态</label>
                	<div class="switch layui-input-inline">
                    	<input id="UserTrend2" type="checkbox" name="c2" lay-skin="switch" lay-text="ON|OFF" <c:if test="${requestScope.trendSettingChars[1] eq 49}">checked</c:if> >
				    </div>
                </div>
            </dd>
        </dl>
        <dl class="layui-clear">
            <dt style="position: relative;">
                <span class="dt-tit">社区</span>
                <span class="dt-line"></span>
            </dt>
            <dd class="clearfix">
                <div class="dd-item layui-clear">
                	<label class="switchname layui-form-label">显示用户建议动态</label>
                	<div class="switch layui-input-inline">
                    	<input id="UserTrend3" type="checkbox" name="c3" lay-skin="switch" lay-text="ON|OFF" <c:if test="${requestScope.trendSettingChars[2] eq 49}">checked</c:if> >
				    </div>
                </div>
                <div class="dd-item">
                	<label class="switchname layui-form-label">显示用户分享动态</label>
                	<div class="switch layui-input-inline">
                    	<input id="UserTrend4" type="checkbox" name="c4" lay-skin="switch" lay-text="ON|OFF" <c:if test="${requestScope.trendSettingChars[3] eq 49}">checked</c:if> >
				    </div>
                </div>
            </dd>
        </dl>
	    <div class="submit">
	        <p class="noctiseTip">关闭选项后，你将不会看到别人的此类动态</p><span class="save" lay-filter="UserTrend" id="UserTrend" lay-submit>保存</span>
	    </div>
    </form>
    </div>
</div>
</c:if>
</html>
