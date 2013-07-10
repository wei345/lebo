<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<ul class="nav nav-tabs" id="myTab">
    <li class="active"><a href="#home">主页</a></li>
    <li><a href="#discover">发现</a></li>
    <li><a href="#me">我</a></li>
    <li><a href="#post">发布新视频</a></li>
</ul>

<div class="tab-content">
    <div class="tab-pane active" id="home">...</div>
    <div class="tab-pane" id="discover">...</div>
    <div class="tab-pane" id="me"></div>
    <div class="tab-pane" id="post">
        <form id="updateStatusForm" action="${ctx}/api/v1/statuses/update" method="post" enctype="multipart/form-data">
            <fieldset>
                <legend>发布</legend>
                <label>视频</label>
                <input type="file" name="video" class="required"/>
                <label>图片</label>
                <input type="file" name="image" class="required"/>
                <label>文字</label>
                <textarea type="text" name="text" placeholder="Type something…" maxlength="140" cols="80" rows="4"></textarea>
                <input type="hidden" name="source" value="乐播网页版">
                <button type="submit" class="btn">Submit</button>
            </fieldset>
        </form>
    </div>
</div>

<script>
    $('#myTab a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
    });

    $(function () {
        $('#myTab a[href="#post"]').tab('show');
    });

    $('#myTab a').on('shown', function (e) {
        var id = e.target.getAttribute("href").substring(1);
        try{
            eval('(' + id + '())');
        }catch(e){
            //忽略
        }

    });

    function discover(){

    }

    var data = {
        me: {}
    };
    function me(){
        var url = '${ctx}/api/v1/statuses/userTimeline?count=1';
        if(data.me.maxId){
            url += '&maxId=' + data.me.maxId;
        }
        if(data.me.sinceId){
            url += '&sinceId=' + data.me.sinceId;
        }
        $.ajax({
            type: 'GET',
            url: url,
            success: function(tweets){
                var html = '';
                for(var i = 0; i < tweets.length; i++){
                    var post = tweets[i];
                    var videoSrc = '${ctx}/files/' + post.files[0];
                    html += '<video src="'+ videoSrc +'" controls="controls" autoplay="autoplay">您的浏览器不支持 video 标签。</video>';
                    html += '<div class="text">'+ post.text +'</div>';
                }
                $('#me').append(html);
                data.me.maxId = tweets[tweets.length - 1].id;
            }
        });
    }
</script>




