<%@ page import="com.lebo.entity.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="FOLLOWERS_COUNT_KEY" value="<%=User.FOLLOWERS_COUNT_KEY%>"/>
<c:set var="BE_FAVORITED_COUNT_KEY" value="<%=User.BE_FAVORITED_COUNT_KEY%>"/>
<c:set var="VIEWS_COUNT_KEY" value="<%=User.VIEWS_COUNT_KEY%>"/>

<tags:form name="搜索用户" method="GET" action="${ctx}/api/1/users/search.json">
    正则表达式查找screenName
    <tags:field name="q" value="麦" optional="true"/>
    每页大小5-200
    <tags:field name="size" value="5" optional="true"/>
    第几页，从0开始，0返回第1页数据
    <tags:field name="page" value="2" optional="true"/>
    按什么字段排序：id,${FOLLOWERS_COUNT_KEY},${BE_FAVORITED_COUNT_KEY},${VIEWS_COUNT_KEY}，缺省id
    <tags:field name="orderBy" value="<%=User.FOLLOWERS_COUNT_KEY%>" optional="true"/>
    顺序：desc或asc，缺省desc
    <tags:field name="order" value="desc" optional="true"/>
</tags:form>


<tags:example method="GET" url="http://localhost:8080/api/1/users/search.json?size=5&orderBy=followersCount">
    [
        {
            id: "51def53f1a883914869e46f5",
            screenName: "家有笨猫咪",
            name: "家有笨猫咪",
            profileImageUrl: "http://tp3.sinaimg.cn/3472643302/50/5663730129/0",
            createdAt: 1373566271844,
            following: false,
            followersCount: 10,
            friendsCount: 0,
            statusesCount: 1,
            verified: null,
            location: null,
            timeZone: null
        },
        {
            id: "51def52f1a883914869e46f4",
            screenName: "小萌君sang",
            name: "小萌君sang",
            profileImageUrl: "http://tp1.sinaimg.cn/3473952784/50/5663727050/0",
            createdAt: 1373566255017,
            following: false,
            followersCount: 9,
            friendsCount: 0,
            statusesCount: 1,
            verified: null,
            location: null,
            timeZone: null
        },
        {
            id: "51def1ce1a883914869e46f2",
            screenName: "Desi_漓沫沫",
            name: "Desi_漓沫沫",
            profileImageUrl: "http://tp2.sinaimg.cn/3287717033/50/5666090989/0",
            createdAt: 1373565390940,
            following: false,
            followersCount: 1,
            friendsCount: 0,
            statusesCount: 0,
            verified: null,
            location: null,
            timeZone: null
        },
        {
            id: "51dfd3d21a8855744379891f",
            screenName: "xueeR_Z",
            name: "xueeR_Z",
            profileImageUrl: "http://tp4.sinaimg.cn/2484091107/50/5668404603/0",
            createdAt: 1373623250172,
            following: false,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 2,
            verified: null,
            location: null,
            timeZone: null
        },
        {
            id: "51e3641f1a8814f22afbd623",
            screenName: "手机用户2916958681",
            name: "手机用户2916958681",
            profileImageUrl: "http://tp2.sinaimg.cn/2916958681/50/40023159574/1",
            createdAt: 1373856799606,
            following: false,
            followersCount: null,
            friendsCount: 0,
            statusesCount: 0,
            verified: null,
            location: null,
            timeZone: null
        }
    ]
</tags:example>