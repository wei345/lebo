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
            <label for="hotDays" class="control-label">热门:</label>
            <div class="controls">
                <input type="text" id="hotDays" name="hotDays" value="${setting.hotDays}"
                       class="input-large required"/>
                最近几天的帖子按红心数降序排序
            </div>
        </div>

        <div class="control-group">
            <label for="appStoreLeboUrl" class="control-label">ios乐播app下载:</label>
            <div class="controls">
                <input type="text" id="appStoreLeboUrl" name="appStoreLeboUrl" value="${setting.appStoreLeboUrl}"
                       class="input-large required"/>
                ios乐播app下载地址/下载页面
            </div>
        </div>

        <div class="control-group">
            <label for="leboAppAndroidDownloadUrl" class="control-label">android乐播app下载:</label>
            <div class="controls">
                <input type="text" id="leboAppAndroidDownloadUrl" name="leboAppAndroidDownloadUrl" value="${setting.leboAppAndroidDownloadUrl}"
                       class="input-large required"/>
                android乐播app下载地址/下载页面
            </div>
        </div>



        <h2>红人榜</h2>

        <h3>粉丝最多按钮</h3>

        <div class="control-group">
            <label for="hotuser_button1_backgroundColor" class="control-label">背景颜色:</label>
            <div class="controls">
                <input type="text" id="hotuser_button1_backgroundColor" name="hotuser_button1_backgroundColor" value="${setting.hotuser_button1_backgroundColor}"
                       class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">图片:</label>
            <div class="controls">
                <img src="${hotuser_button1_imageUrl}"/>
            </div>
        </div>

        <div class="control-group">
            <label for="hotuser_button1_text" class="control-label">文字:</label>
            <div class="controls">
                <input type="text" id="hotuser_button1_text" name="hotuser_button1_text" value="${setting.hotuser_button1_text}"
                       class="input-large required"/>
            </div>
        </div>

        <h3>最受喜欢按钮</h3>

        <div class="control-group">
            <label for="hotuser_button2_backgroundColor" class="control-label">背景颜色:</label>
            <div class="controls">
                <input type="text" id="hotuser_button2_backgroundColor" name="hotuser_button2_backgroundColor" value="${setting.hotuser_button2_backgroundColor}"
                       class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">图片:</label>
            <div class="controls">
                <img src="${hotuser_button2_imageUrl}"/>
            </div>
        </div>

        <div class="control-group">
            <label for="hotuser_button2_text" class="control-label">文字:</label>
            <div class="controls">
                <input type="text" id="hotuser_button2_text" name="hotuser_button2_text" value="${setting.hotuser_button2_text}"
                       class="input-large required"/>
            </div>
        </div>

        <h3>导演排行按钮</h3>

        <div class="control-group">
            <label for="hotuser_button3_backgroundColor" class="control-label">背景颜色:</label>
            <div class="controls">
                <input type="text" id="hotuser_button3_backgroundColor" name="hotuser_button3_backgroundColor" value="${setting.hotuser_button3_backgroundColor}"
                       class="input-large required"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">图片:</label>
            <div class="controls">
                <img src="${hotuser_button3_imageUrl}"/>
            </div>
        </div>

        <div class="control-group">
            <label for="hotuser_button3_text" class="control-label">文字:</label>
            <div class="controls">
                <input type="text" id="hotuser_button3_text" name="hotuser_button3_text" value="${setting.hotuser_button3_text}"
                       class="input-large required"/>
            </div>
        </div>




        <div class="form-actions">
            <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
            <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
        </div>
    </fieldset>
</form>