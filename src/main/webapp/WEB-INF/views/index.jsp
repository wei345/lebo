<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="updateStatusForm" action="${ctx}/api/v1/statuses/update" method="post" enctype="multipart/form-data">
    <fieldset>
        <legend>发布</legend>
        <label>视频</label>
        <input type="file" name="video" class="required"/>
        <label>图片</label>
        <input type="file" name="image" class="required"/>
        <label>文字</label>
        <textarea type="text" name="text" placeholder="Type something…" maxlength="140" cols="80" rows="4"></textarea>
        <input type="hidden" name="source" value="乐播网页版">
        <button type="submit" class="btn">Submit</button>
    </fieldset>
</form>
