<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<tags:form name="支付宝签名" method="GET" action="${ctx}/api/1.1/vg/alipaySigedParams.json">
    <tags:field name="productId"/>
    <tags:field name="service" value="mobile.securitypay.pay"/>
    <tags:field name="paymentType" value="1"/>
</tags:form>

<tags:example method="GET" url="http://192.168.1.103:8080/api/1.1/vg/alipaySigedParams.json?productId=1&service=mobile.securitypay.pay&paymentType=1">
    {
        "signed" : "_input_charset=\"utf-8\"&body=\"50个金币×1\"&notify_url=\"http%3A%2F%2Fapp.lebooo.com%2Fapi%2F1.1%2Fec%2FalipayNotify\"&out_trade_no=\"1\"&partner=\"2088111176370601\"&payment_type=\"1\"&seller_id=\"626070255@qq.com\"&service=\"mobile.securitypay.pay\"&subject=\"购买50个金币×1\"&total_fee=\"0.01\"&sign=\"IN2d%2BmjElfSpekbZL%2BHKPhU2e3GcIElGXg6urDIQsVZTy68nzJlNReCiV%2B6okynt%2B51NxbHm%2FbOFUc%2B8Fod29PDzfhSfcSPhi8Ji%2FW1Y6HP3xrWpL2IfnwfsrWtsWZ9S3bVcDNKih%2BSIThRO5Vr%2BjmCxYtbb4YGJF4Z6noGi%2FsI%3D\"&sign_type=\"RSA\""
    }
</tags:example>