<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
    .td1 {
        width: 25em;
    }
</style>

<strong>登录</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/oauthLogin" text="POST oauthLogin"/>
        </td>
        <td>
            OAuth登录
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/login" text="POST login"/>
        </td>
        <td>
            本地登录
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/logout" text="POST logout"/>
        </td>
        <td>
            登出
        </td>
    </tr>
</table>

<strong>时间线</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/homeTimeline" text="GET statuses/homeTimeline"/>
        </td>
        <td>
            主页时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/statuses/userTimeline" text="GET statuses/userTimeline"/>
        </td>
        <td>
            用户时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/statuses/mentionsTimeline" text="GET statuses/mentionsTimeline"/>
        </td>
        <td>
            提到我时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/statuses/update" text="POST statuses/update"/>
        </td>
        <td>
            发布视频
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/statuses/repost" text="POST statuses/repost"/>
        </td>
        <td>
            转发视频
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/filter" text="GET statuses/filter"/>
        </td>
        <td>
            过滤 - 可以指定一个或多个用户、指定hashtag、指定@某人、指定关键词
        </td>
    </tr>
</table>

<strong>评论</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/comments/create" text="POST comments/create"/>
        </td>
        <td>
            发布评论
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/comments/show" text="GET comments/show"/>
        </td>
        <td>
            查看评论
        </td>
    </tr>
</table>

<strong>用户</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/search" text="GET users/search"/>
        </td>
        <td>
            搜索用户
        </td>
    </tr>
</table>

<strong>关系</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/friendships/create" text="POST friendships/create"/>
        </td>
        <td>
            关注
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/friendships/destroy" text="POST friendships/destory"/>
        </td>
        <td>
            取消关注
        </td>
    </tr>
</table>

<strong>收藏</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/create" text="POST favorites/create"/>
        </td>
        <td>
            收藏
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/show" text="GET favorites/show"/>
        </td>
        <td>
            查看收藏
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/toggle" text="POST favorites/toggle"/>
        </td>
        <td>
            收藏切换
        </td>
    </tr>
</table>

<strong>标签</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/hashtags/search" text="GET hashtags/search"/>
        </td>
        <td>
            搜索Hashtags
        </td>
    </tr>
</table>

<strong>私信</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/directMessages/new" text="POST directMessages/new"/>
        </td>
        <td>
            发送私信
        </td>
    </tr>
</table>

<hr/>

<p>
    使用<a href="http://www.google.com/intl/zh-CN/chrome/browser/">谷歌浏览器</a>+<a
        href="https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc">JSONView扩展</a>看JSON很棒。
</p>
