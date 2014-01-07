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
    <title>乐播</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <meta http-equiv="Cache-Control" content="no-store"/>
    <meta http-equiv="Pragma" content="no-cache"/>
    <meta http-equiv="Expires" content="0"/>

    <link type="image/x-icon" href="${ctx}/static/images/favicon.ico" rel="shortcut icon">
    <link href="${ctx}/static/bootstrap/2.2.2/css/bootstrap.min.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/jquery-validation/1.11.0/validate.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/jquery-ui/jquery-ui-1.10.3.custom.css" type="text/css" rel="stylesheet"/>
    <link href="${ctx}/static/styles/default.css" type="text/css" rel="stylesheet"/>
    <script src="${ctx}/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-validation/1.11.0/messages_bs_zh.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-ui/jquery-ui-1.10.3.custom.js" type="text/javascript"></script>
    <script src="${ctx}/static/jquery-ui/jquery.ui.datepicker-zh-CN.min.js" type="text/javascript"></script>
    <script src="${ctx}/static/application.js" type="text/javascript"></script>

    <sitemesh:head/>
</head>

<body>
<div class="container">

    <div id="header">
        <div id="title">
            <h1><a href="${ctx}/admin">乐播</a>
                <small><sitemesh:title/></small>
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
</body>
</html>