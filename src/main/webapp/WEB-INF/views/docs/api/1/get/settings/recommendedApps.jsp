<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="推荐应用" method="GET" action="${ctx}/api/1/settings/recommendedApps.json">
    <p>
        <code>ios</code>或<code>android</code>，服务器根据此参数选择应用的下载地址，如果没有传此参数，服务器会根据User-Agent自动选择。
    </p>
    <tags:field name="type" optional="true"/>
</tags:form>

<tags:example method="GET" url="http://localhost:8080/api/1/settings/recommendedApps.json?type=android">
    [
        {
            name: "乐播",
            url: "http://apps.wandoujia.com/apps/com.lebo.android/download?pos=www/detail",
            description: "乐播，轻松创造6秒超短视频，分享给家人和朋友，还可以观看和评论明星和女神的6秒视频随手拍，零距离融入她们的生活。 全球首创视频版微博，用6秒来炫出你的特长，让别人模仿去吧，并有丰富的大奖等你来拿! 真正的牛人是你！",
            imageUrl: "images/apps/lebo.png",
            version: "2.6",
            size: "11.2 MB"
        }
    ]
</tags:example>