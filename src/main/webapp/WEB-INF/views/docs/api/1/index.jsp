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
            返回应用基本设置。
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
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/settings/recommendedApps" text="GET settings/recommendedApps"/>
        </td>
        <td>
            返回推荐应用列表。
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
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/userDigest" text="GET statuses/userDigest"/>
        </td>
        <td>
            返回由userId或screenName指定的用户被加精的视频列表。
        </td>
    </tr>
</table>

<strong>视频排序</strong>
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
            <tags:link url="${ctx}/docs/api/1.1/get/statuses/hot" text="GET statuses/hot v1.1"/>
        </td>
        <td>
            返回热门视频列表 + 广告。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/ranking" text="GET statuses/ranking"/>
        </td>
        <td>
            返回作品榜视频列表。
        </td>
    </tr>
    <%--未用到 而且这个接口排序有问题 <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/statuses/search" text="GET statuses/search"/>
        </td>
        <td>
            返回符合搜索条件的视频列表，可以指定hashtag、@某人、关键词，可以按喜欢数、浏览数排序。
        </td>
    </tr>--%>
</table>

<strong>上传</strong>
<table class="table table-hover">
<tr>
    <td>
        <tags:link url="${ctx}/docs/api/1.1/get/upload/newTmpUploadUrl" text="GET upload/newTmpUploadUrl"/>
    </td>
    <td>
        返回有失效期的临时上传地址。
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
            <tags:link url="${ctx}/docs/api/1.1/post/statuses/update" text="POST statuses/update v1.1"/>
        </td>
        <td>
            发布视频，返回发布的视频。客户端直接上传视频到阿里云云存储，发布时传给服务端url。
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
        <td>
            <tags:link url="${ctx}/docs/api/1/post/statuses/unrepost" text="POST statuses/unrepost"/>
        </td>
        <td>
            取消转发视频。
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
            <tags:link url="${ctx}/docs/api/1/get/statuses/show" text="GET statuses/show"/>
        </td>
        <td>
            返回由id参数指定的单个视频。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/statuses/increaseViewCountBatch"
                       text="POST statuses/increaseViewCountBatch"/>
        </td>
        <td>
            视频播放次数+1。
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
            发文字评论，返回发布的评论。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/comments/createWithMedia" text="POST comments/createWithMedia"/>
        </td>
        <td>
            发视频评论、语音评论，返回发布的评论。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/comments/destroy" text="POST comments/destroy"/>
        </td>
        <td>
            删除一条评论，返回被删除的评论。
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
            <tags:link url="${ctx}/docs/api/1/get/account/settings" text="GET account/settings"/>
        </td>
        <td>
            返回当前登录用户的设置。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/account/settings" text="POST account/settings"/>
        </td>
        <td>
            更新用户设置，如APNS token、通知设置。
        </td>
    </tr>
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
            返回符合条件的用户列表，可指定全部或部分screenName。粉丝最多、最受喜欢、票房最高、导演排行。
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
            <tags:link url="${ctx}/docs/api/1/get/users/hotUserList" text="GET users/hotUserList"/>
        </td>
        <td>
            返回红人榜按钮设置 & 推荐用户。
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
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/friends/weiboFriends" text="GET friends/weiboFriends"/>
        </td>
        <td>
            返回新浪微博好友。
        </td>
    </tr>
    <%--未用到 <tr>
        <td>
            <tags:link url="${ctx}/docs/api/1/get/friends/bilateral" text="GET friends/bilateral"/>
        </td>
        <td>
            返回由userId或screenName指定的用户的双向关注好友。
        </td>
    </tr>--%>
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

<strong>通知</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/notifications/list" text="GET notifications/list"/>
        </td>
        <td>
            返回当前登录用户的通知(消息)列表。
        </td>
    </tr>
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/notifications/markAllRead" text="POST notifications/markAllRead"/>
        </td>
        <td>
            将当前登录用户的所有未读通知标记为已读。
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

<strong>即时通讯</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/im/profiles" text="GET im/profiles"/>
        </td>
        <td>
            返回指定用户的profile。
        </td>
    </tr>
</table>

<strong>反馈</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/feedback/videoCanotPlay" text="POST feedback/videoCanotPlay"/>
        </td>
        <td>
            向服务签报告不能播放的视频，服务器会做转码处理。
        </td>
    </tr>
</table>

<strong>版本</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/post/checkVersion" text="POST checkVersion"/>
        </td>
        <td>
            返回客户端最新版本信息，以及是否需要强制升级。
        </td>
    </tr>
</table>

<strong>小应用:每日十个</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/1/get/everyday10/list" text="GET everyday10/list"/>
        </td>
        <td>
            返回<code>每天笑十次</code>用户最近发的10个帖子。
        </td>
    </tr>
</table>

<hr/>

<p>
    使用<a href="http://www.google.com/intl/zh-CN/chrome/browser/">谷歌浏览器</a>+<a
        href="https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc">JSONView扩展</a>看JSON很棒。
</p>
