<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
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
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="http://114.67.139.119/static/image/wctf.ico" type="image/png">
    <title>WCTF - Admin 用户编辑</title>
    
    <link href="${server}/static/admin/css/bootstrap.css" rel="stylesheet" />
    <link href="${server}/static/admin/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/font-awesome.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/custom-styles.css" rel="stylesheet" />
    <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="${server}/static/admin/js/Lightweight-Chart/cssCharts.css">
    <link href="${server}/static/layui/css/layui.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/static.css" rel="stylesheet" />
    <style type="text/css">
    .row .content {
      padding: 10px 15px;
      background-color: #FFF;
    }
    .layui-tab-content {
      padding-top: 30px;
      min-height: 500px;
    }
    .layui-form-label {
      width: 100px;
    }
    .layui-input-block {
      margin-left: 100px;
    }
    legend {
      display: initial;
      width: initial;
      margin-bottom: initial;
      line-height: initial;
      color: initial;
      border: initial;
      border-bottom: initial;
    }
    </style>
  </head>

  <body>
    <div id="wrapper">
      <nav class="navbar navbar-default top-navbar" role="navigation">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".sidebar-collapse">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="index.html">
            <strong style="letter-spacing: 3px;">
              <i class="icon fa fa-plane"></i> WCTF</strong>
          </a>
          <div id="sideNav" href="">
            <i class="fa fa-bars icon"></i>
          </div>
        </div>
        <ul class="nav navbar-top-links navbar-right">
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
              <i class="fa fa-envelope fa-fw"></i>
              <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-messages">
              <li>
                <a href="#">
                  <div>
                    <strong>John Doe</strong>
                    <span class="pull-right text-muted">
                      <em>Today</em></span>
                  </div>
                  <div>Lorem Ipsum has been the industry's standard dummy text ever since the 1500s...</div></a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <strong>John Smith</strong>
                    <span class="pull-right text-muted">
                      <em>Yesterday</em></span>
                  </div>
                  <div>Lorem Ipsum has been the industry's standard dummy text ever since an kwilnw...</div></a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <strong>John Smith</strong>
                    <span class="pull-right text-muted">
                      <em>Yesterday</em></span>
                  </div>
                  <div>Lorem Ipsum has been the industry's standard dummy text ever since the...</div></a>
              </li>
              <li class="divider"></li>
              <li>
                <a class="text-center" href="#">
                  <strong>Read All Messages</strong>
                  <i class="fa fa-angle-right"></i>
                </a>
              </li>
            </ul>
            <!-- /.dropdown-messages --></li>
          <!-- /.dropdown -->
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
              <i class="fa fa-tasks fa-fw"></i>
              <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-tasks">
              <li>
                <a href="#">
                  <div>
                    <p>
                      <strong>Task 1</strong>
                      <span class="pull-right text-muted">60% Complete</span></p>
                    <div class="progress progress-striped active">
                      <div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                        <span class="sr-only">60% Complete (success)</span></div>
                    </div>
                  </div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <p>
                      <strong>Task 2</strong>
                      <span class="pull-right text-muted">28% Complete</span></p>
                    <div class="progress progress-striped active">
                      <div class="progress-bar progress-bar-info" role="progressbar" aria-valuenow="28" aria-valuemin="0" aria-valuemax="100" style="width: 28%">
                        <span class="sr-only">28% Complete</span></div>
                    </div>
                  </div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <p>
                      <strong>Task 3</strong>
                      <span class="pull-right text-muted">60% Complete</span></p>
                    <div class="progress progress-striped active">
                      <div class="progress-bar progress-bar-warning" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 60%">
                        <span class="sr-only">60% Complete (warning)</span></div>
                    </div>
                  </div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <p>
                      <strong>Task 4</strong>
                      <span class="pull-right text-muted">85% Complete</span></p>
                    <div class="progress progress-striped active">
                      <div class="progress-bar progress-bar-danger" role="progressbar" aria-valuenow="85" aria-valuemin="0" aria-valuemax="100" style="width: 85%">
                        <span class="sr-only">85% Complete (danger)</span></div>
                    </div>
                  </div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a class="text-center" href="#">
                  <strong>See All Tasks</strong>
                  <i class="fa fa-angle-right"></i>
                </a>
              </li>
            </ul>
            <!-- /.dropdown-tasks --></li>
          <!-- /.dropdown -->
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
              <i class="fa fa-bell fa-fw"></i>
              <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-alerts">
              <li>
                <a href="#">
                  <div>
                    <i class="fa fa-comment fa-fw"></i>New Comment
                    <span class="pull-right text-muted small">4 min</span></div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <i class="fa fa-twitter fa-fw"></i>3 New Followers
                    <span class="pull-right text-muted small">12 min</span></div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <i class="fa fa-envelope fa-fw"></i>Message Sent
                    <span class="pull-right text-muted small">4 min</span></div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <i class="fa fa-tasks fa-fw"></i>New Task
                    <span class="pull-right text-muted small">4 min</span></div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <div>
                    <i class="fa fa-upload fa-fw"></i>Server Rebooted
                    <span class="pull-right text-muted small">4 min</span></div>
                </a>
              </li>
              <li class="divider"></li>
              <li>
                <a class="text-center" href="#">
                  <strong>See All Alerts</strong>
                  <i class="fa fa-angle-right"></i>
                </a>
              </li>
            </ul>
            <!-- /.dropdown-alerts --></li>
          <!-- /.dropdown -->
          <li class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#" aria-expanded="false">
              <i class="fa fa-user fa-fw"></i>
              <i class="fa fa-caret-down"></i>
            </a>
            <ul class="dropdown-menu dropdown-user">
              <li>
                <a href="#">
                  <i class="fa fa-user fa-fw"></i>User Profile</a>
              </li>
              <li>
                <a href="#">
                  <i class="fa fa-gear fa-fw"></i>Settings</a>
              </li>
              <li class="divider"></li>
              <li>
                <a href="#">
                  <i class="fa fa-sign-out fa-fw"></i>Logout</a>
              </li>
            </ul>
            <!-- /.dropdown-user --></li>
          <!-- /.dropdown --></ul>
      </nav>
      <!--/. NAV TOP -->
      <nav class="navbar-default navbar-side" role="navigation">
        <div class="sidebar-collapse">
          <ul class="nav" id="main-menu">
            <li class="active">
              <a href="index.html">
                <i class="fa fa-dashboard"></i>用户管理
                <span class="fa arrow"></span>
              </a>
              <ul class="nav nav-second-level">
                <li>
                  <a href="${root}/admin/user" class="active-menu">用户列表</a>
                </li>
                <li>
                  <a href="${root}/admin/user/del">回收站</a>
                </li>
              </ul>
            </li>
            <li>
              <a href="ui-elements.html">
                <i class="fa fa-edit"></i>发贴管理
                <span class="fa arrow"></span>
              </a>
              <ul class="nav nav-second-level">
                <li>
                  <a href="chart.html">发贴列表</a>
                </li>
                <li>
                  <a href="morris-chart.html">内容审核</a>
                </li>
                <li>
                  <a href="chart.html">回收站</a>
                </li>
                <li>
                  <a href="morris-chart.html">数据总览</a>
                </li>
              </ul>
            </li>
            <li>
              <a href="#">
                <i class="fa fa-sitemap"></i>数据分析
                <span class="fa arrow"></span></a>
              <ul class="nav nav-second-level">
                <li>
                  <a href="chart.html">用户分析</a></li>
                <li>
                <li>
                  <a href="chart.html">发贴分析</a></li>
                <li>
                <li>
                  <a href="chart.html">回贴分析</a></li>
                <li>
                <li>
                  <a href="chart.html">资源分析</a></li>
                <li>
                <li>
                  <a href="chart.html">热点资讯分析</a></li>
                <li>
                <li>
                  <a href="chart.html">网站统计</a></li>
                <li>
              </ul>
            </li>
          </ul>
        </div>
      </nav>
      <!-- /. NAV SIDE -->
      <div id="page-wrapper">
        <div class="header">
          <h1 class="page-header">Dashboard
            <small>Online - ${sessionScope.user.nickname}</small></h1>
          <ol class="breadcrumb">
            <li class="active">Dashboard</li>
            <li class="active">用户管理</li>
            <li class="active">用户列表</li>
            <li>
              <a href="${root}/admin/user/${edit.id}">用户编辑</a></li>
          </ol>
        </div>
        <div id="page-inner">
          <div class="row">
            <div class="col-md-12">
              <div class="layui-tab layui-tab-brief content">
                <ul class="layui-tab-title">
                  <li class="layui-this">基本信息</li>
                  <li>最近发贴</li>
                  <li>最近回复</li>
                  <li>最近下载</li>
                  <li>账号状态</li>
                </ul>
                <div class="layui-tab-content">
                  <div class="layui-tab-item layui-show">
                    <fieldset class="layui-elem-field">
                      <legend>Readonly</legend>
                      <div class="layui-field-box">
                        <form class="layui-form" action="">
                          <div class="layui-form-item">
                            <label class="layui-form-label">用户ID</label>
                            <div class="layui-input-inline">
                              <input type="text" required  lay-verify="required" autocomplete="off" class="layui-input" value="${edit.id}" readonly="readonly">
                            </div>
                          </div>
                          <div class="layui-form-item">
                            <label class="layui-form-label">账号</label>
                            <div class="layui-input-inline">
                              <input type="text" required lay-verify="required" autocomplete="off" class="layui-input" value="${edit.account}" readonly="readonly">
                            </div>
                          </div>
                          <div class="layui-form-item">
                            <label class="layui-form-label">用户性别</label>
                            <div class="layui-input-inline">
                              <input type="text" autocomplete="off" class="layui-input" value="${edit.genderStr}" readonly="readonly">
                            </div>
                          </div>
                          <div class="layui-form-item">
                            <label class="layui-form-label">账号状态</label>
                            <div class="layui-input-inline">
                              <input type="text" autocomplete="off" class="layui-input" value="${edit.loginFlagStr}" readonly="readonly">
                            </div>
                          </div>
                        </form>
                      </div>
                    </fieldset>
                    <fieldset class="layui-elem-field">
                      <legend>Editable</legend>
                      <div class="layui-field-box">
                        <form class="layui-form" method="post">
                          <input type="hidden" name="id" value="${edit.id}">
                          <div class="layui-form-item">
                            <label class="layui-form-label">用户昵称</label>
                            <div class="layui-input-inline">
                              <input type="text" name="nickname" required  lay-verify="usr_ni" autocomplete="off" class="layui-input" value="${edit.nickname}">
                            </div>
                          </div>
                          <div class="layui-form-item">
                            <div class="layui-input-block">
                              <button class="layui-btn" lay-submit lay-filter="AdminUserEditForm1">立即提交</button>
                              <button type="reset" class="layui-btn layui-btn-primary">重置</button>
                            </div>
                          </div>
                        </form>
                      </div>
                    </fieldset>
                  </div>
                  <div class="layui-tab-item">内容2</div>
                  <div class="layui-tab-item">内容3</div>
                  <div class="layui-tab-item">内容4</div>
                  <div class="layui-tab-item">
                    <form class="layui-form" action="" id="AdminBanUserForm">
                      <input type="hidden" name="userId" value="${edit.id}">
                      <div class="layui-form-item">
                        <label class="layui-form-label">账号状态</label>
                        <div class="layui-input-inline">
                          <select id="ban-select" name="ban" lay-filter="LoginFlag" data-type="${edit.loginFlagVo}" data-s="${edit.loginFlagStart}" data-e="${edit.loginFlagEnd}">
                            <c:if test="${edit.loginFlagVo == '1'}">
                            <option value="1" selected="selected">正常状态</option>
                            <option value="2">暂时封禁</option>
                            <option value="3">停用</option>
                            </c:if>
                            <c:if test="${edit.loginFlagVo == '2'}">
                            <option value="1">正常状态</option>
                            <option value="2" selected="selected">暂时封禁</option>
                            <option value="3">停用</option>
                            </c:if>
                            <c:if test="${edit.loginFlagVo == '3'}">
                            <option value="1">正常状态</option>
                            <option value="2">暂时封禁</option>
                            <option value="3" selected="selected">停用</option>
                            </c:if>
                          </select>
                        </div>
                      </div>
                      <div class="ban-box ban2 none">
                        <div class="layui-form-item">
                          <label class="layui-form-label">封停时间</label>
                          <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="ban2StartTime" name="ban2StartTime" required layui-verify="required">
                          </div>
                          <div class="layui-form-mid"> ~ </div>
                          <div class="layui-input-inline">
                            <input type="text" class="layui-input" id="ban2EndTime" name="ban2EndTime" required layui-verify="required">
                          </div>
                        </div>
                        <div class="layui-form-item">
                          <label class="layui-form-label">原因</label>
                          <div class="layui-input-block">
                            <textarea name="ban2Reason" placeholder="请输入" class="layui-textarea" cols="10">${edit.loginFlagVo == '2' ? edit.loginFlagMsg : ''}</textarea>
                          </div>
                        </div>
                      </div>
                      <div class="ban-box ban3 none">
                        <div class="layui-form-item">
                          <label class="layui-form-label">原因</label>
                          <div class="layui-input-block">
                            <textarea name="ban3Reason" placeholder="请输入" class="layui-textarea">${edit.loginFlagVo == '3' ? edit.loginFlagMsg : ''}</textarea>
                          </div>
                        </div>
                      </div>
                      <div class="layui-form-item">
                        <div class="layui-input-block">
                          <button class="layui-btn" lay-submit lay-filter="AdminUserEditBan">确定</button>
                          <button type="reset" class="layui-btn layui-btn-primary">取消</button>
                        </div>
                      </div>
                    </form>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <footer>
            <p>Copyright &copy; 2016.Company name All rights reserved.
              <a target="_blank" href="http://www.wctf.ink">wctf.ink</a></p>
          </footer>
        </div>
      </div>
    </div>
    <!-- JS Scripts-->
    <script src="${server}/static/admin/js/jquery-1.10.2.js"></script>
    <script src="${server}/static/admin/js/bootstrap.min.js"></script>
    <script src="${server}/static/admin/js/jquery.metisMenu.js"></script>
    <script src="${server}/static/layui/layui.js"></script>
    <script src="${server}/static/admin/js/user/user-list.js"></script>
  </body>
</html>