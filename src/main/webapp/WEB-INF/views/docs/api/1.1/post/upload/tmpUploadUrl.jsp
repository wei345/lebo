<%@ page import="com.lebo.rest.UploadRestController" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="types" value="<%=UploadRestController.UploadType.values()%>"/>


<tags:form name="获取临时上传地址" action="${ctx}/api/1.1/upload/tmpUploadUrl.json" method="POST">
    <table class="table table-hover table-bordered">
        <tr>
            <th>type</th>
            <th>Content-Type</th>
            <th>说明</th>
        </tr>
        <c:forEach items="${types}" var="type" varStatus="stat">
            <tr>
                <td>${type}</td>
                <td>${type.contentType}</td>
                <td>${type.desc}</td>
            </tr>
        </c:forEach>
    </table>
    上面表格中：
    <ul>
        <li>type列: 本接口type参数可能的值。</li>
        <li>Content-Type列: 向本接口返回的URL上传文件时，必须指定HTTP头信息Content-Type字段为对应的值。</li>
    </ul>
    <tags:field name="type"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/upload/tmpUploadUrl.json?type=POST_VIDEO">
    {
        url: "http://file.dev.lebooo.com/tmp/expire-2013-10-19-14-02-33-522-post-video-unique-526212691a880d00f61e9a80.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382162553&Signature=51nkVeEG+baU3BJalbZxlqizQcw="
    }
</tags:example>