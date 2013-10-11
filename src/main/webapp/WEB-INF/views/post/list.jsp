<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>管理帖子</title>
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
    </style>
</head>
<body>

<form id="searchForm" class="form-search pull-left" method="GET" action="">
    <input type="search" class="input-medium search-query" id="screenName" name="screenName" value="${screenName}"
           placeholder="用户名字">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=screenName]').val('')"></span>
    或
    <input type="search" class="input-medium search-query" id="userId" name="userId" value="${userId}"
           placeholder="用户ID">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=userId]').val('')"></span>

    <input type="text" class="input-mini" name="count" value="${count}">条
    <button type="submit" class="btn" style="margin-left: 2em;">搜索</button>
</form>

<table id="contentTable" class="table table-hover">
    <tr>
        <th style="width:20px;">
            #
        </th>
        <th style="width: 100px;">
            视频
        </th>
        <th>
            描述
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
    <c:forEach items="${posts}" var="item">
        <tr>
            <td></td>
            <td>
                <a href="${item.videoUrl}" target="_blank">
                    <img src="${item.videoFirstFrameUrl}"/>
                </a>
            </td>
            <td class="text">
                    ${item.text}
            </td>
            <td class="screenName">
                    ${item.screenName}
            </td>
            <td>
                    ${item.createdAt}
            </td>
            <td>
                <a href="${ctx}/admin/comment/list?postId=${item.id}" target="_blank">查看评论</a>
                <input type="button" value="删除" class="btn btn-link" onclick="deletePost('${item.id}', this)"/>
            </td>
        </tr>
    </c:forEach>
</table>

<script>
    $(function () {
        //行号
        $('#contentTable tr:gt(0)').each(function (index) {
            $('td:first', this).html(index + 1);
        });

    });

    function deletePost(id, btn) {
        if (confirm('确定删除吗?')) {
            $.ajax({
                url: '${ctx}/admin/post/delete/' + id,
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