package com.lebo.service;

/**
 * 支付宝支付.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AlipayService {
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
}
