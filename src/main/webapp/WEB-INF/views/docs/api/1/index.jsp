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
            OAuth登录，返回当前用户信息。
        </td>
    </tr>
    <tr class="disabled" title="未用到">
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/login" text="POST login"/>
        </td>
        <td>
            本地登录，返回当前用户信息。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/logout" text="POST logout"/>
        </td>
        <td>
            登出，返回登出的用户信息。
        </td>
    </tr>
</table>

<strong>设置</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/settings/settings" text="GET settings"/>
        </td>
        <td>
            返回全部设置。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/settings/channels" text="GET settings/channels"/>
        </td>
        <td>
            返回频道列表。
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
            返回我和我关注的人的视频列表。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/statuses/userTimeline" text="GET statuses/userTimeline"/>
        </td>
        <td>
            返回由userId或screenName指定的用户的视频列表。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/statuses/mentionsTimeline" text="GET statuses/mentionsTimeline"/>
        </td>
        <td>
            返回 @我 的视频列表。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/channelTimeline" text="GET statuses/channelTimeline"/>
        </td>
        <td>
            返回id参数指定的频道的视频列表。
        </td>
    </tr>
    <tr class="disabled">
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/filter" text="GET statuses/filter"/>
        </td>
        <td>
            返回符合条件的视频列表，可以指定一个或多个用户、hashtag、@某人、关键词。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/digest" text="GET statuses/digest"/>
        </td>
        <td>
            返回加精的视频列表。
        </td>
    </tr>
    <%--<tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/userDigest" text="GET statuses/userDigest"/>
        </td>
        <td>
            返回由userId或screenName指定的用户被加精的视频列表。
        </td>
    </tr>--%>
</table>

<strong>视频搜索</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/hot" text="GET statuses/hot"/>
        </td>
        <td>
            返回热门视频列表。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/search" text="GET statuses/search"/>
        </td>
        <td>
            返回符合搜索条件的视频列表，可以指定hashtag、@某人、关键词，可以按喜欢数、浏览数排序。
        </td>
    </tr>
</table>

<strong>视频</strong>
<table class="table table-hover">
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/statuses/update" text="POST statuses/update"/>
        </td>
        <td>
            发布视频，返回发布的视频。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/statuses/repost" text="POST statuses/repost"/>
        </td>
        <td>
            转发视频，返回原始视频。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/statuses/destroy" text="POST statuses/destroy"/>
        </td>
        <td>
            删除由id参数指定的视频，当前登录用户必须是被删除视频的作者，返回被删除的视频。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/show" text="POST statuses/show"/>
        </td>
        <td>
            返回由id参数指定的单个视频。
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
            发布评论，返回发布的评论。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/comments/show" text="GET comments/show"/>
        </td>
        <td>
            返回由id指定的视频的评论列表。
        </td>
    </tr>
</table>

<strong>用户</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/account/updateProfile" text="POST account/updateProfile"/>
        </td>
        <td>
            更新当前登录用户的Profile，返回更新后的用户信息。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/account/checkScreenName" text="POST account/checkScreenName"/>
        </td>
        <td>
            检查screenName是否可用。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/search" text="GET users/search"/>
        </td>
        <td>
            返回符合条件的用户列表，可指定全部或部分screenName。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/show" text="GET users/show"/>
        </td>
        <td>
            返回由userId和screenName指定的用户信息。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/suggestions/hot" text="GET users/suggestions/hot"/>
        </td>
        <td>
            返回推荐关注的用户列表。
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
            关注由userId或screenName指定的用户，返回被关注的用户信息。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/post/friendships/destroy" text="POST friendships/destory"/>
        </td>
        <td>
            取消关注由userId或screenName指定的用户，返回被取消关注的用户信息。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/friends/list" text="GET friends/list"/>
        </td>
        <td>
            返回由userId或screenName指定的用户关注的人列表。
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/followers/list" text="GET followers/list"/>
        </td>
        <td>
            返回由userId或screenName指定的用户的粉丝列表。
        </td>
    </tr>
</table>

<strong>收藏</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/favorites/list" text="GET favorites/list"/>
        </td>
        <td>
            返回由userId或screenName指定的用户收藏的视频列表。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/create" text="POST favorites/create"/>
        </td>
        <td>
            收藏由id指定的视频，返回被收藏的视频。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/destroy" text="POST favorites/destroy"/>
        </td>
        <td>
            删除收藏由id指定的视频，返回被删除收藏的视频。
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
            返回符合条件的hashtag列表，可指定hashtag中出现的关键词。
        </td>
    </tr>
</table>

<strong>拉黑</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/blocks/create" text="POST blocks/create"/>
        </td>
        <td>
            将userId或screenName指定的用户加入黑名单，返回被加入黑名单的用户。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/blocks/destroy" text="POST blocks/destroy"/>
        </td>
        <td>
            将userId或screenName指定的用户从黑名单中删除，返回从黑名单中删除的用户信息。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/blocks/list" text="GET blocks/list"/>
        </td>
        <td>
            返回我的黑名单用户列表。
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
            发送私信，返回发送的私信。
        </td>
    </tr>
</table>

<hr/>

<p>
    使用<a href="http://www.google.com/intl/zh-CN/chrome/browser/">谷歌浏览器</a>+<a
        href="https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc">JSONView扩展</a>看JSON很棒。
</p>
