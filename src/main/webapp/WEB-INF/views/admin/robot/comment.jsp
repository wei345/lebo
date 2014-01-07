<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>机器人评论</title>
    <style type="text/css">
        .multi-select-left {
            float: left;
        }

        .multi-select-left .title, .multi-select-right .title {
            display: block;
        }

        .multi-select-btn {
            width: 3em;
            padding: 2em;
            float: left;
        }

        .multi-select-btn input[type=button] {
            margin-top: 0.5em;
        }
    </style>
    <script>
        $(document).ready(function () {
            <c:if test="${param.postId == null}">
            //聚焦第一个输入框
            $("#postId").focus();
            </c:if>

            $('#submitBtn').hide();

            //为inputForm注册validate函数
            $("#inputForm").validate();

            //双向选择列表双击事件
            $('#robots-unchosen').dblclick(function () {
                multiSelectAdd(this, '#robots-chosen');
            });

            $('#robots-chosen').dblclick(function () {
                multiSelectAdd(this, '#robots-unchosen');
            });

            $('#sayings-unchosen').dblclick(function () {
                multiSelectAdd(this, '#sayings-chosen');
            });

            $('#sayings-chosen').dblclick(function () {
                multiSelectAdd(this, '#sayings-unchosen');
            });
        });
    </script>
</head>
<body>

<h3>帖子</h3>

<form id="inputForm" action="${ctx}/admin/robot/comment" method="POST">
    <input type="text" id="postId" name="postId" value="${postId}" placeholder="帖子ID"
           class="required input-xlarge"/>
    <input type="hidden" name="comments" value=""/>
</form>

<h3>机器人</h3>

<div class="multi-select-left">
    <span class="title">候选:</span>
    <select id="robots-unchosen" size="10" multiple="multiple">
        <c:forEach items="${robots}" var="item">
            <option value="${item.id}">${item.screenName}</option>
        </c:forEach>
    </select>
</div>

<div class="multi-select-btn">
    <input type="button" value="&gt;" class="btn"
           onclick="multiSelectAdd($('#robots-unchosen'),$('#robots-chosen'))"/>

    <input type="button" value="&gt;&gt;" class="btn"
           onclick="multiSelectAddAll($('#robots-unchosen'),$('#robots-chosen'))"/>

    <input type="button" value="&lt;" class="btn"
           onclick="multiSelectAdd($('#robots-chosen'),$('#robots-unchosen'))"/>

    <input type="button" value="&lt;&lt;" class="btn"
           onclick="multiSelectAddAll($('#robots-chosen'),$('#robots-unchosen'))"/>
</div>

<div class="multi-select-right">
    <span class="title">已选:</span>
    <select id="robots-chosen" name="robots-chosen" size="10" multiple="multiple">
    </select>
</div>


<h3>语句</h3>

<div class="multi-select-left">
    <span class="title">候选:</span>
    <select id="sayings-unchosen" size="10" multiple="multiple">
        <c:forEach items="${sayings}" var="item">
            <option value="${item.id}">${item.text}</option>
        </c:forEach>
    </select>
</div>

<div class="multi-select-btn">
    <input type="button" value="&gt;" class="btn"
           onclick="multiSelectAdd($('#sayings-unchosen'),$('#sayings-chosen'))"/>

    <input type="button" value="&gt;&gt;" class="btn"
           onclick="multiSelectAddAll($('#sayings-unchosen'),$('#sayings-chosen'))"/>

    <input type="button" value="&lt;" class="btn"
           onclick="multiSelectAdd($('#sayings-chosen'),$('#sayings-unchosen'))"/>

    <input type="button" value="&lt;&lt;" class="btn"
           onclick="multiSelectAddAll($('#sayings-chosen'),$('#sayings-unchosen'))"/>
</div>

<div class="multi-select-right">
    <span class="title">已选:</span>
    <select id="sayings-chosen" name="sayings-chosen" size="10" multiple="multiple">
    </select>
</div>

<h3>时间</h3>

<input id="minutes" type="text" value="5" class="input-mini"/>分钟内，每个的机器人各发1条评论

<h3>预览</h3>

<input type="button" value="生成预览" class="btn" onclick="preview()"/>

<table id="previewTable" class="table table-condensed table-hover">

</table>

<div class="form-actions">
    <input id="submitBtn" type="button" class="btn btn-primary" value="提交" onclick="submit()"/>
</div>


<script>
    /**
     * @param date
     * @returns string 格式化日期 e.g. "Mon Jan 06 12:35:13 +0800 2014"
     */
    function formatDate(date) {

        var weekdaysShort = "Sun_Mon_Tue_Wed_Thu_Fri_Sat".split("_");
        var monthsShort = "Jan_Feb_Mar_Apr_May_Jun_Jul_Aug_Sep_Oct_Nov_Dec".split("_");

        return weekdaysShort[date.getDay()] + " "
                + monthsShort[date.getMonth()] + " "
                + twoNum(date.getDate()) + " "
                + twoNum(date.getHours()) + ":"
                + twoNum(date.getMinutes()) + ":"
                + twoNum(date.getSeconds()) + " "
                + zzzz(date) + " "
                + date.getFullYear();
    }

    function twoNum(n) {
        return n < 10 ? ("0" + n) : n;
    }

    function zzzz(date) {
        var minutes = date.getTimezoneOffset();
        var tz;

        if (minutes > 0) {
            tz = "-";
        } else {
            tz = "+";
            minutes = minutes * -1;
        }

        tz += twoNum(Math.floor(minutes / 60));

        tz += twoNum(minutes % 60);

        return tz;
    }

    function multiSelectAdd(source, target) {
        $(source).find('option:selected').appendTo(target);
    }

    function multiSelectAddAll(source, target) {
        $(source).find('option').appendTo(target);
    }

    function preview() {
        var submitBtn = $('#submitBtn').hide();

        var robots = $('#robots-chosen option');
        var sayings = $('#sayings-chosen option');
        var minutes = $('#minutes').val() || 5;
        var mills = minutes * 60 * 1000;
        var startTime = new Date();
        var sayingsIndex = 0;
        var table = $('#previewTable');

        table.empty();

        if (robots.length == 0) {
            table.append('<tr class="error"><td>请选择机器人</td></tr>');
            return;
        }

        if (sayings.length == 0) {
            table.append('<tr class="error"><td>请选择语句</td></tr>');
            return;
        }

        robots.each(function (index, item) {
            var tr = $('<tr><td></td><td></td><td></td></tr>');

            var time = new Date(startTime.getTime() + Math.floor(((index + 1) / robots.length) * mills));
            var robot = $(item);
            var saying = sayings.eq(sayingsIndex++).html();
            if (sayingsIndex == sayings.length) {
                sayingsIndex = 0;
            }

            tr.find('td:first').append('<input value="' + formatDate(time) + '" type="text" class="time input-large"/>');
            tr.find('td:eq(1)').append('<span userid="' + robot.val() + '" class="user">' + robot.html() + '</span>');
            tr.find('td:eq(2)').append('说: <input value="' + saying + '" type="text" class="text input-xxlarge"/>');

            table.append(tr);
        });

        if (table.find('tr').length > 0) {
            submitBtn.show();
        }
    }

    function submit() {
        var form = $('#inputForm');

        var comments = [];
        $('#previewTable tr').each(function (index, item) {
            comments.push({
                time: $(item).find('.time').val(),
                userId: $(item).find('.user').attr('userid'),
                text: $(item).find('.text').val()
            });
        });

        form[0].comments.value = JSON.stringify(comments);
        form.submit();
    }
</script>

</body>
</html>