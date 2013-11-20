<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="java.util.Properties" %>
<%@ page import="com.lebo.service.AppEnv" %>
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
<%
    ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(request.getSession().getServletContext());
    AppEnv appEnv = applicationContext.getBean(AppEnv.class);
    request.setAttribute("quickLogin", appEnv.isDevelopment());
%>

<c:if test="${quickLogin}">
    <form id="loginForm" onsubmit="login();return false;">
        <fieldset>
            <legend>OAuth登录</legend>

            <div id="fail" class="alert alert-error input-medium controls" style="display: none">
                <button class="close" data-dismiss="alert">×</button>
                登录失败，请重试
            </div>

            <select name="provider" style="width: 100px;">
                <option value="weibo">weibo</option>
                <option value="renren">renren</option>
            </select>

            <input type="text" name="token" placeholder="token" style="width: 24em;"/>

            <div class="btn-group" style="vertical-align: top;">
                <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                    <sapn id="token-name">选择</sapn>
                    <span class="caret"></span>
                </a>
                <ul class="dropdown-menu">
                    <li><a href="#" onclick="useToken('weibo', '2.00TgJysBz7QwTC50dd2ad1facNOSYD', this)">乐播</a>
                    <li><a href="#" onclick="useToken('weibo', '2.00BPEZsBz7QwTC486298e2c9qNIOXE', this)">涛涛_IT</a>
                    <li><a href="#" onclick="useToken('weibo', '2.00vHLEwBz7QwTCbafc736d580QUCCY', this)">法图_麦</a></li>
                    <li><a href="#" onclick="useToken('weibo', '2.00bZzGiCz7QwTC9b9ef08828Zoj34C', this)">xueer_ZZZ</a>
                    </li>
                    <li><a href="#"
                           onclick="useToken('weibo', '2.00slTkaDz7QwTC3063b7dbf6oBZ8VE', this)">佳爷3291424124</a>
                    </li>
                    <li><a href="#" onclick="useToken('weibo', '2.00F5vUaDz7QwTC5e4c9eaa98mTYulD', this)">Desi_漓沫沫</a>
                    </li>
                        <%-- token失效 <li><a href="#" onclick="useToken('weibo', '2.00Wo2GnDz7QwTC7613d764f06EDi5E', this)">小萌君sang</a></li>--%>
                    <li><a href="#" onclick="useToken('weibo', '2.00B4rTGCz7QwTCa6b1957d82XDQ67D', this)">Amy滴柔情老妈</a>
                    </li>
                        <%-- token失效 <li><a href="#" onclick="useToken('weibo', '2.00Vr4AxBz7QwTCc40e517e230Pa7Jn', this)">Lau-s1r</a></li>--%>
                    <li><a href="#" onclick="useToken('weibo', '2.00qJrAnDz7QwTC4578c7aefcA7hdwD', this)">家有笨猫咪</a></li>
                    <li><a href="#" onclick="useToken('weibo', '2.00RopF5Dz7QwTC732d6d7685owDxvB', this)">hyj1013</a>
                    </li>
                    <li><a href="#" onclick="useToken('weibo', '2.00ygOJODz7QwTCbe257cefc20dQLIN', this)">IAM-TB</a>
                    </li>
                    <li><a href="#"
                           onclick="useToken('weibo', '2.00fMQ6LDz7QwTC7890666e3ed3NJYD', this)">手机用户2916958681</a>
                    <li><a href="#"
                           onclick="useToken('weibo', '2.00jISvMDz7QwTCc6f50d5861bKj_8C', this)">手机用户2939368957</a>
                    </li>
                    <li><a href="#"
                           onclick="useToken('renren', '232244|6.480f13a58a6da99eb659bbcfdd9b119d.2592000.1376625600-544808317', this)">renren/刘伟</a>
                    </li>
                </ul>
            </div>
            <div>
                <button class="btn" type="submit">OAuth登录</button>
            </div>
        </fieldset>
    </form>

    <script>
        function login() {
            $('#fail').hide();
            $.ajax({
                type: 'post',
                url: '${ctx}/api/1/oauthLogin.json',
                dataType: 'json',
                data: 'provider=' + $('#loginForm')[0].provider.value + '&token=' + $('#loginForm')[0].token.value,
                success: function (data) {
                    if (data.error) {
                        $('#fail').show();
                    } else {
                        window.location = '${ctx}/';
                    }
                },
                error: function () {
                    $('#fail').show();
                }
            });
        }

        function useToken(provider, token, btn) {
            $('option[value=' + provider + ']').attr('selected', 'selected');
            $('input[name=token]').val(token);
            $('#token-name').html(btn.innerHTML);
        }
    </script>
</c:if>

<form method="POST" action="${ctx}/login">
    <fieldset>
        <legend>本地登录</legend>
        <%
            String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
            if (error != null) {
        %>
        <div class="alert alert-error input-medium controls">
            <button class="close" data-dismiss="alert">×</button>
            登录失败，请重试
        </div>
        <%
            }
        %>
        <div class="control-group">
            <div class="controls">
                <input type="text" id="username" name="username" value="" class="input-medium required"
                       placeholder="email"/>
            </div>
            <div class="controls">
                <input type="password" id="password" name="password" value="" class="input-medium required"
                       placeholder="密码"/>
            </div>
            <div class="controls">
                <label class="checkbox" for="rememberMe"><input type="checkbox" id="rememberMe" name="rememberMe"
                                                                checked="checked"/> 记住我</label>
                <input class="btn" type="submit" value="本地登录"/>
            </div>
        </div>
    </fieldset>
</form>
