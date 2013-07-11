<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="method" type="java.lang.String" required="true"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>例子</h2>
<p><%=method%> <%=url%></p>
响应:
<pre><jsp:doBody/></pre>