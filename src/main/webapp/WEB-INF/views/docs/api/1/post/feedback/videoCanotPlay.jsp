<%@ page import="com.lebo.service.VideoConvertService" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="post" value="<%=VideoConvertService.OBJECT_TYPE_POST%>"/>
<c:set var="comment" value="<%=VideoConvertService.OBJECT_TYPE_COMMENT%>"/>

<tags:form name="报告不能播放的视频" method="POST" action="${ctx}/api/1/feedback/videoCanotPlay.json">
    <p>
        如果客户端遇到不能播放的视频，则调用此接口告知服务器，服务器会做转码处理，转码后视频信息中会增加<code>videoConverted</code>字段，
        详见<a href="${ctx}/docs/api/1/dataStructures">返回对象数据结构</a>。
    </p>
    <p>
        <code>${post}</code> 或 <code>${comment}</code>
    </p>
    <tags:field name="objectType"/>
    <tags:field name="objectId" value=""/>
    <tags:field name="videoUrl"/>
    <p>
        如：<code>lebo/2.7 Android/4.0</code>
    </p>
    <tags:field name="client"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/feedback/videoCanotPlay.json?objectType=post&objectId=5255473a1a887a7601ba082f&videoUrl=&client=">
    {
        ok: true
    }
</tags:example>