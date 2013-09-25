<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="每日十个" action="${ctx}/api/1/everyday10/list.json" method="GET">
    <%--接口也支持一下参数，但客户端为用到，在文档中不显示
    <tags:field name="userId" optional="true"/>
    <tags:field name="screenName" optional="true"/>
    <tags:field name="count" optional="true"/>
    <tags:field name="maxId" optional="true"/>
    <tags:field name="sinceId" optional="true"/>--%>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/everyday10/list.json">
    [
        {
            id: "522061c71a8824abcaaddddb",
            user: {
                id: "521c1c161a88bff4d06f1988",
                screenName: "半妖的秋天",
                description: "一直以为自己会永远是那个在校园里无论男女都勾肩搭背，横冲直撞，笑声响彻整个校园的女子。。。。。。",
                profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                createdAt: "Tue May 28 11:53:03 +0800 2013",
                followersCount: 0,
                friendsCount: 0,
                statusesCount: 6,
                beFavoritedCount: 6,
                viewCount: 28614,
                digestCount: 0,
                level: 0
            },
            createdAt: "Fri Aug 23 13:46:33 +0800 2013",
            text: "好凄惨的叫声",
            files: [
                {
                    length: 568995,
                    contentType: "video/mp4",
                    contentUrl: "http://file.dev.lebooo.com/522061b91a8824abcaadddd9.mp4"
                },
                {
                    length: 97383,
                    contentType: "image/jpeg",
                    contentUrl: "http://file.dev.lebooo.com/522061c51a8824abcaadddda.jpg"
                }
            ],
            video: {
                length: 568995,
                contentType: "video/mp4",
                contentUrl: "http://file.dev.lebooo.com/522061b91a8824abcaadddd9.mp4"
            },
            videoFirstFrameUrl: "http://file.dev.lebooo.com/522061c51a8824abcaadddda.jpg",
            source: "老服务器",
            favoritesCount: 4,
            viewCount: 76,
            userMentions: [ ],
            digest: false
        },
        {
            id: "52205b4d1a882a1c71a338e2",
            user: {
                id: "521c1c161a88bff4d06f1988",
                screenName: "半妖的秋天",
                description: "一直以为自己会永远是那个在校园里无论男女都勾肩搭背，横冲直撞，笑声响彻整个校园的女子。。。。。。",
                profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-ms1lnn-1.jpg",
                createdAt: "Tue May 28 11:53:03 +0800 2013",
                followersCount: 0,
                friendsCount: 0,
                statusesCount: 6,
                beFavoritedCount: 6,
                viewCount: 28614,
                digestCount: 0,
                level: 0
            },
            createdAt: "Fri Aug 23 13:55:03 +0800 2013",
            text: "这时候我们已经脱离景区，慢慢走向了沙漠的深处，天黑以后各种惊险刺激，只可惜手机没电了就没拍上",
            files: [
                {
                    length: 654623,
                    contentType: "video/mp4",
                    contentUrl: "http://file.dev.lebooo.com/52205b3d1a882a1c71a338e0.mp4"
                },
                {
                    length: 51832,
                    contentType: "image/jpeg",
                    contentUrl: "http://file.dev.lebooo.com/52205b4c1a882a1c71a338e1.jpg"
                }
            ],
            video: {
                length: 654623,
                contentType: "video/mp4",
                contentUrl: "http://file.dev.lebooo.com/52205b3d1a882a1c71a338e0.mp4"
            },
            videoFirstFrameUrl: "http://file.dev.lebooo.com/52205b4c1a882a1c71a338e1.jpg",
            source: "老服务器",
            favoritesCount: 5,
            viewCount: 190,
            userMentions: [ ],
            digest: false
        }
    ]
</tags:example>
