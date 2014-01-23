package com.lebo.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.lebo.entity.GoldOrder;
import com.lebo.redis.RedisKeys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springside.modules.nosql.redis.JedisTemplate;
import org.springside.modules.utils.Encodes;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

import static com.lebo.service.AlipayService.AlipayStatus.WAIT_BUYER_PAY;

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

    @Autowired
    private JedisTemplate jedisTemplate;

    private RestTemplate restTemplate = new RestTemplate();

    private Logger logger = LoggerFactory.getLogger(AlipayService.class);

    @Autowired
    private VgService vgService;

    /**
     * 《移动快捷支付应用集成接入包 支付接口 -- 版本号:1.1》 7.4 服务器异步通知参数获取:
     * 如果商户反馈给支付宝的字符不是 success 这 7 个字符,支付宝服务器会不断重发通知,直到 超过24小时22分钟。
     * 一般情况下,25 小时以内完成 8 次通知(通知的间隔频率一般是: 2m,10m,10m,1h,2h,6h,15h)
     */
    private int expireSeconds = 60 * 60 * 24 * 2; //48小时

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

    //-- 支付请求参数 --//

    public String getAlipayParams(String userId, Long productId, String service, String paymentType) {

        GoldOrder goldOrder = vgService.createOrder(productId, userId, GoldOrder.PaymentMethod.ALIPAY);

        Map<String, String> params = new HashMap<String, String>(10);
        //基本参数，不可空
        params.put("service", service);
        params.put("partner", alipayPartnerId);
        params.put("_input_charset", "utf-8");
        //业务参数，不可空
        params.put("out_trade_no", goldOrder.getId().toString());
        params.put("subject", "购买" + goldOrder.getAlipaySubject());
        params.put("payment_type", paymentType);
        params.put("seller_id", alipaySellerId);
        params.put("total_fee", goldOrder.getTotalCost().setScale(2).toString());
        params.put("body", goldOrder.getAlipayBody());
        params.put("notify_url", Encodes.urlEncode(alipayNotifyUrl));

        //值带双引号
        for (Map.Entry<String, String> entry : params.entrySet()) {
            entry.setValue("\"" + entry.getValue() + "\"");
        }

        String signContent = getSignContent(params);
        String sign = sign(signContent);

        return new StringBuilder(signContent)
                .append("&sign=").append("\"" + Encodes.urlEncode(sign) + "\"")
                .append("&sign_type=\"RSA\"")
                .toString();
    }

    //-- 支付宝异步通知 --//

    public boolean checkNotifyId(final String notifyId) {

        return jedisTemplate.execute(new JedisTemplate.JedisAction<Boolean>() {
            @Override
            public Boolean action(Jedis jedis) {
                return !jedis.exists(RedisKeys.getAlipayNotifyIdKey(notifyId));
            }
        });
    }

    public void doneNotifyId(String notifyId) {
        jedisTemplate.setex(RedisKeys.getAlipayNotifyIdKey(notifyId), "1", expireSeconds);
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
            if (anotherStatus == null) {
                return false;
            }
            return anotherStatus.value > this.value;
        }
    }

    public void handleNotify(String outTradeNo, AlipayStatus alipayStatus, String alipayNotifyId) {

        if (!checkNotifyId(alipayNotifyId)) {
            logger.debug("alipayNotifyId '{}' 已处理过", alipayNotifyId);
            return;
        }

        updateOrderStatus(outTradeNo, alipayStatus, alipayNotifyId);

        doneNotifyId(alipayNotifyId);
    }

    public void updateOrderStatus(String orderId, AlipayStatus alipayStatus, String alipayNotifyId) {

        GoldOrder goldOrder = vgService.getOrder(orderId);

        AlipayStatus currentAlipayStatus = AlipayStatus.valueOf(goldOrder.getPaymentStatus());

        if (currentAlipayStatus == null || currentAlipayStatus.canChangeTo(alipayStatus)) {

            logger.debug("正在更新订单状态: {} -> {}", currentAlipayStatus, alipayStatus);

            VgService.PaymentDetail paymentDetail = new AlipayPaymentDetail(alipayNotifyId);

            switch (alipayStatus) {
                //null -> WAIT_BUYER_PAY
                case WAIT_BUYER_PAY:
                    vgService.updateOrderStatus(orderId, GoldOrder.Status.UNPAID, alipayStatus.name(), paymentDetail);
                    logger.debug("未支付 : 支付宝等待用户支付");
                    break;

                case TRADE_SUCCESS:
                    //null -> TRADE_SUCCESS, WAIT_BUYER_PAY -> TRADE_SUCCESS
                    tradeSuccess(goldOrder, alipayStatus, alipayNotifyId);
                    logger.debug("已支付 : 支付宝交易完成，用户金币已增加");
                    break;

                case TRADE_FINISHED:
                    //null -> TRADE_FINISHED, WAIT_BUYER_PAY -> TRADE_FINISHED
                    if (currentAlipayStatus == null || currentAlipayStatus == WAIT_BUYER_PAY) {
                        tradeSuccess(goldOrder, alipayStatus, alipayNotifyId);
                        logger.debug("已支付 : 支付宝交易完成，用户金币已增加");
                    }
                    //TRADE_SUCCESS -> TRADE_FINISHED, 不会出现这种情况吧
                    else {
                        throw new ServiceException("不知如何处理订单状态变化: " + currentAlipayStatus + " -> " + alipayStatus);
                    }
                    break;

                case TRADE_CLOSED:
                    //null -> TRADE_CLOSED, WAIT_BUYER_PAY -> TRADE_CLOSED
                    if (currentAlipayStatus == null || currentAlipayStatus == WAIT_BUYER_PAY) {
                        vgService.updateOrderStatus(orderId, GoldOrder.Status.OBSOLETE, alipayStatus.name(), paymentDetail);
                        logger.debug("订单作废 : 支付宝交易关闭");
                    }
                    //TRADE_SUCCESS -> TRADE_CLOSED? 退款？
                    else {
                        throw new ServiceException("不知如何处理订单状态变化: " + currentAlipayStatus + " -> " + alipayStatus);
                    }
                    break;

                default:
                    throw new ServiceException("未知的订单类型: " + alipayStatus);
            }
        } else {
            logger.debug("订单状态变化不符合支付宝规则，{} -> {}, 不做处理", currentAlipayStatus, alipayStatus);
        }
    }

    public void tradeSuccess(GoldOrder goldOrder, AlipayStatus alipayStatus, String alipayNotifyId) {

        vgService.updateOrderStatus(goldOrder.getId(),
                GoldOrder.Status.PAID,
                alipayStatus.name(),
                new AlipayPaymentDetail(alipayNotifyId));

        vgService.delivery(goldOrder);
    }

    public static class AlipayPaymentDetail implements VgService.PaymentDetail {

        private String notifyId;

        public AlipayPaymentDetail() {
        }

        public AlipayPaymentDetail(String notifyId) {
            this.notifyId = notifyId;
        }

        public String getNotifyId() {
            return notifyId;
        }

        public void setNotifyId(String notifyId) {
            this.notifyId = notifyId;
        }
    }
}
