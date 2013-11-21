<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title>基本设置</title>
    <style>
        .preview-color {
            display: inline-block;
            width: 20px;
            height: 20px;
            vertical-align: middle;
        }

        .preview-image {
            width: 30px;
            height: 30px;
            display: inline-block;
        }

        .radio-label{
            display: inline-block;
            margin: 0;
        }
    </style>
</head>
<body>
<form id="inputForm" action="${ctx}/admin/settings" method="post" class="form-horizontal">

    <h2>基本设置</h2>

    <div class="control-group">
        <label for="logoKey" class="control-label">Logo Key:</label>

        <div class="controls">
            <input type="text" id="logoKey" name="logoKey" value="${setting.logoKey}"
                   class="input-large required"/>
            <a href="${setting.logoUrl}" target="_blank"><img class="preview-image" src="${setting.logoUrl}"/></a>
            乐播logo图片key
        </div>
    </div>

    <div class="control-group">
        <label for="officialAccountId" class="control-label">乐播 账号ID:</label>
        <div class="controls">
            <input type="text" id="officialAccountId" name="officialAccountId" value="${setting.officialAccountId}"
                   class="input-large required"/>
            新用户自动关注该账号
        </div>
    </div>

    <div class="control-group">
        <label for="digestAccountId" class="control-label">乐播精品 账号ID:</label>
        <div class="controls">
            <input type="text" id="digestAccountId" name="digestAccountId" value="${setting.digestAccountId}"
                   class="input-large required"/>
            该账号专门选精品视频，不允许被关注，该账号转发的视频会出现在精华页
        </div>
    </div>

    <div class="control-group">
        <label for="editorAccountId" class="control-label">编辑 账号ID:</label>
        <div class="controls">
            <input type="text" id="editorAccountId" name="editorAccountId" value="${setting.editorAccountId}"
                   class="input-large required"/>
            该账号发的视频将显示在小编制作频道
        </div>
    </div>
    <%--查询时未使用该参数
    <div class="control-group">
        <label for="digestDays" class="control-label">"精华"几天内的内容:</label>

        <div class="controls">
            <input type="text" id="digestDays" name="digestDays" value="${setting.digestDays}"
                   class="input-large required"/>
        </div>
    </div>--%>

    <div class="control-group">
        <label for="hotDays" class="control-label">热门:</label>
        <div class="controls">
            最近<input type="text" id="hotDays" name="hotDays" value="${setting.hotDays}"
                   class="input-mini required"/>
            天的帖子按 <code>红心数+评分</code> 排序，每用户最多上榜
            <input type="text" id="maxHotPostCountPerUser" name="maxHotPostCountPerUser" value="${setting.maxHotPostCountPerUser}"
                   class="input-mini required"/>
            条
        </div>
    </div>

    <div class="control-group">
        <label for="rankingPostsDays" class="control-label">作品榜:</label>
        <div class="controls">
            最近<input type="text" id="rankingPostsDays" name="rankingPostsDays" value="${setting.rankingPostsDays}"
                     class="input-mini required"/>
            天的帖子按红心数排序
        </div>
    </div>

    <div class="control-group">
        <label for="fastestRisingMinutes" class="control-label">上升最快:</label>
        <div class="controls">
            用户按<input type="text" id="fastestRisingMinutes" name="fastestRisingMinutes" value="${setting.fastestRisingMinutes}"
                   class="input-mini required"/>
            分钟内获得的红心数排序
        </div>
    </div>

    <div class="control-group">
        <label for="top50Days" class="control-label">Top 50:</label>
        <div class="controls">
            用户按<input type="text" id="top50Days" name="top50Days" value="${setting.top50Days}"
                      class="input-mini required"/>
            天内获得的红心数排序
        </div>
    </div>


    <h2>下载地址</h2>

    <p>
        在视频播放页面会用到乐播APP下载地址.
    </p>

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
            <input type="text" id="leboAppAndroidDownloadUrl" name="leboAppAndroidDownloadUrl"
                   value="${setting.leboAppAndroidDownloadUrl}"
                   class="input-large required"/>
            android乐播app下载地址/下载页面
        </div>
    </div>

    <h2>红人榜</h2>

    <h3>粉丝最多按钮</h3>

    <div class="control-group">
        <label for="hotuser_button1_backgroundColor" class="control-label">背景颜色:</label>
        <div class="controls">
            <input type="text" id="hotuser_button1_backgroundColor" name="hotuser_button1_backgroundColor"
                   value="${setting.hotuser_button1_backgroundColor}"
                   class="input-large required"/>
            <span class="preview-color" style="background-color:${setting.hotuser_button1_backgroundColor}"></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button1_imageKey" class="control-label">图片key:</label>
        <div class="controls">
            <input type="text" id="hotuser_button1_imageKey" name="hotuser_button1_imageKey"
                   value="${setting.hotuser_button1_imageKey}"
                   class="input-large required"/>
            <span class="preview-image" style="background-color:${setting.hotuser_button1_backgroundColor}"><img
                    src="${setting.hotuser_button1_imageUrl}"/></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button1_text" class="control-label">文字:</label>
        <div class="controls">
            <input type="text" id="hotuser_button1_text" name="hotuser_button1_text"
                   value="${setting.hotuser_button1_text}"
                   class="input-large required"/>
        </div>
    </div>

    <h3>最受喜欢按钮</h3>

    <div class="control-group">
        <label for="hotuser_button2_backgroundColor" class="control-label">背景颜色:</label>
        <div class="controls">
            <input type="text" id="hotuser_button2_backgroundColor" name="hotuser_button2_backgroundColor"
                   value="${setting.hotuser_button2_backgroundColor}"
                   class="input-large required"/>
            <span class="preview-color" style="background-color:${setting.hotuser_button2_backgroundColor}"></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button2_imageKey" class="control-label">图片key:</label>
        <div class="controls">
            <input type="text" id="hotuser_button2_imageKey" name="hotuser_button2_imageKey"
                   value="${setting.hotuser_button2_imageKey}"
                   class="input-large required"/>
            <span class="preview-image" style="background-color:${setting.hotuser_button2_backgroundColor}"><img
                    src="${setting.hotuser_button2_imageUrl}"/></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button2_text" class="control-label">文字:</label>
        <div class="controls">
            <input type="text" id="hotuser_button2_text" name="hotuser_button2_text"
                   value="${setting.hotuser_button2_text}"
                   class="input-large required"/>
        </div>
    </div>

    <h3>导演排行按钮</h3>

    <div class="control-group">
        <label for="hotuser_button3_backgroundColor" class="control-label">背景颜色:</label>
        <div class="controls">
            <input type="text" id="hotuser_button3_backgroundColor" name="hotuser_button3_backgroundColor"
                   value="${setting.hotuser_button3_backgroundColor}"
                   class="input-large required"/>
            <span class="preview-color" style="background-color:${setting.hotuser_button3_backgroundColor}"></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button3_imageKey" class="control-label">图片key:</label>
        <div class="controls">
            <input type="text" id="hotuser_button3_imageKey" name="hotuser_button3_imageKey"
                   value="${setting.hotuser_button3_imageKey}"
                   class="input-large required"/>
            <span class="preview-image" style="background-color:${setting.hotuser_button3_backgroundColor}"><img
                    src="${setting.hotuser_button3_imageUrl}"/></span>
        </div>
    </div>

    <div class="control-group">
        <label for="hotuser_button3_text" class="control-label">文字:</label>
        <div class="controls">
            <input type="text" id="hotuser_button3_text" name="hotuser_button3_text"
                   value="${setting.hotuser_button3_text}"
                   class="input-large required"/>
        </div>
    </div>

    <h2>广告</h2>

    <div class="control-group">
        <label class="control-label">热门页广告初始状态:</label>
        <div class="controls">
            <input type="radio" id="adsHotExpanded-true" name="adsHotExpanded" value="true" ${setting.adsHotExpanded ? "checked" : ""}/>
            <label for="adsHotExpanded-true" class="radio-label">展开</label>
            <input type="radio" id="adsHotExpanded-false" name="adsHotExpanded" value="false" ${setting.adsHotExpanded ? "" : "checked"}/>
            <label for="adsHotExpanded-false" class="radio-label">折叠</label>
        </div>
    </div>

    <h2>即时通讯</h2>

    <div class="control-group">
        <label class="control-label">即时通讯轮询间隔(秒):</label>
        <div class="controls">
            <input type="text" id="imPollingIntervalSeconds" name="imPollingIntervalSeconds"
                   value="${setting.imPollingIntervalSeconds}"
                   class="input-large required"/>
        </div>
    </div>

    <div class="form-actions">
        <input id="cancel_btn" class="btn" type="button" value="返回" onclick="history.back()"/>
        <input id="submit_btn" class="btn btn-primary" type="submit" value="提交"/>&nbsp;
    </div>
</form>

</body>
</html>

