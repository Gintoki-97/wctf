//一般直接写在一个js文件中
;!function() {
	
	// -----------------------
	// Global params config
	// -----------------------
	var base = $("base").attr("href");
	var Global = {
			web_root  : "wctf",
			web_email : "common/email",
			user_login: "user/login",
			user_reg  : "user/reg",
			user_avatar  : "user/avatar",
			user_setting : "user/setting",
			user_rsp  : "user/reset",
			user_punch: "user/punch",
			user_trend: "",
			user_trend_Setting:"user/trend",
			post_send : "post/send",
			post_reply: "post/reply",
			post_coll : "post/coll",
			res		  : "res",
			res_down  : "res/down",
			sl_reply  : "reply",
			reply_thumb  : "reply/thumb",
			util_loc  : "http://114.67.139.119/location/code",
			url_stamp : "?stamp=U2FsdGVkX1+OBmjsb+WDw/dT7V+e9sgdYV31jN/mzOAfCOsPMNxD0wt2g3e5EcSR",
			url_stamp_date : "?stamp=" + new Date(),
			url_stamp_rand : "?stamp=" + Math.random(),
			editor : 0
	};
	Global.user = $("#GlobalUser").val();
	Global.server = $("#GlobalServer").val();
	Global.root = $("#GlobalRoot").val();
	
	/**
	 * Note. 判断一个字符串是否是空字符串
	 */
	var isEmpty = function(str) {
		if(str == null) {
			return true;
		}
		if(typeof(str) == "undefined") {
			return true;
		}
		if(str.length == 0) {
			return true;
		}
		return false;
	}
	
	// -----------------------
	// Global params config end
	// -----------------------
	
	// -----------------------
	// layui 入口
	// -----------------------
	layui.use(['layer', 'form', 'upload', 'laydate', 'laypage', 'carousel', 'util', 'layedit', 'element'], function() {
		var layer = layui.layer, form = layui.form, upload = layui.upload, laydate = layui.laydate, laypage = layui.laypage, carousel = layui.carousel, util = layui.util, layedit = layui.layedit, element = layui.element;
		
		// -----------------------
		// 0. layui 数据准备
		// -----------------------
		// -------------------
		// 0.1 layui 富文本编辑器
		// -------------------
		layedit.set({
			uploadImage: {
				url: '/common/upload', 		// 接口url
				type: 'post' 				// 默认post
			}
		});
		// 渲染编辑器
		var editor = layedit.build('layuiPostSendContent', {
			tool: ['face', 'link', 'unlink', 'image', 'strong', 'italic', 'underline', 'del', '|', 'left', 'center', 'right']
		});
		var replyEditor = layedit.build('postReplyContent', {
			tool: ['face', 'link', 'unlink', 'image', 'strong', 'italic', 'underline', 'del', '|', 'left', 'center', 'right']
		});
		var slreplyEditor = null;
		
		// -----------------------
		// 1. layui 表单验证模块
		// -----------------------
		form.verify({
			log_ac: [/^([\w_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$/, '用户名或密码错误'],
			log_pw: [/^[\w_-]{6,16}$/, '用户名或密码错误'],
			log_ca: [/^[\w]{4}$/, '验证码错误'],
			reg_ac: [/^[\w_-]{1,16}$/, '请输入正确的邮箱'],
			reg_ed: [/^[^0]+$/, '请选择邮箱服务器'],
			reg_pw: [/^[\w_-]{6,16}$/, '密码格式为6-16个字母，数字，下划线，横线组合'],
			reg_ca: [/^[0-9]{4}$/, '验证码错误' ],
			reg_ni: [/^[\w_-\u4e00-\u9fa5]{1,16}$/, '昵称长度不得超过16个字符'],
			set_ni: [/^[\w_-\u4e00-\u9fa5]{1,16}$/, '昵称长度不得超过16个字符'],
			set_si: [/^[\S]{1,50}$/, '签名为1~50个字符'],
			set_pw: [/^[\w_-]{6,16}$/, '密码不正确'],
			set_np: [/^[\w_-]{6,16}$/, '密码格式不正确'],
			pos_cl: [/^[\S]+$/, '请选择发帖分类'],
			pos_ti: [/^[\S]{1,50}$/, '标题长度为1~50个字符'],
			pos_co: [/^[\S]{1,50000}$/, '内容长度为1~50000个字符'],
			pos_ca: [/^[\w]{4}$/, '验证码错误']
		});
		
		// -----------------------
		// 2. layui 表单提交模块
		// -----------------------
			// -----------------------
			// 2.1 layui 登录表单提交
			// -----------------------
		form.on('submit(UserLogin)', function(data) {
			data.field.password = hex_md5(data.field.password);
			data.field.emailLogin = "true";
			data.field.rememberMe = "true";
			$.ajax({
				url: base + Global.user_login,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					account: data.field.account,
					password: data.field.password,
					captcha: data.field.imageCaptcha,
					emailLogin: data.field.emailLogin,
					rememberMe: data.field.rememberMe
				},
				success: function(resp){
					if(!resp.success) {
						// status code: 120 ~ 149
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
						try {
							//回显账号，清除密码和验证码，更新验证码
							$("#email").val(resp.param.account);
							$("#pass").val("");
							if(resp.param.validate) {
								$("#captcha").css("display", "block");
								$("#capt").removeAttr("disabled");
								$("#capt").attr("lay-verify", "log_ca");
								$("#capt").val("");
								$("#image-validate").trigger("click");
							}
						} catch(e) {
							console.log(e);
						}
					} else {
						// 登录成功，提交后置处理表单
//						var referer = null;
//						try {
//							referer = resp.param.referer;
//							if(referer == null || referer == "") {
//								 referer = "/wctf/index";
//							}
//						} catch(e) {
//							referer = "/wctf/index";
//						}
//						referer = referer + Global.url_stamp;
//						$("#userLoginFormPostProcessor").attr("action", "/wctf/index");
						$("#userLoginFormPostProcessor").submit();
					}
					
				},
				error: function(XMLHttpRequest, textStatus, errorThrown){
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
			// -----------------------
			// 2.2 layui 注册表单提交
			// -----------------------
		form.on('submit(UserReg)', function(data) {
			data.field.password = hex_md5(data.field.password);
			data.field.account = data.field.account + "@" + data.field.emailDomain;
			$.ajax({
				url: base + Global.user_reg,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					account: data.field.account,
					emailCaptcha: data.field.emailCaptcha,
					password: data.field.password,
					nickname: data.field.nickname
				},
				success: function(resp) {
					if(!resp.success) {
						// status code: 150 ~ 179
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
						// 回显数据
						$("#nick").val(resp.param.nickname);
						$("#capt").val("");
						$("#image-validate").attr("src", "common/captcha" + Global.url_stamp_date);
						// 回显邮箱账号和域名
						if(resp.param.account != null) {
							var email = resp.param.account.split("@")[0];
							var domain = resp.param.account.split("@")[1];
							$("#email").val(email);
							$("#emailDomain").val(email);
						}
					} else {
						var regLoad = layer.load(2, {
							shade: [0.1,'#fff'], 
							time: 2000
						});
						// 注册成功，提交后置处理表单
						$.ajax({
							url: base + Global.user_login,
							type: "POST",
							async: true,
							scriptCharset: "utf-8",
							data: {
								account: resp.param.account,
								password: resp.param.password,
								emailLogin: "true",
								rememberMe: "true"
							},
							success: function(resp) {
								layer.close(regLoad);
								$("#userRegFormPostProcessor").submit();
							},
							error: function(XMLHttpRequest, textStatus, errorThrown){
								layer.close(regLoad);
								layer.open({
									title: '来自外太空',
									content: '注册成功，登录过程出错，请刷新再试。'
								});
							}
						});
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
			// -----------------------
			// 2.3 layui 修改信息表单提交
			// -----------------------
		form.on('submit(UserSetting)', function(data) {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			var url = base + Global.user_setting + "/" + Global.user;
			$.ajax({
				url: url,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					id: Global.user,
					nickname: data.field.nickname,
					gender: data.field.gender,
					birthday: data.field.birthday,
					address: data.field.county,
					location: data.field.location,
					sign: data.field.sign
				},
				success: function(resp) {
					layer.open({
						title: '来自服务器',
						content: resp.msg
					});
				},
				/**
				 * 1. Please check whether the request address is legal.
				 * 2. Please check whether the dataType is json.			// resolved
				 * */
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
			// -----------------------
			// 2.4 layui 重置密码表单提交
			// -----------------------
		form.on('submit(UserReset)', function(data) {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			data.field.nowpass = hex_md5(data.field.nowpass);
			data.field.pass = hex_md5(data.field.pass);
			data.field.repass = hex_md5(data.field.repass);
			if(data.field.pass == data.field.repass) {
				$.ajax({
					url: base + Global.user_rsp + "/" + Global.user,
					type: "POST",
					async: true,
					scriptCharset: "utf-8",
					data: {
						nowpass: data.field.nowpass,
						pass: data.field.pass
					},
					success: function(resp) {
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
						if(resp.success) {
							/*
							$("#resetPwdFormPostProcessor").attr("action", $("#resetPwdFormPostProcessor").attr("action") + Global.url_stamp_date);
							$("#resetPwdFormPostProcessor").submit();*/
						} else {
							// 清空回显
							$("#resetPwdNow").val("");
							$("#resetPwdNew").val("");
							$("#resetPwdNew2").val("");
						}
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						layer.open({
							title: '来自外太空',
							content: '未知错误，请稍后再试'
						});
					} 
				});
			} else {
				layer.open({
					title: '来自外太空',
					content: '两次输入密码不一致'
				});
			}
			return false;
		});
			// -----------------------
			// 2.5 layui 发贴表单提交
			// -----------------------
		form.on('submit(PostSend)', function(data) {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			var content = data.field.content;
			if(Global.editor == 0) {
				content = layedit.getContent(editor);
			}
			$.ajax({
				url: base + Global.post_send,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					classify : data.field.classify,
					title : data.field.title,
					content : content,
					coin : data.field.coin,
					imageCaptcha : data.field.imageCaptcha,
					userId: Global.user
				},
				success: function(resp) {
					if(!resp.success) {
						// status code: 300 ~ 339
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
						try {
							// 回显表单
							$("#capt").val("");
							$("#image-validate").attr("src", "common/captcha" + Global.url_stamp_date);
						} catch(e) {
							console.log(e);
						}
					} else {
						// 发表成功，提交后置处理表单
						$("#postSendFormPostProcessor").attr("action", $("#postSendFormPostProcessor").attr("action") + Global.url_stamp_date);
						$("#postSendFormPostProcessor").submit();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
			// -----------------------
			// 2.6 layui 发贴回复表单提交
			// -----------------------
		form.on('submit(PostReply)', function(data) {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			// 同步编辑器的内容
			var content = layedit.getContent(replyEditor);
			if(content.length <= 0) {
				layer.msg('回复内容不能为空', {icon: 5});
				return false;
			}
			if(content.length > 5000) {
				layer.msg('回复内容过长', {icon: 5});
				return false;
			}
			var url = base + Global.post_reply;
			$.ajax({
				url: url,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					userId: Global.user,
					postId: data.field.replyId,
					content: content
				},
				success: function(resp) {
					if(resp.success) {
						location.reload(true);
					} else {
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
					}
				},
				/**
				 * 1. Please check whether the request address is legal.
				 * 2. Please check whether the dataType is json.			// resolved
				 * */
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
			// -----------------------
			// 2.6 layui 发贴回复表单提交
			// -----------------------
		form.on('submit(SlReply)', function(data) {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			// 同步编辑器的内容
			var content = layedit.getContent(slreplyEditor);
			if(content.length <= 0) {
				layer.msg('回复内容不能为空', {icon: 5});
				return false;
			}
			if(content.length > 200) {
				layer.msg('回复内容过长', {icon: 5});
				return false;
			}
			var replyId = $("#sleditor").parent().attr("data-li");
			var url = base + Global.sl_reply + "/" + replyId;
			$.ajax({
				url: url,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					userId: Global.user,
					replyId: replyId,
					content: content
				},
				success: function(resp) {
					if(resp.success) {
						// 重新加载当前页，不经过缓存
						location.reload(true);
					} else {
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
			return false;
		});
		
		// -------------------
		// 3. layui 文件上传模块
		// -------------------
		var uploadAvatar = upload.render({
			elem: '#uploadAvatar',  				// 绑定元素
			url: base + Global.user_avatar, 		// 上传接口
			data: {
				id: Global.user
			},
			before: function(obj) {
				layer.load(1, {
					shade: [0.05, '#fff']
				});
			},
			done: function(resp, index, upload){
				layer.closeAll('loading');
				if(resp.success) {
					//上传成功，更新用户头像
					layer.msg(resp.msg, {
			    		icon: 1
			    	});
					$("#viewAvatar").attr("src", resp.param.url);
					$("#reviewAvatar").attr("src", resp.param.url);
				} else {
					layer.msg(resp.msg, {
			    		icon: 5
			    	});
				}
			},
		    error: function(XMLHttpRequest, textStatus, errorThrown){
		    	//请求异常回调
		    	layer.closeAll('loading');
		    	layer.msg('请求失败，请稍后再试', {
		    		icon: 5
		    	});
			}
		});
		
		
		// -------------------
		// 4. layui 时间控件渲染
		// -------------------
		laydate.render({
			elem: '#userSettingBirth', 		// 绑定 Dom 元素
			theme: 'molv',					// 墨绿主题
			format: 'yyyy年MM月dd日', 		// 日期格式
			value: ($("#userSettingBirth").attr("data-bi") == null || $("#userSettingBirth").attr("data-bi") == '') ? new Date() : new Date(parseInt($("#userSettingBirth").attr("data-bi"))),
			change: function(value, date, endDate) {
				var mills = date.getTime();
				$("#userSettingBirth").attr("data-bi", mills);
			}
		});
		
		// -------------------
		// 5. layui 分页控件渲染
		// -------------------
		laypage.render({
			elem: 'paging',
			curr: $("#paging").attr("data-in"),
			limit: $("#paging").attr("data-li"),
			count: $("#paging").attr("data-co"),
			layout: ['count', 'prev', 'page', 'next', 'skip'],
			jump: function(obj, first){
				if(!first) {
					var url = window.location.href;
					if(url.lastIndexOf("/") == (url.length - 1)) {
						url = url.slice(0, -1);
					}
					var newUrl = null;
					if(url.match(/page\/\d+$/) != null) {
						var u = url.substring(0, url.lastIndexOf("/") + 1);
						newUrl = u + obj.curr;
					} else {
						newUrl = u + "/page/" + obj.curr;
					}
					var queryItem = $("#paging").attr("data-qi");
					if(!isEmpty(queryItem)) {
						newUrl = newUrl + "?" + queryItem;
					}
					window.location.href = newUrl;
				}
			}
		});
		
		// -------------------
		// 6. layui 轮播控件渲染
		// -------------------
		carousel.render({
			elem: '#indexCarousel',
			width: '100%', 		// 设置容器宽度
		    arrow: 'hover', 	// 始终显示箭头
		    anim: 'fade', 		// 切换动画方式
		    interval: 5000
		});
		
		// -------------------
		// 7. layui 工具集
		// -------------------
			// -------------------
			// 7.1 layui 工具集格式化发帖日期
			// -------------------
		$(".post-time").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			var date = new Date(mils);
			$(this).html(util.timeAgo(date, true));
		});
		$(".reply-time").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			var date = new Date(mils);
			$(this).html(util.timeAgo(date, true));
		});
		$(".slreply-time").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			var date = new Date(mils);
			$(this).html(util.timeAgo(date, true));
		});
		$(".res-time").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			var date = new Date(mils);
			$(this).html(util.timeAgo(date, true));
		});
		$(".res-time-det").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			$(this).html(util.toDateString(mils, "yyyy-MM-dd HH:mm:ss"));
		});
		$(".res-size").each(function() {
			var size = parseInt($(this).attr("data-si"));
			if(size < 1024) {
				$(this).html(size.toFixed(2) + "B");
			} else if(size < 1024 * 1024 ) {
				$(this).html((size/1024).toFixed(2) + "KB");
			} else if(size < 1024 * 1024 * 1024 ) {
				$(this).html((size/(1024 * 1024)).toFixed(2) + "MB");
			}
		});
		
			// -------------------
			// 7.2 layui 工具集格式化新闻日期
			// -------------------
		$(".activities .layui-timeline-title").each(function() {
			var mils = parseInt($(this).attr("data-ti"));
			$(this).html(util.toDateString(mils, "MM月dd日"));
		});
			// -------------------
			// 7.3 layui 工具集渲染固定块
			// -------------------
		util.fixbar({
			bar1: true,
			click: function(type) {
				console.log(type);
				if(type === 'bar1') {
					alert('点击了bar1')
				}
			}
		});
		
		// -------------------
		// 8. layui 三级联动事件监听
		// -------------------
		// -----------------------
		// 8.1 省联动市
		// -----------------------
		form.on('select(choosePro)', function(data) {
			var chooseCity = $("#chooseCity");
			var chooseCounty = $("#chooseCounty");
			if (chooseCity.children().length > 1)
				chooseCity.empty();
			if (chooseCounty.children().length > 1)
				chooseCounty.empty();
			if ($("#defaultCity").length == 0) {
				chooseCity.append("<option id='defaultCity' value='-1'>城市</option>");
			}
			if ($("#defaultCountry").length == 0) {
				chooseCounty.append("<option id='defaultCountry' value='-1'>区/县</option>");
			}
			var sb = new StringBuffer();
			$.each(cityJson, function(i, val) {
				if (val.item_code.substr(0, 2) == $("#choosePro").val().substr(0, 2) 
						&& val.item_code.substr(2, 4) != '0000' 
						&& val.item_code.substr(4, 2) == '00') {
					sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
				}
			});
			$("#defaultCity").after(sb.toString());
			form.render('select');
		});
		// -----------------------
		// 8.2 市联动区/县
		// -----------------------
		form.on('select(chooseCity)', function(data) {
			var chooseCityVal = data.value;
			var chooseCountry = $("#chooseCountry");
			if (chooseCountry.children().length > 1)
				chooseCountry.empty();
			if ($("#defaultCountry").length == 0) {
				chooseCountry.append("<option id='defaultCountry' value='-1'>区/县</option>");
			}
			var sb = new StringBuffer();
			$.each(cityJson, function(i, val) {
				if (chooseCityVal == '110100' || chooseCityVal == "120100" || chooseCityVal == "310100" || chooseCityVal == "500100") {
					if (val.item_code.substr(0, 3) == chooseCityVal.substr(0, 3)
							&& val.item_code.substr(4, 2) != '00') {
						sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
					}
				} else {
					if (val.item_code.substr(0, 4) == chooseCityVal.substr(0, 4)
							&& val.item_code.substr(4, 2) != '00') {
						sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
					}
				}
			});
			$("#defaultCountry").after(sb.toString());
			form.render('select');
		});
		
		// -------------------
		// 9. layui 富文本编辑器选项卡
		// -------------------
		element.on('tab(switchEditor)', function(data){
			Global.editor = data.index;
		});
		
		// -----------------------
		// 10. 签到
		// -----------------------
		$("#punch").on("click", function() {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return false;
				}
			}
			$.ajax({
				url: base + Global.user_punch + "/" + Global.user,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
				},
				success: function(resp) {
					if(!resp.success) {
						layer.open({
							title: '来自服务器',
							content: resp.msg
						});
						//if(resp.match("<title>Gintoki - 用户登录</title>"))
					} else {
						// 签到成功
						layer.msg('签到成功，+ 20 斤', {icon: 1});
						$("#punch").css("display", "none");
						$("#punch").before('<a><li class="l1 btn"><i class="layui-icon">&#xe605;</i><span>已签到</span></li></a>');
						$("#punch").remove();
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
		});
		
		// -----------------------
		// 11. 定位
		// -----------------------
		$("#userSettingAddress .request").on("click", function() {
			var index = layer.load(1, {
				shade: [0.1,'#fff'], 	// 0.1透明度的白色背景
				time: 5000				// 超时时间
			});
			var address = $("#userSettingAddress").attr("data-ad");
			if(address.length == 6) {
				var province = address.substr(0, 2) + "0000";
				var city = address.substr(0, 4) + "00";
				var sb = new StringBuffer();
				$.each(cityJson, function(i, val) {
					if (val.item_code.substr(0, 2) == province.substr(0, 2) 
							&& val.item_code.substr(2, 4) != '0000' 
							&& val.item_code.substr(4, 2) == '00') {
						sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
					}
				});
				$("#defaultCity").after(sb.toString());
				$("#choosePro").val(province);
				$("#chooseCity").val(city);
				sb = new StringBuffer();
				$.each(cityJson, function(i, val) {
					if (val.item_code.substr(0, 4) == city.substr(0, 4)
							&& val.item_code.substr(4, 2) != '00') {
						sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
					}
				});
				$("#defaultCountry").after(sb.toString());
				$("#chooseCounty").val(address);
				form.render('select');
				layer.close(index);
			} else {
				$.ajax({
					url: Global.util_loc,
					type: "GET",
					success: function(resp) {
						if(resp.success) {
							address = resp.param.location;
							var province = address.substr(0, 2) + "0000";
							var city = address.substr(0, 4) + "00";
							var sb = new StringBuffer();
							$.each(cityJson, function(i, val) {
								if (val.item_code.substr(0, 2) == province.substr(0, 2) 
										&& val.item_code.substr(2, 4) != '0000' 
										&& val.item_code.substr(4, 2) == '00') {
									sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
								}
							});
							$("#defaultCity").after(sb.toString());
							$("#choosePro").val(province);
							$("#chooseCity").val(city);
							sb = new StringBuffer();
							$.each(cityJson, function(i, val) {
								if (val.item_code.substr(0, 4) == city.substr(0, 4)
										&& val.item_code.substr(4, 2) != '00') {
									sb.append("<option value='" + val.item_code + "'>" + val.item_name + "</option>");
								}
							});
							$("#defaultCountry").after(sb.toString());
							$("#chooseCounty").val(address);
							form.render('select');
						} else {
							layer.open({
								title: '来自遥远的服务器',
								content: '定位失败'
							}); 
						}
					},
					error: function() {
						layer.open({
							title: '来自外太空',
							content: '未知错误，请稍后再试'
						}); 
					}
				});
				layer.close(index);
			}
		});
		if(window.location.href.match(base + Global.user_setting) != null) {
			$("#userSettingAddress .request").trigger("click");
		}

		// -----------------------
		// 12. layui 回贴模块
		// -----------------------
			// -----------------------
			// 12.1 layui 收藏发贴
			// -----------------------
		$(".post .collect").on("click", function() {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return;
				}
			}
			var postId = parseInt($(this).attr("data-co"));
			if(postId <= 0) {
				return;
			}
			$(this).text("已收藏");
			$.ajax({
				url: base + Global.post_coll + "/" +  postId,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					userId: Global.user
				},
				success: {
					// Nothing
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
		});
			// -----------------------
			// 12.2 layui 回贴点赞
			// -----------------------
		$(".reply .reply-info .thumb").on("click", function() {
			if(Global.user == "") {
				Global.user = $("#userId").val()
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return;
				}
			}
			if(parseInt($(this).attr("data-th"))) {
				return;
			}
			var thumbEle = $(this).find("em");
			thumbEle.text(parseInt(thumbEle.text()) + 1);
			$(this).css("color", "#FF4444")
			$(this).attr("data-th", "1");
			var replyId = $(this).parent().parent().attr("data-li")
			$.ajax({
				url: base + Global.reply_thumb + "/" +  replyId,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					userId: Global.user
				},
				success: {
					// Nothing
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
		});
			// -----------------------
			// 12.3 layui 回贴回复
			// -----------------------
		$(".reply .reply-info .slreply").on("click", function(event) {
			if(Global.user == "") {
				Global.user = $("#userId").val();
				if(Global.user == "") {
					layer.msg('您还未登录~');
					return;
				}
			}
			var replyEle = $(this).parent().parent();
			if(slreplyEditor != null) {
				// 重复点击同一个回复按钮，直接返回
				if(replyEle.attr("data-li") == $("#sleditor").parent().attr("data-li")) {
					$("#sleditor").slideToggle();
					return;
				} else {
					// 移除原来的回贴编辑器
					$("#sleditor").remove();
				}
			}
			replyEle.append("<div id='sleditor' style='display:none;margin-top: 20px;'><form><div class='layui-form-item'><div class='layui-input-block' style='margin-left: 0px;'><textarea id='slreplyContent'></textarea></div></div><div class='layui-form-item'><button class='layui-btn' lay-filter='SlReply' lay-submit>提交回复</button></div></form</div>");
			slreplyEditor = layedit.build('slreplyContent', {
				tool: ['face', 'link', 'unlink', 'strong', 'italic'],
				height: 80
			});
			$("#sleditor").slideDown();
		});
		
		
		// -----------------------
		// eof. layui 初始化指定页
		// -----------------------
			// -----------------------
			// eof. layui 初始化发贴页面
			// -----------------------
		if(window.location.href.match(Global.post_send) != null) {
			var ques = $.cookie("_ps_qu");
			if(ques != null && ques == "yes") {
				$("#PostSendClassify option[value=2]").attr("selected", "selected");
			} else {
				$("#PostSendClassify option[value=1]").attr("selected", "selected");
			}
			form.render('select');
		}
		// -----------------------
		// eof. layui 初始化用户注册页面
		// -----------------------
		if(window.location.href.match(Global.user_reg) != null) {
			var minuteStr = $.cookie("_ur_co");
			if(minuteStr != "null") {
				var minute = parseInt(minuteStr);
				var sender = $("#send-email");
				$("#send-email-cover").css("display", "inline-block");
				var interval = setInterval(function() {
					var value = "重新发送 ( " + (--minute).toString() + "s )";
					$.cookie("_ur_co", minute);
					sender.text(value)
				}, 1000);
				setTimeout(function() {
					// evictcookie:_userregister_countdown
					$.cookie('_ur_co', null); 
					sender.text("发送验证码")
					sender.removeAttr("disabled");
					clearInterval(interval);
					$("#send-email-cover").css("display", "none");
				}, parseInt(minuteStr) * 1000);
			}
		}
		// -----------------------
		// eof. layui 初始化用户主页
		// -----------------------
		if(window.location.href.match(/user\/(\d+)/)) {
				// -----------------------
				// 格式化用户信息
				// -----------------------
			var regTime = parseInt($("#userPageReg").attr("data-re"));
			$("#userPageReg").html(util.toDateString(regTime, "yyyy年MM月dd日"));
			var address = $("#userPageAddress").attr("data-ad");
			if(address.match(/\d{6}/g)) {
				$.each(cityJson, function(i, val) {
					if (val.item_code == address) {
						$("#userPageAddress").text(val.item_name);
						address = -1;
						return false;
					}
				});
			}
			if(address != -1) {$("#userPageAddress").text("未知");}
				// -----------------------
				// 动态设置按钮
				// -----------------------
			var trendSettingLayer = null;
			$(".dynamic-setting").on("click", function() {
				trendSettingLayer = layer.open({
					type: 1,
					shade: .3,
					area: "640px",
					resize:false,
					title: false,
					content: $('.dynamic-setting-panel')
				});	
			});
				// -----------------------
				// 动态设置提交
				// -----------------------
			form.on('submit(UserTrend)', function(data) {
				var trend1 = data.field.c1 == "on" ? "1" : "0";
				var trend2 = data.field.c2 == "on" ? "1" : "0";
				var trend3 = data.field.c3 == "on" ? "1" : "0";
				var trend4 = data.field.c4 == "on" ? "1" : "0";
				var trend = trend1 + trend2 + trend3 + trend4;
				$.ajax({
					url: base + Global.user_trend_Setting,
					type: "POST",
					data: {
						userId: Global.user,
						trendSetting: trend
					},
					success: function() {
						// Nothing todo
					},
					error: function(XMLHttpRequest, textStatus, errorThrown) {
						layer.open({
							title: '来自外太空',
							content: '未知错误，请稍后再试'
						});
					}
				});
				if(trendSettingLayer != null)
					layer.close(trendSettingLayer);
			});
				// -----------------------
				// 流加载用户动态
				// -----------------------
			var userPageId = window.location.href.match(/user\/(\d+)/)[1];
			try {
				userPageId = parseInt(userPageId);
			} catch(e) {
				window.location.href = base + "404";
				return;
			}
			layui.use('flow', function() {
				var flow = layui.flow;
				flow.load({
					elem: '#UserPageActivities',
					isAuto: false,
					done: function(page, next) {
						var lis = [];
						$.ajax({
							url: base + "user/" + userPageId,
							type: "POST",
							success: function(resp) {
								var templet = '<li class="item layui-clear"><div class="header"><img alt=""src="#{trend.header}"style="width: 100%; border-radius: 100%;"></div><div class="content"><div class="head"><div class="name"><a class=""href="#{root}/user/#{trend.userId}">#{trend.nickname}</a></div><div class="time">#{trend.time}</div><div class="title">#{trend.classify}</div></div><div class="body"><a class="fl"href="#{root}/#{trend.link}"><img src="#{server}/#{trend.imgLink}"width="40"height="40"></a><div class="detail"><div class="tag"><span>来自</span><a href="#{root}/#{trend.tagLink}"target="_blank"><span class="ml10" style="color: #666;">#{trend.tag}</span></a></div><div class="subtitle"><a href="#{trend.link}"> #{trend.title} </a></div></div></div><hr></div></li>';
								layui.each(resp.param.trends, function(index, item) {
									var s = templet;
									var time = util.timeAgo(new Date(item.time), true);
									s = s.replace(/#{server}/g, Global.server).replace(/#{root}/g, Global.root).
										  replace(/#{trend.id}/g, item.id).replace(/#{trend.userId}/g, item.userId).
										  replace(/#{trend.classify}/g, item.classify).replace(/#{trend.title}/g, item.title).
										  replace(/#{trend.time}/g, time).replace(/#{trend.link}/g, item.link).
										  replace(/#{trend.imgLink}/g, item.imgLink).replace(/#{trend.nickname}/g, item.nickname).
										  replace(/#{trend.header}/g, item.header).replace(/#{trend.tag}/g, item.tag).
										  replace(/#{trend.tagLink}/g, item.tagLink).replace(/#{trend.msg}/g, item.msg).
										  replace(/#{trend.desc}/g, item.desc);
									lis.push(s);
							    });
								lis.push(resp.param.trends);
								next(lis.join(''), page < resp.param.pages);
							},
							error: function(XMLHttpRequest, textStatus, errorThrown) {
								layer.open({
									title: '来自外太空',
									content: '未知错误，请稍后再试'
								});
							}
						});
					}
				});
			});
				// -----------------------
				// eof. layui 初始化发贴页面
				// -----------------------
		}
		
	});
	// -----------------------
	// layui end
	// -----------------------
	
	
	// -----------------------
	// jquery 入口
	// -----------------------
	$(function() {
		
		// -----------------------
		// 1. jquery 注销登录模块
		// -----------------------
		$(".logout").on("click", function(){
			layer.confirm('确认退出？', {icon: 3, title:'提示'}, function(index) {
				layer.close(index);
				window.location.href = $("base").attr("href") + "user/logout";
			},function(index) {
				layer.close(index);
			});    
		});
		
		// -----------------------
		// 2. jquery 邮件发送模块
		// -----------------------
		$("#send-email").on("click", function() {
			// 向服务器发送 SendEmail 请求
			var email = $("#email").val();
			if(!email.match(/[a-zA-Z0-9_-]+/)) {
				layer.tips('邮箱格式不正确', '#email', {
					tips: 1
				});
				return;
			}
			email = email + "@" + $("#emailDomain").val();
			if(!email.match(/[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\.[a-zA-Z0-9_-]+/)) {
				layer.tips('请选择邮箱域名', '.layui-form-select', {
					tips: 1
				});
				return;
			}
			var sendEmailLoading = layer.load(1, {
				shade: [0.1,'#fff'] 	// 0.1 透明度的白色背景
			});
			$.ajax({
				url: base + Global.web_email,
				type: "POST",
				async: true,
				scriptCharset: "utf-8",
				data: {
					account:email
				},
				success: function(resp) {
					layer.close(sendEmailLoading);
					layer.open({
						title: '来自服务器',
						content: resp.msg
					});
					if(resp.success) {
						var sender = $("#send-email");
						// setcookie:_userregister_countdown 
						$.cookie("_ur_co", "60");
						$("#send-email-cover").css("display", "inline-block");
						var minute = 60;
						var interval = setInterval(function() {
							var value = "重新发送 ( " + (--minute).toString() + "s )";
							// updatecookie:_userregister_countdown 
							$.cookie("_ur_co", minute.toString());
							sender.text(value)
						}, 1000);
						setTimeout(function() {
							// evictcookie:_userregister_countdown
							$.cookie('_ur_co', null); 
							sender.text("发送验证码")
							sender.removeAttr("disabled");
							clearInterval(interval);
							$("#send-email-cover").css("display", "none");
						}, 60000);
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
					layer.close(sendEmailLoading);
					layer.open({
						title: '来自外太空',
						content: '未知错误，请稍后再试'
					});
				}
			});
		});
		
		// -----------------------
		// 3. jquery 用户性别回显
		// -----------------------
		var gender = $(".gender").attr("data-gender");
		$(".gender input:eq(" + gender + ")").attr("checked", "checked");
		
		// -----------------------
		// 4. jquery 用户基本设置页切换模块
		// -----------------------
		$(".base-settings-tab li").on("click", function() {
			if($(".base-settings-tab .layui-this") == $(this))
				return;
			var will = $(this).attr("lay-id");
			$(".base-settings-tab .layui-this").removeClass("layui-this");
			$(this).addClass("layui-this");
			$(".base-settings .layui-show").removeClass("layui-show");
			$(".base-settings ." + will).addClass("layui-show");
		});
		
		// -----------------------
		// 5. jquery 设置发贴分类随URL高亮
		// -----------------------
		var url = window.location.href;
		if((url.match(/discuss$/) != null) || (url.match(/discuss\/page.+$/) != null)) {
			$("#classify .dis").addClass("layui-this");
		} else if((url.match(/question$/) != null) || (url.match(/question\/page.+$/) != null)) {
			$("#classify .que").addClass("layui-this");
		} else if((url.match(/suggest$/) != null) || (url.match(/suggest\/page.+$/) != null)) {
			$("#classify .sug").addClass("layui-this");
		} else if((url.match(/sharing$/) != null) || (url.match(/sharing\/page.+$/) != null)) {
			$("#classify .sha").addClass("layui-this");
		} else {
			// Nothing
		}
		
		// -----------------------
		// 6. 收起回贴列表
		// -----------------------
		$(".reply .fold").on("click", function() {
			var foldEle = $(this);
			var slreplysEle = $(this).parent().parent().find(".slreplys");
			slreplysEle.slideToggle(function() {
				if(slreplysEle.css("display") == "none") {
					foldEle.find(".up").css("display", "none");
					foldEle.find(".down").css("display", "block");
				} else {
					foldEle.find(".up").css("display", "block");
					foldEle.find(".down").css("display", "none");
				}
			});
		});
		
		// -----------------------
		// 7. 提问按钮设置 Cookie
		// -----------------------
		$(".post-send-btn").on("click", function() {
			var val = $(this).attr("data-val");
			if(val == "2") {
				// _postsend_question
				$.cookie("_ps_qu", "yes", {path: "/"});
			} else {
				$.cookie("_ps_qu", "no", {path: "/"});
			}
			window.location.href = base + Global.post_send;
		});
		
		
		// -----------------------
		// Eof. 特定 URL 初始化函数
		// -----------------------
		var location = window.location.href;
		if(location.match(Global.user_setting) != null) {
			// -----------------------
			// a. jquery 预加载用户信息设置界面
			// -----------------------
			$("#userSettingAddress .request").trigger("click");
		} else if(location == (base + Global.post_send)) {
			// -----------------------
			// b. jquery 渲染Simditor编辑器
			// -----------------------
			var editor = new Simditor({
				textarea: $("#simditorPostContent"),	// 编辑器容器
				// 工具条（默认true，也可以传入按钮组）
				toolbar: ['title', 'bold', 'italic', 'underline', 'strikethrough', 'fontScale', 'ol', 'ul',
					'blockquote', 'code', 'link', 'hr', 'alignment'],
					tabIndent: true						// 启用tab键缩进（默认true）
			});
		} else if(location.match("/post/\\d+") != null) {
			// -----------------------
			// c. jquery 初始化查看发贴详情页面
			// -----------------------
			$(".reply .reply-info .thumb").each(function() {
				if($(this).attr("data-th") == "1") {
					$(this).css("color", "#FF4444");
				}
			});
		} else if(location.match(Global.res) != null) {
			// -----------------------
			// d. jquery 初始化资源索引页
			// -----------------------
			// 初始化方法
			var resIndexSubmit = function() {
				var items = $(".query-items").attr("data-qi");
				var indexItems = $(".query-item-console").attr("data-qi");
				if(items == indexItems) {
					return;
				}
				if(items == "0-0-0" || items == "0-0-1") {
					$(".query-item-console .items li:gt(0)").remove();
				} else {
					var arr = items.split("-");
					var tagVal = parseInt(arr[0]);
					var clsVal = parseInt(arr[1]);
					if(tagVal != 0) {
						var tagEle = $(".query-item-console .items li:eq(1)");
						tagEle.find("a").text($(".query-items .tags .items .option:eq(" + tagVal + ")").text());
						tagEle.css("display", "list-item");
					} else {
						$(".query-item-console .items li:eq(1)").css("display", "none");
					}
					if(clsVal != 0) {
						var tagEle = $(".query-item-console .items li:eq(2)");
						tagEle.find("a").text($(".query-items .cls .items .option:eq(" + clsVal + ")").text());
						tagEle.css("display", "list-item");
					} else {
						$(".query-item-console .items li:eq(2)").css("display", "none");
					}
				}
				// 提交表单
				$("#ResSearchForm input").val(items);
				$("#ResSearchForm").submit();
			}
			// 提交选项
			var indexPage = $("body").attr("data-page");
			if(indexPage == "index") {
				var itemsStr = $(".query-items").attr("data-qi");
				if(isEmpty(itemsStr)) {
					itemsStr = "0-0-0";
					$(".query-items").attr("data-qi", itemsStr);
				}
				var items = itemsStr.split("-");
				for (var i = 0; i < items.length; i++) {
					if(i == 0) {
						var index = parseInt(items[0]);
						var firEle = $(".query-items .tags .items .option:eq(" + index + ")");
						firEle.addClass("layui-this");
						if(index > 0) {
							$(".query-item-console .items li:eq(1) a").text(firEle.text());
							$(".query-item-console .items li:eq(1)").css("display", "list-item");
						}
					} else if(i == 1) {
						var index = parseInt(items[1]);
						var secEle = $(".query-items .cls .items .option:eq(" + index + ")");
						secEle.addClass("layui-this");
						if(index > 0) {
							$(".query-item-console .items li:eq(2) a").text(secEle.text());
							$(".query-item-console .items li:eq(2)").css("display", "list-item");
						}
					} else if(i == 2) {
						var index = parseInt(items[2]);
						if(index == 1)
							$(".order-item .option:last").addClass("layui-this");
						else
							$(".order-item .option:first").addClass("layui-this");
					}
				}
				// Tag 索引
				$(".tags .option").on("click", function() {
					var index = $(this).parent().index();
					var items = $(".query-items").attr("data-qi").split("-");
					items[0] = index;
					$(".query-items").attr("data-qi", items.join("-"));
					$(this).parent().parent().find(".layui-this").removeClass("layui-this");
					$(this).toggleClass("layui-this");
					resIndexSubmit();
				});
				// Classify 索引
				$(".cls .option").on("click", function() {
					var index = $(this).parent().index();
					var items = $(".query-items").attr("data-qi").split("-");
					items[1] = index;
					$(".query-items").attr("data-qi", items.join("-"));
					$(this).parent().parent().find(".layui-this").removeClass("layui-this");
					$(this).toggleClass("layui-this");
					resIndexSubmit();
				});
				// 排序方式
				$(".order-item .option").on("click", function() {
					var index = $(this).attr("data-in");
					var items = $(".query-items").attr("data-qi").split("-");
					items[2] = index;
					$(".query-items").attr("data-qi", items.join("-"));
					$(this).parent().parent().find(".layui-this").removeClass("layui-this");
					$(this).toggleClass("layui-this");
					resIndexSubmit();
				});
				// 显示删除已选条件按钮
				$(".query-item-console li").hover(function() {
					$(this).find(".close").removeClass("hidden");
				}, function() {
					$(this).find(".close").addClass("hidden");
				});
				// 删除已选条件
				$(".query-item-console .close").on("click", function() {
					var index = $(this).parent().index();
					var items = $(".query-items").attr("data-qi").split("-");
					items[index - 1] = 0;
					$(".query-items").attr("data-qi", items.join("-"));
					$(this).parent().remove();
					resIndexSubmit();
				});
			}
			// -----------------------
			// e. jquery 初始化资源详情页
			// -----------------------
			else if(indexPage == "detail") {
				// 生成二维码
				var qrcode = new QRCode(document.getElementById("ResDetailQRcode"), {
					width : 100,
					height : 100
				});
				var text = $("#ResDetailQRcode").attr("data-qr");
				if(isEmpty(text)) {
					text = "请求无效！"
				}
				qrcode.makeCode(text);
				// 文件下载按钮
				$("#ResDetailDownload").on("click", function() {
					if(Global.user == "") {
						Global.user = $("#userId").val();
						if(Global.user == "") {
							layer.msg('您还未登录~');
							return;
						}
					}
					var resId = $(this).attr("data-id");
					var thisEle = $(this);
					if(isEmpty(resId)) {
						layer.open({
							title: '来自服务器',
							content: "请求无效"
						});
						return;
					}
					$.ajax({
						url: base + Global.res_down + "/" + resId,
						type: "POST",
						async: true,
						scriptCharset: "utf-8",
						data: {
							userId: Global.user
						},
						success: function(resp) {
							if(!resp.success) {
								// 非法请求
								layer.open({
									title: '来自服务器',
									content: resp.msg
								});
							} else {
								// 新窗口发起下载请求
								var downUrl = base + Global.res_down + "/" + resId;
								var form = "<form id='ResDetailDownProcessor' style='display:none;' target='_blank' action='#{downUrl}'><input type='hidden' name='userId' value='#{userId}'></form>";
								form = form.replace(/#{downUrl}/g, downUrl).replace(/#{userId}/g, Global.user);
								$("body").append(form);
								$("body form").submit();
								$("body form").remove();
							}
						},
						error: function(XMLHttpRequest, textStatus, errorThrown) {
							layer.open({
								title: '来自外太空',
								content: '未知错误，请稍后再试'
							});
						}
					});
				});
				// 二维码生成按钮
				$("#ResDetailQR").hover(function() {
					$("#ResDetailQRcode").css("display", "block");
				}, function() {
					$("#ResDetailQRcode").css("display", "none");
				});
			}
		}
		
	});
	
	// -----------------------
	// jquery end
	// -----------------------
}();
