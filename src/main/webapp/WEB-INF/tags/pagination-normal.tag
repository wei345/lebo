<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true"%>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    int current =  page.getNumber();
    int begin = Math.max(1, current - paginationSize/2);
    int end = Math.min(begin + (paginationSize - 1), page.getTotalPages() - 1);
    String queryString = request.getQueryString().replaceAll("(^|&)page=" + current, "");

    request.setAttribute("current", current);
    request.setAttribute("begin", begin);
    request.setAttribute("end", end);
    request.setAttribute("queryString", queryString);

%>

<div class="pagination">
    <ul>
        <% if (page.hasPreviousPage()){%>
        <li><a href="?page=0&${queryString}">&lt;&lt;</a></li>
        <li><a href="?page=${current-1}&${queryString}">&lt;</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">&lt;&lt;</a></li>
        <li class="disabled"><a href="#">&lt;</a></li>
        <%} %>

        <c:forEach var="i" begin="${begin}" end="${end}">
            <c:choose>
                <c:when test="${i == current}">
                    <li class="active"><a href="?page=${i}&${queryString}">${i + 1}</a></li>
                </c:when>
                <c:otherwise>
                    <li><a href="?page=${i}&${queryString}">${i + 1}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <% if (page.hasNextPage()){%>
        <li><a href="?page=${current+1}&${queryString}">&gt;</a></li>
        <li><a href="?page=${page.totalPages-1}&${queryString}">&gt;&gt;</a></li>
        <%}else{%>
        <li class="disabled"><a href="#">&gt;</a></li>
        <li class="disabled"><a href="#">&gt;&gt;</a></li>
        <%} %>

    </ul>
</div>

