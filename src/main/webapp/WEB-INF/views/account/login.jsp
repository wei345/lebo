<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter" %>
<%@ page import="org.springframework.context.ApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
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
                    <li><a href="#" onclick="useToken('weibo', '2.00VSNsMCz7QwTCa4561714ddLul8AC', this)">烟雨醉相思</a>
                    <li><a href="#" onclick="useToken('weibo', '2.00TgJysBz7QwTC908dd41b6aycBzVD', this)">明丫丫是个爷们</a>
                    <li><a href="#" onclick="useToken('weibo', '2.00BPEZsBz7QwTC08a522f020k_mxZB', this)">涛涛_IT</a>
                    <li><a href="#" onclick="useToken('weibo', '2.00kEBMdDz7QwTC82ac69ff36CdLlCC', this)">洛洛家的小九</a>
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
