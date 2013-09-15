<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<form id="inputForm" action="${ctx}/admin/settings" method="post" class="form-horizontal">
    <fieldset>
        <legend>
            <small>设置</small>
        </legend>

        <div class="control-group">
            <label for="officialAccountId" class="control-label">官方账号ID:</label>

            <div class="controls">
                <input type="text" id="officialAccountId" name="officialAccountId" value="${setting.officialAccountId}"
                       class="input-large required"/>
            </div>
        </div>
        <div class="control-group">
            <label for="digestDays" class="control-label">"精华"几天内的内容:</label>

            <div class="controls">
                <input type="text" id="digestDays" name="digestDays" value="${setting.digestDays}"
                       class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label for="digestDays" class="control-label">热门:</label>
            <div class="controls">
                <input type="text" id="hotDays" name="hotDays" value="${setting.hotDays}"
                       class="input-large required"/>
                最近几天的帖子按红心数降序排序
            </div>
        </div>

        <div class="form-actions">
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
        </div>
    </fieldset>
</form>