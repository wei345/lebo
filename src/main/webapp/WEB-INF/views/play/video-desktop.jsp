<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html><html prefix='og: http://ogp.me/ns#'>
<head>
    <meta charset='utf-8'/>
    <title>乐播-6秒视频,分享精彩瞬间！</title>
    <meta http-equiv='X-UA-Compatible' content='IE=edge,chrome=1'/>
    <meta name='description' content='乐播-6秒视频'/>
    <%-- 人人网分享 begin --%>
    <meta property="og:videosrc" content="http://www.lebooo.com/movie/player.swf?vcastr_file=${post.video.contentUrl}&IsAutoPlay=1&IsContinue=1&IsShowBar=0"/>
    <meta property="og:type" content="video"/>
    <meta property="og:title" content="乐播-6秒视频"/>
    <meta property="og:url" content="${baseurl}/play/video/${post.id}"/>
    <meta property="og:image" content="${post.videoFirstFrameUrl}"/>
    <meta property="og:width" content="500"/>
    <meta property="og:height" content="500"/>
    <meta property="og:caption" content="CAPTION"/>
    <meta property="og:description" content="${post.text}"/>
    <%-- 人人网分享 end --%>

    <meta name='width' content='500px'/>
    <meta name='height' content='500px'/>
    <meta name='viewport' content='width=device-width, initial-scale=1.0,user-scalable=no'/>
    <%--<link rel='shortcut icon' href='/favicon.ico'/>--%>
    <%--<link rel='apple-touch-icon' href='/apple-touch-icon.png'/>--%>
    <style type='text/css'>
    body {
        background-color: #19212d;
    }

    img {
        border: none
    }

    #wrap {
        margin: auto
    }

    #content {
        box-shadow: 0px 0px 5px 5px #080808;
        width: 780px;
        height: 480px;
        margin: 100px auto;
    }

    .left_part {
        width: 500px;
        height: 500px;
        float: left;
    }

    #right_part {
        background-color: #EAD6D7;
        width: 280px;
        height: 500px;
        float: left;
        text-align: center;
        background-color: #f4f4f4;
    }

    #right_top_part {
        padding-top: 30px;
        background-color: #FFFFFF;
    }

    #user_photo {
        margin-bottom: 5px;
    }

    #user_name {
        margin-bottom: 10px;
        font: 14px bold;
    }

    #video_instruction {
        font-size: 12px;
        color: #90908F;
        margin-bottom: 24px;
        padding: 0 20px;
    }

    #video_instruction .channel {
        color: #000000;
        padding-right: 5px;
    }

    #hr_white {
        border-top: 1px solid #adadad;
        border-bottom: 1px solid white
    }

    #lebo_insturction {
        margin-bottom: 10px;
        margin-top: 50px;
        font-size: 12px;
        color: #90908F;
    }

    #lebo_insturction .title {
        font-size: 18px;
        color: #404040;
    }

    #link_button {
        margin-top: 35px;
    }

    #link_button a + a {
        margin-top: 10px;
        display: block;
    }
    </style>
</head>

<body>
<div id='wrap'>
    <div id='content'>
        <div id='left_part'>
            <div id='flashPlayer' class='left_part'>

                <img style='display:none' src='${post.videoFirstFrameUrl}'/>
                <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab" height="500" width="500">
                    <param name="movie"
                           value="http://www.lebooo.com/movie/player.swf?vcastr_file=${post.video.contentUrl}&IsAutoPlay=1&IsContinue=1&IsShowBar=0">
                    <param name="quality" value="high">
                    <param name="allowFullScreen" value="true"/>
                    <embed src="http://www.lebooo.com/movie/player.swf?vcastr_file=${post.video.contentUrl}&IsAutoPlay=1&IsContinue=1&IsShowBar=0"
                           quality="high" pluginspage="http://get.adobe.com/cn/flashplayer/"
                           type="application/x-shockwave-flash" width="500" height="500">
                    </embed>
                </object>
            </div>
            <div id='right_part'>
                <div id='right_top_part'>
                    <div id='user_message'>
                        <div id='user_photo'>
                            <img src='${post.user.profileImageUrl}' height='60' width='60'/></div>
                        <div id='user_name'><span>${post.user.screenName}</span></div>
                        <div id='video_instruction'>${post.text}</div>
                    </div>
                    <div id='hr_white'></div>
                </div>
                <div id='lebo_insturction'><span class='title'>乐播－6秒视频</span><br/><br/>视频版微博，零距离接触明星和女神</div>
                <div id='link_button'><a href='${appStoreLeboUrl}'
                                         target='_blank' onclick='this.blur()'><img src='${image_dl_iphone_app_url}'/></a><a
                        href='${leboAppAndroidDownloadUrl}'><img src='${image_dl_android_app_url}'/></a>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    var vinst = document.getElementById('video_instruction');
    if (vinst)
        vinst.innerHTML = document.getElementById('video_instruction').innerHTML.replace(/^\s+|\s+$/, '').replace(/^(#[^#\n]+?#)/, '<span class="channel">$1</span>');
</script>
</body>
</html>
