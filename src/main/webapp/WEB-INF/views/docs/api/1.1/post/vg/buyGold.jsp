<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="购买金币" method="POST" action="${ctx}/api/1.1/vg/buyGold.json">
    <tags:field name="productId"/>
    <tags:field name="paymentMethod" value="ALIPAY"/>
    <tags:field name="alipayService" value="mobile.securitypay.pay"/>
    <tags:field name="alipayPaymentType" value="1"/>
</tags:form>

<tags:example method="POST" url="http://localhost:8080/api/1.1/vg/buyGold.json?productId=1&paymentMethod=ALIPAY&alipayService=mobile.securitypay.pay&alipayPaymentType=1">
    {
        "alipaySignedParams" : "_input_charset=\"utf-8\"&body=\"10个金币×1\"&notify_url=\"http%3A%2F%2Fapp.dev.lebooo.com%3A8080%2Fapi%2F1%2Falipay%2Fnotify\"&out_trade_no=\"1\"&partner=\"2088111176370601\"&payment_type=\"1\"&seller_id=\"626070255@qq.com\"&service=\"mobile.securitypay.pay\"&subject=\"购买10个金币×1\"&total_fee=\"0.01\"&sign=\"Arj%2BtL%2FajBs2WUBp2uy3J8uijc76oNa%2FFtNgcC%2FyknvHpBiHZJ0y%2F5dEe6a4piks502PG%2FjCBlPClPofdT%2BRmuQ3MOrZluNLGA6SEc3jz5SlExbBcpNZ0qWTxmQ6ZfvTfGQzZDnG2t9HVrKNY3AgpnaGf%2B1QM6%2F4xY3wEEX7U4U%3D\"&sign_type=\"RSA\""
    }
</tags:example>