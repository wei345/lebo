package com.lebo.service;

import com.lebo.SpringContextTestCase;
import com.lebo.entity.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: Wei Liu
 * Date: 13-10-31
 * Time: PM7:51
 */
public class EcServiceTest extends SpringContextTestCase {

    @Autowired
    private EcService ecService;

    @Test
    public void createOrder() throws Exception {
        Order order = ecService.createOrder(1L, "5216d0dc1a8829c4ae1bbec3");
    }

    @Test
    public void getAlipayParams(){
        String signedParams = ecService.getAlipayParams("5216d0dc1a8829c4ae1bbec3", 1L, "mobile.securitypay.pay", "1");
        System.out.println(signedParams);
    }
}
