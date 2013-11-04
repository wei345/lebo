package com.lebo.service;

/**
 * 订单.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.lebo.entity.GoldOrder;
import com.lebo.entity.GoldProduct;
import com.lebo.repository.mybatis.GoldOrderDao;
import com.lebo.repository.mybatis.GoldProductDao;
import com.lebo.rest.dto.OrderDto;
import com.lebo.rest.dto.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.utils.Encodes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class VgService {

    @Autowired
    private GoldProductDao goldProductDao;
    @Autowired
    private GoldOrderDao goldOrderDao;
    @Autowired
    private AlipayService alipayService;
    @Autowired
    private AppEnv appEnv;

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

    public GoldOrder createOrder(Long goldProductId, String mongoUserId) {
        GoldOrder goldOrder = new GoldOrder(mongoUserId, BigDecimal.ZERO, GoldOrder.Status.UNPAID);

        GoldProduct goldProduct = goldProductDao.get(goldProductId);
        Assert.notNull(goldProduct);
        goldOrder.setGoldProduct(goldProduct);

        goldOrder.setQuantity(1);

        goldOrderDao.save(goldOrder);
        return goldOrder;
    }

    public List<GoldProduct> findAllGoldProducts() {
        return goldProductDao.getAll();
    }

    public GoldOrder getOrderWithDetail(Long orderId) {
        GoldOrder goldOrder = goldOrderDao.get(orderId);
        goldOrder.setGoldProduct(goldProductDao.get(goldOrder.getGoldProduct().getId()));
        return goldOrder;
    }

    public void updateOrderStatus(GoldOrder goldOrder, GoldOrder.Status status) {

    }

    public String getAlipayParams(String userId, Long productId, String service, String paymentType) {
        GoldOrder goldOrder = createOrder(productId, userId);

        Map<String, String> params = new HashMap<String, String>(10);
        //基本参数，不可空
        params.put("service", service);
        params.put("partner", alipayPartnerId);
        params.put("_input_charset", "utf-8");
        //业务参数，不可空
        params.put("out_trade_no", goldOrder.getId().toString());
        params.put("subject", "购买" + goldOrder.getSubject());
        params.put("payment_type", paymentType);
        params.put("seller_id", alipaySellerId);
        //开发环境，订单支付1分钱
        if (appEnv.isDevelopment()) {
            params.put("total_fee", "0.01");
        }
        //生成环境，订单支付实际金额
        else {
            params.put("total_fee", goldOrder.getTotalCost().setScale(2).toString());
        }
        //
        params.put("body", goldOrder.getBody());
        params.put("notify_url", Encodes.urlEncode(alipayNotifyUrl));

        //值带双引号
        for (Map.Entry<String, String> entry : params.entrySet()) {
            entry.setValue("\"" + entry.getValue() + "\"");
        }

        String signContent = alipayService.getSignContent(params);
        String sign = alipayService.sign(signContent);

        return new StringBuilder(signContent)
                .append("&sign=").append("\"" + Encodes.urlEncode(sign) + "\"")
                .append("&sign_type=\"RSA\"")
                .toString();
    }

    public ProductDto toProductDto(GoldProduct goldProduct) {
        return BeanMapper.map(goldProduct, ProductDto.class);
    }

    public OrderDto toOrderDto(GoldOrder goldOrder) {
        return BeanMapper.map(goldOrder, OrderDto.class);
    }

    /**
     * 购买金币，支付宝支付成功.
     */
    public void tradeSuccess(Long orderId) {
        GoldOrder goldOrder = getOrderWithDetail(orderId);


    }
}
