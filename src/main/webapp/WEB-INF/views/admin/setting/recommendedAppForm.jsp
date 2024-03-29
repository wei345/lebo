<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>推荐应用</title>

    <style>
        .inline {
            display: inline-block;
        }

        div.input-large {
            padding: 4px 6px;
        }
    </style>

    <script>
        $(document).ready(function () {
            //聚焦第一个输入框
            $("#name").focus();
            //为inputForm注册validate函数
            $("#inputForm").validate();
            //回填下拉列表
            $('select#type option[value=${app.type}]').attr('selected', true);
        });
    </script>
</head>

<body>

<a href="${ctx}/admin/recommendedApps">= 列表</a>

<form id="inputForm" action="${ctx}/admin/recommendedApps/update" method="post" class="form-horizontal"
      enctype="multipart/form-data">
    <c:if test="${app.id != null}">
        <input type="hidden" name="id" value="${app.id}"/>
    </c:if>
    <fieldset>
        <legend>
            <small>新建</small>
        </legend>

        <div class="control-group">
            <label for="type" class="control-label">应用类型:</label>

            <div class="controls">
                <select name="type" id="type">
                    <option value="ios">ios</option>
                    <option value="android">android</option>
                </select>
            </div>
        </div>

        <div class="control-group">
            <label for="name" class="control-label">应用名:</label>

            <div class="controls">
                <input type="text" id="name" name="name" value="${app.name}" class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="version" class="control-label">版本:</label>

            <div class="controls">
                <input type="text" id="version" name="version" value="${app.version}"
                       class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="description" class="control-label">描述:</label>

            <div class="controls">
                <textarea id="description" name="description" cols="80" rows="5">${app.description}</textarea>
            </div>
        </div>

        <div class="control-group">
            <label for="url" class="control-label">URL:</label>

            <div class="controls">
                <input type="text" id="url" name="url" value="${app.url}" class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">直接下载:</label>

            <div class="controls">
                <div class="input-large inline">
                    <input type="radio" name="directDownload" id="directDownload-true"
                           value="true" ${app.directDownload ? "checked" : ""}>
                    <label for="directDownload-true" class="inline">是</label>

                    <input type="radio" name="directDownload" id="directDownload-false"
                           value="false" ${app.directDownload ? "" : "checked"}>
                    <label for="directDownload-false" class="inline">否</label>
                </div>

                上面的URL是否是直接下载地址
            </div>
        </div>

        <div class="control-group">
            <label for="size" class="control-label">大小:</label>

            <div class="controls">
                <input type="text" id="size" name="size" value="${app.size}" class="input-large required"/>
                如: 12.3M
            </div>
        </div>

        <div class="control-group">
            <label for="image" class="control-label">
                <c:if test="${app.id != null}">
                    <input type="checkbox"
                           onclick="if(this.checked){$('#image, #imageSlug').removeAttr('disabled')}else{$('#image, #imageSlug').attr('disabled', 'disabled')}"/>
                </c:if>
                图片:
            </label>

            <div class="controls">
                <input type="file" id="image" name="image" value="${image}" class="input-large required"
                       minlength="3" ${app.id == null ? "" : "disabled"}/>
            </div>
        </div>

        <div class="control-group">
            <label for="imageSlug" class="control-label">图片 slug:</label>

            <div class="controls">
                <input type="text" id="imageSlug" name="imageSlug" value="${app.imageSlug}"
                       class="input-large required" ${app.id == null ? "" : "disabled"}/>
                由小写字母、数字、连字符(-)组成
            </div>
        </div>

        <div class="control-group">
            <label for="backgroundColor" class="control-label">背景颜色:</label>

            <div class="controls">
                <input type="text" id="backgroundColor" name="backgroundColor" value="${app.backgroundColor}"
                       class="input-large"/>
                如#abcdef, 不能使用颜色名
            </div>
        </div>

        <div class="control-group">
            <label for="order" class="control-label">顺序:</label>

            <div class="controls">
                <input type="text" id="order" name="order" value="${app.order}" class="input-large required"/>
                任意整数。列表按此字段排序，由小到大.
            </div>
        </div>

        <div class="control-group">
            <label for="enabled" class="control-label">启用:</label>

            <div class="controls">
                <input type="checkbox" id="enabled" name="enabled" ${app.enabled == null ? "" : "checked='checked'"}/>
            </div>
        </div>

        <div class="form-actions">
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
        </div>
    </fieldset>
</form>
</body>
</html>