<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="title" value="<a style='color:#658A16' href='${ctx}/docs/api/v1'>REST API v1</a>"/>

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
    <link href="${ctx}/static/jquery-validation/1.10.0/validate.css" type="text/css" rel="stylesheet"/>
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
    </style>

    <sitemesh:head/>
</head>

<body>
<div class="container">
    <%@ include file="/WEB-INF/layouts/header.jsp" %>
    <div id="content">
        <sitemesh:body/>
    </div>
    <%@ include file="/WEB-INF/layouts/footer.jsp" %>
</div>
<script src="${ctx}/static/bootstrap/2.2.2/js/bootstrap.min.js" type="text/javascript"></script>
<script>
    $(document).ready(function() {
        $('pre code').each(function(i, e) {
            //去掉各行开头的4个空格
            if(e.innerHTML.match(/(    .*)+/)){
                e.innerHTML = e.innerHTML.replace(/    (.*)/g, "$1");
            }
            //去掉首尾空行
            e.innerHTML = e.innerHTML.replace(/^\n+|\n+$/g, '');

            hljs.highlightBlock(e);
        });
    });
</script>
</body>
</html>