<%@ page import="com.lebo.service.FileContentUrlUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <title>乐播-6秒视频,分享精彩瞬间！</title>
    <meta charset='utf-8'/>
    <meta name='viewport' content='width=640'>
    <style type='text/css'>
        html, body, div {
            padding: 0;
            margin: 0
        }

        body {
            background-color: #929292;
        }

        #container {
            width: 640px;
            margin: 0 auto;
            background-color: #E9E0D7;
        }

        #container .header {
            background-color: #55B4D6;
            padding: 8px 18px;
        }

        #container .header .btn-dl-lebo {
            height: 70px;
            display: inline-block;
            float: right;
            position: relative;
        }

        #container .header .btn-dl-lebo img {
            height: 70px;
        }

        #video video {
            min-height: 640px;
        }

        #info {
            padding: 36px 22px 15px;
        }

        #info .text, #lebo-sign .text {
            padding-left: 16px;
            display: inline-block;
            vertical-align: top;
        }

        #user-name {
            font-size: 28px;
            color: #404040;
            margin-bottom: 10px;
        }

        #video-desc {
            font-size: 24px;
            color: #898989;
        }

        #video-desc .channel {
            font-size: 24px;
            color: #404040;
            padding-right: 5px;
        }

        .line {
            border-bottom: 1px #BDAFA0 solid;
            height: 1px;
        }

        #lebo-sign {
            padding: 22px 22px 0;
        }

        #lebo-sign, .lebo-name {
            font-size: 30px;
            font-weight: bold;
            color: #404040;
        }

        #lebo-sign, .desc {
            font-weight: normal;
        }

        #lebo-sign .desc {
            font-size: 24px;
            color: #898989;
        }

        #dl-buttons {
            padding: 22px;
        }

        #dl-buttons img {
            width: 220px;
        }

        #dl-buttons .btn-android {
            float: right;
        }
    </style>
</head>
<body>

<div id='container'>
    <div class='header'>
        <img src='<%=FileContentUrlUtils.getContentUrl("images/logo-long.png")%>'/>
        <a class='btn-dl-lebo' href='${appStoreLeboUrl}'>
            <img src='<%=FileContentUrlUtils.getContentUrl("images/btn-bg.png")%>'/>
            <span style='text-decoration: none; font-size: 26px; color: #ffffff; position:absolute; left:29px; top:17px;'>免费下载乐播</span>
        </a>
    </div>

    <div id='video'>
        <video autoplay='autoplay' controls='controls' loop='loop' width='100%'>
            <source src='${post.video.contentUrl}'/>
        </video>
    </div>
    <div id='info'>
        <img id='user-photo' src='${post.user.profileImageUrl}' style='width:72px;'/>

        <div class='text'>
            <div id='user-name'>${post.user.screenName}</div>
            <div id='video-desc'>${post.text}</div>
        </div>
    </div>
    <div class='line'></div>

    <div id='lebo-sign'>
        <img class='logo' src='<%=FileContentUrlUtils.getContentUrl("images/logo.png")%>' style='width:90px;'/>

        <div class='text'>
            <div class='lebo-name'>乐播</div>
            <div class='desc'>
                <div>视频版微博</div>
                <div>零距离接触明星和女神</div>
            </div>
        </div>
    </div>

    <div id='dl-buttons'>
        <a href='${appStoreLeboUrl}'><img src='${image_dl_iphone_app_url}'/></a>
        <a class='btn-android' href='${leboAppAndroidDownloadUrl}'><img src='${image_dl_android_app_url}'/></a>
    </div>

</div>
<script type='text/javascript'>
    <c:if test="${!pvt}"> <%--私有视频不跳到乐播APP--%>
    if (/iPhone|iPad|iPod|iOS/i.test(navigator.userAgent)) {
        window.location.href = 'lebo://topic/${post.id}';
    }
    </c:if>
    var vdesc = document.getElementById('video_desc');
    if (vdesc) vdesc.innerHTML = document.getElementById('video_desc').innerHTML.replace(/^\s+|\s+$/, '').replace(/^(#[^#\n]+?#)/, '<span class="channel">$1</span>');
</script>
</body>
</html>
