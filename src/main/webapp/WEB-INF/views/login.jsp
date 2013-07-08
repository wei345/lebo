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
    <input type="text" name="token" value="2.00vHLEwBz7QwTCbafc736d580QUCCY" placeholder="新浪token"/>
    <button id="btn-login" type="submit">登录</button>
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
                    $('#result').html('登录失败');
                } else {
                    window.location = '${ctx}/';
                }
            },
            error: function () {
                $('#result').html('登录出错');
            }
        });
    }
</script>
