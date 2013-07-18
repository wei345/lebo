<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" %>
<%@ attribute name="paginationSize" type="java.lang.Integer" required="true" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%
    int n = page.getContent().size();
    if(n == page.getSize()){
       request.setAttribute("last", n - 1);
    }
%>

<div class="pagination">
    <ul>
        <li><a href="?count=${page.size}" title="首页">&lt;&lt;</a></li>
        <li><a href="?count=${page.size}&maxId=${page.content[last].id}" title="下一页">&gt;</a></li>
    </ul>
</div>

