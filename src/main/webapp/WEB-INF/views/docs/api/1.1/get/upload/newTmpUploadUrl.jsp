<%@ page import="com.lebo.service.UploadService" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="types" value="<%=UploadService.allowedContentType%>"/>


<tags:form name="获取新的临时上传地址" action="${ctx}/api/1.1/upload/newTmpUploadUrl.json" method="GET">
    <p>
        内容类型必须为以下值之一:
    </p>
    <ul>
        <c:forEach items="${types}" var="type" varStatus="stat">
            <li>${type}</li>
        </c:forEach>
    </ul>

    <tags:field name="contentType"/>
</tags:form>

<p>使用该接口返回的url上传文件:</p>
<ul>
    <li>
        请求 method 必须为<code>PUT</code>
    </li>
    <li>
        请求 <code>contentType</code> 必须与获取该url时提交的 contentType 一样
    </li>
    <li>
        该url是有<code>有效期</code>的，客户端应该每次上传文件时通过该接口获取新的url
    </li>
</ul>

<p>curl命令测试上传文件的例子:</p>
<code>
    curl -H "Content-Type: image/jpeg" -T "/path/to/photo.jpg"
    "http://file.dev.lebooo.com/tmp/expire-2013-10-21-21-05-25-image-jpeg-526518851a88064aeb74ef0a.jpg?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382360725&Signature=eEF7WVVkWp4PlArduYibk8yV3g4%3D"
</code>

<tags:example method="GET" url="http://localhost:8080/api/1.1/upload/newTmpUploadUrl.json?contentType=audio%2Famr">
    {
        url: "http://file.dev.lebooo.com/tmp/expire-2013-10-22-16-09-23-audio-amr-526624a31a88da6a33097b97.amr?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1382429363&Signature=Okrtq22P%2FafbMGohY1NX6vXr2fQ%3D"
    }
</tags:example>