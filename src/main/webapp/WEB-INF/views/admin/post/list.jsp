<%
    /**
     帖子管理 和 热门帖子 view
     */
%>
<%@ page import="com.lebo.entity.Post" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>帖子管理</title>
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

        .rate-form {
            background-color: #FFF;
            padding: 10px;
            width: 180px;
            border: 1px solid;
        }

        .rate-form input {
            margin: 0 0 0 5px;
            vertical-align: top;
        }

        .rate-form .close {
            margin: -10px 0 0 0;
        }

        .rate-form .muted, .rate-form .text-error {
            padding: 5px 0 0 5px;
        }

        .icon-pencil {
            cursor: pointer;
        }

        .rating-wrapper {
            width: 60px;
            text-align: left;
            padding-left: 30px;
        }

        .preview a {
            position: relative;
            display: block;
        }

        .preview .badge {
            position: absolute;
            top: 5px;
            right: 5px;
        }

        ul.moreinfo {
            list-style: none;
            margin: 10px 0 0 0;
        }

        .moreinfo-title {
            margin-left: 1em;
        }
    </style>
</head>
<body>

<c:if test="${controllerMethod == 'list'}">
    <form id="searchForm" class="form-search" method="GET" action="">

        <input type="search" class="input-medium search-query" id="screenName" name="screenName"
               value="${param.screenName}"
               placeholder="用户名字">
        <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
              onclick="$('input[name=screenName]').val('')" title="清除输入内容"></span>

        <input type="search" class="input-medium search-query" id="userId" name="userId" value="${param.userId}"
               placeholder="用户ID">
        <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
              onclick="$('input[name=userId]').val('')" title="清除输入内容"></span>

        <input type="search" class="input-medium search-query" id="track" name="track" value="${param.track}"
               placeholder="关键词">
        <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
              onclick="$('input[name=track]').val('')" title="清除输入内容"></span>

        <input type="search" class="input-medium search-query" id="postId" name="postId" value="${param.postId}"
               placeholder="帖子ID">
        <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
              onclick="$('input[name=postId]').val('')" title="清除输入内容"></span>

        <div style="padding-top: 15px;">
            从
            <input type="text" id="startDate" name="startDate" class="input-medium search-query" value="${startDate}"
                   class="form-inline" placeholder="起始日期"/>
            <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
                  onclick="$('input[name=startDate]').val('')" title="清除输入内容"></span>
            到
            <input type="text" id="endDate" name="endDate" class="input-medium search-query" value="${endDate}"
                   placeholder="结束日期"/>
            <span class="icon-remove" style="cursor: pointer; margin-left:-2em; margin-right: 2em;"
                  onclick="$('input[name=endDate]').val('')" title="清除输入内容"></span>
        </div>

        <div style="padding-top: 15px;">

            <select name="orderBy" class="input-small">
                <option value="<%=Post.ID_KEY%>" ${param.orderBy == "_id" ? "selected" : ""}>发布时间</option>
                <option value="favoritesCount" ${param.orderBy == "favoritesCount" ? "selected" : ""}>红心数</option>
                <option value="viewCount" ${param.orderBy == "viewCount" ? "selected" : ""}>播放数</option>
                <option value="rating" ${param.orderBy == "rating" ? "selected" : ""}>评分</option>
            </select>

            <select name="order" class="input-mini">
                <option value="DESC" ${param.order == "DESC" ? "selected" : ""}>降序</option>
                <option value="ASC" ${param.order == "ASC" ? "selected" : ""}>升序</option>
            </select>

            <input type="text" class="input-mini" name="size" value="${page.size}">条/页

            <button type="submit" class="btn" style="margin-left: 2em;">搜索</button>
        </div>
    </form>
</c:if>

<c:if test="${controllerMethod == 'hotPosts'}">
    <h2>热门帖子</h2>

    <p>最近 ${hotDays} 天的帖子按 <code>红心数+人气+评分</code> 排序，每用户最多上榜 ${maxHotPostCountPerUser} 条。</p>
</c:if>

<tags:pageinfo page="${page}" spentSeconds="${spentSeconds}"/>

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
        <th class="input-small">
            评分
        </th>
        <th style="width:6em;">
            操作
        </th>
    </tr>
    <c:forEach items="${posts}" var="item">
        <tr>

            <td></td>

            <td class="preview">
                <a href="${item.videoUrl}" target="_blank">
                    <img src="${item.videoFirstFrameUrl}"/>
                    <c:if test="${item.duration != null}">
                        <span class="badge badge-important">${item.duration}''</span>
                    </c:if>
                </a>
            </td>

            <td class="detail">

                <div class="authorDisplayName">
                    <a href="${ctx}/admin/user/update/${item.userId}">
                        <img class="authorPhoto" src="${item.profileImageUrl}" title="作者头像"/>
                        <span>${item.screenName}</span>
                    </a>

                    <span class="time" title="发布时间">${item.createdAt}</span>

                    <c:if test="${item.pvt}">
                        <span class="icon-lock pull-right" title="仅作者本人可见"></span>
                    </c:if>
                </div>

                <p class="content">${item.text}</p>

                <div class="info">
                    <label title="红心"><span class="icon-heart"></span>${item.favoritesCount}</label>
                    <label title="转发"><span class="icon-retweet"></span>${item.repostCount}</label>
                    <label title="播放"><span class="icon-play"></span>${item.viewCount}</label>
                    <label title="评论"><span class="icon-comment"></span>${item.commentCount}</label>
                    <label title="人气"><span class="icon-gift"></span>${item.popularity}</label>
                    <label title="红心+人气+评分"><span class="icon-star"></span><span
                            class="starValue">${item.favoritesCount + item.popularity + item.rating}</span></label>
                    <a href="javascript:void(0)" class="moreinfo-title" onclick="$('#moreinfo-${item.id}').toggle();">更多>></a>
                </div>

                <ul id="moreinfo-${item.id}" class="moreinfo" style="display:none">
                    <li>帖子ID: ${item.id}</li>
                    <li>用户ID: ${item.userId}</li>
                </ul>
            </td>

            <td title="评分" onclick="showRateInputForm($(this).find('.rating-wrapper'), '${item.id}')">
                <div class="rating-wrapper">
                    <span class="rating">${item.rating == null ? '<i>无</i>' : item.rating}</span>
                    <span class="icon-pencil" style="display: none;"></span>
                </div>
            </td>

            <td>
                <a href="${ctx}/admin/comment/list?postId=${item.id}" target="_blank">查看评论</a><br/>
                <a href="${ctx}/admin/robot/comment?postId=${item.id}" target="_blank">机器人评论</a>
                <input type="button" value="删除" class="btn btn-link" onclick="deletePost('${item.id}', this)"/>
            </td>

        </tr>
    </c:forEach>
