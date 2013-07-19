<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="用户设置" method="POST" action="${ctx}/api/1/users/set.json" enctype="multipart/form-data">
    <tags:field name="head" type="file"/>
    <tags:field name="screenName" value=""/>
    <tags:field name="description" value="介绍"/>
</tags:form>

<tags:example method="POST" url="">
    {
        id: "51deb74d1a883ab010ebcc2e",
        userId: "51dcf1d81a883e712783f124",
        createdAt: 1373550413109,
        text: "法图_麦提到了@小萌君sang 完毕",
        truncated: false,
        files: [
            "51dcf33c1a883e712783f14f",
            "51dcf33c1a883e712783f153"
        ],
        source: null,
        geoLocation: null,
        originPostId: null,
        mentions: [
            "51dcf1f71a883e712783f12c"
        ]
    }
</tags:example>
