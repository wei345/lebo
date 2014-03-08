<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>发布评论</title>
    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#author").focus();

            //为inputForm注册validate函数
            jQuery.validator.addMethod("postIdOrReplyCommentId", function (value, element) {
                var postId = $.trim($("#postId").val());
                var replyCommentId = $.trim($("#replyCommentId").val());
                return postId.length > 0 || replyCommentId.length > 0;
            }, "帖子ID 和 回复评论ID 不能都为空");

            $("#inputForm").validate({
                rules: {
                    postId: {postIdOrReplyCommentId: true},
                    replyCommentId: {postIdOrReplyCommentId: true}
                }
            });
        });
    </script>
</head>
<body>

<form action="${ctx}/admin/comment/create" method="POST" class="form-horizontal" id="inputForm">

    <div class="control-group">
        <label for="screenName" class="control-label">用户名字:</label>

        <div class="controls">
            <input type="text" id="screenName" name="screenName" value="${param.screenName}"
                   class="input-large required"/>
            以该用户身份发布评论
        </div>
    </div>

    <div class="control-group">
        <label for="postId" class="control-label">帖子ID:</label>

        <div class="controls">
            <input type="text" id="postId" name="postId" value="${param.postId}"
                   class="input-large"/>
            被评论的帖子
        </div>
    </div>


    <div class="control-group">
        <label for="replyCommentId" class="control-label">回复评论ID:</label>

        <div class="controls">
            <input type="text" id="replyCommentId" name="replyCommentId" value="${param.replyCommentId}"
                   class="input-large"/>
            被回复的评论
        </div>
    </div>

    <div class="control-group">
        <label for="text" class="control-label">内容:</label>

        <div class="controls">
            <textarea id="text" name="text" class="input-large required" rows="5" cols="20">${param.text}</textarea>
        </div>
    </div>

    <div class="form-actions">
        <button type="button" class="btn">返回</button>
        <button type="submit" class="btn btn-primary">提交</button>
    </div>
</form>

</body>
</html>