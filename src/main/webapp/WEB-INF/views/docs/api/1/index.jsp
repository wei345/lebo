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

<strong>设置</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/settings/settings" text="GET settings"/>
        </td>
        <td>
            返回全部设置
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/settings/channels" text="GET settings/channels"/>
        </td>
        <td>
            返回频道列表
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
            返回当前登录用户视频，
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/statuses/userTimeline" text="GET statuses/userTimeline"/>
        </td>
        <td>
            返回由userId或screenName指定的用户的视频，包括转发。
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
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/search" text="GET statuses/search"/>
        </td>
        <td>
            搜索 - 可以按喜欢数、浏览数排序，可搜索hashtag、指定@某人、指定关键词
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/digest" text="GET statuses/digest"/>
        </td>
        <td>
            精华 - 乐播官方账号转发，2天之内
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/hot" text="GET statuses/hot"/>
        </td>
        <td>
            热门 - 按红心数(收藏数)排序，最近2天
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/userDigest" text="GET statuses/userDigest"/>
        </td>
        <td>
            我的精华 - 被乐播官方账号转发的视频
        </td>
    </tr>
    <tr>
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
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/show" text="GET users/show"/>
        </td>
        <td>
            查看用户
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/account/updateProfile" text="POST account/updateProfile"/>
        </td>
        <td>
            更新Profile
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/account/checkScreenName" text="POST account/checkScreenName"/>
        </td>
        <td>
            检查screenName是否可用
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/users/suggestions/hot" text="GET users/suggestions/hot"/>
        </td>
        <td>
            推荐关注
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
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/friends/list" text="GET friends/list"/>
        </td>
        <td>
            返回指定用户关注的人
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/followers/list" text="GET followers/list"/>
        </td>
        <td>
            返回指定用户的粉丝
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
            查看收藏
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/create" text="POST favorites/create"/>
        </td>
        <td>
            添加收藏
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/favorites/destroy" text="POST favorites/destroy"/>
        </td>
        <td>
            删除收藏
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

<strong>拉黑</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/blocks/create" text="POST blocks/create"/>
        </td>
        <td>
            拉黑，返回被拉黑的用户
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/blocks/destroy" text="POST blocks/destroy"/>
        </td>
        <td>
            取消拉黑，返回被取消拉黑的用户
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/blocks/list" text="GET blocks/list"/>
        </td>
        <td>
            返回被拉黑的用户列表
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
