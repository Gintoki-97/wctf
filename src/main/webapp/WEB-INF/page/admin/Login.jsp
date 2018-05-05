<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="icon" href="http://114.67.139.119/static/image/wctf.ico" type="image/png">
    <link href="${server}/static/admin/css/bootstrap.css" rel="stylesheet" />
    <link href="${server}/static/admin/css/font-awesome.css" rel="stylesheet" />

    <style type="text/css">

    input[type="text"], input[type="password"], textarea, textarea.form-control {
      height: 50px;
      margin: 0;
      padding: 0 20px;
      vertical-align: middle;
      background: #fff;
      border: 3px solid #fff;
      font-family: 'Roboto', sans-serif;
      font-size: 16px;
      font-weight: 300;
      line-height: 50px;
      color: #888;
      -moz-border-radius: 4px;
      -webkit-border-radius: 4px;
      border-radius: 4px;
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      box-shadow: none;
      -o-transition: all .3s;
      -moz-transition: all .3s;
      -webkit-transition: all .3s;
      -ms-transition: all .3s;
      transition: all .3s;
    }

    textarea, textarea.form-control {
      padding-top: 10px;
      padding-bottom: 10px;
      line-height: 30px;
    }

    input[type="text"]: focus, input[type="password"]: focus, textarea: focus, textarea.form-control: focus {
      outline: 0;
      background: #fff;
      border: 3px solid #fff;
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      box-shadow: none;
    }

    input[type="text"]: -moz-placeholder, input[type="password"]: -moz-placeholder, textarea: -moz-placeholder, textarea.form-control: -moz-placeholder {
      color: #888;
    }

    input[type="text"]: -ms-input-placeholder, input[type="password"]: -ms-input-placeholder, textarea: -ms-input-placeholder, textarea.form-control: -ms-input-placeholder {
      color: #888;
    }

    input[type="text"]: : -webkit-input-placeholder, input[type="password"]: : -webkit-input-placeholder, textarea: : -webkit-input-placeholder, textarea.form-control: : -webkit-input-placeholder {
      color: #888;
    }

    button.btn {
      height: 50px;
      margin: 0;
      padding: 0 20px;
      vertical-align: middle;
      background: #de615e;
      border: 0;
      font-family: 'Roboto', sans-serif;
      font-size: 16px;
      font-weight: 300;
      line-height: 50px;
      color: #fff;
      -moz-border-radius: 4px;
      -webkit-border-radius: 4px;
      border-radius: 4px;
      text-shadow: none;
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      box-shadow: none;
      -o-transition: all .3s;
      -moz-transition: all .3s;
      -webkit-transition: all .3s;
      -ms-transition: all .3s;
      transition: all .3s;
    }

    button.btn: hover {
      opacity: 0.6;
      color: #fff;
    }

    button.btn: active {
      outline: 0;
      opacity: 0.6;
      color: #fff;
      -moz-box-shadow: none;
      -webkit-box-shadow: none;
      box-shadow: none;
    }

    button.btn: focus {
      outline: 0;
      opacity: 0.6;
      background: #de615e;
      color: #fff;
    }

    button.btn: active: focus, button.btn.active: focus {
      outline: 0;
      opacity: 0.6;
      background: #de615e;
      color: #fff;
    }

    body {
      font-family: 'Roboto', sans-serif;
      font-size: 16px;
      font-weight: 300;
      color: #888;
      line-height: 30px;
      text-align: center;
    }

    strong {
      font-weight: 500;
    }

    a, a: hover, a: focus {
      color: #de615e;
      text-decoration: none;
      -o-transition: all .3s;
      -moz-transition: all .3s;
      -webkit-transition: all .3s;
      -ms-transition: all .3s;
      transition: all .3s;
    }

    h1, h2 {
      margin-top: 10px;
      font-size: 38px;
      font-weight: 100;
      color: #555;
      line-height: 50px;
    }

    h3 {
      font-size: 22px;
      font-weight: 300;
      color: #555;
      line-height: 30px;
    }

    img {
      max-width: 100%;
    }

    : : -moz-selection {
      background: #de615e;
      color: #fff;
      text-shadow: none;
    }

    : : selection {
      background: #de615e;
      color: #fff;
      text-shadow: none;
    }
    
    .btn-link-1 {
      display: inline-block;
      height: 50px;
      margin: 5px;
      padding: 16px 20px 0 20px;
      background: #de615e;
      font-size: 16px;
      font-weight: 300;
      line-height: 16px;
      color: #fff;
      -moz-border-radius: 4px;
      -webkit-border-radius: 4px;
      border-radius: 4px;
    }

    .btn-link-1: hover, .btn-link-1: focus, .btn-link-1: active {
      outline: 0;
      opacity: 0.6;
      color: #fff;
    }

    .btn-link-1.btn-link-1-facebook {
      background: #4862a3;
    }

    .btn-link-1.btn-link-1-twitter {
      background: #55acee;
    }

    .btn-link-1.btn-link-1-google-plus {
      background: #dd4b39;
    }
    .btn-link-1 i {
      padding-right: 5px;
      vertical-align: middle;
      font-size: 20px;
      line-height: 20px;
    }

    .btn-link-2 {
      display: inline-block;
      height: 50px;
      margin: 5px;
      padding: 15px 20px 0 20px;
      background: rgba(0, 0, 0, 0.3);
      border: 1px solid #fff;
      font-size: 16px;
      font-weight: 300;
      line-height: 16px;
      color: #fff;
      -moz-border-radius: 4px;
      -webkit-border-radius: 4px;
      border-radius: 4px;
    }

    .btn-link-2: hover, .btn-link-2: focus, .btn-link-2: active, .btn-link-2: active: focus {
      outline: 0;
      opacity: 0.6;
      background: rgba(0, 0, 0, 0.3);
      color: #fff;
    }

      .inner-bg {
      padding: 100px 0 170px 0;
    }

    .top-content .text {
      color: #fff;
    }

    .top-content .text h1 {
      color: #fff;
    }

    .top-content .description {
      margin: 20px 0 10px 0;
    }

    .top-content .description p {
      opacity: 0.8;
    }

    .top-content .description a {
      color: #fff;
    }

    .top-content .description a: hover, .top-content .description a: focus {
      border-bottom: 1px dotted #fff;
    }

    .form-box {
      margin-top: 35px;
    }

    .form-top {
      overflow: hidden;
      padding: 0 25px 15px 25px;
      background: #444;
      background: rgba(0, 0, 0, 0.35);
      -moz-border-radius: 4px 4px 0 0;
      -webkit-border-radius: 4px 4px 0 0;
      border-radius: 4px 4px 0 0;
      text-align: left;
    }

    .form-top-left {
      float: left;
      width: 75%;
      padding-top: 25px;
    }

    .form-top-left h3 {
      margin-top: 0;
      color: #fff;
    }

    .form-top-left p {
      opacity: 0.8;
      color: #fff;
    }

    .form-top-right {
      float: left;
      width: 25%;
      padding-top: 5px;
      font-size: 66px;
      color: #fff;
      line-height: 100px;
      text-align: right;
      opacity: 0.3;
    }

    .form-bottom {
      padding: 25px 25px 30px 25px;
      background: #444;
      background: rgba(0, 0, 0, 0.3);
      -moz-border-radius: 0 0 4px 4px;
      -webkit-border-radius: 0 0 4px 4px;
      border-radius: 0 0 4px 4px;
      text-align: left;
    }

    .form-bottom form textarea {
      height: 100px;
    }

    .form-bottom form button.btn {
      width: 100%;
    }

    .form-bottom form .input-error {
      border-color: #de615e;
    }

    .social-login {
      margin-top: 35px;
    }

    .social-login h3 {
      color: #fff;
    }

    .social-login-buttons {
      margin-top: 25px;
    }
  
    @media (max-width: 767px) {
      .inner-bg {
        padding: 60px 0 110px 0;
      }
    }

    @media (max-width: 415px) {
      h1, h2 {
        font-size: 32px;
      }
    }
    </style>
    <title>WCTF - Admin Login</title>
  </head>
  <body>

    <div class="top-content">
      <div class="inner-bg">
        <div class="container">
          <div class="row">
            <div class="col-sm-8 col-sm-offset-2 text">
              <h1>Back-End Login</h1>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-6 col-sm-offset-3 form-box">
              <div class="form-top">
                <div class="form-top-left">
                  <h3>Login to the back end</h3>
                  <p>Enter your account and password to log in --</p>
                </div>
                <div class="form-top-right">
                  <i class="fa fa-lock"></i>
                </div>
              </div>
              <div class="form-bottom">
                <form role="form" action="${root}/admin/login" method="post" class="login-form">
                  <c:if test="${msg != null && msg != ''}">
                  <div class="form-group">
                    ${msg}
                  </div>
                  </c:if>
                  <div class="form-group">
                    <label class="sr-only" for="account">Account</label>
                    <input type="text" name="account" placeholder="Account..." class="form-username form-control" id="form-username"></div>
                  <div class="form-group">
                    <label class="sr-only" for="password">Password</label>
                    <input type="password" name="password" placeholder="Password..." class="form-password form-control" id="form-password"></div>
                  <button type="submit" class="btn">Sign in!</button>
                </form>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-6 col-sm-offset-3 social-login">
              <h3>or login with :</h3>
              <div class="social-login-buttons">
                <a class="btn btn-link-2" href="#">
                  <i class="fa fa-facebook"></i> Facebook</a>
                <a class="btn btn-link-2" href="#">
                  <i class="fa fa-twitter"></i> Twitter</a>
                <a class="btn btn-link-2" href="#">
                  <i class="fa fa-google-plus"></i> Google Plus</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Javascript-->
    <script src="${server}/static/admin/js/jquery-1.10.2.js"></script>
    <script src="${server}/static/admin/js/bootstrap.min.js"></script>
    <script src="${server}/static/admin/js/jquery.backstretch.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
    <script type="text/javascript">
    jQuery(document).ready(function() {
        
        // Fullscreen background
        $.backstretch("${server}/static/admin/img/login.jpg");
        
        // Form validation
        $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function() {
            $(this).removeClass('input-error');
        });
        
        $('.login-form').on('submit', function(e) {
            
            $(this).find('input[type="text"], input[type="password"], textarea').each(function(){
                if( $(this).val() == "" ) {
                    e.preventDefault();
                    $(this).addClass('input-error');
                }
                else {
                    $(this).removeClass('input-error');
                }
            });
            
        });
    });
    </script>
  </body>
</html>