package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.GoldOrder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.lebo.service.AlipayService.AlipayStatus.*;

import static org.junit.Assert.*;

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
        vgService.tradeSuccess(1L, AlipayService.AlipayStatus.TRADE_SUCCESS, "test-123456");
    }

    @Test
    public void handleAlipayNotify(){
        vgService.handleAlipayNotify(2013121312071706588L, TRADE_FINISHED, "test-12345678");
    }

    @Test
    public void nextOrderId(){
        assertEquals(19, String.valueOf(vgService.nextOrderId()).length());
    }
}