</table>

<c:if test="${controllerMethod == 'list'}">
    <tags:pagination-normal page="${page}" paginationSize="5"/>
</c:if>

<script>
    $(function () {
        //行号
        $('#contentTable tr:gt(0)').each(function (index) {
            $('td:first', this).html(index + 1);
        });

        //评分编辑按钮显示/隐藏
        $('#contentTable tr')
                .mouseenter(function () {
                    $(this).find('.icon-pencil').show();
                })
                .mouseleave(function () {
                    $(this).find('.icon-pencil').hide();
                });

        //日期条件
        $("#startDate")
                .datepicker({
                    defaultDate: "-1w",
                    changeMonth: true,
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd",
                    onClose: function (selectedDate) {
                        $("#endDate").datepicker("option", "minDate", selectedDate);
                    }
                })
                .datepicker($.datepicker.regional["zh-CN"])
                .datepicker("option", "maxDate", '${endDate == null ? today : endDate}');

        $("#endDate")
                .datepicker({
                    defaultDate: "+0",
                    changeMonth: true,
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd",
                    onClose: function (selectedDate) {
                        $("#startDate").datepicker("option", "maxDate", selectedDate);
                    }
                })
                .datepicker($.datepicker.regional["zh-CN"])
                .datepicker("option", "minDate", '${startDate}')
                .datepicker("option", "maxDate", '${today}');
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

    //---- 修改评分 begin ----//

    function showRateInputForm(wrapper, postId) {

        var rating = wrapper.find('.rating').html();

        if (isNaN(rating)) rating = 0;

        //创建输入框和按钮
        var inputForm = $('<div class="rate-form">' +
                '<input type="text" class="input-mini" value="' + rating + '" placeholder="整数"/>' +
                '<input type="button" value="确定" class="btn btn-primary" title="确定 - 快捷键\'回车\'"/>' +
                '<button type="button" class="close" onclick="$(this).parent().remove()" title="关闭 - 快捷键\'ESC\'">&times;</button>' +
                '<div class="text-error" style="display: none"></div>' +
                '<div class="muted">输入正整数、负整数或0</div>' +
                '</div>')

                .appendTo(document.body)

                .offset($(wrapper).find('.icon-pencil').offset());

        $('.btn-primary', inputForm).click(function () {

            updateRating(postId, inputForm, wrapper);
        });

        //输入框获得焦点、键盘事件
        $('input[type=text]', inputForm)

                .focus()

                .keydown(function (e) {

                    if (e.keyCode == 13) {

                        updateRating(postId, inputForm, wrapper);

                    } else if (e.keyCode == 27) {

                        inputForm.remove();
                    }
                });
    }

    function updateRating(postId, inputForm, wrapper) {

        var newRating = $('input[type=text]', inputForm).val();

        if (newRating.match(/^-?\d+$/)) {

            newRating = parseInt(newRating);

        } else {

            $('.muted', inputForm).hide();

            $('.text-error', inputForm).html('输入正整数、负整数或0').show();

            $('input[type=text]', inputForm).focus();

            return;
        }

        $('.muted', inputForm).html('正在保存..').show();

        $('.text-error', inputForm).hide();

        $.ajax({

            url: '${ctx}/admin/post/updateRating',

            type: 'POST',

            data: {id: postId, rating: newRating},

            success: function (data) {

                if (data == 'ok') {

                    $('.rating', wrapper).html(newRating);

                    $(inputForm).remove();

                    updateStarValue(
                            $(wrapper).parents('tr'),
                            newRating);
                } else {

                    $('.muted', inputForm).hide();

                    $('.text-error', inputForm).html('保存失败').show();
                }
            },

            error: function () {

                $('.muted', inputForm).hide();

                $('.text-error', inputForm).html('保存失败').show();
            }
        });
    }

    function updateStarValue(tr, rating) {

        var favoritesCount = parseInt($(tr).find('label[title="红心"]').text());

        var popularity = parseInt($(tr).find('label[title="人气"]').text());

        $(tr).find('.starValue').html(favoritesCount + popularity + rating);
    }

    //---- 修改评分 end ----//

</script>

<c:if test="${controllerMethod == 'hotPosts'}">
    <h2>更新热门帖子</h2>
    <ul>
        <li>
            系统会定时更新热门帖子，通常情况下，你不需要执行此操作。
        </li>
        <li>
            如果你修改了帖子评分，想立刻看到效果，可以执行此操作。
        </li>
    </ul>

    <form action="${ctx}/admin/post/refreshHotPosts" method="POST">
        <input type="submit" value="立即更新热门帖子" class="btn"/>
    </form>
</c:if>

</body>
</html>
