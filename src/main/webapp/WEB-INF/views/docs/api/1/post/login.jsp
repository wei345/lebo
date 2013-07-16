<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="本地登录" method="POST" action="${ctx}/api/1/login.json">
    <tags:field name="username" value="test@lebooo.com"/>
    <tags:field name="password" value="user"/>
</tags:form>

<tags:example method="POST" url="/api/1/login.json?username=test@lebooo.com&password=user">
    {
        screenName: "法图_麦",
        name: "法图_麦",
        profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
        provider: "local",
        uid: "51def1e61a883914869e46f3",
        token: null
    }
</tags:example>