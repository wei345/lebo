<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<a href="${ctx}/admin/channels/create" class="pull-right">新建频道</a>

<ul class="nav nav-tabs" id="myTab">
    <li class="active"><a href="#preview">预览</a></li>
    <li><a href="#edit">编辑</a></li>
</ul>

<form id="channelsForm" method="POST" action="${ctx}/admin/channels/update">
    <div class="tab-content">
        <div class="tab-pane active" id="preview">
            <%@include file="channelPreview.jsp" %>
        </div>
        <div class="tab-pane" id="edit">
            <textarea name="channels" style="width: 800px; height: 300px">${channelsJson}</textarea>
        </div>
    </div>
    <div>
        <button type="reset" class="btn">重置</button>
        <button type="submit" class="btn">保存</button>
    </div>
</form>

<script>
    $(function () {
        $('#myTab a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });

        $('a[href=#preview]').on('show', function (e) {
            $('#preview').html('加载中..');
            $.ajax({
                type: 'POST',
                url: '${ctx}/admin/channels/preview',
                data: $('#channelsForm').serialize(),
                success: function (html) {
                    $('#preview').html(html);
                }
            });
        });
    });

</script>

