<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<p>
    返回红人榜按钮图片地址、按钮背景颜色、按钮文字。
</p>
<tags:form name="红人榜设置" method="GET" action="${ctx}/api/1/settings/hotUserList.json">
    <p>
        同时返回指定数量的推荐用户，以减少客户端请求次数。默认 0 条。
    </p>
    <tags:field name="count" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings/hotUserList.json">
    {
        hotuser_button1_backgroundColor: "#7E5CDA",
        hotuser_button1_imageUrl: "http://file.dev.lebooo.com/images/hotuser/btn1.png",
        hotuser_button1_text: "粉丝最多",
        hotuser_button2_backgroundColor: "#D67E89",
        hotuser_button2_imageUrl: "http://file.dev.lebooo.com/images/hotuser/btn2.png",
        hotuser_button2_text: "最受喜欢",
        hotuser_button3_backgroundColor: "#30B5F0",
        hotuser_button3_imageUrl: "http://file.dev.lebooo.com/images/hotuser/btn3.png",
        hotuser_button3_text: "导演排行",
        users: [ ]
    }
</tags:example>