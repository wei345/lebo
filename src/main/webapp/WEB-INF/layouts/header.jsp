<%@ page language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div id="header">
    <div id="title">
        <h1><a href="${ctx}/">乐播</a>
            <small><sitemesh:title/></small>
            <shiro:user>
                <div class="btn-group pull-right">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-user"><img src="<shiro:principal property="profileImageUrl"/>"/></i>
                        <shiro:principal property="name"/>
                        <span class="caret"></span>
                    </a>

                    <ul class="dropdown-menu">
                            <%--<li><a href="${ctx}/profile">Edit Profile</a></li>--%>
                        <li><a href="${ctx}/logout">Logout</a></li>
                    </ul>
                </div>
            </shiro:user>
        </h1>
    </div>
</div>
