<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="发布评论" method="POST" action="${ctx}/api/1/comments/create.json" enctype="multipart/form-data">
    <p>
        需要postId或replyCommentId，二者不能都为空。
    </p>
    <tags:field name="postId" value="51de1c3c1a88b82f949585b4" optional="true"/>
    <p>
        被回复的评论id
    </p>
    <tags:field name="replyCommentId" value="" optional="true"/>
    <tags:textarea name="text" value="中文abc" optional="true"/>
    <tags:field name="video" type="file" optional="true"/>
    <tags:field name="image" type="file" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/comments/create.json">
    {
        id: "51fa4d841a88e74e79eaf4ef",
        postId: "51e3a0ca1a8890916e962c94",
        createdAt: "Thu Aug 01 19:59:00 +0800 2013",
        text: "回复评论",
        files: [ ],
        hasVideo: false,
        user: {
            id: "51e778ea1a8816dc79e40aaf",
            screenName: "liuwei",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd",
            createdAt: "Thu Jul 18 13:11:06 +0800 2013",
            followersCount: 0,
            friendsCount: 2,
            statusesCount: 5,
            favoritesCount: 3,
            beFavoritedCount: 1,
            viewCount: 3,
            blocking: false
        },
        replyCommentId: "51f0c3851a8830dba5744f64",
        replyCommentUser: {
            id: "51def1e61a883914869e46f3",
            screenName: "法图_麦",
            profileImageUrl: "/files/51ed11161a88f15acf2d87fd"
        }
    }
</tags:example>