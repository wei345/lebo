<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="Friends" action="${ctx}/api/1/friends/list.json" method="GET">
    <p>
        返回由userId或screenName指定的用户的粉丝，如果userId和screenName都未指定，则返回当前登录用户的粉丝。
    </p>
    <tags:field name="userId" value="51def1e61a883914869e46f3" optional="true"/>
    <tags:field name="screenName" value="法图_麦" optional="true"/>
    <tags:field name="page" value="" optional="true"/>
    <tags:field name="size" value="" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/friends/list.json?userId=51def1e61a883914869e46f3">
    [
        {
            id: "51def53f1a883914869e46f5",
            screenName: "家有笨猫咪",
            name: "家有笨猫咪",
            profileImageUrl: "http://tp3.sinaimg.cn/3472643302/50/5663730129/0",
            createdAt: "Fri Jul 12 02:11:11 +0800 2013",
            following: false,
            followersCount: 10,
            friendsCount: 0,
            statusesCount: 1,
            verified: null,
            location: null,
            timeZone: null
        }
    ]
</tags:example>