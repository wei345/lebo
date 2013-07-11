<%@tag pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="method" type="java.lang.String" required="true" %>
<%@ attribute name="action" type="java.lang.String" required="true" %>
<%@ attribute name="enctype" type="java.lang.String" required="false" %>

<form method="<%=method%>" action="<%=action%>" <% if (enctype != null) { %> enctype="<%=enctype%>" <% } %>>
    <fieldset>
        <p>
            <%=method%> <%=action%>
        </p>
        <b>参数：</b>
        <legend><%=name%>
        </legend>
        <jsp:doBody/>
        <button type="submit" class="btn">Submit</button>
    </fieldset>
</form>
