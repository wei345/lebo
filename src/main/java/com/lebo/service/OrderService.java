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
import com.lebo.service.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
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
}
