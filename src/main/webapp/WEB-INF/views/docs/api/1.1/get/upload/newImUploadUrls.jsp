<%@ page import="com.lebo.service.UploadService" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="types" value="<%=UploadService.allowedContentType%>"/>


<tags:form name="获取即时通讯文件上传地址" action="${ctx}/api/1.1/upload/newImUploadUrls.json" method="GET">
    <p>
        内容类型必须为以下值之一:
    </p>
    <ul>
        <c:forEach items="${types}" var="type" varStatus="stat">
            <li>${type}</li>
        </c:forEach>
    </ul>

    <tags:field name="contentType"/>

    <div id="insert-before-here" style="display: none"></div>

    <div class="controls-group">
        <button class="btn" onclick="addAtt()" type="button">
            <span class="icon-plus"></span>
            contentType
        </button>
    </div>
    <br/>
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

<tags:example method="GET"
              url="http://localhost:8080/api/1.1/upload/newImUploadUrls.json?contentType=video%2Fmp4&contentType=image%2Fjpeg">
    [
        "http://file.dev.lebooo.com/im/5280d5f31a8805f70c993fc0.mp4?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1384178691&Signature=5PwSLNFHJvfBiiaIqSpas27V8iY%3D",
        "http://file.dev.lebooo.com/im/5280d5f31a8805f70c993fc2.jpg?OSSAccessKeyId=7sKDB271X0Ur9ej0&Expires=1384178691&Signature=QT0gP6TwXhHYk2%2F7djl7bih%2BhcQ%3D"
    ]
</tags:example>

<script>
    var s = '<div class="control-group">' +
            '<label class="control-label">' +
            'contentType' +
            '</label>' +
            '<div class="controls">' +
            '<input type="text" name="contentType" placeholder="contentType" value="">' +
            '<button class="btn"  type="button" onclick="$(this).parent().parent().remove()"><span class="icon-minus"></span></button>' +
            '</div>' +
            '</div>';

    function addAtt() {
        $(s).insertBefore('#insert-before-here');
    }
</script>