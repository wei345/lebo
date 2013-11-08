<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="完成上传文件" method="POST" action="${ctx}/api/1.1/ims/completeUpload.json">
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

<tags:example method="POST" url="http://localhost:8080/api/1.1/ims/completeUpload.json?toUserId=52356929343539a89a52dc8d&attachmentUrl=http%3A%2F%2Ffile.dev.lebooo.com%2Ftmp%2Fexpire-2013-11-08-12-36-25-video-mp4-527c5c391a88f89afff63232.mp4&attachmentUrl=http%3A%2F%2Ffile.dev.lebooo.com%2Ftmp%2Fexpire-2013-11-08-12-38-02-image-jpeg-527c5c9a1a88f89afff63235.jpg">
    {
        id: "527c648a1a884c28e939d568",
        fromUserId: "52356929343539a89a52dc8d",
        toUserId: "52356929343539a89a52dc8d",
        attachments: [
            {
                length: 997271,
                contentType: "video/mp4",
                contentUrl: "http://file.dev.lebooo.com/im/527c648a1a884c28e939d568-0.mp4"
            },
            {
                length: 26038,
                contentType: "image/jpeg",
                contentUrl: "http://file.dev.lebooo.com/im/527c648a1a884c28e939d568-1.jpg"
            }
        ],
        createdAt: "Fri Nov 08 12:11:54 +0800 2013"
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