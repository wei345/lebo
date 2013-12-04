package com.lebo.service;

/**
 * 订单.
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.google.common.eventbus.EventBus;
import com.lebo.entity.*;
import com.lebo.event.AfterGiveGoodsEvent;
import com.lebo.repository.mybatis.*;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.GoldOrderDto;
import com.lebo.rest.dto.GoldProductDto;
import com.lebo.rest.dto.GoodsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.utils.Encodes;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private UserGoldDao userGoldDao;
    @Autowired
    private UserGoodsDao userGoodsDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GiveGoodsDao giveGoodsDao;
    @Autowired
    private EventBus eventBus;

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

    public GoldOrder createOrder(Long goldProductId, String userId, GoldOrder.PaymentMethod paymentMethod) {

        GoldOrder goldOrder = new GoldOrder(userId, BigDecimal.ZERO, GoldOrder.Status.UNPAID, paymentMethod);

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

    public String getAlipayParams(String userId, Long productId, String service, String paymentType) {

        GoldOrder goldOrder = createOrder(productId, userId, GoldOrder.PaymentMethod.ALIPAY);

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
        //开发环境，订单支付1分钱
        if (appEnv.isDevelopment()) {
            params.put("total_fee", "0.01");
        }
        //生成环境，订单支付实际金额
        else {
            params.put("total_fee", goldOrder.getTotalCost().setScale(2).toString());
        }
        //
        params.put("body", goldOrder.getAlipayBody());
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

    public GoldProductDto toProductDto(GoldProduct goldProduct) {
        return BeanMapper.map(goldProduct, GoldProductDto.class);
    }

    public List<GoldProductDto> toProductDtos(List<GoldProduct> goldProducts) {
        ArrayList<GoldProductDto> dtos = new ArrayList<GoldProductDto>(goldProducts.size());
        for (GoldProduct goldProduct : goldProducts) {
            dtos.add(toProductDto(goldProduct));
        }
        return dtos;
    }

    public GoldOrderDto toGoldOrderDto(GoldOrder goldOrder) {
        return BeanMapper.map(goldOrder, GoldOrderDto.class);
    }

    /**
     * 购买金币，支付宝支付成功.
     */
    public void tradeSuccess(Long orderId, String alipayStatus, String alipayNotifyId) {
        //更新订单状态
        updateTradeStatus(orderId, GoldOrder.Status.PAID, alipayStatus, alipayNotifyId);

        //更新用户金币数
        GoldOrder goldOrder = getOrderWithDetail(orderId);
        addUserGoldQuantity(goldOrder.getUserId(), goldOrder.getGoldQuantity());
    }

    private void addUserGoldQuantity(String userId, Long goldQuantity) {
        UserGold userGold = userGoldDao.getByUserId(userId);
        if (userGold == null) {
            userGoldDao.insert(new UserGold(userId, goldQuantity));
        } else {
            userGold.setGoldQuantity(userGold.getGoldQuantity() + goldQuantity);
            userGoldDao.updateUserGoldQuantity(userGold);
        }
    }

    public void updateTradeStatus(Long orderId, GoldOrder.Status status, String alipayStatus, String alipayNotifyId) {
        GoldOrder goldOrder = new GoldOrder();
        goldOrder.setId(orderId);
        goldOrder.setStatus(status);
        goldOrder.setAlipayStatus(alipayStatus);
        goldOrder.setAlipayNotifyId(alipayNotifyId);
        goldOrderDao.updateStatus(goldOrder);
    }

    public Long getUserGoldQuantity(String userId) {
        Long quantity = userGoldDao.getUserGoldQuantity(userId);
        return quantity == null ? 0 : quantity;
    }

    public List<UserGoods> getUserGoodsByUserId(String userId) {
        return userGoodsDao.getByUserId(userId);
    }

    public Goods getGoodsById(Long goodsId) {
        return goodsDao.getById(goodsId);
    }

    public List<Goods> getAllGoods() {
        return goodsDao.getAll();
    }

    public GoodsDto toGoodsDto(Goods goods) {
        return BeanMapper.map(goods, GoodsDto.class);
    }

    public List<GoodsDto> toGoodsDtos(List<Goods> goodsList) {
        List<GoodsDto> goodsDtos = new ArrayList<GoodsDto>(goodsList.size());
        for (Goods goods : goodsList) {
            goodsDtos.add(toGoodsDto(goods));
        }
        return goodsDtos;
    }

    public void giveGoods(String fromUserId, String toUserId, String postId, Long goodsId, Integer quantity) {
        Long fromUserGold = getUserGoldQuantity(fromUserId);
        Goods goods = getGoodsById(goodsId);
        Integer totalCost = goods.getPrice() * quantity;

        //检查金币余额
        if (fromUserGold < totalCost) {
            throw new ServiceException(ErrorDto.INSUFFICIENT_GOLD);
        }

        //支付金币
        userGoldDao.updateUserGoldQuantity(new UserGold(fromUserId, fromUserGold - totalCost));

        //增加物品
        addUserGoodsQuantity(toUserId, goodsId, quantity);

        //记录历史
        GiveGoods giveGoods = new GiveGoods();
        giveGoods.setFromUserId(fromUserId);
        giveGoods.setToUserId(toUserId);
        giveGoods.setPostId(postId);
        giveGoods.setGoodsId(goodsId);
        giveGoods.setQuantity(quantity);
        giveGoods.setGiveDate(new Date());
        giveGoodsDao.insert(giveGoods);

        eventBus.post(new AfterGiveGoodsEvent(giveGoods));
    }

    private void addUserGoodsQuantity(String userId, long goodsId, int quantity) {
        UserGoods userGoods = userGoodsDao.getByUserIdGoodsId(new UserGoods(userId, goodsId));
        if (userGoods == null) {
            userGoodsDao.insert(new UserGoods(userId, goodsId, quantity));
        } else {
            userGoods.setQuantity(userGoods.getQuantity() + quantity);
            userGoodsDao.updateQuantityByUserIdAndGoodsId(userGoods);
        }
    }

}
