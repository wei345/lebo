<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="取消转发视频" method="POST" action="${ctx}/api/1/statuses/unrepost.json">
    <p>
        原始贴ID
    </p>
    <tags:field name="id" value=""/>
</tags:form>

<tags:example method="POST" url="http://121.199.48.160:8080/api/1/statuses/unrepost.json?id=520a0ecd0cf23db19441ac74">
    {
        id: "520e0f5f0cf26b6c369d2cbe",
        createdAt: "Fri Aug 16 19:39:11 +0800 2013",
        files: [ ],
        favoritesCount: 0,
        viewCount: 0,
        originStatus: {
            id: "520a0ecd0cf23db19441ac74",
            createdAt: "Tue Aug 13 18:47:41 +0800 2013",
            text: "#新人报到#",
            files: [
                {
                    filename: "movie.mp4",
                    length: 169286,
                    contentType: "video/mp4",
                    contentUrl: "/files/520a0ecd0cf23db19441ac70?postId=520a0ecd0cf23db19441ac74"
                },
                {
                    filename: "photo.jpg",
                    length: 45426,
                    contentType: "image/jpeg",
                    contentUrl: "/files/520a0ecd0cf23db19441ac72?postId=520a0ecd0cf23db19441ac74"
                }
            ],
            source: "iOS",
            favoritesCount: 0,
            viewCount: 7,
            userMentions: [ ],
            digest: false
        },
        userMentions: [ ],
        digest: true
    }
</tags:example>
