<%@ page import="com.lebo.service.account.ShiroUser" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%
    ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
    if (user != null) {
        String profileImageUrl = user.getProfileImageUrl();
        request.setAttribute("profileImageUrl", profileImageUrl);
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>乐播API文档</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
    <link href="${ctx}/static/bootstrap/2.2.2/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/jquery-validation/1.11.0/validate.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/hightlight/github.min.css" type="text/css" rel="stylesheet"/>
    <script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.0/messages_bs_zh.js" type="text/javascript"></script>
    <script src="${ctx}/static/hightlight/highlight.min.js" type="text/javascript"></script>

    <style>
        pre code {
            background-color: transparent;
        }

        #header #title small a {
            margin-left: 1em;
            color: #658A16
        }
    </style>

    <sitemesh:head/>
</head>

<body>
<div class="container">

    <div id="header">
        <div id="title">
            <h1><a href="${ctx}/docs/api/1">乐播<small> REST API v1</small></a>
                <small>
                    <a href='${ctx}/docs/api/1/dataStructures'>返回对象数据结构</a>
                    <a href='${ctx}/docs/api/1/pagination'>分页</a>
                    <a href='${ctx}/docs/api/1/environments'>环境</a>
                </small>
                <shiro:user>
                    <div class="btn-group pull-right">
                        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="icon-user"><c:if test="${profileImageUrl != null}"><img
                                    src="${profileImageUrl}"/></c:if></i>
                            <shiro:principal property="screenName"/>
                            <span class="caret"></span>
                        </a>

                        <ul class="dropdown-menu">
                            <li><a href="${ctx}/profile">Edit Profile</a></li>
                            <li><a href="${ctx}/logout">Logout</a></li>
                        </ul>
                    </div>
                </shiro:user>
            </h1>
        </div>
        <c:if test="${success != null}">
            <div class="alert alert-success controls">
                <button class="close" data-dismiss="alert">×</button>
                    ${success}
            </div>
        </c:if>
        <c:if test="${error != null}">
            <div class="alert alert-error controls">
                <button class="close" data-dismiss="alert">×</button>
                    ${error}
            </div>
        </c:if>
    </div>

    <div id="content">
        <sitemesh:body/>
    </div>
    <%@ include file="/WEB-INF/layouts/footer.jsp" %>
</div>
<script src="${ctx}/static/bootstrap/2.2.2/js/bootstrap.min.js" type="text/javascript"></script>
<script>
    $(document).ready(function () {
        $('pre code').each(function (i, e) {
            //去掉各行开头的4个空格
            if (e.innerHTML.match(/(    .*)+/)) {
                e.innerHTML = e.innerHTML.replace(/    (.*)/g, "$1");
            }
            //去掉首尾空行
            e.innerHTML = e.innerHTML.replace(/^\n+|\n+$/g, '');

            hljs.highlightBlock(e);
        });
    });
    //去掉textarea首尾空格 -- 如果textarea.tag被格式化，那么页面上textarea内容有空格。
    $('textarea').each(function () {
        this.innerHTML = $.trim(this.innerHTML);
    });
</script>
</body>
</html>