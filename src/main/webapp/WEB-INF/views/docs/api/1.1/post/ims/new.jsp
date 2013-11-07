<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="完成上传文件" method="POST" action="${ctx}/api/1.1/ims/new.json">
    <tags:field name="fromUserId"/>
    <tags:field name="toUserId"/>
    <tags:field name="message" optional="true"/>
    <tags:field name="attachmentUrl" optional="true"/>

    <div id="insert-before-here" style="display: none"></div>

    <div class="controls-group">
        <button class="btn" onclick="addAtt()" type="button">
            <span class="icon-plus"></span>
            attachmentUrl
        </button>
    </div>
    <br/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/ims/new.json">
    {
        id: "52771a271a88eac34747bb37",
        from: {
            id: "525124e81a88ac9dfcbd9ce0",
            screenName: "明丫丫是个爷们",
            profileImageUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce1",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce3",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/525124e91a88ac9dfcbd9ce5",
            createdAt: "Sun Oct 06 16:52:56 +0800 2013",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 10,
            beFavoritedCount: 1,
            viewCount: 2,
            digestCount: 0,
            weiboVerified: false,
            level: 0
        },
        to: {
            id: "52356929343539a89a52dc8d",
            screenName: "admin",
            description: "秋",
            profileImageUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-normal-4252.png",
            profileImageBiggerUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-bigger-16865.png",
            profileImageOriginalUrl: "http://file.dev.lebooo.com/user/2013-09-15/52356929343539a89a52dc8d-original-1705885.png",
            followersCount: 0,
            friendsCount: 0,
            statusesCount: 6,
            beFavoritedCount: 0,
            viewCount: 1,
            digestCount: 0,
            level: 0
        },
        attachments: [
            {
                length: 7286,
                contentType: "image/png",
                contentUrl: "http://file.dev.lebooo.com/im/52771a271a88eac34747bb37-0.png"
            }
        ],
        createdAt: "Mon Nov 04 11:53:11 +0800 2013"
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