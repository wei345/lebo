<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>统计图表</title>
    <script src="${ctx}/static/highcharts/3.0.7/highcharts.js"></script>
    <style>
        #searchForm{
            text-align: center;
        }
        #searchForm .dropdown-menu{
            text-align: left;
        }
        #searchForm input {
            margin: 0
        }
    </style>
</head>
<body>

<h2>即时通讯</h2>

<form id="searchForm" action="">
    从
    <input type="text" id="startDate" name="startDate" class="input-medium" value="${startDate}" class="form-inline"/>
    到
    <input type="text" id="endDate" name="endDate" class="input-medium" value="${endDate}"/>

    <div class="btn-group" style="vertical-align: top;">
        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
            <sapn id="token-name">选择</sapn>
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
            <li><a href="#" onclick="recentDateRange(7)">最近 1 周</a>
            <li><a href="#" onclick="recentDateRange(30)">最近 1 个月</a>
            <li><a href="#" onclick="recentDateRange(30)">最近 2 个月</a>
        </ul>
    </div>

    <input type="submit" class="btn" value="查询" style="margin-left: 20px;"/>
</form>

<div id="daily-ims-chart" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

<script>
    var chart = new Highcharts.Chart({
        chart: {
            renderTo: 'daily-ims-chart',
            type: 'line',
            marginRight: 130,
            marginBottom: 40
        },
        title: {
            text: '每日即时通讯统计',
            x: -20 //center
        },
        subtitle: {
            text: '${startDate} -- ${endDate}',
            x: -20
        },
        xAxis: {
            categories: [<c:forEach items="${dailyList}" var="item" varStatus="status">'<fmt:formatDate type="date" value="${item.statisticsDate}" pattern="M.d"/>'<c:if test="${!status.last}">, </c:if></c:forEach>]
        },
        yAxis: {
            min: 0,
            allowDecimals: false,
            title: {
                text: '数量'
            },
            plotLines: [
                {
                    value: 0,
                    width: 1,
                    color: '#808080'
                }
            ]
        },
        tooltip: {
            formatter: function () {
                return '<b>' + this.series.name + '</b><br/>' +
                        this.x.replace('.', '月') + '日: ' + this.y + '条';
            }
        },
        legend: {
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },
        series: [
            {
                name: '全部消息',
                data: [<c:forEach items="${dailyList}" var="item" varStatus="status">${item.imCount}<c:if test="${!status.last}">, </c:if></c:forEach>]
            },
            {
                name: '视频消息',
                data: [<c:forEach items="${dailyList}" var="item" varStatus="status">${item.imVideoCount}<c:if test="${!status.last}">, </c:if></c:forEach>]
            },
            {
                name: '语音消息',
                data: [<c:forEach items="${dailyList}" var="item" varStatus="status">${item.imAudioCount}<c:if test="${!status.last}">, </c:if></c:forEach>]
            },
            {
                name: '文字消息',
                data: [<c:forEach items="${dailyList}" var="item" varStatus="status">${item.imTextCount}<c:if test="${!status.last}">, </c:if></c:forEach>]
            },
            {
                name: '发消息的用户',
                data: [<c:forEach items="${dailyList}" var="item" varStatus="status">${item.imFromUserCount}<c:if test="${!status.last}">, </c:if></c:forEach>]
            }
        ]
    });

    $(function () {
        $("#startDate").datepicker({
            defaultDate: "-1w",
            changeMonth: true,
            numberOfMonths: 3,
            dateFormat: "yy-mm-dd",
            onClose: function (selectedDate) {
                $("#endDate").datepicker("option", "minDate", selectedDate);
            }
        });
        $("#endDate").datepicker({
            defaultDate: "+0",
            changeMonth: true,
            numberOfMonths: 3,
            dateFormat: "yy-mm-dd",
            onClose: function (selectedDate) {
                $("#startDate").datepicker("option", "maxDate", selectedDate);
            }
        });
    });

    function recentDateRange(amount){
        var oneDayMills = 1000 * 60 * 60 * 24;
        var today = new Date();

        var yestoday = new Date(today.getTime() - oneDayMills);
        $("#endDate").val(yestoday.getFullYear() + "-" + (yestoday.getMonth() + 1) + "-" + yestoday.getDate());

        var startDate = new Date(today.getTime() - (oneDayMills * amount));
        $("#startDate").val(startDate.getFullYear() + "-" + (startDate.getMonth() + 1) + "-" + startDate.getDate());
    }

</script>
</body>
</html>