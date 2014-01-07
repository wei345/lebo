<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="page" type="org.springframework.data.domain.Page" required="true" %>
<%@ attribute name="spentSeconds" type="java.lang.Float" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="pageinfo">第 ${page.size * page.number + 1} - ${page.size * page.number + page.numberOfElements}
    条，共 ${page.totalElements} 条<c:if test="${spentSeconds != null}">（用时 ${spentSeconds} 秒）</c:if>
</div>
