<%@ page import="com.lebo.entity.ReportSpam" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>举报管理</title>
    <style>
        td.desc {
            position: relative;
            text-align: left;
        }

        .media {
            width: 110px;
        }

        .media .audio {
            margin: 40px 0;
        }

        .media .text {
            margin: 40px 0;
            font-style: italic;
        }

        .media a {
            width: 100px;
            margin-right: 10px;
            position: relative;
            display: block;
        }

        .media .badge {
            position: absolute;
            top: 5px;
            right: 5px;
        }

        .media a img {
            width: 100px;
        }

        .desc .author img {
            height: 2em;
        }

        .desc .text {
            margin-top: 10px;
        }

        .desc .info label {
            display: inline-block;
            margin: 0;
            padding: 0 5px;
            height: 100%;
            cursor: default;
        }

        .desc .info label [class^='icon-'] {
            margin-right: 3px;
        }

        td.action a {
            display: block;
        }

        tr.processed td.processed-icon .icon-ok {
            display: inline-block;
        }

        td.processed-icon .icon-ok {
            display: none;
        }

        .moreinfo-toggle {
            float: right;
            margin-top: -1.4em;
        }

        .moreinfo {
            margin-top: 1em;
        }

        .moreinfo label {
            width: 6em;
            display: inline-block;
            text-align: right;
            padding-right: 1em;
        }

        .icon-remove {
            cursor: pointer;
            margin-left: -2em;
            margin-right: 2em;
        }

        #searchForm select {
            margin-right: 1em;
        }
    </style>
</head>
<body>

<form id="searchForm" class="form-search" method="GET" action="">

    <select name="processed" class="input-small">
        <option value="">处理状态</option>
        <option value="true" ${param.processed == 'true' ? 'selected' : ''}>已处理</option>
        <option value="false" ${param.processed == 'false' ? 'selected' : ''}>未处理</option>
    </select>

    <select name="reportObjectType" class="input-small">
        <option value="">内容类型</option>
        <%
            for (ReportSpam.ObjectType type : ReportSpam.ObjectType.values()) {
                String selected = type.name().equals(request.getParameter("reportObjectType")) ? "selected" : "";
        %>
        <option value="<%=type.name()%>" <%=selected%>><%=type.getName()%>
        </option>
        <%}%>
    </select>

    <input type="text" name="reportUserId" value="${param.reportUserId}" class="search-query" placeholder="被举报人ID"
           title="被举报人ID"/>
    <span class="icon-remove" onclick="$('input[name=reportUserId]').val('')" title="清除输入内容"></span>

    <input type="text" name="informerUserId" value="${param.informerUserId}" class="search-query" placeholder="举报人ID"
           title="举报人ID"/>
    <span class="icon-remove" onclick="$('input[name=informerUserId]').val('')" title="清除输入内容"></span>

    <button type="submit" class="btn">查询</button>
</form>

<tags:pageinfo page="${page}" spentSeconds="${spentSeconds}"/>

