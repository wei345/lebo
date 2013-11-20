<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>广告管理</title>

    <script>
        $(document).ready(function () {
            //为inputForm注册validate函数
            $("#inputForm").validate();
            //回填下拉列表
            $('#group option[value=${ad.group}]').attr('selected', 'selected');
        });
    </script>
</head>

<body>

<a href="${ctx}/admin/ads">= 列表</a>

<form id="inputForm" action="${ctx}/admin/ads/update" method="post" class="form-horizontal"
      enctype="multipart/form-data">
    <c:if test="${ad.id != null}">
        <input type="hidden" name="id" value="${ad.id}"/>
    </c:if>
    <fieldset>
        <legend>
            <small>新建</small>
        </legend>

        <div class="control-group">
            <label for="group" class="control-label">组:</label>

            <div class="controls">
                <select id="group" name="group">
                    <option value="hot">热门</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label for="subject" class="control-label">主题:</label>

            <div class="controls">
                <input type="text" id="subject" name="subject" value="${ad.subject}" class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="description" class="control-label">描述:</label>

            <div class="controls">
                <textarea id="description" name="description" cols="80" rows="5" class="required">${ad.description}</textarea>
            </div>
        </div>

        <div class="control-group">
            <label for="image" class="control-label">
                <c:if test="${ad.id != null}">
                    <input type="checkbox"
                           onclick="if(this.checked){$('#image, #imageSlug').removeAttr('disabled')}else{$('#image, #imageSlug').attr('disabled', 'disabled')}"/>
                </c:if>
                图片:
            </label>

            <div class="controls">
                <input type="file" id="image" name="image" value="${image}" class="input-large required"
                       minlength="3" ${ad.id == null ? "" : "disabled"}/>
            </div>
        </div>

        <div class="control-group">
            <label for="imageSlug" class="control-label">图片 slug:</label>

            <div class="controls">
                <input type="text" id="imageSlug" name="imageSlug" value="${imageSlug}"
                       class="input-large required" ${ad.id == null ? "" : "disabled"}/>
                由小写字母、数字、连字符(-)组成
            </div>
        </div>

        <div class="control-group">
            <label for="url" class="control-label">点击广告跳转到:</label>

            <div class="controls">

                <input type="text" id="url" name="url" value="${ad.url}" class="input-large required"/>

                <div class="btn-group" style="vertical-align: top;">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <sapn id="urlSelected">选择</sapn>
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li><a href="#" onclick="setUrlPrefix('http://www.example.com', '', false, this)">HTTP地址</a>
                        <li><a href="#" onclick="setUrlPrefix('', 'lebo://channels/{频道名}', false, this)">乐播频道</a>
                        <li><a href="#" onclick="setUrlPrefix('', 'lebo://fastest-rising-and-top50', true, this)">上升最快 & Top50</a>
                    </ul>
                </div>

            </div>
        </div>

        <div class="control-group">
            <label for="order" class="control-label">顺序:</label>

            <div class="controls">
                <input type="text" id="order" name="order" value="${ad.order}" class="input-large required"/>
                任意整数。列表按此字段排序，由小到大.
            </div>
        </div>

        <div class="control-group">
            <label for="enabled" class="control-label">启用:</label>

            <div class="controls">
                <input type="checkbox" id="enabled" name="enabled" ${ad.enabled == null ? "" : "checked='checked'"}/>
            </div>
        </div>

        <div class="form-actions">
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
        </div>
    </fieldset>
</form>

<script>
    function setUrlPrefix(placeholder, val, readonly, option){
        $('#url').val(val).attr('readonly', readonly).attr('placeholder', placeholder);
        $('#urlSelected').html(option.innerHTML);
    }
</script>
</body>
</html>