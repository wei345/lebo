package com.lebo.service;

import com.alipay.api.internal.util.AlipaySignature;
import com.lebo.SpringContextTestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

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
}
