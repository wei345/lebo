package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.GoldOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM7:51
 */
public class VgServiceTest extends SpringContextTestCase {

    @Autowired
    private VgService vgService;

    @Test
    public void createOrder() throws Exception {
        GoldOrder goldOrder = vgService.createOrder(1L, "5216d0dc1a8829c4ae1bbec3", GoldOrder.PaymentMethod.ALIPAY);
    }

    @Test
    public void getAlipayParams(){
        String signedParams = vgService.getAlipayParams("5216d0dc1a8829c4ae1bbec3", 1L, "mobile.securitypay.pay", "1");
        System.out.println(signedParams);
    }

    @Test
    public void tradeSuccess(){
        vgService.tradeSuccess(2L, "TRADE_SUCCESS");
    }
}
