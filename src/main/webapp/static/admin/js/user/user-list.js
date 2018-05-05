(function ($) {
    "use strict";
    var mainApp = {

        initFunction: function () {
            /*MENU 
            ------------------------------------*/
            $('#main-menu').metisMenu();
            
            $(window).bind("load resize", function () {
                if ($(this).width() < 768) {
                    $('div.sidebar-collapse').addClass('collapse')
                } else {
                    $('div.sidebar-collapse').removeClass('collapse')
                }
            });
        },

        initialization: function () {
            mainApp.initFunction();
        }

    }
    // Initializing

    $(document).ready(function () {
        mainApp.initFunction(); 
        $("#sideNav").click(function(){
            if($(this).hasClass('closed')){
                $('.navbar-side').animate({left: '0px'});
                $(this).removeClass('closed');
                $('#page-wrapper').animate({'margin-left' : '260px'});
                
            }
            else{
                $(this).addClass('closed');
                $('.navbar-side').animate({left: '-260px'});
                $('#page-wrapper').animate({'margin-left' : '0px'}); 
            }
        });
    });
}(jQuery));

/**
 * 日期格式化，将 Layui 中的日期对象转换为 JS Date 对象
 */
function format(obj) {
    var date = new Date();
    date.setYear(obj.year);
    date.setMonth(obj.month - 1);
    date.setDate(obj.date);
    date.setHours(obj.hours);
    date.setMinutes(obj.minutes);
    date.setSeconds(obj.seconds);
    return date;
}

