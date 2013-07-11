<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<%--<script src=" http://tjs.sjs.sinajs.cn/open/api/js/wb.js?appkey=2274665609" type="text/javascript" charset="utf-8"></script>--%>
<%--
<script>
    WB2.login(function(){
        console.log(WB2.checkLogin());
        console.log('登录成功', arguments);
    });
</script>
--%>

<form id="loginForm" onsubmit="login();return false;">
    <input type="text" name="token" placeholder="新浪token" style="width: 24em;"/>

    <div class="btn-group" style="vertical-align: top;">
        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
            <sapn id="token-name">选择</sapn>
            <span class="caret"></span>
        </a>
        <ul class="dropdown-menu">
            <li><a href="#" onclick="useToken('2.00vHLEwBz7QwTCbafc736d580QUCCY', this)">法图_麦</a></li>
            <li><a href="#" onclick="useToken('2.00bZzGiCz7QwTC9b9ef08828Zoj34C', this)">xueer_ZZZ</a></li>
            <li><a href="#" onclick="useToken('2.00slTkaDz7QwTC3063b7dbf6oBZ8VE', this)">佳爷3291424124</a></li>
            <li><a href="#" onclick="useToken('2.00F5vUaDz7QwTC5e4c9eaa98mTYulD', this)">Desi_漓沫沫</a></li>
            <li><a href="#" onclick="useToken('2.00Wo2GnDz7QwTC7613d764f06EDi5E', this)">小萌君sang</a></li>
            <li><a href="#" onclick="useToken('2.00B4rTGCz7QwTCa6b1957d82XDQ67D', this)">Amy滴柔情老妈</a></li>
            <li><a href="#" onclick="useToken('2.00Vr4AxBz7QwTCc40e517e230Pa7Jn', this)">Lau-s1r</a></li>
            <li><a href="#" onclick="useToken('2.00qJrAnDz7QwTC4578c7aefcA7hdwD', this)">家有笨猫咪</a></li>
            <li><a href="#" onclick="useToken('2.00RopF5Dz7QwTC732d6d7685owDxvB', this)">hyj1013</a></li>
            <li><a href="#" onclick="useToken('2.00ygOJODz7QwTCbe257cefc20dQLIN', this)">IAM-TB</a></li>
            <li><a href="#" onclick="useToken('2.00fMQ6LDz7QwTC7890666e3ed3NJYD', this)">手机用户2916958681</a></li>
        </ul>
    </div>
    <button class="btn" type="submit" style="margin-left: 4em; vertical-align: top;">登录</button>
</form>

<div id="result"></div>

<script>
    function login() {
        $.ajax({
            type: 'post',
            url: '${ctx}/api/v1/oauthLogin',
            dataType: 'json',
            data: 'provider=weibo&token=' + $('#loginForm')[0].token.value,
            success: function (data) {
                if (data.error) {
                    $('#result').html('登录失败，多试几次');
                } else {
                    window.location = '${ctx}/';
                }
            },
            error: function () {
                $('#result').html('登录出错');
            }
        });
    }

    function useToken(token, btn) {
        $('input[name=token]').val(token);
        $('#token-name').html(btn.innerHTML);
    }
</script>
