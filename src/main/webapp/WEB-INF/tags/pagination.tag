<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="page" type="java.lang.Integer" required="true" %>
<%@ attribute name="size" type="java.lang.Integer" required="true" %>
<%@ attribute name="currentSize" type="java.lang.Integer" required="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pagination">
    <ul>
        <c:if test="${page == 0}">
            <li class="disabled"><a href="#">&lt;&lt;</a></li>
            <li class="disabled"><a href="#">&lt;</a></li>
        </c:if>
        <c:if test="${page > 0}">
            <li><a href="?size=${size}" title="首页">&lt;&lt;</a></li>
            <li><a href="?size=${size}&page=${page - 1}" title="上一页">&lt;</a></li>
        </c:if>
        <c:if test="${currentSize == size}">
            <li><a href="?size=${size}&page=${page + 1}" title="下一页">&gt;</a></li>
        </c:if>
    </ul>
</div>

