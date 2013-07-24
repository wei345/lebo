<%@ page import="com.lebo.service.account.ShiroUser" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<%@ page language="java" pageEncoding="UTF-8" %>
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
<div id="header">
    <div id="title">
        <h1><a href="${ctx}/">乐播</a>
            <small><sitemesh:title/>${title}</small>
            <shiro:user>
                <div class="btn-group pull-right">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-user"><c:if test="${profileImageUrl != null}"><img
                                src="${profileImageUrl}"/></c:if></i>
                        <shiro:principal property="name"/>
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
