package com.lebo.rest;

import com.lebo.entity.*;
import com.lebo.rest.dto.*;
import com.lebo.service.InAppPurchaseService;
import com.lebo.service.StatusService;
import com.lebo.service.VgService;
import com.lebo.service.account.AccountService;
import com.lebo.service.param.PageRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springside.modules.mapper.BeanMapper;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Wei Liu
 * Date: 13-10-29
 * Time: PM6:16
 */
@Controller
public class VgRestController {
    @Autowired
    private VgService vgService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private StatusService statusService;
    @Autowired
    private InAppPurchaseService inAppPurchaseService;

    private static final String API_1_1_VG = "/api/1.1/vg/";

    @RequestMapping(value = API_1_1_VG + "goldProducts.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldProducts() {
        return vgService.toProductDtos(vgService.findAllGoldProducts());
    }

    @RequestMapping(value = API_1_1_VG + "buyGold.json", method = RequestMethod.POST)
    @ResponseBody
    public Object buyGold(@RequestParam("productId") Long productId,
                          @RequestParam("paymentMethod") GoldOrder.PaymentMethod paymentMethod,
                          @RequestParam("alipayService") String alipayService,
                          @RequestParam("alipayPaymentType") String alipayPaymentType) {

        return ErrorDto.badRequest("现在暂时无法充值，预计下一个版本开通，谢谢!");


        /*if (GoldOrder.PaymentMethod.ALIPAY == paymentMethod) {

            String params = alipayService.getAlipayParams(
                    accountService.getCurrentUserId(),
                    productId,
                    alipayService,
                    alipayPaymentType);

            Map<String, String> result = new HashMap<String, String>(1);
            result.put("alipaySignedParams", params);

            return result;
        } else {
            return ErrorDto.badRequest("不支持该付款方式: [" + paymentMethod + "]");
        }*/

    }

    @RequestMapping(value = API_1_1_VG + "inAppPurchase.json", method = RequestMethod.POST)
    @ResponseBody
    public Object inAppPurchase(@RequestParam("receiptData") String receiptData,
                                @RequestParam("userId") String userId) {

        InAppPurchaseService.VerifyReceiptResult verifyReceiptResult = inAppPurchaseService.verifyReceipt(receiptData);

        if (verifyReceiptResult.getStatus() != 0) {
            return ErrorDto.badRequest("收据验证失败, status " + verifyReceiptResult.getStatus());
        }

        //创建订单, 交付金币, 记录receiptData
        inAppPurchaseService.delivery(verifyReceiptResult, receiptData, userId);

        //返回购买的商品和数量
        InAppPurchaseService.Receipt receipt = verifyReceiptResult.getReceipt();

        GoldProductWithQuantityDto dto = BeanMapper.map(
                vgService.getGoldProduct(receipt.getGoldProductId()),
                GoldProductWithQuantityDto.class);

        dto.setQuantity(receipt.getQuantity());

        return dto;
    }

    @RequestMapping(value = API_1_1_VG + "goldOrders/detail.json", method = RequestMethod.GET)
    @ResponseBody
    public Object goldOrdersDetail(@RequestParam("orderId") Long orderId) {

        return vgService.toGoldOrderDto(vgService.getOrderWithDetail(orderId));
    }

