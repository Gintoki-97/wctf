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
<title>WCTF - 用户信息设置</title>
<style type="text/css">
#main {
	position: relative;
	top: 90px; 
	width : 1079px;
	min-height: 600px;
	margin: 0 auto 15px;
}
.panel {
	height: 620px;
	background-color: #fff;
}
#main .panel .setting-nav {
	position: absolute;
    left: 0;
    top: 0;
    z-index: 999;
    height: 100%;
}
.setting-nav .layui-nav {
	position: absolute;
    left: 0;
    top: 0;
    z-index: 999;
    height: 100%;
    padding: 15px 0;
} 
.setting-nav .layui-nav .layui-nav-item .layui-icon {
	position: relative;
    top: 2px;
    font-size: 20px;
    margin-right: 10px;
} 

#main .panel .setting-panel {
	min-height: 575px;
    margin: 0 0 10px 215px;
    padding-top: 5px;
    padding: 5px 20px 0px 20px;
    border-radius: 2px;
    background-color: #fff;
    box-shadow: 0 1px 2px 0 rgba(0,0,0,.05);
}

#main .base-settings .avatar-add  {
	position: relative;
    width: 373px;
    height: 373px;
}
#main .base-settings .avatar-add img {
	position: relative;
    width: 200px;
    height: 200px;
    left: 75px;
    top: 20px;
    border-radius: 50%;
}
#main .base-settings .avatar-add button {
	position: absolute;
    left: 50%;
    top: 250px;
    margin: 0 0 0 -56px;
}
#main .base-settings .avatar-add p {
	position: absolute;
    top: 285px;
    width: 100%;
    margin-top: 10px;
    font-size: 12px;
    text-align: center;
    color: #999;
}

