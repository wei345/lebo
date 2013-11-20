<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="通知分组" method="GET" action="${ctx}/api/1.1/notifications/groups.json">
    <p>返回通知分组、未读数、最新通知。</p>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1.1/notifications/groups.json">
    [
        {
            groupName: "通知",
            activityTypes: [
                "lebo_team"
            ],
            unreadCount: 0,
            recentNotifications: [
                {
                    id: "526a6aaa1a8805aa4e5f361f",
                    sender: {
                        id: "525124e81a88ac9dfcbd9ce0",
                        screenName: "明丫丫是个爷们",
                        profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
                        profileImageBiggerUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce3",
                        profileImageOriginalUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce5",
                        createdAt: "Sun Oct 06 16:52:56 +0800 2013",
                        followersCount: 0,
                        friendsCount: 0,
                        statusesCount: 10,
                        beFavoritedCount: 1,
                        viewCount: 0,
                        digestCount: 0,
                        weiboVerified: false,
                        level: 0
                    },
                    activityType: "lebo_team",
                    createdAt: "Fri Oct 25 20:57:14 +0800 2013",
                    unread: false,
                    text: "test4",
                    senderName: "乐播团队",
                    senderImageUrl: "http://file.dev.lebooo.com/images/logo.png"
                }
            ]
        },
        {
            groupName: "互动",
            activityTypes: [
                "repost",
                "reply_post",
                "post_at",
                "reply_comment",
                "comment_at",
                "favorite",
                "follow"
            ],
            unreadCount: 0,
            recentNotifications: [ ]
        }
    ]
</tags:example>