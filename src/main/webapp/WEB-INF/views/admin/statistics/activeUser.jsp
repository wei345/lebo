<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>日活跃用户统计</title>
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

<form id="searchForm" action="">
    从
    <input type="text" id="start" name="start" class="input-medium" value="${start}" class="form-inline"/>
    到
    <input type="text" id="end" name="end" class="input-medium" value="${end}"/>

    <div class="btn-group" style="vertical-align: top;">
        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
            <sapn id="token-name">选择</sapn>
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
            <li><a href="#" onclick="recentDateRange(7)">最近 1 周</a>
            <li><a href="#" onclick="recentDateRange(14)">最近 2 周</a>
            <li><a href="#" onclick="recentDateRange(30)">最近 1 个月</a>
        </ul>
    </div>

    <input type="submit" class="btn" value="查询" style="margin-left: 20px;"/>
</form>

<div id="line-chart" style="min-width: 400px; height: 300px; margin: 0 auto"></div>

<script>

    new Highcharts.Chart({
        chart: {
            renderTo: 'line-chart',
            type: 'line',
            marginRight: 130,
            marginBottom: 80
        },
        title: {
            text: '日活跃用户数',
            x: -20 //center
        },
        subtitle: {
            text: '${start} -- ${end}',
            x: -20
        },
        xAxis: {
            categories: [<c:forEach items="${list}" var="item" varStatus="status">'${fn:replace(fn:substringAfter(item.id, "-"), "-", ".")}<c:if test="${item.id == today}"> (今天)</c:if>'<c:if test="${!status.last}">, </c:if></c:forEach>].reverse()
        },
        yAxis: {
            min: 0,
            allowDecimals: false,
            title: {
                text: '数量(人)'
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
                        this.x.replace('.', '月') + '日: ' + this.y + '人';
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
                name: '日活跃用户',
                data: [<c:forEach items="${list}" var="item" varStatus="status">${item.total}<c:if test="${!status.last}">, </c:if></c:forEach>].reverse()
            }
        ]
    });
</script>

<c:forEach items="${list}" var="item">

    <hr style="margin: 1em 0"/>

    <div id="pie-chart-${item.id}" style="min-width: 400px; height: 400px; margin: 0 auto"></div>

    <script>
        $('#pie-chart-${item.id}').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false
            },
            title: {
                text: '日活跃用户活跃天数分布 ${item.id}<c:if test="${item.id == today}"> (今天)</c:if>'
            },
            subtitle: {
                text: '统计第 ${item.statisticsDays} 天'
            },
            tooltip: {
                pointFormat: '用户数量: <b>{point.y}</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %'
                    }
                }
            },
            series: [{
                type: 'pie',
                name: '比例',
                data: [
                    {
                        name: '1',
                        y: ${item.days1 + 0},
                        sliced: true,
                        selected: true
                    }
                    <c:if test="${item.days2 != null}">,['2',   ${item.days2}]</c:if>
                    <c:if test="${item.days3 != null}">,['3',   ${item.days3}]</c:if>
                    <c:if test="${item.days4 != null}">,['4',   ${item.days4}]</c:if>
                    <c:if test="${item.days5 != null}">,['5',   ${item.days5}]</c:if>
                    <c:if test="${item.days6To10 != null}">,['6-10',   ${item.days6To10}]</c:if>
                    <c:if test="${item.days11To20 != null}">,['11-20',   ${item.days11To20}]</c:if>
                    <c:if test="${item.days21To50 != null}">,['21-50',   ${item.days21To50}]</c:if>
                    <c:if test="${item.days51To100 != null}">,['51-100',   ${item.days51To100}]</c:if>
                    <c:if test="${item.days101To3000 != null}">,['101-3000',   ${item.days101To3000}]</c:if>
                ]
            }]
        });
    </script>

</c:forEach>


<script>

    $(function () {
        //日期条件
        $("#start")
                .datepicker({
                    defaultDate: "-1w",
                    changeMonth: true,
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd",
                    onClose: function (selectedDate) {
                        $("#end").datepicker("option", "minDate", selectedDate);
                    }
                })
                .datepicker($.datepicker.regional["zh-CN"])
                .datepicker("option", "maxDate", '${end == null ? today : end}');

        $("#end")
                .datepicker({
                    defaultDate: "+0",
                    changeMonth: true,
                    numberOfMonths: 2,
                    dateFormat: "yy-mm-dd",
                    onClose: function (selectedDate) {
                        $("#start").datepicker("option", "maxDate", selectedDate);
                    }
                })
                .datepicker($.datepicker.regional["zh-CN"])
                .datepicker("option", "minDate", '${start}')
                .datepicker("option", "maxDate", '${today}');
    });

    function recentDateRange(amount){
        var oneDayMills = 1000 * 60 * 60 * 24;
        var today = new Date();

        var end = today;
        $("#end").val(end.getFullYear() + "-" + (end.getMonth() + 1) + "-" + end.getDate());

        var start = new Date(today.getTime() - (oneDayMills * amount));
        $("#start").val(start.getFullYear() + "-" + (start.getMonth() + 1) + "-" + start.getDate());
    }

</script>
</body>
</html>