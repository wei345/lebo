<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>机器人自动喜欢</title>
    <style type="text/css">
        .btn-group, #robotAutoFavoriteEnable {
            vertical-align: top;
        }

        .group {
            margin-top: 1.5em;
        }

        .input-append {
            display: inline-block;
            vertical-align: top;
        }
    </style>
    <script>
        $(function () {
            $('#robotAutoFavoriteRobotGroup').val('${setting.robotAutoFavoriteRobotGroup}');
        });
    </script>
</head>

<body>

<form method="POST" action="">

    <div class="group">
        <label>
            <input type="checkbox" value="true" id="robotAutoFavoriteEnable"
                   name="robotAutoFavoriteEnable" ${setting.robotAutoFavoriteEnable ? "checked='checked'" : ""}/>
            启用
        </label>

        当新帖子发布时，

        <br/>

        从
        <select class="input-small" title="机器人组" id="robotAutoFavoriteRobotGroup" name="robotAutoFavoriteRobotGroup"
                class="robotAutoFavoriteRobotGroup" value="${setting.robotAutoFavoriteRobotGroup}">
            <option value="">全部 (${robotTotalCount})</option>
            <c:forEach items="${groups}" var="group">
                <option value="${group.name}">${group.name} (${group.memberCount})</option>
            </c:forEach>
        </select>
        机器人中，

        <br/>

        随机选取<input type="text" class="input-mini" id="robotAutoFavoriteRobotCountFrom"
                   name="robotAutoFavoriteRobotCountFrom" value="${setting.robotAutoFavoriteRobotCountFrom}"/>
        - <input type="text" class="input-mini" id="robotAutoFavoriteRobotCountTo" name="robotAutoFavoriteRobotCountTo"
                 value="${setting.robotAutoFavoriteRobotCountTo}"/>个，

        <br/>

        在
        <div class="input-append">
            <input type="text" class="input-mini" id="robotAutoFavoriteTimeInMinuteFrom"
                   name="robotAutoFavoriteTimeInMinuteFrom" value="${setting.robotAutoFavoriteTimeInMinuteFrom}"/>

            <div class="btn-group">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <sapn id="token-name">选择</sapn>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteFrom').val(60)">60 (1小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteFrom').val(240)">240 (4小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteFrom').val(480)">480 (8小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteFrom').val(1440)">1440 (1天)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteFrom').val(2880)">2880 (2天)</a>
                </ul>
            </div>
        </div>

        -

        <div class="input-append">
            <input type="text" class="input-mini" id="robotAutoFavoriteTimeInMinuteTo"
                   name="robotAutoFavoriteTimeInMinuteTo" value="${setting.robotAutoFavoriteTimeInMinuteTo}"/>

            <div class="btn-group">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <sapn id="token-name">选择</sapn>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteTo').val(60)">60 (1小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteTo').val(240)">240 (4小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteTo').val(480)">480 (8小时)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteTo').val(1440)">1440 (1天)</a>
                    <li><a href="javascript:$('#robotAutoFavoriteTimeInMinuteTo').val(2880)">2880 (2天)</a>
                </ul>
            </div>
        </div>
        分钟内喜欢该帖子。
    </div>

    <%-- 不好实现
    <div class="group">
        <label>
            <input type="checkbox" id="robotAutoFavoriteNightNotDisturb" name="robotAutoFavoriteNightNotDisturb" ${setting.robotAutoFavoriteNightNotDisturb ? "checked='checked'" : ""}/>
            夜间休息，不打扰用户
        </label>

        休息时间：
        从<select id="robotAutoFavoriteRestTimeFromHour" name="robotAutoFavoriteRestTimeFromHour" class="input-mini">
        <%
            for (int i = 0; i < 24; i++) {
                out.println("<option value='" + i + "'>" + i + "</option>");
            }
        %>
    </select>
        :
        <select id="robotAutoFavoriteRestTimeFromMinute" name="robotAutoFavoriteRestTimeFromMinute" class="input-mini">
            <%
                for (int i = 0; i < 60; i++) {
                    out.println("<option value='" + i + "'>" + i + "</option>");
                }
            %>
        </select>
        至
        <select id="robotAutoFavoriteRestTimeToHour" name="robotAutoFavoriteRestTimeToHour" class="input-mini">
            <%
                for (int i = 0; i < 24; i++) {
                    out.println("<option value='" + i + "'>" + i + "</option>");
                }
            %>
        </select>
        :
        <select id="robotAutoFavoriteRestTimeToMinute" name="robotAutoFavoriteRestTimeToMinute" class="input-mini">
            <%
                for (int i = 0; i < 60; i++) {
                    out.println("<option value='" + i + "'>" + i + "</option>");
                }
            %>
        </select>
        <script>
        $(function(){
            $('#robotAutoFavoriteRestTimeFromHour').val('${setting.robotAutoFavoriteRestTimeFromHour}');
            $('#robotAutoFavoriteRestTimeFromMinute').val('${setting.robotAutoFavoriteRestTimeFromMinute}');
            $('#robotAutoFavoriteRestTimeToHour').val('${setting.robotAutoFavoriteRestTimeToHour}');
            $('#robotAutoFavoriteRestTimeToMinute').val('${setting.robotAutoFavoriteRestTimeToMinute}');
        });
    </script>
    </div>
    --%>

    <div class="group">
        <button type="reset" class="btn">重置</button>
        <button type="submit" class="btn btn-primary">提交</button>
    </div>

</form>

</body>
</html>
