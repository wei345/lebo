<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="url" type="java.lang.String" required="true"%>
<%@ attribute name="text" type="java.lang.String" required="false"%>
<a href="<%=url%>"><%=(text == null ? url : text)%></a>