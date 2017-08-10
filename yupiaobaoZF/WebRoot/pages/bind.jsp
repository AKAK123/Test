<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <%--meta name=viewport content="width=device-width, initial-scale=1.1, minimum-scale=1.0, maximum-scale=1.2, user-scalable=yes"/--%>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <%--link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet"--%>
    <%--link href="./css/bootstrap.min.css" rel="stylesheet"--%>
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <%--link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap-theme.min.css"--%>
    <%--link rel="stylesheet" type="text/css" href="./css/custom/bootstrap-combined.min.css"--%>

    <link rel="stylesheet"  href="<%=path %>/css/radio.css" />
    <link rel="stylesheet"  href="<%=path %>/css/reset.css"/>
    <link rel="stylesheet"  href="<%=path %>/css/base.css" />

    <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
    <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>


    <title></title>
</head>
<body>

<nav class="navbar navbar-default ">
    <%-- We use the fluid option here to avoid overriding the fixed width of a normal container within the narrow content columns. --%>
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-6" aria-expanded="false">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#"><img src="<%=path %>/images/etripplanlogo_txt.png" height="30px" ></a>
        </div>

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-6">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#"><span class="glyphicon glyphicon-user" aria-hidden="true" style="margin-right: 10px"></span>余票宝用户注册</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-pushpin" aria-hidden="true" style="margin-right: 10px"></span>用户绑定微信</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-plane" aria-hidden="true" style="margin-right: 10px"></span>航班查询</a></li>
                <li><a href="#"><span class="glyphicon glyphicon-save" aria-hidden="true" style="margin-right: 10px"></span>APP下载</a></li>
            </ul>
        </div><%-- /.navbar-collapse --%>
    </div>
</nav>

<%--nav class="navbar navbar-default" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
                data-target="#example-navbar-collapse">
            <span class="sr-only">切换导航</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#" style=""><img src="./imgs/etripplanlogo.png" height="20px" style="margin-right: 10px" >余票宝</a>
    </div>
    <div class="collapse navbar-collapse" id="example-navbar-collapse">
        <ul class="nav navbar-nav">
            <li class="active"><a href="#">用户注册</a></li>
            <li><a href="#">用户绑定</a></li>
            <li><a href="#">APP下载</a></li>
            <li><a href="#">航班查询</a></li>
            <%--li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                    Java <b class="caret"></b>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#">jmeter</a></li>
                    <li><a href="#">EJB</a></li>
                    <li><a href="#">Jasper Report</a></li>
                    <li class="divider"></li>
                    <li><a href="#">分离的链接</a></li>
                    <li class="divider"></li>
                    <li><a href="#">另一个分离的链接</a></li>
                </ul>
            </li--%>
        </ul>
    </div>
</nav--%>

<div class="bs-docs-header" id="content" tabindex="-1">
    <div class="container">
        <h1>余票宝微信绑定</h1>
        <p>绑定微信用户可以获取最新余票消息。</p>
    </div>
</div>





<%-----------------------content--------------------------------------------%>
<div id="wxBindPage">



    <%------------------------------------------- Form --------------------------------------------%>
    <div class="content">

        <form action="submitBindViaCardSelect.wx" method="POST" onsubmit="return validate();" autocomplete="off">
            <input type="hidden" name="from" value="">
            <ul>
                <li class="grid-a">
                    <div class="grida-block-a"><label>微信用户</label></div>
                    <div class="grida-block-b"><input class="form-control" id="disabledInput" type="text" placeholder="annie_lau" disabled></div>
                </li>
                <%----------------------------------------- 华丽的分隔线 ----------------------------------------------%>

                <li id="wxBindPageByCard">
                    <div class="grid-a">
                        <div class="grida-block-a"><label>手机号码</label></div>
                        <div class="grida-block-b"><input type="password" class="form-control" id="inputPassword3" autocomplete="off"></div>
                    </div>
                    <div class="grid-a">
                        <div class="grida-block-a"><label>余票宝密码</label></div>
                        <div class="grida-block-b"><input type="password" class="form-control" id="inputPassword3" autocomplete="off"></div>

                    </div>
                </li>


                <%----------------------------------------- 骄傲的分割线 ----------------------------------------------%>
                <li class="grid-c">
                    <div class="gridc-block-a"><label>验证码</label></div>
                    <div class="gridc-block-b"><input type="password" class="form-control" id="inputPassword3" autocomplete="off"></div>
                    <div class="gridc-block-c"><img id="imgHolder" src="../gencode?wxid=oer35Pldeljwlh73Wfda0N6MheRo&signature=2b42fe45cd21c196cc7e927fb20a8b907f3c836f&timestamp=1458715716&nonce=1498799733" alt="验证码"></div>
                    <div class="gridc-block-d"><a href="#" id="cardChangeCode" onclick="chengeCheckCode();">换一张</a></div>
                </li>
                <li>
                    <div class="error-block">
                        <span class="error-msg" id="errorMsg"></span>
                    </div>
                </li>
            </ul>
            <div class="grid-b">
                <%--input class="ui-btn ui-btn-orange ui-shadow" id="cardBind" type="submit" value="绑  定" /--%>
                <button type="button" class="btn btn-warning btn-lg btn-block">绑  定</button>
            </div>
            <input name="signature" id="signature" type="hidden" value="2b42fe45cd21c196cc7e927fb20a8b907f3c836f" />
            <input name="timestamp" id="timestamp" type="hidden" value="1458715716" />
            <input name="nonce" id="nonce" type="hidden" value="1498799733" />
            <input name="wxid" id="wxid" type="hidden" id="wxid" value="oer35Pldeljwlh73Wfda0N6MheRo" />
            <input id="bindType" name="bindType" type="hidden" value="1" />
        </form>
    </div>
    <script src="../js/bind-card-certification.js?a=a"></script>

</div>


<div>

</div>



</body>
</html>