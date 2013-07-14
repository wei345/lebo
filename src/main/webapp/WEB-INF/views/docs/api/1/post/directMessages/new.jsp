<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发送私信" method="POST" action="${ctx}/api/1/directMessages/new.json" enctype="multipart/form-data">
    <ul>
        <li>userId或screenName指定收件人，不能都为空</li>
        <li>text、video，不能都为空。如果有video，那么必须有image</li>
    </ul>
    <tags:field name="userId" value="51def1ce1a883914869e46f2" optional="true"/>
    <tags:field name="screenName" value="Desi_漓沫沫" optional="true"/>
    <tags:textarea name="text" value="中文abc" optional="true"/>
    <tags:field name="video" type="file" optional="true"/>
    <tags:field name="image" type="file" optional="true"/>
    <tags:field name="source" value="网页版" optional="true"/>
</tags:form>