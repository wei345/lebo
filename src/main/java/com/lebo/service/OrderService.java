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
import com.lebo.entity.OrderDetail;
import com.lebo.entity.Product;
import com.lebo.repository.mybatis.OrderDao;
import com.lebo.repository.mybatis.OrderDetailDao;
import com.lebo.repository.mybatis.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class OrderService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDetailDao orderDetailDao;

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

    public String sign(String stringToSign) {
        try {
            return AlipaySignature.rsaSign(stringToSign, alipayPartnerPrivateKey, "utf-8");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public String sign(Map<String, String> params) {
        try {
            return AlipaySignature.rsaSign(params, alipayPartnerPrivateKey, "utf-8");
        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }
    }

    public Order createOrder(Long productId, String mongoUserId) {
        Order order = new Order(mongoUserId, BigDecimal.ZERO, Order.Status.UNPAID);

        Product product = productDao.get(productId);
        Assert.notNull(product);

        OrderDetail orderDetail = new OrderDetail(order, product, 1, BigDecimal.ZERO);

        order.getOrderDetails().add(orderDetail);

        orderDao.save(order);
        orderDetailDao.save(orderDetail);

        return order;
    }

    public List<Product> findProductByCategoryId(Long productCategoryId){
        return productDao.findByCategoryId(productCategoryId);
    }

    /*public void generateAlipayParam(Order order){

        Map<String,String> params = new HashMap<String, String>(10);
        params.put("partner", alipayPartnerId);
        params.put("seller_id", alipaySellerId);
        params.put("out_trade_no", order.getOrderId().toString());
        params.put("subject", "购买金币");
        params.put("body", order.getOrderDetails().get(0).getProduct().getName());
        params.put("total_fee", order.getTotalCost().setScale(2).toString());
        params.put("notify_url", alipayNotifyUrl);
        params.put("sign", sign(params));
    }*/

}
