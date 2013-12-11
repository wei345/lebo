<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="用户的虚拟物品" method="GET" action="${ctx}/api/1.1/vg/userVg.json">
    <p>
        需要<code>userId</code>或<code>screenName</code>
    </p>
    <tags:field name="userId" optional="true"/>
    <tags:field name="screenName" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/vg/userVg.json?screenName=%E6%98%8E%E4%B8%AB%E4%B8%AB%E6%98%AF%E4%B8%AA%E7%88%B7%E4%BB%AC">
    {
        userId: "525124e81a88ac9dfcbd9ce0",
        screenName: "明丫丫是个爷们",
        profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
        profileImageBiggerUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce3",
        profileImageOriginalUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce5",
        gold: 0,
        goods: [ ],
        goodsTotalPrice: 0
    }
</tags:example>