<table id="contentTable" class="table table-hover">
    <tr>
        <th style="width:2em;">
            #
        </th>
        <th colspan="2">
            被举报内容
        </th>
        <th style="width:8em">
            举报人
        </th>
        <th style="width:8em">
            举报时间
        </th>
        <th style="width:7em">
            操作
        </th>
    </tr>
    <c:forEach items="${list}" var="item" varStatus="varStatus">
        <tr class="<c:if test="${item.processed}">processed</c:if>">
            <td class="processed-icon">
                <span class="icon-ok" title="已处理"></span>
                    ${varStatus.index + 1}
            </td>
            <td class="media">

                <c:if test="${item.reportObject != null}">
                    <c:if test="${item.reportObject.videoUrl != null}">
                        <a href="${item.reportObject.videoUrl}">
                            <img src="${item.reportObject.videoFirstFrameUrl}"/>
                            <c:if test="${item.reportObject.duration != null}">
                                <span class="badge badge-important">${item.reportObject.duration}''</span>
                            </c:if>
                        </a>
                    </c:if>

                    <c:if test="${item.reportObjectType.name == '评论' && item.reportObject.audioUrl != null}">
                        <a href="${item.reportObject.audioUrl}" class="audio">
                            语音评论
                        </a>
                    </c:if>

                    <c:if test="${item.reportObjectType.name == '评论' && item.reportObject.audioUrl == null && item.reportObject.videoUrl == null}">
                        <div class="text">文字评论</div>
                    </c:if>
                </c:if>

                <c:if test="${item.reportObject == null}">
                    <div class="text">${item.reportObjectType.name}已被删除</div>
                </c:if>
            </td>

            <td class="desc" title="${item.reportObjectType.name}">

                <div class="author">
                    <a href="${ctx}/admin/user?userId=${item.reportUser.id}">
                        <img src="${item.reportUser.profileImageUrl}" title="作者头像"/>
                        <span>${item.reportUser.screenName}</span>
                    </a>

                    <span class="time" title="发布时间">${item.reportObject.createdAt}</span>

                    <c:if test="${item.reportObject.pvt}">
                        <span class="icon-lock pull-right" title="仅作者本人可见"></span>
                    </c:if>
                </div>

                <c:if test="${item.reportObject != null}">
                    <p class="text">${item.reportObject.text}</p>

                    <c:if test="${item.reportObjectType.name == '帖子'}">
                        <div class="info">
                            <label title="红心"><span
                                    class="icon-heart"></span>${item.reportObject.favoritesCount}
                            </label>
                            <label title="转发"><span class="icon-retweet"></span>${item.reportObject.repostCount}
                            </label>
                            <label title="播放"><span class="icon-play"></span>${item.reportObject.viewCount}
                            </label>
                            <label title="评论"><span
                                    class="icon-comment"></span>${item.reportObject.commentCount}
                            </label>
                            <label title="人气"><span class="icon-gift"></span>${item.reportObject.popularity}
                            </label>
                            <label title="红心+人气+评分"><span class="icon-star"></span><span
                                    class="starValue">${item.reportObject.favoritesCount + item.reportObject.popularity + item.reportObject.rating}</span></label>
                        </div>
                    </c:if>
                </c:if>

                <a href="javascript:void(0)" class="moreinfo-toggle"
                   onclick="$('#moreinfo-${item.id}').toggle();">更多>></a>

                <ul id="moreinfo-${item.id}" class="moreinfo" style="display:none">
                    <li><label>被举报人ID:</label>${item.reportUserId}</li>
                    <li><label>${item.reportObjectType.name}ID:</label>${item.reportObjectId}</li>
                    <li><label>举报人ID:</label>${item.informerUserId}</li>
                </ul>

            </td>

            <td>
                    ${item.informerUser.screenName}
            </td>
            <td>
                <fmt:formatDate value="${item.createdAt}" pattern="EEE MMM dd HH:mm:ss ZZZZZ yyyy"/>
            </td>
            <td class="action">

                <c:if test="${item.reportObject != null}">
                    <a href="javascript:void(0)"
                       onclick="deleteReportObject('${item.reportObjectType.name}', '${item.id}', this);">删除${item.reportObjectType.name}</a>
                </c:if>

                <a class="processed-toggle" href="javascript:void(0)"
                   onclick="setProcessed('${item.id}', this);">${item.processed ? '标记为未处理' : '标记为已处理'}</a>

                <a href="javascript:void(0)" onclick="deleteReportSpam('${item.id}', this);">删除举报</a>
            </td>
        </tr>
    </c:forEach>

</table>

<tags:pagination-normal page="${page}" paginationSize="5"/>

<script>
    function setProcessed(id, btn) {
        var action = $.trim($(btn).text());

        var processed = (action == '标记为已处理');

        $.ajax({
            url: '${ctx}/admin/report-spam/setProcessed',
            data: {id: id, processed: processed},
            type: 'POST',
            success: function (data) {
                if (data == 'ok') {
                    updateTrCss($(btn).parents('tr'), processed);
                }
            }
        });
    }

    function deleteReportSpam(id, btn) {
        if (!confirm("确定删除举报吗？")) {
            return;
        }

        $.ajax({
            url: '${ctx}/admin/report-spam/delete',
            data: {id: id},
            type: 'POST',
            success: function (data) {
                if (data == 'ok') {
                    $(btn).parents('tr').remove();
                }
            }
        });
    }

    function deleteReportObject(type, id, btn) {
        if (confirm('确定删除' + type + '吗?')) {

            $.ajax({
                url: '${ctx}/admin/report-spam/deleteReportObject',
                type: 'POST',
                data: {id: id},
                success: function (data) {
                    if (data == 'ok') {
                        updateTrCss($(btn).parents('tr'), true);
                        $(btn).remove();
                    }
                }
            });
        }
    }

    function updateTrCss(tr, processed) {
        if (processed) {
            $(tr).addClass('processed');
            tr.find('.processed-toggle').html('标记为未处理');
        } else {
            $(tr).removeClass('processed');
            tr.find('.processed-toggle').html('标记为已处理');
        }
    }
</script>

</body>
</html>