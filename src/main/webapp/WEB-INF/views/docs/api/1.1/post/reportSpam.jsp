<%@ page import="com.lebo.entity.ReportSpam" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%
    StringBuilder sb;
    /*预留参数
    sb = new StringBuilder();
    for (ReportSpam.ReportType type : ReportSpam.ReportType.values()) {
        sb.append("<li>");
        sb.append("<code>");
        sb.append(type.name());
        sb.append("</code>");
        sb.append(" ");
        sb.append(type.getName());
        sb.append("</li>");
    }
    request.setAttribute("reportTypeList", sb.toString());
    */

    sb = new StringBuilder();
    for (ReportSpam.ObjectType type : ReportSpam.ObjectType.values()) {
        sb.append("<li>");
        sb.append("<code>");
        sb.append(type.name());
        sb.append("</code>");
        sb.append(" ");
        sb.append(type.getName());
        sb.append("</li>");
    }
    request.setAttribute("reportObjectTypeList", sb.toString());
%>

<tags:form name="举报" method="POST" action="${ctx}/api/1.1/reportSpam.json">
    <%--预留参数
    <p>
        举报类型：
    <ul>
            ${reportTypeList}
    </ul>
    </p>
    <tags:field name="reportType" optional="true"/>
    --%>

    <p>
        举报对象类型：
    <ul>
            ${reportObjectTypeList}
    </ul>
    </p>
    <tags:field name="reportObjectType"/>

    <p>
        举报对象ID:
    </p>
    <tags:field name="reportObjectId"/>

    <%--预留参数
    <p>
        举报补充说明
    </p>
    <tags:textarea name="reportNotes" optional="true"/>
    --%>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/reportSpam.json?reportObjectType=POST&reportObjectId=528b2b111a888afc0a256dbb">
    {
        id: "52d6440d1a88407ecc8e51e2",
        reportUserId: "528b27921a888afc0a256db3",
        reportObjectType: "POST",
        reportObjectId: "528b2b111a888afc0a256dbb",
        informerUserId: "52356929343539a89a52dc8d"
    }
</tags:example>