<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="搜索Posts" method="GET" action="${ctx}/api/1/statuses/search.json">
    搜索视频描述，可以为Hashtag、@某人、一个词
    <tags:field name="q" value="#标签#" optional="true"/>
    每页大小5-200
    <tags:field name="size" value="5" optional="true"/>
    第几页，从0开始，0返回第1页数据
    <tags:field name="page" value="2" optional="true"/>
    按什么字段排序：id,<%=Post.FAVOURITES_COUNT_KEY%>,<%=Post.VIEWS_COUNT_KEY%>，缺省id
    <tags:field name="orderBy" value="<%=Post.FAVOURITES_COUNT_KEY%>" optional="true"/>
    顺序：desc或asc，缺省desc
    <tags:field name="order" value="desc" optional="true"/>
</tags:form>


<tags:example method="GET" url="">

</tags:example>