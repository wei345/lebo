<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="签名" method="GET" action="${ctx}/api/1.1/alipay/sign.json">

    <tags:field name="stringToSign"/>

</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/alipay/sign.json?stringToSign=1234">
    {
        sign: "I6RCnHaeZ4jDldCwSSpfYtnwz1oo023w4GEz4uLtyLxQKuwFAa2KlMyFDz5S0vBN2XHb2UCIuq+kcIrEy4ab74NBsUSUvhao5f7VN3NS8xpw/fx8grvAS6Ojci9EppD/vNtNpN1nPHSocRf9G+i1t++41pEM8EPMAZX4SC2E8Ng="
    }
</tags:example>