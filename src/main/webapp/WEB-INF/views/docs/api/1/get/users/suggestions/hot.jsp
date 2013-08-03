<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="" method="GET" action="${ctx}/api/1/users/suggestions/hot.json">
    每页大小5-200
    <tags:field name="size" value="5" optional="true"/>
    第几页，从0开始，0返回第1页数据
    <tags:field name="page" value="2" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://121.199.48.160:8080/api/1/users/suggestions/hot.json">
    [
        {
            id: "51def1e61a883914869e46f3",
            screenName: "法图_麦",
            description: "介绍",
            profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
            createdAt: "Fri Jul 12 01:56:54 +0800 2013",
            following: true,
            followersCount: 1,
            friendsCount: 1,
            statusesCount: 19,
            favoritesCount: 5,
            beFavoritedCount: 3,
            viewCount: 16,
            blocking: false
        }
    ]
</tags:example>