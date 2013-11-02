package com.lebo.service;

/**
 * 订单.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.lebo.entity.Order;
import com.lebo.entity.OrderDetail;
import com.lebo.entity.Product;
import com.lebo.repository.mybatis.OrderDao;
import com.lebo.repository.mybatis.OrderDetailDao;
import com.lebo.repository.mybatis.ProductDao;
import com.lebo.rest.dto.OrderDetailDto;
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
public class EcService {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private OrderDetailDao orderDetailDao;
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

    public Order createOrder(Long productId, String mongoUserId) {
        Order order = new Order(mongoUserId, BigDecimal.ZERO, Order.Status.UNPAID);

        Product product = productDao.getWithDetail(productId);
        Assert.notNull(product);

        OrderDetail orderDetail = new OrderDetail(order, product, 1, BigDecimal.ZERO);

        order.getOrderDetails().add(orderDetail);

        orderDao.save(order);
        orderDetailDao.save(orderDetail);

        return order;
    }

    public List<Product> findProductByCategoryId(Long productCategoryId) {
        return productDao.findByCategoryId(productCategoryId);
    }

    public void updateOrderStatus(Order order, Order.Status status) {

    }

    public String getAlipayParams(String userId, Long productId, String service, String paymentType) {
        Order order = createOrder(productId, userId);

        Map<String, String> params = new HashMap<String, String>(10);
        //基本参数，不可空
        params.put("service", service);
        params.put("partner", alipayPartnerId);
        params.put("_input_charset", "utf-8");
        //业务参数，不可空
        params.put("out_trade_no", order.getOrderId().toString());
        params.put("subject", "购买" + order.getSubject());
        params.put("payment_type", paymentType);
        params.put("seller_id", alipaySellerId);
        //开发环境，订单支付1分钱
        if (appEnv.isDevelopment()) {
            params.put("total_fee", "0.01");
        }
        //生成环境，订单支付实际金额
        else {
            params.put("total_fee", order.getTotalCost().setScale(2).toString());
        }
        //
        params.put("body", order.getBody());
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

    public ProductDto toProductDto(Product product) {
        return BeanMapper.map(product, ProductDto.class);
    }

    public OrderDto toOrderDto(Order order) {
        return BeanMapper.map(order, OrderDto.class);
    }

    public OrderDetailDto toOrderDetailDto(OrderDetail orderDetail) {
        return BeanMapper.map(orderDetail, OrderDetailDto.class);
    }
}
