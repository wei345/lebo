<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>刷新热门帖子</title>
</head>
<body>


<ul>
    <li>
        系统会定时刷新热门帖子，通常情况下，你不需要执行此操作。
    </li>
    <li>
        如果你修改了帖子评分，想立刻看到效果，可以执行此操作刷新热门帖子。
    </li>
</ul>

<input type="button" value="立即刷新热门帖子" class="btn" onclick="refreshHotPosts()"/>

<div class="text-info" style="display: none">正在处理..</div>

<div class="text-error" style="display: none">处理失败</div>

<script>
    function refreshHotPosts() {
        $('input[type=button]').hide();

        $('.text-error').hide();

        $('.text-info').show();

        $.ajax({
            url: '${ctx}/admin/post/refreshHotPosts',
            type: 'POST',
            success: function (data) {
                if (data == 'ok') {
                    $('.text-info').hide();
                    $('.text-error').hide();
                    $('input[type=button]').show();
                } else {
                    $('.text-error').show();
                    $('.text-info').hide();
                }
            },
            error: function () {
                $('.text-error').show();
                $('.text-info').hide();
            }
        });
    }
</script>

</body>
</html>