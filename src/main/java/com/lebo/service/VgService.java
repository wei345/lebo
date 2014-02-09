package com.lebo.service;

/**
 *
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM5:53
 */

import com.google.common.eventbus.EventBus;
import com.lebo.entity.*;
import com.lebo.event.GiveGoodsNotificationEvent;
import com.lebo.redis.RedisKeys;
import com.lebo.repository.PostDao;
import com.lebo.repository.UserDao;
import com.lebo.repository.mybatis.*;
import com.lebo.rest.dto.ErrorDto;
import com.lebo.rest.dto.GoldOrderDto;
import com.lebo.rest.dto.GoldProductDto;
import com.lebo.rest.dto.GoodsDto;
import com.lebo.service.param.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springside.modules.mapper.BeanMapper;
import org.springside.modules.mapper.JsonMapper;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class VgService {

    @Autowired
    private GoldProductDao goldProductDao;
    @Autowired
    private GoldOrderDao goldOrderDao;
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
    @Autowired
    private SettingService settingService;

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

    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();

    //-- 金币产品 --//

    public List<GoldProduct> findAllGoldProducts() {
        return goldProductDao.getActive();
    }

    public GoldProduct getGoldProduct(long id) {
        return goldProductDao.get(id);
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

    //-- 订单 --//
    public GoldOrder createOrder(Long goldProductId, String userId, GoldOrder.PaymentMethod paymentMethod) {
        return createOrder(nextOrderId(), goldProductId, 1, userId, paymentMethod);
    }

    public GoldOrder createOrder(String orderId,
                                 Long goldProductId,
                                 int quantity,
                                 String userId,
                                 GoldOrder.PaymentMethod paymentMethod) {

        BigDecimal discount = BigDecimal.ZERO;

        GoldOrder goldOrder = new GoldOrder(orderId, userId, discount, GoldOrder.Status.UNPAID, paymentMethod);

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

    String nextOrderId() {
        return IdWorker.nextId();
    }

    private static class IdWorker {

        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); //17位

        public static synchronized String nextId() {

            int n = (int) Thread.currentThread().getId() % 100;

            return sdf.format(new Date())
                    + (n < 10 ? "0" + n : n);
        }
    }

    public GoldOrder getOrderWithDetail(String orderId) {
        GoldOrder goldOrder = goldOrderDao.get(orderId);
        goldOrder.setGoldProduct(goldProductDao.get(goldOrder.getGoldProduct().getId()));
        return goldOrder;
    }

    public GoldOrder getOrder(String orderId) {
        return goldOrderDao.get(orderId);
    }

    public GoldOrderDto toGoldOrderDto(GoldOrder goldOrder) {
        return BeanMapper.map(goldOrder, GoldOrderDto.class);
    }


    public static interface PaymentDetail {

    }

    public void updateOrderStatus(String orderId, GoldOrder.Status status, String paymentStatus, PaymentDetail paymentDetail) {
        GoldOrder goldOrder = new GoldOrder(orderId);

        goldOrder.setStatus(status);
        goldOrder.setPaymentStatus(paymentStatus);
        goldOrder.setPaymentDetail(jsonMapper.toJson(paymentDetail));

        goldOrderDao.updateStatus(goldOrder);
    }

    public void delivery(GoldOrder goldOrder) {
        addUserGold(goldOrder.getUserId(), goldOrder.getGold(), goldOrder.getTotalCost());
    }

    //-- UserInfo --//

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

    private void addUserGold(String userId, Integer gold, BigDecimal cost) {
        UserInfo userInfo = getUserInfoByIdOrCreateIfNotExist(userId);

        UserInfo update = new UserInfo(userId); //只更新需要更新的字段
        update.setGold(userInfo.getGold() + gold);
        update.setRecharge(userInfo.getRecharge().add(cost));
        userInfoDao.update(update);
    }

    private void addUserPopularity(String userId, int popularity) {
        UserInfo userInfo = getUserInfoByIdOrCreateIfNotExist(userId);

        UserInfo update = new UserInfo(userId);
        update.setPopularity(userInfo.getPopularity() + popularity);
        userInfoDao.update(update);
    }

    //-- Goods --//

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

    //-- 赠送礼物 --//
    public void giveGoods(String fromUserId, String toUserId, String postId, Integer goodsId, Integer quantity) {
        GiveGoods giveGoods = giveGoodsInternal(fromUserId, toUserId, postId, goodsId, quantity, true);
        eventBus.post(new GiveGoodsNotificationEvent(giveGoods));
    }

    /**
     * 帖子加精，赠送用户一朵玫瑰
     */
    public void giveGoodsWhenDigest(String toUserId, String postId) {
        giveGoodsInternal(settingService.getSetting().getDigestAccountId(),
                toUserId,
                postId,
                getGoodsIdByName("玫瑰"),
                1,
                false);
    }

    @Cacheable(value = RedisKeys.CACHE_NAME_DEFAULT, key = RedisKeys.GOODS_ID_SPEL)
    private Integer getGoodsIdByName(String name) {
        return goodsDao.getIdByName(name);
    }

    private GiveGoods giveGoodsInternal(String fromUserId, String toUserId, String postId, Integer goodsId, Integer quantity, boolean checkGold) {
        Assert.isTrue(quantity > 0);
        Assert.isTrue(userDao.exists(fromUserId));
        Assert.isTrue(userDao.exists(toUserId));
        Assert.isTrue(postDao.exists(postId));

        Goods goods = getGoodsById(goodsId);
        Assert.notNull(goods);

        Integer totalPrice = goods.getPrice() * quantity;

        UserInfo fromUserInfo = getUserInfoByIdOrCreateIfNotExist(fromUserId);

        //检查金币余额
        if (checkGold) {
            if (fromUserInfo == null || fromUserInfo.getGold() < totalPrice) {
                throw new ServiceException(ErrorDto.INSUFFICIENT_GOLD);
            }
        }

        //支付金币
        UserInfo update = new UserInfo(fromUserId);
        update.setGold(fromUserInfo.getGold() - totalPrice);
        update.setConsumeGold(fromUserInfo.getConsumeGold() + totalPrice);
        userInfoDao.update(update);

        //增加用户物品
        addUserGoodsQuantity(toUserId, goodsId, quantity);

        //增加帖子物品
        addPostGoodsQuantity(postId, goodsId, quantity);

        //增加用户人气
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
        addOrUpdateGiverValue(toUserId, fromUserId, totalPrice);

        //增加帖子人气
        statusService.addPopularity(postId, totalPrice);

        return giveGoods;
    }

    private UserInfo getUserInfoByIdOrCreateIfNotExist(String userId) {
        UserInfo userInfo = userInfoDao.get(userId);

        if (userInfo == null) {
            userInfo = UserInfo.newUserInfoWithDefaultValue(userId);
            userInfoDao.insert(userInfo);
        }

        return userInfo;
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

    private void addOrUpdateGiverValue(String userId, String giverId, int giveValue) {

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

    public List<UserGoods> getUserGoodsByUserId(String userId) {
        return userGoodsDao.getByUserId(userId);
    }

    public GiverValue getGiverValue(String userId, String giverId) {
        return giverValueDao.get(new GiverValue(userId, giverId));
    }

    public int getGiverRank(GiverValue giverValue) {
        return giverValueDao.countBefore(giverValue) + 1;
    }

}
