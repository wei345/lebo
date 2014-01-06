<%@ page import="com.lebo.service.TaskService" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>发布视频</title>
    <style type="text/css">
        #todoTable th {
            text-align: center;
        }

        #todoTable td {
            vertical-align: middle;
            text-align: center;
        }

        #todoTable .text {
            width: 400px;
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
    </style>
</head>

<body>

<h2>发布视频</h2>

<form id="publishVideoForm" action="${ctx}/admin/task/publish-video" method="post" enctype="multipart/form-data">

    <div>
        发布到:<br/>
        <input name="screenName" type="text" value="${screenName}" placeholder="用户名字" class="required"/>
    </div>

    <div style="margin-top: 1em;">
        视频:<br/>
        <input type="file" name="video"/>
    </div>

    <div>
        <textarea name="text" maxlength="140" placeholder="描述.." class="required"
                  style="width:540px;height:100px;">${text}</textarea>
    </div>

    <div style="padding-top: 10px">
        <select id="timeOption" name="timeOption">
            <option value="now">现在发布</option>
            <option value="schedule"<c:if test="${publishDate != null}"> selected="selected"</c:if>>定时发布</option>
        </select>

        <div id="publishDateTimeInputGroup" style="display: none;">
            <input type="text" id="publishDate" name="publishDate" value="${publishDate}" placeholder="年/月/日" style="width: 10em;"/>
            <input type="text" id="publishTime" name="publishTime" value="${publishTime}" placeholder="时:分" style="width: 5em;"/>
        </div>
    </div>

    <div>
        <input type="submit" value="发布" class="btn-primary btn"/>
    </div>
</form>

<h2>定时</h2>

<table id="todoTable" class="table table-hover">
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
            发布到
        </th>
        <th>
            发布时间
        </th>
        <th>
            操作
        </th>
    </tr>
    <c:forEach items="${tasks}" var="task">
        <tr>
            <td></td>
            <td class="preview">
                <a href="${task.videoUrl}">
                    <img src="${task.videoFirstFrameUrl}"/>
                    <c:if test="${task.duration != null}">
                        <span class="badge badge-important">${task.duration}''</span>
                    </c:if>
                </a>
            </td>
            <td class="text">
                    ${task.text}
            </td>
            <td class="screenName">
                    ${task.screenName}
            </td>
            <td class="scheduledAt">
                    ${task.scheduledAt}
            </td>
            <td>
                <input type="button" value="删除" class="btn btn-danger" onclick="deleteTask('${task.id}', this)"/>
            </td>
        </tr>
    </c:forEach>
</table>

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
            timeOptionOnChange();
            $('#publishDateTimeInputGroup input:first').focus();
        });

        $('#todoTable tr:gt(0)').each(function (index) {
            $('td:first', this).html(index + 1);
        });

        //回填时间
        timeOptionOnChange();
    });

    function timeOptionOnChange(){
        if ($('#timeOption').val() == 'schedule') {
            $('#publishDateTimeInputGroup')
                    .show()
                    .find('input')
                    .removeAttr('disabled');
        } else {
            $('#publishDateTimeInputGroup')
                    .hide()
                    .find('input')
                    .attr('disabled', 'disabled');
        }
    }

    function deleteTask(id, btn) {
        if (confirm('确定删除吗?')) {
            $.ajax({
                url: '${ctx}/admin/task/publish-video/delete/' + id,
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