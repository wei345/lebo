<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<b>REST API V1</b>

<table class="table table-hover">
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/get/statuses/homeTimeline" text="GET ${ctx}/api/v1/statuses/homeTimeline" />
        </td>
        <td>
            主页时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/get/statuses/userTimeline" text="GET ${ctx}/api/v1/statuses/userTimeline" />
        </td>
        <td>
            用户时间线
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/statuses/update" text="POST ${ctx}/api/v1/statuses/update" />
        </td>
        <td>
            发布视频
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/comments/create" text="POST ${ctx}/api/v1/comments/create" />
        </td>
        <td>
            发布评论
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/friendships/create" text="POST ${ctx}/api/v1/friendships/create" />
        </td>
        <td>
            关注
        </td>
    </tr>
    <tr>
        <td>
            <tags:link url="${ctx}/docs/api/v1/post/friendships/destroy" text="POST ${ctx}/api/v1/friendships/destory" />
        </td>
        <td>
            取消关注
        </td>
    </tr>


</table>

<hr/>

<p>
    使用<a href="http://www.google.com/intl/zh-CN/chrome/browser/">谷歌浏览器</a>和<a href="https://chrome.google.com/webstore/detail/jsonview/chklaanhfefbnpoihckbnefhakgolnmc">JSONView扩展</a>
    会获得非常棒的JSON浏览体验。
</p>
