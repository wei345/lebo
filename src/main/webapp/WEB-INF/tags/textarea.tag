<%@tag pageEncoding="UTF-8" %>
<%@ attribute name="name" type="java.lang.String" required="true" %>
<%@ attribute name="value" type="java.lang.String" required="false" %>
<%@ attribute name="optional" type="java.lang.Boolean" required="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% if (optional == null) optional = false; %>
<% if (value == null) value = ""; %>
<div class="control-group" ${optional ? "title='这个参数是可选的'" : ""}>
    <label class="control-label" for="<%=name%>">
        <c:if test="${optional}">
            <input type="checkbox"
                   onclick="if(this.checked){this.form.${name}.removeAttribute('disabled');}else{this.form.${name}.setAttribute('disabled','disabled')}"/>
        </c:if>
        <%=name%>
    </label>

    <div class="controls">
        <textarea type="text" id="<%=name%>" name="<%=name%>" placeholder="Type something…" cols="80"
                  rows="4" <%=optional ? "disabled='disabled'" : ""%>><%=value%>
        </textarea>
    </div>
</div>
