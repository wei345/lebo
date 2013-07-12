<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="收藏" method="POST" action="${ctx}/api/v1/favorites/create.json">
    <tags:field name="id" value="51df969c1a88cb49eec1f5f7"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/v1/favorites/create.json?id=51df969c1a88cb49eec1f5f7">
    {
    id: "51df969c1a88cb49eec1f5f7",
    user: {
    id: "51def53f1a883914869e46f5",
    screenName: "家有笨猫咪",
    name: "家有笨猫咪",
    description: null,
    profileImageUrl: "http://tp3.sinaimg.cn/3472643302/50/5663730129/0",
    createdAt: "2013-07-12 02:11:11",
    verified: null,
    location: null,
    timeZone: null,
    oAuthIds: [
    "weibo/3472643302"
    ],
    gender: -1,
    email: null,
    lastSignInAt: 1373566271844
    },
    createdAt: 1373607580249,
    text: "视频1",
    truncated: false,
    files: [
    "51df969c1a88cb49eec1f5f2",
    "51df969c1a88cb49eec1f5f5"
    ],
    source: null,
    geoLocation: null,
    favorited: true,
    favouritesCount: null,
    repostsCount: null,
    reposted: null,
    originPostId: null
    }
</tags:example>