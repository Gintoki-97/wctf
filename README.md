# wctf
世界 CTF 黑客大赛交流社区

## 平台简介

WCTF 是基于多个开源框架，在它们的基础上搭建的社区论坛系统。

WCTF 是在Spring Framework基础上搭建的一个Java 社区论坛系统，以Spring MVC作为模型视图控制器，MyBatis为数据访问层，
Apache Shiro为权限授权层，Redis作为二级缓存（对需要保证缓存一致性的数据进行缓存），Ehcahe作为一级缓存（对常用数据进行缓存）。

WCTF 内置了一系列基于Java语言的插件型工具和高效的**异步任务调度**工具，
包括：系统权限组件、核心工具组件、动态自动管理组件、多级缓存系统、集群支持等。
前端界面风格采用了国内流行的结构简单、性能优良、页面美观的Layui模块化前端框架。
采用分层设计、双重验证、提交数据安全编码、密码加密、访问验证、数据权限验证。
使用Maven做项目管理，提高项目的易开发性、扩展性。

WCTF 目前包括以下四大模块，系统管理（sys）模块、用户管理（user）模块、发贴管理（post）模块、新闻管理（news）模块、
动态管理（trend）模块。 **系统管理模块** ，包括系统管理（用户管理、发贴审核、新闻管理）。

WCTF 提供了常用工具进行封装，包括异步任务工具、日志工具、缓存工具、服务器端验证、以及其它常用小工具等。

## 内置功能

1.	用户管理：用户是系统操作者，该功能主要提供用户的一些列可用操作。
2.	发贴管理：系统中所有发贴均由此模块提供支持，如：发布公告、提问、发表讨论、提出建议等等。
4.	新闻管理：配系统中提供了三中新闻类型，包括banner新闻，活动新闻，社区新闻。
5.	动态管理：角用户在进行某些模块的操作时，会自动更新自己的动态模块，并更新到该用户的首页上。
6.	权限管理：使用Shiro注解式权限配置，即可完成用户的权限校验。
7.	操作日志：系统正常操作日志记录和查询；系统异常信息日志记录和查询。
8.	连接池监视：监视当期系统数据库连接池状态，可进行分析SQL找出系统性能瓶颈。


## 技术选型

1、后端

* 核心框架：Spring Framework 4.1
* 安全框架：Apache Shiro 1.2
* 视图框架：Spring MVC 4.1
* 服务端验证：Hibernate Validator 5.2
* 任务调度：Spring Task 4.1
* 持久层框架：MyBatis 3.2
* 数据库连接池：C3P0 0.9
* 缓存框架：Redis、Ehcache 2.6
* 日志管理：SLF4J 1.7、Log4j
* 工具类：Apache Commons、Gson、Guava

2、前端

* JS框架：JQuery、JQuery-cookie、Layui
* CSS框架：Layui
* 客户端验证：Layui-verify
* 富文本在线编辑：Layui-editor、Simditor
* 数据表格：Lay-table
* 对话框：Layer

4、平台

* 硬件平台：Linux
* 服务器中间件：在Java EE 5规范（Servlet 3.0、JSP 2.1）下开发，支持应用服务器中间件有Tomcat 6+、Jboss 7+，同时支持整合 Nginx 1.2+。
* 数据库支持：MySQL
* 开发环境：Java、Eclipse Java EE Neon.2 Release (4.6.2)、Maven 3.1、Git

## 安全考虑

1. 开发语言：系统采用Java 语言开发，具有卓越的通用性、高效性、平台移植性和安全性。
2. 分层设计：（数据库层，数据访问层，业务逻辑层，展示层）层次清楚，低耦合，各层必须通过接口才能接入并进行参数校验（如：在展示层不可直接操作数据库），保证数据操作的安全。
3. 双重验证：用户表单提交双验证：包括服务器端验证及客户端验证，防止用户通过浏览器恶意修改（如不可写文本域、隐藏变量篡改、上传非法文件等），跳过客户端验证操作数据库。
4. 安全编码：用户表单提交所有数据，在服务器端都进行安全编码，防止用户提交非法脚本及SQL注入获取敏感数据等，确保数据安全。
5. 密码加密：登录用户密码进行MD5加盐加密，此加密方法是不可逆的，同时又大大增加了彩虹表暴力破解的难度，保证密文泄露后甚至是数据库拖库后的安全问题。
6. 强制访问：系统对所有管理端链接都进行用户身份权限验证，防止用户直接填写url进行访问。

## 常见问题

1. 用一段时间提示内存溢出，请修改JVM参数：-Xmx512m -XX:MaxPermSize=256m
2. 有时出现文字乱码：修改Tomcat的server.xml文件的Connector项，增加URIEncoding="UTF-8"

## 更多文档

* 正在编写
