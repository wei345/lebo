<%@tag pageEncoding="UTF-8"%>
<%@ attribute name="name" type="java.lang.String" required="true"%>
<%@ attribute name="value" type="java.lang.String" required="false"%>
<%@ attribute name="type" type="java.lang.String" required="false"%>
<%@ attribute name="optional" type="java.lang.Boolean" required="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% if(type == null) type = "text"; %>
<% if(value == null) value = ""; %>
<% if(optional == null) optional = false; %>
<div class="control-group" ${optional ? "title='这个参数是可选的'" : ""}>
    <label class="control-label" for="<%=name%>">
        <c:if test="${optional}">
            <input type="checkbox" onclick="if(this.checked){this.form.${name}.removeAttribute('disabled');}else{this.form.${name}.setAttribute('disabled','disabled')}" />
        </c:if>
        <%=name%>
    </label>
    <div class="controls">
        <input type="<%=type%>" id="<%=name%>" name="<%=name%>" placeholder="<%=name%>" value="<%=value%>" <%=optional ? "disabled='disabled'" : ""%>>
    </div>
</div>