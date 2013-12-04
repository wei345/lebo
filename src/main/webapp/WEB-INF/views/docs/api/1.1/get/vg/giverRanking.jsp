<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="送礼者排名" method="GET" action="${ctx}/api/1.1/vg/giverRanking.json">
    <p>
        需要<code>userId</code>或<code>screenName</code>
    </p>
    <tags:field name="userId" optional="true"/>
    <tags:field name="screenName" optional="true"/>
    <tags:fields-page-size/>
</tags:form>

<p>
    如果当前用户没有给<code>userId</code>送过物品，则返回结果无<code>me</code>字段。
</p>
<tags:example method="GET" url="http://localhost:8080/api/1.1/vg/giverRanking.json?screenName=%E7%83%9F%E9%9B%A8%E9%86%89%E7%9B%B8%E6%80%9D">
    {
        me: {
            id: "525124e81a88ac9dfcbd9ce0",
            screenName: "明丫丫是个爷们",
            profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
            giveValue: 20,
            rank: 1
        },
        giverList: [
            {
                id: "525124e81a88ac9dfcbd9ce0",
                screenName: "明丫丫是个爷们",
                profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
                giveValue: 20
            },
            {
                id: "52356929343539a89a52dc8d",
                screenName: "admin",
                profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
                giveValue: 18
            }
        ]
    }
</tags:example>