package com.lebo.service;

import com.alipay.api.internal.util.AlipaySignature;
import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author: Wei Liu
 * Date: 13-11-2
 * Time: PM5:42
 */
public class AlipayServiceTest extends SpringContextTestCase {

    @Autowired
    private AlipayService alipayService;

    @Test
    public void rsaCheckContent() throws Exception {
        String content = "_input_charset=\"utf-8\"&body=\"10个金币×1\"&notify_url=\"http%3A%2F%2Fapp.lebooo.com%2Fapi%2F1.1%2Fec%2FalipayNotify\"&out_trade_no=\"51\"&partner=\"2088111176370601\"&payment_type=\"1\"&seller_id=\"626070255@qq.com\"&service=\"mobile.securitypay.pay\"&subject=\"购买金币\"&total_fee=\"0.01\"&success=\"true\"";
        String sign = "eDtc7aINQX0XSpxa6Ix3Ar6ZUrbpE90tkd22weF4inHukyYhkeGkT4cJGhXVm4LnSyYePOdod0tH9DIOA+KEfzWfFIku/CygLJOELfX8DWis7i+B85zclqnsOTMDKSSjgi33JU2zin+f9y85pclGXLlSt6zDwsIxutr1suQef7M=";

        Map<String, String> params = new HashMap<String, String>();
        String[] parts = content.split("&");
        for (String part : parts) {
            String[] kv = part.split("=");
            params.put(kv[0], kv[1]);
        }
        String stringToSign = AlipaySignature.getSignContent(params);
        System.out.println("stringToSign : " + stringToSign);

        boolean b = alipayService.rsaCheckContent(content, sign);
        assertTrue(b);
    }

    @Test
    public void rsaCheckV1() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("buyer_id", "2088502230978354");
        params.put("trade_no", "2013120320353835");
        params.put("body", "1000个金币×1");
        params.put("use_coupon", "N");
        params.put("notify_time", "2013-12-03 18:47:49");
        params.put("subject", "购买1000个金币×1");
        params.put("sign_type", "RSA");
        params.put("is_total_fee_adjust", "N");
        params.put("notify_type", "trade_status_sync");
        params.put("gmt_close", "2013-12-03 18:47:49");
        params.put("out_trade_no", "10");
        params.put("gmt_payment", "2013-12-03 18:47:49");
        params.put("trade_status", "TRADE_FINISHED");
        params.put("discount", "0.00");
        params.put("sign", "RvUkF3ja4FepPTre7eSBirrslEbsPuCcNz/TgrgJfny5cAWhsGBE1zQ2p5MNz+UanrEZIiZNoUo+N8tyB6+tH/cwOEHndm/3uaU8pISomGCH1VaToQT0C7AmkY7O+Oab3x5Armrs6J2Ajs03i58J77SmpIa5Ge+nomXsdAyv9Mc=");
        params.put("buyer_email", "410221b0002.cdb@sina.cn");
        params.put("gmt_create", "2013-12-03 18:47:47");
        params.put("price", "0.01");
        params.put("total_fee", "0.01");
        params.put("quantity", "1");
        params.put("seller_id", "2088111176370601");
        params.put("notify_id", "a165ad6fb216c1b9baee8bac3d85bd5f3y");
        params.put("seller_email", "626070255@qq.com");
        params.put("payment_type", "1");

        assertTrue(alipayService.rsaCheckV1(params));
    }

    @Test
    public void checkIfAlipayRequest() {
        assertTrue(alipayService.checkIfAlipayRequest("a165ad6fb216c1b9baee8bac3d85bd5f3y")); //过期为false, 多长时间过期？
        assertFalse(alipayService.checkIfAlipayRequest("123456"));
    }

    @Test
    public void alipayStatus(){
        assertTrue(AlipayService.AlipayStatus.TRADE_SUCCESS == AlipayService.AlipayStatus.valueOf("TRADE_SUCCESS"));
    }

    @Test
    public void checkNotifyId(){
        String key = "123";
        assertTrue(alipayService.checkNotifyId(key));
        alipayService.doneNotifyId(key);
        assertFalse(alipayService.checkNotifyId(key));
    }

}
