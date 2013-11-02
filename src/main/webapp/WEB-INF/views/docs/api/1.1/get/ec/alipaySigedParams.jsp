<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="支付宝签名" method="GET" action="${ctx}/api/1.1/ec/alipaySigedParams.json">
    <tags:field name="productId"/>
    <tags:field name="service" value="mobile.securitypay.pay"/>
    <tags:field name="paymentType" value="1"/>
</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/ec/alipaySigedParams.json?productId=1&service=mobile.securitypay.pay&paymentType=1">
    {
        signed: "_input_charset=utf-8&body=10个金币×1&notify_url=http://app.lebooo.com/api/1.1/ec/alipayNotify&out_trade_no=26&partner=2088111176370601&payment_type=52356929343539a89a52dc8d&seller_id=626070255@qq.com&service=mobile.securitypay.pay&subject=购买金币&total_fee=1.00&sign=FFT9p82jy0X/3H2TsIx/QumuF3FBiPgwD7p7+MsCrX2tnGSmgWQ4DneD57Fk13HMe/huFux37/pjFq5+dbURo5mNrc2KM1dHXOuO76EaNXR89FZpuojp+1q1gTfpygUDgVG9v7gbxpSQ5az70BjabM4zvUUniZbw8SyfrZ4ud/A=&sign_type=RSA"
    }
</tags:example>