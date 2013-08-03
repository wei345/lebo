<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p>
    获取应用设置，包括频道列表、精华内容URL。
</p>
<tags:form name="选项" method="GET" action="${ctx}/api/1/settings.json">
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings.json">
    {
        channels: [
            {
                name: "运动",
                contentUrl: null,
                image: "51e8af331a8835c0f2160bc5",
                backgroundColor: "#123456",
                enabled: false
            }
        ],
        officialAccountId: "51def1e61a883914869e46f3",
        guanZhu: "/api/1/statuses/homeTimeline.json",
        reMen: "/api/1/statuses/search.json?orderBy=favoritesCount&order=desc&after=Tue+Jul+23+12%3A28%3A35+%2B0800+2013",
        jingHua: "/api/1/statuses/filter?follow=51def1e61a883914869e46f3&after=Tue+Jul+23+12%3A28%3A35+%2B0800+2013",
        fenSiZuiDuo: "/api/1/users/search.json?orderBy=followersCount&order=desc",
        zuiShouXiHuan: "/api/1/users/search.json?orderBy=beFavoritedCount&order=desc",
        piaoFangZuiGao: "/api/1/users/search.json?orderBy=viewCount&order=desc"
    }
</tags:example>