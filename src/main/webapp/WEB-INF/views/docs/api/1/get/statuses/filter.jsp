<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="过滤" method="GET" action="${ctx}/api/1/statuses/filter.json">
    <ul>
        <li>follow - 逗号分隔的用户ID，表示返回这些用户的status</li>
        <li>track
            <ul>
                <li>由一个或多个短语组成，短语间以逗号分隔，短语间逻辑运算是OR</li>
                <li>一个短语由一个或多个关键词组成，关键词间以空格分隔，关键词间逻辑运算是AND</li>
                <li>查Hashtag：<code>#xx#</code></li>
                <li>查@某人：<code>@xx</code></li>
                <li>Hashtag和@区分大小写</li>
                <li>其他关键词不区分大小写</li>
                <li>查找范围包括：text、expandedUrl、displayUrl</li>
                <li>每个短语1-60字</li>
            </ul>
        </li>
        <li>分页参数：maxId、sinceId、count</li>
    </ul>
    <tags:field name="follow" value="" optional="true"/>
    <tags:field name="track" value="杨过 华山论剑,#紫禁之巅# @阿飞" optional="true"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1/statuses/filter.json?track=%E6%9D%A8%E8%BF%87+%E5%8D%8E%E5%B1%B1%E8%AE%BA%E5%89%91%2C%23%E7%B4%AB%E7%A6%81%E4%B9%8B%E5%B7%85%23+%40%E9%98%BF%E9%A3%9E">
    [
        {
            id: "51e437711a88da1af1c21c72",
            user: {
                id: "51def52f1a883914869e46f4",
                screenName: "小萌君sang",
                name: "小萌君sang",
                profileImageUrl: "http://tp1.sinaimg.cn/3473952784/50/5663727050/0",
                createdAt: 1373566255017,
                following: null,
                followersCount: 0,
                friendsCount: 0,
                statusesCount: 1,
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373910896693,
            text: "#紫禁之巅#视频@阿飞",
            truncated: false,
            files: [
                "51e437701a88da1af1c21c6b",
                "51e437701a88da1af1c21c70"
            ],
            source: "网页版",
            geoLocation: null,
            favorited: false,
            favouritesCount: 0,
            repostsCount: null,
            reposted: null,
            commentsCount: null,
            originStatus: null
        },
        {
            id: "51e3a0ca1a8890916e962c94",
            user: {
                id: "51def1e61a883914869e46f3",
                screenName: "法图_麦",
                name: "法图_麦",
                profileImageUrl: "http://tp4.sinaimg.cn/1774156407/50/5657962784/0",
                createdAt: 1373565414511,
                following: false,
                followersCount: 0,
                friendsCount: 1,
                statusesCount: 5,
                verified: null,
                location: null,
                timeZone: null
            },
            createdAt: 1373872329824,
            text: "#searchTerms#@显示名 今天下雨了，@虚竹@无名#华山论剑#@杨过",
            truncated: false,
            files: [
                "51e3a0c91a8890916e962c8d",
                "51e3a0c91a8890916e962c92"
            ],
            source: "乐播网页版",
            geoLocation: null,
            favorited: false,
            favouritesCount: 0,
            repostsCount: null,
            reposted: null,
            commentsCount: null,
            originStatus: null
        }
    ]
</tags:example>