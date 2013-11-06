<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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

        #contentTable td.detail {
            vertical-align: top;
            text-align: left;
            padding: 15px;
        }

        #contentTable tr td.detail img.authorPhoto {
            height: 2em;
        }

        #contentTable td.detail .time {
            margin-left: 2em;
        }

        #contentTable tr td .content {
            margin-top: 10px;
        }

        #contentTable tr td .info label {
            display: inline-block;
            margin: 0;
            padding: 0 5px;
            height: 100%;
            cursor: default;
        }

        #contentTable tr td .info label [class^='icon-'] {
            margin-right: 3px;
        }
    </style>
</head>
<body>

<form id="searchForm" class="form-search pull-left" method="GET" action="">
    <input type="search" class="input-medium search-query" id="screenName" name="screenName" value="${param.screenName}"
           placeholder="用户名字">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=screenName]').val('')"></span>
    或
    <input type="search" class="input-medium search-query" id="userId" name="userId" value="${param.userId}"
           placeholder="用户ID">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=userId]').val('')"></span>

    <input type="search" class="input-medium search-query" id="track" name="track" value="${param.track}"
           placeholder="关键词">
    <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
          onclick="$('input[name=track]').val('')"></span>

    <div style="padding-top: 15px;">

        <select name="orderBy" class="input-small">
            <option value="<%=Post.ID_KEY%>" ${param.orderBy == "_id" ? "selected" : ""}>发布时间</option>
            <option value="favoritesCount" ${param.orderBy == "favoritesCount" ? "selected" : ""}>红心数</option>
            <option value="viewCount" ${param.orderBy == "viewCount" ? "selected" : ""}>播放数</option>
        </select>

        <select name="order" class="input-mini">
            <option value="DESC" ${param.order == "DESC" ? "selected" : ""}>降序</option>
            <option value="ASC" ${param.order == "ASC" ? "selected" : ""}>升序</option>
        </select>

        <input type="text" class="input-mini" name="size" value="${count}">条/页

        <button type="submit" class="btn" style="margin-left: 2em;">搜索</button>
    </div>
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
            详细
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
            <td class="detail">
                <div class="authorDisplayName">
                    <a href="${ctx}/admin/user/update/${item.userId}">
                        <img class="authorPhoto" src="${item.profileImageUrl}" title="作者头像"/>
                        <span>${item.screenName}</span>
                    </a>
                    <span class="time" title="发布时间">${item.createdAt}</span>
                </div>

                <p class="content">${item.text}</p>

                <div class="info">
                    <label title="喜欢"><span class="icon-heart"></span>${item.favoritesCount}</label>
                    <label title="转发"><span class="icon-retweet"></span>${item.repostCount}</label>
                    <label title="播放"><span class="icon-play"></span>${item.viewCount}</label>
                    <label title="评论"><span class="icon-comment"></span>${item.commentCount}</label>
                </div>
            </td>
            <td>
                <a href="${ctx}/admin/comment/list?postId=${item.id}" target="_blank">查看评论</a>
                <input type="button" value="删除" class="btn btn-link" onclick="deletePost('${item.id}', this)"/>
            </td>
        </tr>
    </c:forEach>
</table>

<tags:pagination-normal page="${page}" paginationSize="5"/>

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