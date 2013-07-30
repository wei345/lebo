<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="Followers" action="${ctx}/api/1/followers/list.json" method="GET">
    <p>
        返回由userId或screenName指定的用户的粉丝，如果userId和screenName都未指定，则返回当前登录用户的粉丝。
    </p>
    <tags:field name="userId" value="51def53f1a883914869e46f5" optional="true"/>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="page" value="" optional="true"/>
    <tags:field name="size" value="" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/followers/list.json?userId=51def53f1a883914869e46f5">
    [
        {
            id: "51def1e61a883914869e46f3",
            screenName: "法图_麦",
            name: "法图_麦",
            profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
            createdAt: "Fri Jul 12 01:56:54 +0800 2013",
            following: false,
            followersCount: null,
            friendsCount: 1,
            statusesCount: 5,
            verified: null,
            location: null,
            timeZone: null
        }
    ]
</tags:example>