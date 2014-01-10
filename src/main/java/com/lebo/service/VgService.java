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
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.repository.mybatis.*;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.GoldOrderDto;
import com.lebo.rest.dto.GoldProductDto;
import com.lebo.rest.dto.GoodsDto;
import com.lebo.service.param.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.utils.Encodes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.lebo.service.AlipayService.AlipayStatus;
import static com.lebo.service.AlipayService.AlipayStatus.WAIT_BUYER_PAY;

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
    private UserInfoDao userInfoDao;
    @Autowired
    private UserGoodsDao userGoodsDao;
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GiveGoodsDao giveGoodsDao;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private UserDao userDao;
    @Autowired
    private PostDao postDao;
    @Autowired
    private PostGoodsDao postGoodsDao;
    @Autowired
    private GiverValueDao giverValueDao;
    @Autowired
    private StatusService statusService;

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

    private Logger logger = LoggerFactory.getLogger(VgService.class);

    public GoldOrder createOrder(Long goldProductId, String userId, GoldOrder.PaymentMethod paymentMethod) {

        BigDecimal discount = BigDecimal.ZERO;
        int quantity = 1;

        GoldOrder goldOrder = new GoldOrder(nextOrderId(), userId, discount, GoldOrder.Status.UNPAID, paymentMethod);

        GoldProduct goldProduct = goldProductDao.get(goldProductId);
        Assert.notNull(goldProduct);
        goldOrder.setGoldProduct(goldProduct);

        goldOrder.setQuantity(quantity);

        goldOrder.setSubject(goldProduct.getName() + "×" + quantity);

        goldOrder.setTotalCost(goldProduct.getCost().multiply(new BigDecimal(quantity)).add(discount));

        goldOrder.setGold(goldProduct.getGold() * quantity);

        goldOrderDao.insert(goldOrder);
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
        params.put("total_fee", goldOrder.getTotalCost().setScale(2).toString());
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

    public void handleAlipayNotify(long outTradeNo, AlipayStatus alipayStatus, String alipayNotifyId) {

        if (!alipayService.checkNotifyId(alipayNotifyId)) {
            logger.debug("alipayNotifyId '{}' 已处理过", alipayNotifyId);
            return;
        }

        updateOrderStatus(outTradeNo, alipayStatus, alipayNotifyId);

        alipayService.doneNotifyId(alipayNotifyId);
    }

    public void updateOrderStatus(long outTradeNo, AlipayStatus alipayStatus, String alipayNotifyId) {

        GoldOrder goldOrder = goldOrderDao.get(outTradeNo);

        AlipayStatus currentAlipayStatus = AlipayStatus.valueOf(goldOrder.getPaymentStatus());

        if (currentAlipayStatus == null || currentAlipayStatus.canChangeTo(alipayStatus)) {

            logger.debug("正在更新订单状态: {} -> {}", currentAlipayStatus, alipayStatus);

            switch (alipayStatus) {
                //null -> WAIT_BUYER_PAY
                case WAIT_BUYER_PAY:
                    updateTradeStatus(outTradeNo, GoldOrder.Status.UNPAID, alipayStatus, alipayNotifyId);
                    logger.debug("未支付 : 支付宝等待用户支付");
                    break;

                case TRADE_SUCCESS:
                    //null -> TRADE_SUCCESS, WAIT_BUYER_PAY -> TRADE_SUCCESS
                    tradeSuccess(outTradeNo, alipayStatus, alipayNotifyId);
                    logger.debug("已支付 : 支付宝交易完成，用户金币已增加");
                    break;

                case TRADE_FINISHED:
                    //null -> TRADE_FINISHED, WAIT_BUYER_PAY -> TRADE_FINISHED
                    if (currentAlipayStatus == null || currentAlipayStatus == WAIT_BUYER_PAY) {
                        tradeSuccess(outTradeNo, alipayStatus, alipayNotifyId);
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
                        updateTradeStatus(outTradeNo, GoldOrder.Status.OBSOLETE, alipayStatus, alipayNotifyId);
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

    /**
     * 购买金币，支付宝支付成功.
     */
    public void tradeSuccess(Long orderId, AlipayStatus alipayStatus, String alipayNotifyId) {
        //更新订单状态
        updateTradeStatus(orderId, GoldOrder.Status.PAID, alipayStatus, alipayNotifyId);

        //更新用户金币数
        GoldOrder goldOrder = goldOrderDao.get(orderId);
        addUserGold(goldOrder.getUserId(), goldOrder.getGold(), goldOrder.getTotalCost());
    }

    private void addUserGold(String userId, Integer gold, BigDecimal recharge) {
        UserInfo userInfo = userInfoDao.get(userId);
        if (userInfo == null) {
            userInfo = new UserInfo(userId);
            userInfo.setGold(gold);
            userInfo.setRecharge(recharge);
            userInfoDao.insert(userInfo);
        } else {
            UserInfo up = new UserInfo(userId); //只更新需要更新的字段
            up.setGold(userInfo.getGold() + gold);
            up.setRecharge(userInfo.getRecharge().add(recharge));
            userInfoDao.update(up);
        }
    }

    public void updateTradeStatus(Long orderId, GoldOrder.Status status, AlipayStatus alipayStatus, String alipayNotifyId) {
        GoldOrder goldOrder = new GoldOrder(orderId);
        goldOrder.setStatus(status);
        goldOrder.setPaymentStatus(alipayStatus.name());
        goldOrderDao.updateStatus(goldOrder);
    }

    public UserInfo getUserInfoNullToDefault(String userId) {
        UserInfo userInfo = userInfoDao.get(userId);

        //如果数据库没有，则返回默认值
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setGold(0);
            userInfo.setConsumeGold(0);
            userInfo.setRecharge(BigDecimal.ZERO);
        }

        return userInfo;
    }

    public List<UserGoods> getUserGoodsByUserId(String userId) {
        return userGoodsDao.getByUserId(userId);
    }

    public Goods getGoodsById(Integer goodsId) {
        return goodsDao.get(goodsId);
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

    public void giveGoods(String fromUserId, String toUserId, String postId, Integer goodsId, Integer quantity) {
        Assert.isTrue(quantity > 0);
        Assert.isTrue(userDao.exists(fromUserId));
        Assert.isTrue(userDao.exists(toUserId));
        Assert.isTrue(postDao.exists(postId));

        Goods goods = getGoodsById(goodsId);
        Assert.notNull(goods);

        Integer totalPrice = goods.getPrice() * quantity;

        UserInfo fromUserInfo = userInfoDao.get(fromUserId);

        //检查金币余额
        if (fromUserInfo == null || fromUserInfo.getGold() < totalPrice) {
            throw new ServiceException(ErrorDto.INSUFFICIENT_GOLD);
        }

        //支付金币
        UserInfo up = new UserInfo(fromUserId);
        up.setGold(fromUserInfo.getGold() - totalPrice);
        up.setConsumeGold(fromUserInfo.getConsumeGold() + totalPrice);
        userInfoDao.update(up);

        //增加用户物品
        addUserGoodsQuantity(toUserId, goodsId, quantity);

        //增加帖子物品
        addPostGoodsQuantity(postId, goodsId, quantity);

        //增加人气
        addUserPopularity(toUserId, totalPrice);

        //记录历史
        GiveGoods giveGoods = new GiveGoods();
        giveGoods.setFromUserId(fromUserId);
        giveGoods.setToUserId(toUserId);
        giveGoods.setPostId(postId);
        giveGoods.setGoodsId(goodsId);
        giveGoods.setQuantity(quantity);
        giveGoods.setGiveDate(new Date());
        giveGoodsDao.insert(giveGoods);

        //更新排名数据
        addGiveValue(toUserId, fromUserId, totalPrice);

        //增长帖子人气
        statusService.addPopularity(postId, totalPrice);

        eventBus.post(new AfterGiveGoodsEvent(giveGoods));
    }

    public List<GiverValue> getGiverRanking(String userId, PageRequest pageRequest) {

        Map<String, Object> params = new HashMap<String, Object>(3);
        params.put("userId", userId);
        params.put("offset", pageRequest.getOffset());
        params.put("count", pageRequest.getPageSize());

        return giverValueDao.getByUserIdOrderByGiveValueDesc(params);
    }

    private void addUserGoodsQuantity(String userId, int goodsId, int quantity) {
        UserGoods userGoods = userGoodsDao.get(new UserGoods(userId, goodsId));
        if (userGoods == null) {
            userGoodsDao.insert(new UserGoods(userId, goodsId, quantity));
        } else {
            userGoods.setQuantity(userGoods.getQuantity() + quantity);
            userGoodsDao.updateQuantity(userGoods);
        }
    }

    private void addPostGoodsQuantity(String postId, int goodsId, int quantity) {
        PostGoods postGoods = postGoodsDao.get(new PostGoods(postId, goodsId));
        if (postGoods == null) {
            postGoodsDao.insert(new PostGoods(postId, goodsId, quantity));
        } else {
            postGoods.setQuantity(postGoods.getQuantity() + quantity);
            postGoodsDao.updateQuantity(postGoods);
        }
    }

    private void addUserPopularity(String userId, int popularity) {
        UserInfo userInfo = userInfoDao.get(userId);
        if (userInfo == null) {
            userInfo = new UserInfo(userId);
            userInfo.setPopularity(popularity);
            userInfoDao.insert(userInfo);
        } else {
            UserInfo up = new UserInfo(userId);
            up.setPopularity(userInfo.getPopularity() + popularity);
            userInfoDao.update(up);
        }
    }

    private void addGiveValue(String userId, String giverId, int giveValue) {

        GiverValue giverValue = giverValueDao.get(new GiverValue(userId, giverId));

        if (giverValue == null) {
            giverValue = new GiverValue(userId, giverId);
            giverValue.setGiveValue(giveValue);
            giverValueDao.insert(giverValue);
        } else {
            giverValue.setGiveValue(giverValue.getGiveValue() + giveValue);
            giverValueDao.update(giverValue);
        }
    }

    public GiverValue getGiverValue(String userId, String giverId) {
        return giverValueDao.get(new GiverValue(userId, giverId));
    }

    public int getGiverRank(GiverValue giverValue) {
        return giverValueDao.countBefore(giverValue) + 1;
    }

    long nextOrderId() {
        return IdWorker.nextId();
    }

    private static class IdWorker {

        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //17位

        public static synchronized long nextId() {

            int n = (int) Thread.currentThread().getId() % 100;

            return Long.parseLong(
                    sdf.format(new Date())
                            + (n < 10 ? "0" + n : n));
        }
    }

}
