<%@ page import="com.lebo.service.TaskService" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>发布视频</title>
</head>

<body>

<h2>发布视频</h2>

<form id="publishVideoForm" action="${ctx}/admin/tasks/publish-video" method="post" enctype="multipart/form-data">

    <div>
        发布到:<br/>
        <input name="screenName" type="text" value="${param.screenName}" placeholder="用户名字" class="required"/>
    </div>

    <div style="margin-top: 1em;">
        视频:<br/>
        <input type="file" name="video"/>
    </div>

    <div>
        <textarea name="text" maxlength="140" placeholder="描述.." class="required"
                  style="width:540px;height:100px;">${param.text}</textarea>
    </div>

    <div style="padding-top: 10px">
        <select id="timeOption" name="timeOption">
            <option value="now">现在发布</option>
            <option value="schedule">定时发布</option>
        </select>

        <div id="publishDateTimeInputGroup" style="display: none;">
            <input type="text" id="publishDate" name="publishDate" placeholder="年/月/日" style="width: 10em;"/>
            <input type="text" id="publishTime" name="publishTime" placeholder="时:分" style="width: 5em;"/>
        </div>
    </div>

    <div>
        <input type="submit" value="发布" class="btn-primary btn"/>
    </div>
</form>

<script>

    $(document).ready(function () {
        $("#publishVideoForm").validate({
            rules: {
                video: {
                    required: true,
                    regex: ".+\\.<%= TaskService.UPLOAD_VIDEO_EXT %>"
                },
                publishDate: {
                    required: true,
                    regex: "\\d{4}/\\d{2}/\\d{2}"
                },
                publishTime: {
                    required: true,
                    regex: "\\d{2}:\\d{2}"
                }
            },
            messages: {
                video: {
                    regex: "视频格式不对，只支持 <%= TaskService.UPLOAD_VIDEO_EXT %>"
                },
                publishDate: {
                    regex: "日期格式不正确"
                },
                publishTime: {
                    regex: "时间格式不正确"
                }
            },
            submitHandler: function (form) {
                var submitButton = $('[type=submit]', form).attr("disabled", true);
                setTimeout(function () {
                    submitButton.val('正在上传文件，请等待..');
                }, 600);
                form.submit();
            }
        });

        $('#timeOption').change(function () {
            if (this.value == 'schedule') {
                $('#publishDateTimeInputGroup').show().find('input[name=publishDate]').focus();
            } else {
                $('#publishDateTimeInputGroup').hide();
            }
        });
    });
</script>


</body>
</html>