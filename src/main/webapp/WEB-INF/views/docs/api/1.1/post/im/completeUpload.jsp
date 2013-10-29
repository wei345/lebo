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