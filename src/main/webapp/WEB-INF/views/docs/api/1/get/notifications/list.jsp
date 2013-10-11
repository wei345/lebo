<%@ page import="com.lebo.entity.Notification" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="通知(消息)" action="${ctx}/api/1/notifications/list.json" method="GET">
    <p>
        返回当前登录用户的通知(消息)列表。
    </p>

    <p>
        unread=true，只返回未读通知。
    </p>
    <tags:field name="unread" value="true" optional="true"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>
</tags:form>

<table class="table table-hover">
    <tr>
        <th class="input-large">字段</th>
        <th class="input-small">类型</th>
        <th>说明</th>
    </tr>
    <tr>
        <td>
            id
        </td>
        <td>
            string
        </td>
        <td></td>
    </tr>
    <tr>
        <td>
            sender
        </td>
        <td>
            object
        </td>
        <td>
            触发通知的用户
        </td>
    </tr>
    <tr>
        <td>
            activityType
        </td>
        <td>
            string
        </td>
        <td>
            通知类型
        </td>
    </tr>
    <tr>
        <td>
            relatedStatus
        </td>
        <td>
            object
        </td>
        <td>
            相关的视频
        </td>
    </tr>
    <tr>
        <td>
            relatedComment
        </td>
        <td>
            object
        </td>
        <td>
            相关的评论
        </td>
    </tr>
    <tr>
        <td>
            createdAt
        </td>
        <td>
            string
        </td>
        <td>
            该通知创建日期
        </td>
    </tr>
    <tr>
        <td>
            unread
        </td>
        <td>
            boolean
        </td>
        <td>
            true: 未读，false: 已读
        </td>
    </tr>
</table>

<strong>类型</strong>
<table class="table table-hover">
    <tr>
        <th>类型</th>
        <th>activityType</th>
        <th>sender</th>
        <th>relatedStatus</th>
        <th>relatedComment</th>
    </tr>
    <tr>
        <td>
            用户被关注
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_FOLLOW%>
        </td>
        <td>谁关注了该用户</td>
        <td>无</td>
        <td>无</td>
    </tr>
    <tr>
        <td>
            用户的视频被转播
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_REPOST%>
        </td>
        <td>谁转播了该用户的视频</td>
        <td>被转播的视频(原贴)</td>
        <td>无</td>
    </tr>
    <tr>
        <td>
            用户的视频被回复
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_REPLY_POST%>
        </td>
        <td>谁回复了该用户的视频</td>
        <td>被回复的视频</td>
        <td>回复视频的评论</td>
    </tr>
    <tr>
        <td>
            用户的评论被回复
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_REPLY_COMMENT%>
        </td>
        <td>谁回复了该用户的评论</td>
        <td>评论所属视频</td>
        <td>回复评论的评论</td>
    </tr>
    <tr>
        <td>
            用户的视频被喜欢
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_FAVORITE%>
        </td>
        <td>谁喜欢了该用户的视频</td>
        <td>被喜欢的视频</td>
        <td>无</td>
    </tr>
    <tr>
        <td>
            用户被别人的视频描述<code>@</code>
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_POST_AT%>
        </td>
        <td>谁在视频描述里<code>@</code>了该用户</td>
        <td><code>@</code>了该用户的视频</td>
        <td>无</td>
    </tr>
    <tr>
        <td>
            用户被别人的评论<code>@</code>
        </td>
        <td>
            <%=Notification.ACTIVITY_TYPE_COMMENT_AT%>
        </td>
        <td>谁在评论里<code>@</code>了该用户</td>
        <td>评论所属视频</td>
        <td><code>@</code>了该用户的评论</td>
    </tr>
</table>

<strong>返回结果中的relatedStatus</strong>

<p>
    当activityType值为<%=Notification.ACTIVITY_TYPE_REPOST%>、 <%=Notification.ACTIVITY_TYPE_REPLY_POST%>、
    <%=Notification.ACTIVITY_TYPE_REPLY_COMMENT%>、<%=Notification.ACTIVITY_TYPE_FAVORITE%>、
    <%=Notification.ACTIVITY_TYPE_POST_AT%>、<%=Notification.ACTIVITY_TYPE_COMMENT_AT%>时，
    如果返回结果中没有relatedStatus字段或其值为null，则表示该视频已被删除。
</p>

<strong>返回结果中的relatedComment</strong>

<p>
    当activityType值为<%=Notification.ACTIVITY_TYPE_REPLY_POST%>、
    <%=Notification.ACTIVITY_TYPE_REPLY_COMMENT%>、<%=Notification.ACTIVITY_TYPE_COMMENT_AT%>时，
    如果返回结果中没有relatedStatus字段或其值为null，则表示该评论已被删除。
</p>

<tags:example method="GET" url="http://localhost:8080/api/1/notifications/list.json">
    [
        {
            sender: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
                beFavoritedCount: 2,
                viewCount: 1
            },
            activityType: "comment_at",
            relatedStatus: {
                id: "51ff1af01a8856d1a7eeec9b",
                createdAt: "Mon Aug 05 11:24:30 +0800 2013",
                files: [ ],
                favoritesCount: 0,
                viewCount: 0,
                originStatus: {
                    id: "51f8db8a1a88178f820c1422",
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
                    favoritesCount: 0,
                    viewCount: 1,
                    userMentions: [ ],
                    digest: false
                },
                userMentions: [ ],
                digest: true
            },
            relatedComment: {
                id: "52049f851a887068197ceaae",
                postId: "51ff1af01a8856d1a7eeec9b",
                createdAt: "Fri Aug 09 15:51:33 +0800 2013",
                text: "@法图_麦 good",
                files: [ ],
                hasVideo: false
            },
            createdAt: "Fri Aug 09 15:51:33 +0800 2013",
            unread: false
        },
        {
            sender: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
                beFavoritedCount: 2,
                viewCount: 1
            },
            activityType: "reply_post",
            relatedStatus: {
                id: "51ff1af01a8856d1a7eeec9b",
                createdAt: "Mon Aug 05 11:24:30 +0800 2013",
                files: [ ],
                favoritesCount: 0,
                viewCount: 0,
                originStatus: {
                    id: "51f8db8a1a88178f820c1422",
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
                    favoritesCount: 0,
                    viewCount: 1,
                    userMentions: [ ],
                    digest: false
                },
                userMentions: [ ],
                digest: true
            },
            relatedComment: {
                id: "52049f851a887068197ceaae",
                postId: "51ff1af01a8856d1a7eeec9b",
                createdAt: "Fri Aug 09 15:51:33 +0800 2013",
                text: "@法图_麦 good",
                files: [ ],
                hasVideo: false
            },
            createdAt: "Fri Aug 09 15:51:33 +0800 2013",
            unread: false
        },
        {
            sender: {
                id: "51dfd3d21a8855744379891f",
                screenName: "xueeR_Z",
                profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
                createdAt: "Fri Jul 12 18:00:50 +0800 2013",
                beFavoritedCount: 2,
                viewCount: 1
            },
            activityType: "follow",
            createdAt: "Fri Aug 09 14:15:15 +0800 2013",
            unread: false
        },
        {
            sender: {
                id: "51e550051a88b4eb7fd6494c",
                screenName: "Amy滴柔情老妈",
                profileImageUrl: "http://tp4.sinaimg.cn/1928040351/50/40022538280/0",
                createdAt: "Tue Jul 16 21:52:05 +0800 2013",
                followersCount: 1
            },
            activityType: "follow",
            createdAt: "Fri Aug 09 14:09:16 +0800 2013",
            unread: false
        }
    ]
</tags:example>