    @RequestMapping(value = API_1_1_VG + "userVg.json", method = RequestMethod.GET)
    @ResponseBody
    public Object userVg(@RequestParam(value = "userId", required = false) String userId,
                         @RequestParam(value = "screenName", required = false) String screenName) {

        userId = accountService.getUserId(userId, screenName);

        User user = accountService.getUser(userId);
        if (user == null) {
            ErrorDto.badRequest("用户[" + userId + "]不存在");
        }

        //用户名字、头像
        UserVgDto userVgDto = new UserVgDto();
        userVgDto.setUserId(user.getId());
        userVgDto.setScreenName(user.getScreenName());
        userVgDto.setProfileImageUrl(user.getProfileImageUrl());
        userVgDto.setProfileImageBiggerUrl(user.getProfileImageBiggerUrl());
        userVgDto.setProfileImageOriginalUrl(user.getProfileImageOriginalUrl());

        //用户金币数
        UserInfo userInfo = vgService.getUserInfoNullToDefault(userId);
        userVgDto.setGold(userInfo.getGold());
        userVgDto.setConsumeGold(userInfo.getConsumeGold());

        //用户物品和总价值
        Integer goodsTotalPrice = 0;
        List<UserGoods> userGoodsList = vgService.getUserGoodsByUserId(userId);
        List<UserGoodsDto> userGoodsDtos = new ArrayList<UserGoodsDto>(userGoodsList.size());
        for (UserGoods userGoods : userGoodsList) {

            UserGoodsDto userGoodsDto = new UserGoodsDto();
            Goods goods = vgService.getGoodsById(userGoods.getGoodsId());
            BeanMapper.copy(goods, userGoodsDto);
            userGoodsDto.setQuantity(userGoods.getQuantity());

            userGoodsDtos.add(userGoodsDto);
            goodsTotalPrice += goods.getPrice() * userGoods.getQuantity();
        }
        userVgDto.setGoods(userGoodsDtos);
        userVgDto.setGoodsTotalPrice(goodsTotalPrice);

        return userVgDto;
    }

    @RequestMapping(value = API_1_1_VG + "goods.json", method = RequestMethod.GET)
    @ResponseBody
    public Object getAllGoods() {
        return vgService.toGoodsDtos(vgService.getAllGoods());
    }

    @RequestMapping(value = API_1_1_VG + "giveGoods.json", method = RequestMethod.POST)
    @ResponseBody
    public Object giveGoods(@RequestParam(value = "postId") String postId,
                            @RequestParam("goodsId") int goodsId,
                            @RequestParam("quantity") int quantity) {

        if (StringUtils.isBlank(postId)) {
            return ErrorDto.badRequest("postId不能为空");
        }

        if (quantity <= 0) {
            return ErrorDto.badRequest("数量必须大于0");
        }

        Post post = statusService.getPost(postId);

        if (post == null) {
            return ErrorDto.badRequest("帖子不存在(postId=" + postId + ")");
        }

        if (post.getOriginPostId() != null) {
            return ErrorDto.badRequest("postId不能为转发贴");
        }

        String toUserId = post.getUserId();

        String currentUserId = accountService.getCurrentUserId();

        if (currentUserId.equals(toUserId)) {
            return ErrorDto.badRequest("不能给自己送礼物");
        }

        vgService.giveGoods(currentUserId, toUserId, postId, goodsId, quantity);

        return ErrorDto.OK;
    }

    @RequestMapping(value = API_1_1_VG + "giverRanking.json", method = RequestMethod.GET)
    @ResponseBody
    public Object giverRanking(@RequestParam(value = "userId", required = false) String userId,
                               @RequestParam(value = "screenName", required = false) String screenName,
                               @Valid PageRequest pageRequest) {

        userId = accountService.getUserId(userId, screenName);

        //送礼者列表
        GiverRankingDto dto = new GiverRankingDto();

        List<GiverValue> giverValueList = vgService.getGiverRanking(userId, pageRequest);

        for (GiverValue giverValue : giverValueList) {

            User user = accountService.getUser(giverValue.getGiverId());
            GiverRankingDto.Giver giver = BeanMapper.map(user, GiverRankingDto.Giver.class);
            giver.setGiveValue(giverValue.getGiveValue());

            dto.getGiverList().add(giver);
        }

        //当前用户
        String currentUserId = accountService.getCurrentUserId();
        if (!currentUserId.equals(userId)) {
            GiverValue giverValue = vgService.getGiverValue(userId, currentUserId);
            if (giverValue != null) {
                //查排名
                GiverRankingDto.Giver me = BeanMapper.map(
                        accountService.getUser(currentUserId),
                        GiverRankingDto.Giver.class);
                me.setGiveValue(giverValue.getGiveValue());
                me.setRank(vgService.getGiverRank(giverValue));

                dto.setMe(me);
            }
        }

        return dto;
    }

}
