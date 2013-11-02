package com.lebo.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author: Wei Liu
 * Date: 13-11-1
 * Time: PM7:57
 */
@Service
public class AlipayService {
    @Value("${alipay.alipay_public_key}")
    private String alipayPublicKey;
    @Value("${alipay.partner_private_key}")
    public String alipayPartnerPrivateKey;
    @Value("${alipay.partner_id}")
    public String alipayPartnerId;
    @Value("${alipay.notify_url}")
    public String alipayNotifyUrl;
    @Value("${alipay.seller_id}")
    public String alipaySellerId;
    public static final String DEFAULT_CHARSET = "UTF-8";

    public String sign(String stringToSign) {
        try {
            return AlipaySignature.rsaSign(stringToSign, alipayPartnerPrivateKey, DEFAULT_CHARSET);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String sign(Map<String, String> params) {
        try {
            return AlipaySignature.rsaSign(params, alipayPartnerPrivateKey, DEFAULT_CHARSET);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean rsaCheckV1(Map<String, String> params) {
        try {
            return AlipaySignature.rsaCheckV1(params, alipayPublicKey, DEFAULT_CHARSET);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSignContent(Map<String, String> params) {
        return AlipaySignature.getSignContent(params);
    }
}
