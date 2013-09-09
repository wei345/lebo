<%@ page import="com.lebo.rest.dto.ErrorDto" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="noTokenCode" value="<%=ErrorDto.FIND_WEIBO_FRIEND_NO_TOKEN.getError().getCode()%>"/>
<c:set var="errorTokenCode" value="<%=ErrorDto.FIND_WEIBO_FRIEND_ERROR_TOKEN.getError().getCode()%>"/>

<tags:form name="weiboFriends" action="${ctx}/api/1/friends/weiboFriends.json" method="GET">
    <p>
        客户端第一次调用此接口，不需要带token参数，
    <ul>
        <li>如果服务端返回新浪好友，那么客户端可以继续获取下一页数据。</li>
        <li>如果服务端返回错误状态码<code>${noTokenCode}</code>或<code>${errorTokenCode}</code>(状态码含义见<a href="/docs/api/1/dataStructures#error_code">返回对象数据结构</a>)，客户端需要显示新浪微博登录页，获得token后，带着token再次调用此接口，服务端返回新浪好友，之后客户端可以获取下一页数据。</li>
    </ul>
    服务端会保存最后一次使用的新浪token。
    </p>

    <tags:field name="token" value="" optional="true"/>
    <p>
        每页返回多少条，默认50
    </p>
    <tags:field name="count" value="" optional="true"/>
    <p>
        返回结果的游标，下一页用返回值里的nextCursor，默认为0
    </p>
    <tags:field name="cursor" value="" optional="true"/>
</tags:form>

<p>
    如果某新浪好友也在乐播中，那么用户信息会有<code>userId</code>字段。
</p>
<tags:example method="GET"
              url="http://localhost:8080/api/1/friends/weiboFriends.json?token=2.00TgJysBz7QwTC50dd2ad1facNOSYD">
    {
        users: [
            {
                weiboId: "3327539674",
                screenName: "洛洛家的小九",
                name: "洛洛家的小九",
                gender: "f",
                verified: false,
                profileImageUrl: "http://tp3.sinaimg.cn/3327539674/50/5660982641/0"
            },
            {
                weiboId: "3051578312",
                screenName: "志龙小朋友",
                name: "志龙小朋友",
                gender: "m",
                verified: false,
                profileImageUrl: "http://tp1.sinaimg.cn/3051578312/50/5650947497/1"
            }
        ],
        nextCursor: 2,
        totalNumber: 35
    }
</tags:example>