<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
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
    <title>WCTF - Admin 用户回收站</title>
    
    <link href="${server}/static/admin/css/bootstrap.css" rel="stylesheet" />
    <link href="${server}/static/admin/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/font-awesome.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/custom-styles.css" rel="stylesheet" />
    <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="${server}/static/admin/js/Lightweight-Chart/cssCharts.css">
    <link href="${server}/static/layui/css/layui.css" rel="stylesheet" />
    <style type="text/css">
    .layui-form-checked[lay-skin=primary] i {
      border-color: #000000;
      background-color: #3E3E3E;
      color: #FFF;
    }
    .layui-table-page {
      margin: 10px 0 0 0;
    }
    .layui-laypage .layui-laypage-curr .layui-laypage-em {
      background-color: #F36A5A;
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
              <a href="">
                <i class="fa fa-dashboard"></i>用户管理
                <span class="fa arrow"></span>
              </a>
              <ul class="nav nav-second-level">
                <li>
                  <a href="${root}/admin/user">用户列表</a>
                </li>
                <li>
                  <a href="javascript:;"  class="active-menu">回收站</a>
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
            <li>
              <a href="${root}/admin/user/del">回收站</a></li>
          </ol>
        </div>
        <div id="page-inner">
          <div class="row">
            <div class="col-md-12 wrapper clear">
              <div class="layui-btn-group" style="float: left;">
                <button class="layui-btn layui-bg-black layui-btn-sm reload-btn">
                  <i class="layui-icon">&#x1002;</i> 刷新
                </button>
              </div>
              <div class="layui-btn-group" style="float: right;">
                <button class="layui-btn layui-btn-primary layui-btn-sm">
                  <i class="layui-icon">&#xe654;</i>
                </button>
                <button class="layui-btn layui-btn-primary layui-btn-sm">
                  <i class="layui-icon">&#xe642;</i>
                </button>
                <button class="layui-btn layui-btn-primary layui-btn-sm" id="DeleteBar">
                  <i class="layui-icon">&#xe640;</i>
                </button>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12">
              <table class="layui-hide" id="UserList"></table>
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