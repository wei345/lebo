<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>推送通知</title>
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
    </style>
</head>

<body>

<h2>全网推送通知</h2>

<ul>
    <li>所有用户都会收到乐播应用内通知(消息页)</li>
    <li>iOS用户会收到苹果推送通知
        <ul>
            <li>系统推送一条通知需要 ${avgPushTimeSeconds} 秒</li>
            <li>系统每秒可推送 ${oneSecondPushCount} 条通知</li>
            <li>有些用户先收到通知，有些用户后收到，全部用户都收到通知可能需要几分钟至几小时</li>
        </ul>
    </li>
    <li>Android用户不会收到推送通知</li>
    <li>不要重复发同一通知</li>
</ul>

<br/>

<form id="publishForm" action="" method="post" enctype="multipart/form-data">

    <div class="control-group" title='这个参数是可选的'>
        <label class="control-label" for="image">发送者显示名:</label>

        <div class="controls">
            <input type="text" name="senderName" value="乐播团队" placeholder="发送者显示名"/>
        </div>
    </div>

    <div class="control-group" title='这个参数是可选的'>
        <label class="control-label" for="image">
            <input type="checkbox"
                   onclick="if(this.checked){this.form.image.removeAttribute('disabled');}else{this.form.image.setAttribute('disabled','disabled')}"/>
            图片(可选)
        </label>

        <div class="controls">
            <input type="file" id="image" name="image" placeholder="image"
                   value="" disabled='disabled'>
        </div>
    </div>

    <div>
        <textarea name="text" maxlength="140" placeholder="文字内容.." class="required"
                  style="width:540px;height:100px;">${text}</textarea>
    </div>

    <%--暂不做定时发布
    <div style="padding-top: 10px">
        <select id="timeOption" name="timeOption">
            <option value="now">现在发布</option>
            <option value="schedule">定时发布</option>
        </select>

        <div id="publishDateTimeInputGroup" style="display: none;">
            <input type="text" id="publishDate" name="publishDate" placeholder="年/月/日" style="width: 10em;"/>
            <input type="text" id="publishTime" name="publishTime" placeholder="时:分" style="width: 5em;"/>
        </div>
    </div>--%>

    <div>
        <input type="submit" value="发布" class="btn-primary btn"/>
    </div>
</form>

<h2>历史</h2>

<table id="todoTable" class="table table-hover">
    <tr>
        <th style="width:20px;">
            #
        </th>
        <th>
            内容
        </th>
        <th style="width: 50px;">
            发送数
        </th>
        <th style="width: 50px;">
            APNS
        </th>
        <th style="width: 100px;">
            发布者
        </th>
        <th style="width: 150px;">
            发布时间
        </th>
    </tr>
    <c:forEach items="${tasks}" var="task">
        <tr>
            <td></td>
            <td class="text">
                <div>
                    <c:if test="${task.senderUrl != null}">
                        <img style="width: 30px" src="${task.senderUrl}"/>
                    </c:if>
                        ${task.senderName}
                </div>

                <c:if test="${task.imageUrl != null}">
                    <img style="width: 100px" src="${task.imageUrl}"/>
                </c:if>
                    ${task.text}
            </td>
            <td>
                    ${task.count}
            </td>
            <td>
                    ${task.apnsCount}
            </td>
            <td class="screenName">
                    ${task.screenName}
            </td>
            <td class="scheduledAt">
                    ${task.createdAt}
            </td>
        </tr>
    </c:forEach>
</table>

<script>

    $(document).ready(function () {
        $("#publishForm").validate();

        /* 暂不做定时发布
         $('#timeOption').change(function () {
         if (this.value == 'schedule') {
         $('#publishDateTimeInputGroup').show().find('input[name=publishDate]').focus();
         } else {
         $('#publishDateTimeInputGroup').hide();
         }
         });*/

        $('#todoTable tr:gt(0)').each(function (index) {
            $('td:first', this).html(index + 1);
        });
    });

    /* 暂不做删除
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
     }*/
</script>


</body>
</html>