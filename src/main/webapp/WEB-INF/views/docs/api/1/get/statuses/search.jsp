<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="FAVOURITES_COUNT_KEY" value="<%=Post.FAVOURITES_COUNT_KEY%>"/>
<c:set var="VIEW_COUNT_KEY" value="<%=Post.VIEW_COUNT_KEY%>"/>

<tags:form name="搜索视频" method="GET" action="${ctx}/api/1/statuses/search.json">
    搜索视频描述，可以为Hashtag、@某人、一个词
    <tags:field name="q" value="#标签#" optional="true"/>
    每页大小5-200
    <tags:field name="size" value="5" optional="true"/>
    第几页，从0开始，0返回第1页数据
    <tags:field name="page" value="2" optional="true"/>
    按什么字段排序：id,${FAVOURITES_COUNT_KEY},${VIEW_COUNT_KEY}，缺省id
    <tags:field name="orderBy" value="<%=Post.FAVOURITES_COUNT_KEY%>" optional="true"/>
    顺序：desc或asc，缺省desc
    <tags:field name="order" value="desc" optional="true"/>
</tags:form>


<tags:example method="GET"
              url="http://localhost:8080/api/1/statuses/search.json?q=%23%E7%95%AA%E7%9F%B3%E6%A6%B4%23&size=5">
    [
        {
            id: "51f8db8a1a88178f820c1422",
            user: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
                followersCount: 1,
                friendsCount: 2,
                statusesCount: 3,
                favoritesCount: 2,
                beFavoritedCount: 2,
                blocking: false
            },
            createdAt: "Wed Jul 31 17:40:26 +0800 2013",
            text: "视频#番石榴##獴狐猴#",
            files: [
                {
                    filename: "2013-07-10_12-11-16.mp4",
                    length: 1043721,
                    contentType: "video/mp4",
                    contentUrl: "/files/51e29dfd1a8881a5c61992f7?postId=51f8db8a1a88178f820c1422"
                },
                {
                    filename: "2013-07-10_12-11-16.jpg",
                    length: 34869,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51e29dfd1a8881a5c61992fc?postId=51f8db8a1a88178f820c1422"
                }
            ],
            source: "网页版",
            favorited: false,
            favoritesCount: 0,
            repostsCount: 0,
            reposted: false,
            commentsCount: 0,
            viewCount: 0,
            comments: [ ],
            userMentions: [ ],
            digest: false
        },
        {
            id: "51f8d50e1a88e8e0719a2db5",
            user: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
                followersCount: 1,
                friendsCount: 2,
                statusesCount: 3,
                favoritesCount: 2,
                beFavoritedCount: 2,
                blocking: false
            },
            createdAt: "Wed Jul 31 17:12:45 +0800 2013",
            text: "视频#番石榴#",
            files: [
                {
                    filename: "2013-07-04_12-17-56.mp4",
                    length: 893731,
                    contentType: "video/mp4",
                    contentUrl: "/files/51f8d50d1a88e8e0719a2dae?postId=51f8d50e1a88e8e0719a2db5"
                },
                {
                    filename: "2013-07-04_12-22-55.jpg",
                    length: 32408,
                    contentType: "image/jpeg",
                    contentUrl: "/files/51f8d50d1a88e8e0719a2db3?postId=51f8d50e1a88e8e0719a2db5"
                }
            ],
            source: "网页版",
            favorited: false,
            favoritesCount: 0,
            repostsCount: 0,
            reposted: false,
            commentsCount: 0,
            viewCount: 0,
            comments: [ ],
            userMentions: [ ],
            digest: false
        }
    ]
</tags:example>