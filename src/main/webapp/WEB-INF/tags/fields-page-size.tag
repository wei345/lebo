<%@ tag import="com.lebo.service.param.PageRequest" %>
<%@tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
每页大小<c:out value="<%=PageRequest.PAGE_SIZE_MIN%>"/>-<c:out value="<%=PageRequest.PAGE_SIZE_MAX%>"/>
<tags:field name="size" value="5" optional="true"/>
页码，从<c:out value="<%=PageRequest.PAGE_NUBMER_START%>"/>(第1页)开始，最大<c:out value="<%=PageRequest.PAGE_NUBMER_MAX%>"/>
<tags:field name="page" value="2" optional="true"/>