(function() {
    
    const base = $("base").attr("href");
    const location = window.location.href;
    var Admin = {
        web_root  : "wctf",
        admin: "admin",
        admin_user : "admin/user",
        admin_user_list : "admin/user/list",
        admin_user_update : "admin/user/update",
        admin_user_del: "admin/user/del",
        admin_user_delete: "admin/user/delete",
        admin_user_ban: "admin/user/ban",
        url_stamp_date : "?stamp=" + new Date(),
        url_stamp_rand : "?stamp=" + Math.random(),
    }
    const currUrl = window.location.href;
    
    layui.use(['layer', 'table', 'element', 'laydate'], function() {
        var layer = layui.layer, table = layui.table, element = layui.element, form = layui.form, laydate = layui.laydate;
        
        form.verify({
            log_ac: [/^([\w_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+$/, '用户名或密码错误'],
            usr_ni: [/^[\w_-\u4e00-\u9fa5]{1,16}$/, '昵称长度不得超过16个字符'],
        });
        
        // Listened the form submit
        form.on('submit(AdminUserEditForm1)', function(data) {
            $.ajax({
                url: base + Admin.admin_user_update,
                type: "POST",
                async: true,
                scriptCharset: "utf-8",
                data: data.field,
                success: function(resp) {
                    layer.open({
                        title: '来自服务器',
                        content: resp.msg
                    });
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
        
        $(".reload-btn").on("click", function() {
           window.location.reload(true); 
        });
        
        if (location.match(/admin\/user$/) != null) {
            
            var data = null;
            var paging = null;
            
            // Send the user list request.
            $.ajax({
                url: base + Admin.admin_user_list,
                type: "GET",
                scriptCharset: "utf-8",
                success: function(resp) {
                    if (resp.success) {
                        // Save the data.
                        data = resp.param.data,
                        paging = resp.param.paging;
                        // Render the layui table.
                        table.render({
                            elem: '#UserList',
                            data: resp.param.data,
                            rownumbers: true,
                            page: {
                                curr: resp.param.paging.index,
                                count: resp.param.paging.count,
                                limit: resp.param.paging.limit,
                            },
                            cols: [[
                                {
                                    type:'checkbox'
                                }, 
                                {
                                    type:'numbers',
                                    width:80,
                                    title: 'ID'
                                }, 
                                {
                                    field:'nickname',
                                    width:80,
                                    title: '用户名',
                                    templet: '<div><a href="/wctf/user/{{d.id}}" target="_blank" class="row-data" data-id="{{d.id}}">{{d.nickname}}</a></div>'
                                },
                                {
                                    field:'genderStr', 
                                    width:80, 
                                    title: '性别', 
                                    sort: true
                                }, 
                                {
                                    field:'email', 
                                    width:210, 
                                    title: '邮箱' 
                                },
                                {
                                    field:'loginFlagStr', 
                                    title: '登录状态', 
                                    width: 80, 
                                    sort: true
                                },
                                {
                                    field:'birthdayStr', 
                                    width:150, 
                                    title: '生日'
                                },
                                {
                                    field:'regStr', 
                                    width:150, 
                                    title: '注册时间', 
                                    sort: true
                                },
                                {
                                    field:'weight', 
                                    width:80, 
                                    title: '体重', 
                                    sort: true
                                },
                                {
                                    field:'operate', 
                                    width:60, 
                                    title: '操作',
                                    templet: '<div><a href="/wctf/admin/user/{{d.id}}" class="layui-icon">&#xe642;</a></div>'
                                }
                            ]]
                        });
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                 // User list page occurred some error, redirect to the back-end index page.
                    window.location.href = base + Admin.admin;
                }
            });
            
            // Listening table bar - delete event
            $("#DeleteBar").on("click", function() {
                var arr = $(".layui-table tbody .layui-form-checked");
                if (arr.length) {
                    layer.confirm("您确定要删除选中用户吗？", {
                        btn: ["确定", "点错了"]
                    }, function() {
                        var ids = new Array();
                        arr.each(function() {
                            const id = $(this).parents("tr").find(".row-data").attr("data-id");
                            ids.push(id);
                        });
                        $.ajax({
                            url: base + Admin.admin_user_delete,
                            type: "POST",
                            scriptCharset: "utf-8",
                            data: {
                                id: ids.toString()
                            },
                            success: function(resp) {
                                if (resp.success) {
                                    window.location.reload(true);
                                } else {
                                    layer.open({
                                        title: '来自外太空',
                                        content: resp.msg
                                    });
                                }
                            },
                            error: function(XMLHttpRequest, textStatus, errorThrown) {
                                layer.open({
                                    title: '来自外太空',
                                    content: '出了点问题，请稍后再试'
                                });
                            }
                        });
                    });
                }
            });
        } else if (location.match(Admin.admin_user_del) != null) {
            
            var data = null;
            var paging = null;
            
            // Send the user list request.
            $.ajax({
                url: base + Admin.admin_user_delete,
                type: "GET",
                scriptCharset: "utf-8",
                success: function(resp) {
                    if (resp.success) {
                        // Save the data.
                        data = resp.param.data,
                        paging = resp.param.paging;
                        // Render the layui table.
                        table.render({
                            elem: '#UserList',
                            data: resp.param.data,
                            rownumbers: true,
                            page: {
                                curr: resp.param.paging.index,
                                count: resp.param.paging.count,
                                limit: resp.param.paging.limit,
                            },
                            cols: [[
                                {
                                    type:'checkbox'
                                }, 
                                {
                                    type:'numbers',
                                    width:80,
                                    title: 'ID'
                                }, 
                                {
                                    field:'nickname',
                                    width:80,
                                    title: '用户名',
                                    templet: '<div><a href="/wctf/user/{{d.id}}" target="_blank" class="row-data" data-id="{{d.id}}">{{d.nickname}}</a></div>'
                                },
                                {
                                    field:'genderStr', 
                                    width:80, 
                                    title: '性别', 
                                    sort: true
                                }, 
                                {
                                    field:'email', 
                                    width:210, 
                                    title: '邮箱' 
                                },
                                {
                                    field:'loginFlagStr', 
                                    title: '登录状态', 
                                    width: 80, 
                                    sort: true
                                },
                                {
                                    field:'birthdayStr', 
                                    width:150, 
                                    title: '生日'
                                },
                                {
                                    field:'regStr', 
                                    width:150, 
                                    title: '注册时间', 
                                    sort: true
                                },
                                {
                                    field:'weight', 
                                    width:80, 
                                    title: '体重', 
                                    sort: true
                                },
                                {
                                    field:'operate', 
                                    width:60, 
                                    title: '操作',
                                    templet: '<div><a href="/wctf/admin/user/{{d.id}}" class="layui-icon">&#xe642;</a></div>'
                                }
                            ]]
                        });
                    }
                },
                error: function(XMLHttpRequest, textStatus, errorThrown) {
                    // User list page occurred some error, redirect to the back-end index page.
                    window.location.href = base + Admin.admin;
                }
            });
        } else if (location.match(/admin\/user\/(\d+)/)) {
            
            const AdminUserEdit = {
                
                ban: {
                    typeBak: $("#ban-select").attr("data-type"),
                    type: $("#ban-select").attr("data-type"),
                    startTime: $("#ban-select").attr("data-s"),
                    endTime: $("#ban-select").attr("data-e")
                }
            }
            
            if (AdminUserEdit.ban.type == "2") {
                $(".ban2").removeClass("none");
            } else if (AdminUserEdit.ban.type == "3") {
                $(".ban3").removeClass("none");
            }
            
            // Listened the select event
            form.on("select(LoginFlag)", function(data) {
                const selected = data.value;
                
                if (selected == 2) {
                    AdminUserEdit.ban.type = 2;
                    $(".ban-box:not([class$='none'])").addClass("none");
                    $(".ban2").removeClass("none");
                } else if (selected == 3) {
                    AdminUserEdit.ban.type = 3;
                    $(".ban-box:not([class$='none'])").addClass("none");
                    $(".ban3").removeClass("none");
                } else {
                    AdminUserEdit.ban.type = 1;
                    $(".ban-box:not([class$='none'])").addClass("none");
                }
            });
            
            if (AdminUserEdit.ban.startTime) {
                laydate.render({
                    elem: '#ban2StartTime',
                    format: "yyyy-MM-dd HH:mm:ss",
                    value: new Date(parseInt(AdminUserEdit.ban.startTime)),
                    type: 'datetime',
                    done: function(value, date) {
                        AdminUserEdit.ban.startTime = format(date);
                    }
                });
            } else {
                laydate.render({
                    elem: '#ban2StartTime',
                    format: "yyyy-MM-dd HH:mm:ss",
                    type: 'datetime',
                    done: function(value, date) {
                        AdminUserEdit.ban.startTime = format(date);
                    }
                });
            }
            
            if (AdminUserEdit.ban.endTime) {
                laydate.render({
                    elem: '#ban2EndTime',
                    format: "yyyy-MM-dd HH:mm:ss",
                    value: new Date(parseInt(AdminUserEdit.ban.endTime)),
                    type: 'datetime',
                    done: function(value, date) {
                        AdminUserEdit.ban.endTime = format(date);
                        if (AdminUserEdit.ban.startTime && Number(AdminUserEdit.ban.endTime) < Number(AdminUserEdit.ban.startTime)) {
                            alert("太小了");
                        }
                    }
                });
            } else {
                laydate.render({
                    elem: '#ban2EndTime',
                    format: "yyyy-MM-dd HH:mm:ss",
                    type: 'datetime',
                    done: function(value, date) {
                        AdminUserEdit.ban.endTime = format(date);
                        if (AdminUserEdit.ban.startTime && Number(AdminUserEdit.ban.endTime) < Number(AdminUserEdit.ban.startTime)) {
                            alert("太小了");
                        }
                    }
                });
            }
            
            // Update the user login flag.
            form.on("submit(AdminUserEditBan)", function(data) {
                
                layer.confirm("您确定要修改吗（可能不会立即生效）？", {
                    btn: ["确定", "取消"]
                }, function() {
                    
                    const param = {
                            id: data.field.userId,
                            loginFlagVo: AdminUserEdit.ban.type,
                    }
                    if (AdminUserEdit.ban.type == "2") {
                        param.loginFlagStart = Number(AdminUserEdit.ban.startTime);
                        param.loginFlagEnd = Number(AdminUserEdit.ban.endTime);
                        param.loginFlagMsg = data.field.ban2Reason;
                    } else if (AdminUserEdit.ban.type == "3") {
                        param.loginFlagMsg = data.field.ban3Reason;
                    }
                    
                    $.ajax({
                        url: base + Admin.admin_user_ban,
                        type: "post",
                        data: param,
                        scriptCharset: "utf-8",
                        success: function(resp) {
                            layer.open({
                                title: '来自外太空',
                                content: resp.msg
                            });
                        },
                        error: function(XMLHttpRequest, textStatus, errorThrown){
                            layer.open({
                                title: '来自外太空',
                                content: '未知错误，请稍后再试'
                            });
                        }
                    });
                }, function() {
                    $("#ban-select").val(AdminUserEdit.ban.typeBak);
                    form.render('select');
                    $(".ban-box").addClass("none");
                    if (AdminUserEdit.ban.typeBak == "2") {
                        $(".ban2").removeClass("none");
                    } else if (AdminUserEdit.ban.typeBak == "3") {
                        $(".ban3").removeClass("none");
                    }
                });
                return false;
            });
        }
    });
})();