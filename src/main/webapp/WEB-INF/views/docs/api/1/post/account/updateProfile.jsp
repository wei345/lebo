<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="更新Profile" method="POST" action="${ctx}/api/1/account/updateProfile.json"
           enctype="multipart/form-data">
    <tags:field name="image" type="file" optional="true"/>
    <tags:field name="screenName" value="" optional="true"/>
    <tags:field name="description" value="介绍" optional="true"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1/account/updateProfile.json">
    {
        id: "51e778ea1a8816dc79e40aaf",
        screenName: "liuwei",
        name: "Liu Wei",
        profileImageUrl: "/files/51ed10f11a88f15acf2d87f9",
        createdAt: "Thu Jul 18 13:11:06 +0800 2013",
        following: null,
        followersCount: null,
        friendsCount: 0,
        statusesCount: 1,
        verified: null,
        location: null,
        timeZone: null
    }
</tags:example>
