<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<style>
    .td1 { width: 25em; }
</style>

<h3>REST API V1</h3>

<strong>时间线</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/v1/get/statuses/homeTimeline" text="GET statuses/homeTimeline" />
        </td>
        <td>
            主页时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/get/statuses/userTimeline" text="GET statuses/userTimeline" />
        </td>
        <td>
            用户时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/get/statuses/mentionsTimeline" text="GET statuses/mentionsTimeline" />
        </td>
        <td>
            提到我时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/statuses/update" text="POST statuses/update" />
        </td>
        <td>
            发布视频
        </td>
    </tr>
</table>

<strong>评论</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/v1/post/comments/create" text="POST comments/create" />
        </td>
        <td>
            发布评论
        </td>
    </tr>
</table>

<strong>关系</strong>
<table class="table table-hover">
    <tr>
        <td class="td1">
            <tags:link url="${ctx}/docs/api/v1/post/friendships/create" text="POST friendships/create" />
        </td>
        <td>
            关注
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/friendships/destroy" text="POST friendships/destory" />
        </td>
        <td>
            取消关注
        </td>
    </tr>
</table>

<hr/>

<p>
    使用<a href="http://www.google.com/intl/zh-CN/chrome/browser/">谷歌浏览器</a>和<a href="https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc">JSONView扩展</a>看JSON很棒。
</p>
