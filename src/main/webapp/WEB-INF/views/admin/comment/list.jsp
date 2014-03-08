<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>管理评论</title>
    <style type="text/css">
        #contentTable th {
            text-align: center;
        }

        #contentTable td {
            vertical-align: middle;
            text-align: center;
        }

        #contentTable .text {
            width: 400px;
        }

        .replyTo {
            color: #ccc;
        }
    </style>
</head>
<body>

<form id="searchForm" class="form-search pull-left" method="GET" action="">
    <input type="search" class="input-large search-query" id="postId" name="postId" value="${param.postId}"
           placeholder="帖子ID">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=postId]').val('')"></span>

    <input type="search" class="input-large search-query" id="userId" name="userId" value="${param.userId}" placeholder="用户ID">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;" onclick="$('input[name=userId]').val('')"></span>

    <input type="text" class="input-mini" name="size" value="${page.size}">条/页
    <button type="submit" class="btn" style="margin-left: 2em;">查询</button>
</form>

<c:if test="${param.postId != null}">
    <a class="pull-right" href="${ctx}/admin/comment/create?postId=${param.postId}">回复帖子</a>
</c:if>

<table id="contentTable" class="table table-hover">
    <tr>
        <th style="width:20px;">
            #
        </th>
        <th style="width: 100px;">
            视频/语音
        </th>
        <th>
            文字
        </th>
        <th>
            作者
        </th>
        <th>
            发布时间
        </th>
        <th>
            操作
        </th>
    </tr>
    <c:forEach items="${comments}" var="item">
        <tr>
            <td></td>
            <td>
                <c:if test="${item.mediaLinkText == null}">
                    无
                </c:if>
                <a href="${item.mediaUrl}" target="_blank">
                        ${item.mediaLinkText}
                </a>
            </td>
            <td class="text">
                <c:if test="${item.replyTo != null}">
                    <span class="replyTo">回复 ${item.replyTo}: </span>
                </c:if>
                    ${item.text}
            </td>
            <td class="screenName">
                    ${item.screenName}
            </td>
            <td>
                    ${item.createdAt}
            </td>
            <td>
                <a href="${ctx}/admin/comment/create?replyCommentId=${item.id}">回复</a>
                <input type="button" value="删除" class="btn btn-link" onclick="deleteComment('${item.id}', this)"/>
            </td>
        </tr>
    </c:forEach>
</table>

<tags:pagination-normal page="${page}" paginationSize="5"/>

<c:if test="${empty comments}">
    <div style="font-style: italic; text-align: center">没有了</div>
</c:if>

<script>
    $(function () {
        //行号
        $('#contentTable tr:gt(0)').each(function (index) {
            $('td:first', this).html(index + 1);
        });

    });

    function deleteComment(id, btn) {
        if (confirm('确定删除吗?')) {
            $.ajax({
                url: '${ctx}/admin/comment/delete/' + id,
                type: 'POST',
                success: function (data) {
                    if (data == 'ok') {
                        $(btn).parents('tr').remove();
                    }
                }
            });
        }
    }
</script>
</body>
</html>