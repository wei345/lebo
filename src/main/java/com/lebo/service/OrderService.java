package com.lebo.service;

/**
 * 订单.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.lebo.entity.Order;
import com.lebo.entity.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    @Value("${alipay.lebo_private_key}")
    public String leboPrivateKey;

    public String sign(String stringToSign) {
        try {
            return AlipaySignature.rsaSign(stringToSign, leboPrivateKey, "utf-8");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String sign(Map<String, String> params) {
        try {
            return AlipaySignature.rsaSign(params, leboPrivateKey, "utf-8");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String createOrder(Long productId){
        Order order = new Order();

        List<Product> products = new ArrayList<Product>(1);

        return null;


    }
}
