<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="完成上传文件" method="POST" action="${ctx}/api/1.1/im/completeUpload.json">
    <tags:field name="fromUserId"/>
    <tags:field name="toUserId"/>
    <tags:field name="attachmentUrl"/>

    <div id="insert-before-here" style="display: none"></div>

    <div class="controls-group">
        <button class="btn" onclick="addAtt()" type="button">
            <span class="icon-plus"></span>
            attachmentUrl
        </button>
    </div>
    <br/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/im/completeUpload.json">
    {
        id: "526f77f01a88b86f9cef916f",
        from: {
            id: "521c1c291a88bff4d06f1ac8",
            screenName: "手机用户3123213047",
            description: "",
            profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4vbh-1.gif",
            profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4vbh-1.gif",
            profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4vbh-1.gif",
            createdAt: "Thu Jul 18 21:11:41 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 0,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            level: 0
        },
        to: {
            id: "521c1c291a88bff4d06f1ac7",
            screenName: "La-double-vie",
            description: "「Love」 「Liberation」 「Agony」 「Solitude」 「Contradiction」",
            profileImageUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4v8m-.jpeg",
            profileImageBiggerUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4v8m-.jpeg",
            profileImageOriginalUrl: "http://121.199.1.164:8080/?cmd=GEOWEIBOD.mime3+photo-mq4v8m-.jpeg",
            createdAt: "Thu Jul 18 21:09:57 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 0,
            beFavoritedCount: 0,
            viewCount: 0,
            digestCount: 0,
            level: 0
        },
        attachments: [
            {
                length: 8473,
                contentType: "image/png",
                contentUrl: "http://file.dev.lebooo.com/im/2013-10-29/526f77f01a88b86f9cef916f-0-image-png-8473.png"
            }
        ],
        createdAt: "Tue Oct 29 16:55:12 +0800 2013"
    }
</tags:example>

<script>
    var s = '<div class="control-group">' +
            '<label class="control-label">' +
            'attachmentUrl' +
            '</label>' +
            '<div class="controls">' +
            '<input type="text" name="attachmentUrl" placeholder="attachmentUrl" value="">' +
            '<button class="btn"  type="button"><span class="icon-minus"></span></button>' +
            '</div>' +
            '</div>';

    function addAtt(){
        $(s).insertBefore('#insert-before-here');
    }
</script>