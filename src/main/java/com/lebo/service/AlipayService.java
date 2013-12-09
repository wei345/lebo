package com.lebo.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.lebo.repository.mybatis.GoldOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private GoldOrderDao goldOrderDao;

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

    public boolean rsaCheckContent(String content, String sign) {
        try {
            return AlipaySignature.rsaCheckContent(content, sign, alipayPublicKey, DEFAULT_CHARSET);
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String getSignContent(Map<String, String> params) {
        return AlipaySignature.getSignContent(params);
    }

    public boolean checkIfAlipayRequest(String notifyId) {

        try {

            String url = new StringBuilder("https://mapi.alipay.com/gateway.do?service=notify_verify")
                    .append("&partner=").append(alipayPartnerId)
                    .append("&notify_id=").append(notifyId)
                    .toString();

            String responseText = restTemplate.getForObject(url, String.class);

            return "true".equals(responseText);

        } catch (Exception e) {
            return false;
        }

    }

    public boolean isNotifyIdProcessed(String notifyId) {
        return goldOrderDao.countByAlipayNotifyId(notifyId) > 0;
    }

    public static enum AlipayStatus {
        WAIT_BUYER_PAY(1),  //￼交易创建,等待买家付款。
        TRADE_SUCCESS(2),   //交易成功,且可对该交易做操作,如:多级分润、退款等。
        TRADE_FINISHED(3), //交易成功且结束,即不可再做任何操作。
        TRADE_CLOSED(3);  //在指定时间段内未支付时关闭的交易; 在交易完成全额退款成功时关闭的交易。

        int value;

        AlipayStatus(int value) {
            this.value = value;
        }

        public boolean canChangeTo(AlipayStatus anotherStatus) {
            if(anotherStatus == null){
                return false;
            }
            return anotherStatus.value > this.value;
        }
    }
}
