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
    <title>Admin Dashboard</title>
    
    <!-- Bootstrap Styles-->
    <link href="${server}/static/admin/css/bootstrap.css" rel="stylesheet" />
    <!-- FontAwesome Styles-->
    <link href="${server}/static/admin/css/font-awesome.css" rel="stylesheet" />
    <!-- Morris Chart Styles-->
    <link href="${server}/static/admin/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
    <!-- Custom Styles-->
    <link href="${server}/static/admin/css/custom-styles.css" rel="stylesheet" />
    <!-- Google Fonts-->
    <link href='https://fonts.googleapis.com/css?family=Open+Sans' rel='stylesheet' type='text/css' />
    <link rel="stylesheet" href="${server}/static/admin/js/Lightweight-Chart/cssCharts.css">
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
            <li>
              <a href="index.html">
                <i class="fa fa-dashboard"></i>用户管理
                <span class="fa arrow"></span>
              </a>
              <ul class="nav nav-second-level">
                <li>
                  <a href="${root}/admin/user">用户列表</a>
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
            <li class="active">
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
                  <a href="chart.html" class="active-menu">网站统计</a></li>
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
            <li class="active">数据分析</li>
            <li>
              <a href="#">网站统计</a></li>
          </ol>
        </div>
        <div id="page-inner">
          <!-- /. ROW -->
          <div class="row">
            <div class="col-md-3 col-sm-12 col-xs-12">
              <div class="board">
                <div class="panel panel-primary">
                  <div class="number">
                    <h3>
                      <h3>44,023</h3>
                      <small>Daily Visits</small></h3>
                  </div>
                  <div class="icon">
                    <i class="fa fa-eye fa-5x red"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 col-sm-12 col-xs-12">
              <div class="board">
                <div class="panel panel-primary">
                  <div class="number">
                    <h3>
                      <h3>32,850</h3>
                      <small>Sales</small></h3>
                  </div>
                  <div class="icon">
                    <i class="fa fa-shopping-cart fa-5x blue"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 col-sm-12 col-xs-12">
              <div class="board">
                <div class="panel panel-primary">
                  <div class="number">
                    <h3>
                      <h3>56,150</h3>
                      <small>Comments</small></h3>
                  </div>
                  <div class="icon">
                    <i class="fa fa-comments fa-5x green"></i>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-3 col-sm-12 col-xs-12">
              <div class="board">
                <div class="panel panel-primary">
                  <div class="number">
                    <h3>
                      <h3>89,645</h3>
                      <small>Daily Profits</small></h3>
                  </div>
                  <div class="icon">
                    <i class="fa fa-user fa-5x yellow"></i>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-6 col-xs-12">
              <div class="panel panel-default chartJs">
                <div class="panel-heading">
                  <div class="card-title">
                    <div class="title">Line Chart</div></div>
                </div>
                <div class="panel-body">
                  <canvas id="line-chart" class="chart"></canvas>
                </div>
              </div>
            </div>
            <div class="col-sm-6 col-xs-12">
              <div class="panel panel-default chartJs">
                <div class="panel-heading">
                  <div class="card-title">
                    <div class="title">Bar Chart</div></div>
                </div>
                <div class="panel-body">
                  <canvas id="bar-chart" class="chart"></canvas>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-xs-6 col-md-3">
              <div class="panel panel-default">
                <div class="panel-body easypiechart-panel">
                  <h4>Profit</h4>
                  <div class="easypiechart" id="easypiechart-blue" data-percent="82">
                    <span class="percent">82%</span></div>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-md-3">
              <div class="panel panel-default">
                <div class="panel-body easypiechart-panel">
                  <h4>Sales</h4>
                  <div class="easypiechart" id="easypiechart-orange" data-percent="55">
                    <span class="percent">55%</span></div>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-md-3">
              <div class="panel panel-default">
                <div class="panel-body easypiechart-panel">
                  <h4>Customers</h4>
                  <div class="easypiechart" id="easypiechart-teal" data-percent="84">
                    <span class="percent">84%</span></div>
                </div>
              </div>
            </div>
            <div class="col-xs-6 col-md-3">
              <div class="panel panel-default">
                <div class="panel-body easypiechart-panel">
                  <h4>No. of Visits</h4>
                  <div class="easypiechart" id="easypiechart-red" data-percent="46">
                    <span class="percent">46%</span></div>
                </div>
              </div>
            </div>
          </div>
          <!--/.row-->
          <div class="copyrights">Collect from
            <a href="http://www.cssmoban.com/">企业网站模板</a></div>
          <div class="row">
            <div class="col-md-5">
              <div class="panel panel-default">
                <div class="panel-heading">Line Chart</div>
                <div class="panel-body">
                  <div id="morris-line-chart"></div>
                </div>
              </div>
            </div>
            <div class="col-md-7">
              <div class="panel panel-default">
                <div class="panel-heading">Bar Chart Example</div>
                <div class="panel-body">
                  <div id="morris-bar-chart"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-9 col-sm-12 col-xs-12">
              <div class="panel panel-default">
                <div class="panel-heading">Area Chart</div>
                <div class="panel-body">
                  <div id="morris-area-chart"></div>
                </div>
              </div>
            </div>
            <div class="col-md-3 col-sm-12 col-xs-12">
              <div class="panel panel-default">
                <div class="panel-heading">Donut Chart Example</div>
                <div class="panel-body">
                  <div id="morris-donut-chart"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-md-12"></div>
          </div>
          <!-- /. ROW -->
          <div class="row">
            <div class="col-md-4 col-sm-12 col-xs-12">
              <div class="panel panel-default">
                <div class="panel-heading">Tasks Panel</div>
                <div class="panel-body">
                  <div class="list-group">
                    <a href="#" class="list-group-item">
                      <span class="badge">7 minutes ago</span>
                      <i class="fa fa-fw fa-comment"></i>Commented on a post</a>
                    <a href="#" class="list-group-item">
                      <span class="badge">16 minutes ago</span>
                      <i class="fa fa-fw fa-truck"></i>Order 392 shipped</a>
                    <a href="#" class="list-group-item">
                      <span class="badge">36 minutes ago</span>
                      <i class="fa fa-fw fa-globe"></i>Invoice 653 has paid</a>
                    <a href="#" class="list-group-item">
                      <span class="badge">1 hour ago</span>
                      <i class="fa fa-fw fa-user"></i>A new user has been added</a>
                    <a href="#" class="list-group-item">
                      <span class="badge">1.23 hour ago</span>
                      <i class="fa fa-fw fa-user"></i>A new user has added</a>
                    <a href="#" class="list-group-item">
                      <span class="badge">yesterday</span>
                      <i class="fa fa-fw fa-globe"></i>Saved the world</a>
                  </div>
                  <div class="text-right">
                    <a href="#">More Tasks
                      <i class="fa fa-arrow-circle-right"></i></a>
                  </div>
                </div>
              </div>
            </div>
            <div class="col-md-8 col-sm-12 col-xs-12">
              <div class="panel panel-default">
                <div class="panel-heading">Responsive Table Example</div>
                <div class="panel-body">
                  <div class="table-responsive">
                    <table class="table table-striped table-bordered table-hover">
                      <thead>
                        <tr>
                          <th>S No.</th>
                          <th>First Name</th>
                          <th>Last Name</th>
                          <th>User Name</th>
                          <th>Email ID.</th></tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>1</td>
                          <td>John</td>
                          <td>Doe</td>
                          <td>John15482</td>
                          <td>name@site.com</td></tr>
                        <tr>
                          <td>2</td>
                          <td>Kimsila</td>
                          <td>Marriye</td>
                          <td>Kim1425</td>
                          <td>name@site.com</td></tr>
                        <tr>
                          <td>3</td>
                          <td>Rossye</td>
                          <td>Nermal</td>
                          <td>Rossy1245</td>
                          <td>name@site.com</td></tr>
                        <tr>
                          <td>4</td>
                          <td>Richard</td>
                          <td>Orieal</td>
                          <td>Rich5685</td>
                          <td>name@site.com</td></tr>
                        <tr>
                          <td>5</td>
                          <td>Jacob</td>
                          <td>Hielsar</td>
                          <td>Jac4587</td>
                          <td>name@site.com</td></tr>
                        <tr>
                          <td>6</td>
                          <td>Wrapel</td>
                          <td>Dere</td>
                          <td>Wrap4585</td>
                          <td>name@site.com</td></tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- /. ROW -->
          <footer>
            <p>Copyright &copy; 2016.Company name All rights reserved.
              <a target="_blank" href="http://www.wctf.ink">wctf.ink</a></p>
          </footer>
        </div>
        <!-- /. PAGE INNER --></div>
      <!-- /. PAGE WRAPPER --></div>
    <!-- /. WRAPPER -->
    <!-- JS Scripts-->
    <!-- jQuery Js -->
    <script src="${server}/static/admin/js/jquery-1.10.2.js"></script>
    <!-- Bootstrap Js -->
    <script src="${server}/static/admin/js/bootstrap.min.js"></script>
    <!-- Metis Menu Js -->
    <script src="${server}/static/admin/js/jquery.metisMenu.js"></script>
    <!-- Morris Chart Js -->
    <script src="${server}/static/admin/js/morris/raphael-2.1.0.min.js"></script>
    <script src="${server}/static/admin/js/morris/morris.js"></script>
    <script src="${server}/static/admin/js/easypiechart.js"></script>
    <script src="${server}/static/admin/js/easypiechart-data.js"></script>
    <script src="${server}/static/admin/js/Lightweight-Chart/jquery.chart.js"></script>
    <!-- Custom Js -->
    <script src="${server}/static/admin/js/custom-scripts.js"></script>
    <!-- Chart Js -->
    <script type="text/javascript" src="${server}/static/admin/js/chart.min.js"></script>
    <script type="text/javascript" src="${server}/static/admin/js/chartjs.js"></script>
    
    <script src="${server}/static/js/vue.js"></script>
    <script src="${server}/static/admin/js/dashboard/dashboard.js"></script>
  </body>
</html>