</style>
</head>
<body>
	<jsp:include page="/WEB-INF/page/templet/UserHeader.jsp"></jsp:include>

	<div id="main" class="layui-clear">
		<div class="panel">
			<div class="setting-nav layui-nav-tree">
				<ul class="layui-nav layui-nav-tree layui-inline">
					<li class="layui-nav-item">
						<a href="${root}/user/${sessionScope.user.id}">
							<i class="layui-icon">&#xe68e;</i>
							我的主页
						</a>
					</li>
					<li class="layui-nav-item">
				    	<a href="/u/6847008/">
							<i class="layui-icon">&#xe60c;</i>
							用户中心
						</a>
				    </li>
				  	<li class="layui-nav-item layui-this">
				    	<a>
							<i class="layui-icon">&#xe620;</i>
							基本设置
						</a>
				    </li>
				  	<li class="layui-nav-item">
				    	<a href="/u/6847008/">
							<i class="layui-icon">&#xe606;</i>
							我的消息
						</a>
				    </li>
				  	<span class="layui-nav-bar" style="top: 167.5px; height: 0px; opacity: 0;"></span>
				</ul>
			</div>
			<div class="setting-panel">
				<div class="layui-tab layui-tab-brief" lay-filter="user">
					<ul class="layui-tab-title base-settings-tab" id="LAY_mine">
						<li class="layui-this" lay-id="info">我的资料</li>
						<li lay-id="avatar">头像</li>
						<li lay-id="pass">密码</li>
						<li lay-id="bind">帐号绑定</li>
					</ul>
					<div class="layui-tab-content base-settings" style="padding: 20px 0;">
						<div class="layui-form layui-form-pane layui-tab-item info layui-show">
							<form method="post" id="userSettingForm">
								<div class="layui-form-item">
							    	<label for="L_email" class="layui-form-label">邮箱</label>
							     	<div class="layui-input-inline">
							        	<input type="text" id="L_email" name="account" required lay-verify="email" autocomplete="off" value="${sessionScope.user.account}" class="layui-input" readonly="readonly">
							        </div>
							        <div class="layui-form-mid layui-word-aux">
							        	如果您在邮箱已激活的情况下，变更了邮箱，需
							        	<a href="/user/activate/" style="font-size: 12px; color: #4f99cf;">重新验证邮箱</a>。
							        </div>
							    </div>
							    <div class="layui-form-item">
							    	<label for="L_username" class="layui-form-label">昵称</label>
							    	<div class="layui-input-inline">
							    		<input type="text" name="nickname" required lay-verify="set_ni" autocomplete="off" value="${sessionScope.user.nickname}" class="layui-input">
							    	</div>
								</div>
								<div class="layui-form-item">
							    	<label class="layui-form-label">性别</label>
							    	<div class="layui-input-block gender" data-gender="${sessionScope.user.gender}">
								      	<input type="radio" name="gender" value="0" title="保密">
								      	<input type="radio" name="gender" value="1" title="男">
								      	<input type="radio" name="gender" value="2" title="女">
								  	</div>
							  </div>
							  <div class="layui-form-item">
							  	<label class="layui-form-label">出生日期</label>
							  <div class="layui-input-inline">
							  	<input lay-verify="required" class="layui-input" type="text" name="birthday" id="userSettingBirth" data-bi="${sessionScope.user.birthday.time}">
							  </div>
							  </div>
							  <div id="userSettingAddress" class="layui-form-item" data-ad="${requestScope.address == null ? user.address : requestScope.address}">
							    <label class="layui-form-label">地址</label>
							    <div class="layui-input-inline">
							    	<select lay-verify="required" lay-filter="choosePro" id="choosePro" name="province">
							        	<option id="defaultPro" value="-1">省份</option>
							     	</select>
							    </div>
							    <div class="layui-input-inline">
							      	<select lay-verify="required" lay-filter="chooseCity" id="chooseCity" name="city">
							        	<option id="defaultCity" value="-1">城市</option>
							     	</select>
							    </div>
							    <div class="layui-input-inline">
							      	<select lay-verify="required" id="chooseCounty" name="county">
							        	<option id="defaultCountry" value="-1">区、县</option>
							     	</select>
							    </div>
							   	<div title="定位" class="layui-input-inline request" style="position: absolute;width: 20px;height: 38px;text-align: center;line-height: 38px;cursor:pointer;">
							     	<i class="layui-icon">&#xe715;</i>
							   	</div>
							</div>
							<div class="layui-form-item"">
								<label for="L_city" class="layui-form-label">详细地址</label>
								<div class="layui-input-block" style="width: 590px;">
									<input type="text" name="location" autocomplete="off" class="layui-input" value="${sessionScope.user.location}">
								</div>
							</div>
							<div class="layui-form-item layui-form-text">
								<label for="L_sign" class="layui-form-label">签名</label>
								<div class="layui-input-block">
									<textarea lay-verify="set_si" placeholder="随便写些什么刷下存在感" name="sign" autocomplete="off" class="layui-textarea" style="height: 80px;">${sessionScope.user.sign }</textarea>
								</div>            
							</div>
							<div class="layui-form-item">
								<button class="layui-btn" key="set-mine" lay-filter="UserSetting" lay-submit>确认修改</button>            
							</div>          
						</form>
					</div>
					<div class="layui-form layui-form-pane layui-tab-item avatar">  
						<div class="layui-form-item">              
							<div class="avatar-add">                
								<input class="layui-upload-file" type="file" name="file">
								<img src="${sessionScope.user.header}" id="reviewAvatar">
								<button type="button" class="layui-btn upload-img" id="uploadAvatar">                  
									<i class="layui-icon"></i>
									上传头像                
								</button>
								<p>建议尺寸168*168，支持jpg、png、gif，最大不能超过50KB</p>                
								<span class="loading"></span>              
							</div>            
						</div>
					</div>                    
					<div class="layui-form layui-form-pane layui-tab-item pass">  
						<form method="post" id="resetPwdForm">                                       
							<div class="layui-form-item">                
								<label for="L_nowpass" class="layui-form-label">当前密码</label>                
								<div class="layui-input-inline">                  
									<input type="password" id="resetPwdNow" name="nowpass" required lay-verify="set_pw" autocomplete="off" class="layui-input">                
								</div>              
							</div>                            
							<div class="layui-form-item">                
								<label for="L_pass" class="layui-form-label">新密码</label>                
								<div class="layui-input-inline">                  
									<input type="password" id="resetPwdNew" name="pass" required lay-verify="set_np" autocomplete="off" class="layui-input">                
								</div>                
								<div class="layui-form-mid layui-word-aux">6到16个字符</div>              
							</div>              
							<div class="layui-form-item">                
								<label for="L_repass" class="layui-form-label">确认密码</label>                
								<div class="layui-input-inline">                  
									<input type="password" id="resetPwdNew2" name="repass" required lay-verify="set_np" autocomplete="off" class="layui-input">               
								</div>              
							</div>              
							<div class="layui-form-item">                
								<button class="layui-btn" key="set-mine" lay-filter="UserReset" lay-submit>确认修改</button>              
							</div> 
						</form>                     
					</div>                    
					<div class="layui-form layui-form-pane layui-tab-item bind">            
						<ul class="app-bind">              
							<li class="fly-msg  ">                
								<i class="iconfont icon-qq"></i>                                 
								<a href="http://fly.layui.com/app/qq" onclick="layer.msg('正在绑定微博QQ', {icon:16, shade: 0.1, time:0})" class="acc-bind" type="qq_id">立即绑定</a>                 
								<span>，即可使用QQ帐号登录Fly社区</span>                               
							</li>              
							<li class="fly-msg ">                
								<i class="iconfont icon-weibo"></i>                                
								<a href="http://fly.layui.com/app/weibo/" class="acc-weibo" type="weibo_id" onclick="layer.msg('正在绑定微博', {icon:16, shade: 0.1, time:0})">立即绑定</a>                
								<span>，即可使用微博帐号登录Fly社区</span>                              
							</li>            
						</ul>          
					</div>        
				</div>      
			</div>
		</div>
	</div>
	</div>

<script type="text/javascript" src="${server}/static/layui/layui.js"></script>
<script type="text/javascript" src="${server}/static/js/jquery.min.js"></script>
<script type="text/javascript" src="${server}/static/js/codec.js"></script>
<script type="text/javascript" src="${server}/static/js/linkage.js"></script>
<script type="text/javascript" src="${server}/static/js/static.js"></script>
<c:if test="${msg ne null }">
	<input id="msg" type="hidden" value="${msg }">
	<script type="text/javascript">
		var msg = $("#msg").val();
		layer.open({
		  title: '消息'
		  ,content: msg
		});  
	</script>
<%
	session.removeAttribute("msg");
%>
</c:if>
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
